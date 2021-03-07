const process = {
  "env": {
    "NODE_ENV": 'production'
  }
}
var rsocketTypes = (function (exports) {
  'use strict';

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
                         * A contract providing different interaction models per the [ReactiveSocket protocol]
                         (https://github.com/ReactiveSocket/reactivesocket/blob/master/Protocol.md).
                         */

  /**
   * Represents a network connection with input/output used by a ReactiveSocket to
   * send/receive data.
   */

  /**
   * Describes the connection status of a ReactiveSocket/DuplexConnection.
   * - NOT_CONNECTED: no connection established or pending.
   * - CONNECTING: when `connect()` has been called but a connection is not yet
   *   established.
   * - CONNECTED: when a connection is established.
   * - CLOSED: when the connection has been explicitly closed via `close()`.
   * - ERROR: when the connection has been closed for any other reason.
   */

  const CONNECTION_STATUS = {
    CLOSED: Object.freeze({kind: 'CLOSED'}),
    CONNECTED: Object.freeze({kind: 'CONNECTED'}),
    CONNECTING: Object.freeze({kind: 'CONNECTING'}),
    NOT_CONNECTED: Object.freeze({kind: 'NOT_CONNECTED'}),
  };

  /**
   * A type that can be written to a buffer.
   */

  exports.CONNECTION_STATUS = CONNECTION_STATUS;

  Object.defineProperty(exports, '__esModule', {value: true});

  return exports;
})({});
