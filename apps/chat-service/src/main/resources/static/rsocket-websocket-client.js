var rsocketWebSocketClient = (function (
  rsocketFlowable,
  rsocketCore,
  rsocketTypes
) {
  'use strict';

  function _defineProperty(obj, key, value) {
    if (key in obj) {
      Object.defineProperty(obj, key, {
        value: value,
        enumerable: true,
        configurable: true,
        writable: true,
      });
    } else {
      obj[key] = value;
    }

    return obj;
  }

  /**
   * Copyright (c) 2013-present, Facebook, Inc.
   *
   * This source code is licensed under the MIT license found in the
   * LICENSE file in the root directory of this source tree.
   *
   *
   */

  var validateFormat =
    process.env.NODE_ENV !== 'production'
      ? function (format) {
          if (format === undefined) {
            throw new Error(
              'invariant(...): Second argument must be a string.'
            );
          }
        }
      : function (format) {};
  /**
   * Use invariant() to assert state which your program assumes to be true.
   *
   * Provide sprintf-style format (only %s is supported) and arguments to provide
   * information about what broke and what you were expecting.
   *
   * The invariant message will be stripped in production, but the invariant will
   * remain to ensure logic does not differ in production.
   */

  function invariant(condition, format) {
    for (
      var _len = arguments.length,
        args = new Array(_len > 2 ? _len - 2 : 0),
        _key = 2;
      _key < _len;
      _key++
    ) {
      args[_key - 2] = arguments[_key];
    }

    validateFormat(format);

    if (!condition) {
      var error;

      if (format === undefined) {
        error = new Error(
          'Minified exception occurred; use the non-minified dev environment ' +
            'for the full error message and additional helpful warnings.'
        );
      } else {
        var argIndex = 0;
        error = new Error(
          format.replace(/%s/g, function () {
            return String(args[argIndex++]);
          })
        );
        error.name = 'Invariant Violation';
      }

      error.framesToPop = 1; // Skip invariant's own stack frame.

      throw error;
    }
  }

  var invariant_1 = invariant;

  /** Copyright (c) Facebook, Inc. and its affiliates.
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   *     http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   *
   *
   */

  /**
   * A WebSocket transport client for use in browser environments.
   */
  class RSocketWebSocketClient {
    constructor(options, encoders) {
      _defineProperty(
        this,
        '_handleClosed',

        (e) => {
          this._close(
            new Error(
              e.reason || 'RSocketWebSocketClient: Socket closed unexpectedly.'
            )
          );
        }
      );
      _defineProperty(
        this,
        '_handleError',

        (e) => {
          this._close(e.error);
        }
      );
      _defineProperty(
        this,
        '_handleOpened',

        () => {
          this._setConnectionStatus(rsocketTypes.CONNECTION_STATUS.CONNECTED);
        }
      );
      _defineProperty(
        this,
        '_handleMessage',

        (message) => {
          try {
            const frame = this._readFrame(message);
            this._receivers.forEach((subscriber) => subscriber.onNext(frame));
          } catch (error) {
            this._close(error);
          }
        }
      );
      this._encoders = encoders;
      this._options = options;
      this._receivers = new Set();
      this._senders = new Set();
      this._socket = null;
      this._status = rsocketTypes.CONNECTION_STATUS.NOT_CONNECTED;
      this._statusSubscribers = new Set();
    }
    close() {
      this._close();
    }
    connect() {
      invariant_1(
        this._status.kind === 'NOT_CONNECTED',
        'RSocketWebSocketClient: Cannot connect(), a connection is already ' +
          'established.'
      );
      this._setConnectionStatus(rsocketTypes.CONNECTION_STATUS.CONNECTING);
      const wsCreator = this._options.wsCreator;
      const url = this._options.url;
      this._socket = wsCreator ? wsCreator(url) : new WebSocket(url);
      const socket = this._socket;
      socket.binaryType = 'arraybuffer';
      socket.addEventListener('close', this._handleClosed);
      socket.addEventListener('error', this._handleError);
      socket.addEventListener('open', this._handleOpened);
      socket.addEventListener('message', this._handleMessage);
    }
    connectionStatus() {
      return new rsocketFlowable.Flowable((subscriber) => {
        subscriber.onSubscribe({
          cancel: () => {
            this._statusSubscribers.delete(subscriber);
          },
          request: () => {
            this._statusSubscribers.add(subscriber);
            subscriber.onNext(this._status);
          },
        });
      });
    }
    receive() {
      return new rsocketFlowable.Flowable((subject) => {
        subject.onSubscribe({
          cancel: () => {
            this._receivers.delete(subject);
          },
          request: () => {
            this._receivers.add(subject);
          },
        });
      });
    }
    sendOne(frame) {
      this._writeFrame(frame);
    }
    send(frames) {
      let subscription;
      frames.subscribe({
        onComplete: () => {
          subscription && this._senders.delete(subscription);
        },
        onError: (error) => {
          subscription && this._senders.delete(subscription);
          this._close(error);
        },
        onNext: (frame) => this._writeFrame(frame),
        onSubscribe: (_subscription) => {
          subscription = _subscription;
          this._senders.add(subscription);
          subscription.request(Number.MAX_SAFE_INTEGER);
        },
      });
    }
    _close(error) {
      if (this._status.kind === 'CLOSED' || this._status.kind === 'ERROR') {
        // already closed
        return;
      }
      const status = error
        ? {error, kind: 'ERROR'}
        : rsocketTypes.CONNECTION_STATUS.CLOSED;
      this._setConnectionStatus(status);
      this._receivers.forEach((subscriber) => {
        if (error) {
          subscriber.onError(error);
        } else {
          subscriber.onComplete();
        }
      });
      this._receivers.clear();
      this._senders.forEach((subscription) => subscription.cancel());
      this._senders.clear();
      const socket = this._socket;
      if (socket) {
        socket.removeEventListener('close', this._handleClosed);
        socket.removeEventListener('error', this._handleClosed);
        socket.removeEventListener('open', this._handleOpened);
        socket.removeEventListener('message', this._handleMessage);
        socket.close();
        this._socket = null;
      }
    }
    _setConnectionStatus(status) {
      this._status = status;
      this._statusSubscribers.forEach((subscriber) =>
        subscriber.onNext(status)
      );
    }
    _readFrame(message) {
      const buffer = rsocketCore.toBuffer(message.data);
      const frame = this._options.lengthPrefixedFrames
        ? rsocketCore.deserializeFrameWithLength(buffer, this._encoders)
        : rsocketCore.deserializeFrame(buffer, this._encoders);
      return frame;
    }

    _writeFrame(frame) {
      try {
        if (false);
        const buffer = this._options.lengthPrefixedFrames
          ? rsocketCore.serializeFrameWithLength(frame, this._encoders)
          : rsocketCore.serializeFrame(frame, this._encoders);
        invariant_1(
          this._socket,
          'RSocketWebSocketClient: Cannot send frame, not connected.'
        );

        this._socket.send(buffer);
      } catch (error) {
        this._close(error);
      }
    }
  }

  /** Copyright (c) Facebook, Inc. and its affiliates.
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   *     http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   *
   *
   */

  return RSocketWebSocketClient;
})(rsocketFlowable, rsocketCore, rsocketTypes);
