var rsocketCore = (function (exports, rsocketFlowable, rsocketTypes) {
  'use strict';

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

  function ownKeys(object, enumerableOnly) {
    var keys = Object.keys(object);

    if (Object.getOwnPropertySymbols) {
      var symbols = Object.getOwnPropertySymbols(object);
      if (enumerableOnly)
        symbols = symbols.filter(function (sym) {
          return Object.getOwnPropertyDescriptor(object, sym).enumerable;
        });
      keys.push.apply(keys, symbols);
    }

    return keys;
  }

  function _objectSpread2(target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i] != null ? arguments[i] : {};

      if (i % 2) {
        ownKeys(Object(source), true).forEach(function (key) {
          _defineProperty(target, key, source[key]);
        });
      } else if (Object.getOwnPropertyDescriptors) {
        Object.defineProperties(
          target,
          Object.getOwnPropertyDescriptors(source)
        );
      } else {
        ownKeys(Object(source)).forEach(function (key) {
          Object.defineProperty(
            target,
            key,
            Object.getOwnPropertyDescriptor(source, key)
          );
        });
      }
    }

    return target;
  }

  /**
   * Copyright (c) 2013-present, Facebook, Inc.
   *
   * This source code is licensed under the MIT license found in the
   * LICENSE file in the root directory of this source tree.
   *
   * @typechecks
   */

  var hasOwnProperty = Object.prototype.hasOwnProperty;
  /**
   * Executes the provided `callback` once for each enumerable own property in the
   * object. The `callback` is invoked with three arguments:
   *
   *  - the property value
   *  - the property name
   *  - the object being traversed
   *
   * Properties that are added after the call to `forEachObject` will not be
   * visited by `callback`. If the values of existing properties are changed, the
   * value passed to `callback` will be the value at the time `forEachObject`
   * visits them. Properties that are deleted before being visited are not
   * visited.
   *
   * @param {?object} object
   * @param {function} callback
   * @param {*} context
   */

  function forEachObject(object, callback, context) {
    for (var name in object) {
      if (hasOwnProperty.call(object, name)) {
        callback.call(context, object[name], name, object);
      }
    }
  }

  var forEachObject_1 = forEachObject;

  /**
   * Copyright (c) 2013-present, Facebook, Inc.
   *
   * This source code is licensed under the MIT license found in the
   * LICENSE file in the root directory of this source tree.
   *
   * @typechecks
   */

  /**
   * Simple function for formatting strings.
   *
   * Replaces placeholders with values passed as extra arguments
   *
   * @param {string} format the base string
   * @param ...args the values to insert
   * @return {string} the replaced string
   */
  function sprintf(format) {
    for (
      var _len = arguments.length,
        args = new Array(_len > 1 ? _len - 1 : 0),
        _key = 1;
      _key < _len;
      _key++
    ) {
      args[_key - 1] = arguments[_key];
    }

    var index = 0;
    return format.replace(/%s/g, function (match) {
      return args[index++];
    });
  }

  var sprintf_1 = sprintf;

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

  const CONNECTION_STREAM_ID = 0;

  const FRAME_TYPES = {
    CANCEL: 0x09, // Cancel Request: Cancel outstanding request.
    ERROR: 0x0b, // Error: Error at connection or application level.
    EXT: 0x3f, // Extension Header: Used To Extend more frame types as well as extensions.
    KEEPALIVE: 0x03, // Keepalive: Connection keepalive.
    LEASE: 0x02, // Lease: Sent by Responder to grant the ability to send requests.
    METADATA_PUSH: 0x0c, // Metadata: Asynchronous Metadata frame
    PAYLOAD: 0x0a, // Payload: Payload on a stream. For example, response to a request, or message on a channel.
    REQUEST_CHANNEL: 0x07, // Request Channel: Request a completable stream in both directions.
    REQUEST_FNF: 0x05, // Fire And Forget: A single one-way message.
    REQUEST_N: 0x08, // Request N: Request N more items with Reactive Streams semantics.
    REQUEST_RESPONSE: 0x04, // Request Response: Request single response.
    REQUEST_STREAM: 0x06, // Request Stream: Request a completable stream.
    RESERVED: 0x00, // Reserved
    RESUME: 0x0d, // Resume: Replaces SETUP for Resuming Operation (optional)
    RESUME_OK: 0x0e, // Resume OK : Sent in response to a RESUME if resuming operation possible (optional)
    SETUP: 0x01, // Setup: Sent by client to initiate protocol processing.
  };

  // Maps frame type codes to type names
  const FRAME_TYPE_NAMES = {};
  forEachObject_1(FRAME_TYPES, (value, name) => {
    FRAME_TYPE_NAMES[value] = name;
  });

  const FLAGS = {
    COMPLETE: 0x40, // PAYLOAD, REQUEST_CHANNEL: indicates stream completion, if set onComplete will be invoked on receiver.
    FOLLOWS: 0x80, // PAYLOAD, REQUEST_XXX: indicates that frame was fragmented and requires reassembly
    IGNORE: 0x200, // (all): Ignore frame if not understood.
    LEASE: 0x40, // SETUP: Will honor lease or not.
    METADATA: 0x100, // (all): must be set if metadata is present in the frame.
    NEXT: 0x20, // PAYLOAD: indicates data/metadata present, if set onNext will be invoked on receiver.
    RESPOND: 0x80, // KEEPALIVE: should KEEPALIVE be sent by peer on receipt.
    RESUME_ENABLE: 0x80, // SETUP: Client requests resume capability if possible. Resume Identification Token present.
  };

  // Maps error names to codes
  const ERROR_CODES = {
    APPLICATION_ERROR: 0x00000201,
    CANCELED: 0x00000203,
    CONNECTION_CLOSE: 0x00000102,
    CONNECTION_ERROR: 0x00000101,
    INVALID: 0x00000204,
    INVALID_SETUP: 0x00000001,
    REJECTED: 0x00000202,
    REJECTED_RESUME: 0x00000004,
    REJECTED_SETUP: 0x00000003,
    RESERVED: 0x00000000,
    RESERVED_EXTENSION: 0xffffffff,
    UNSUPPORTED_SETUP: 0x00000002,
  };

  // Maps error codes to names
  const ERROR_EXPLANATIONS = {};
  forEachObject_1(ERROR_CODES, (code, explanation) => {
    ERROR_EXPLANATIONS[code] = explanation;
  });

  const FLAGS_MASK = 0x3ff; // low 10 bits
  const FRAME_TYPE_OFFFSET = 10; // frame type is offset 10 bytes within the uint16 containing type + flags

  const MAX_CODE = 0x7fffffff; // uint31
  const MAX_KEEPALIVE = 0x7fffffff; // uint31
  const MAX_LIFETIME = 0x7fffffff; // uint31
  const MAX_MIME_LENGTH = 0xff; // int8
  const MAX_REQUEST_N = 0x7fffffff; // uint31
  const MAX_RESUME_LENGTH = 0xffff; // uint16
  const MAX_STREAM_ID = 0x7fffffff; // uint31
  const MAX_VERSION = 0xffff; // uint16

  /**
   * Returns true iff the flags have the IGNORE bit set.
   */
  function isIgnore(flags) {
    return (flags & FLAGS.IGNORE) === FLAGS.IGNORE;
  }

  /**
   * Returns true iff the flags have the METADATA bit set.
   */
  function isMetadata(flags) {
    return (flags & FLAGS.METADATA) === FLAGS.METADATA;
  }

  /**
   * Returns true iff the flags have the COMPLETE bit set.
   */
  function isComplete(flags) {
    return (flags & FLAGS.COMPLETE) === FLAGS.COMPLETE;
  }

  /**
   * Returns true iff the flags have the NEXT bit set.
   */
  function isNext(flags) {
    return (flags & FLAGS.NEXT) === FLAGS.NEXT;
  }

  /**
   * Returns true iff the flags have the RESPOND bit set.
   */
  function isRespond(flags) {
    return (flags & FLAGS.RESPOND) === FLAGS.RESPOND;
  }

  /**
   * Returns true iff the flags have the RESUME_ENABLE bit set.
   */
  function isResumeEnable(flags) {
    return (flags & FLAGS.RESUME_ENABLE) === FLAGS.RESUME_ENABLE;
  }

  /**
   * Returns true iff the flags have the LEASE bit set.
   */
  function isLease(flags) {
    return (flags & FLAGS.LEASE) === FLAGS.LEASE;
  }

  function isFollows(flags) {
    return (flags & FLAGS.FOLLOWS) === FLAGS.FOLLOWS;
  }

  /**
   * Returns true iff the frame type is counted toward the implied
   * client/server position used for the resumption protocol.
   */
  function isResumePositionFrameType(type) {
    return (
      type === FRAME_TYPES.CANCEL ||
      type === FRAME_TYPES.ERROR ||
      type === FRAME_TYPES.PAYLOAD ||
      type === FRAME_TYPES.REQUEST_CHANNEL ||
      type === FRAME_TYPES.REQUEST_FNF ||
      type === FRAME_TYPES.REQUEST_RESPONSE ||
      type === FRAME_TYPES.REQUEST_STREAM ||
      type === FRAME_TYPES.REQUEST_N
    );
  }

  function getFrameTypeName(type) {
    const name = FRAME_TYPE_NAMES[type];
    return name != null ? name : toHex(type);
  }

  /**
   * Constructs an Error object given the contents of an error frame. The
   * `source` property contains metadata about the error for use in introspecting
   * the error at runtime:
   * - `error.source.code: number`: the error code returned by the server.
   * - `error.source.explanation: string`: human-readable explanation of the code
   *   (this value is not standardized and may change).
   * - `error.source.message: string`: the error string returned by the server.
   */
  function createErrorFromFrame(frame) {
    const {code, message} = frame;
    const explanation = getErrorCodeExplanation(code);
    const error = new Error(
      sprintf_1(
        'RSocket error %s (%s): %s. See error `source` property for details.',
        toHex(code),
        explanation,
        message
      )
    );

    error.source = {
      code,
      explanation,
      message,
    };

    return error;
  }

  /**
   * Given a RSocket error code, returns a human-readable explanation of that
   * code, following the names used in the protocol specification.
   */
  function getErrorCodeExplanation(code) {
    const explanation = ERROR_EXPLANATIONS[code];
    if (explanation != null) {
      return explanation;
    } else if (code <= 0x00300) {
      return 'RESERVED (PROTOCOL)';
    } else {
      return 'RESERVED (APPLICATION)';
    }
  }

  /**
   * Pretty-prints the frame for debugging purposes, with types, flags, and
   * error codes annotated with descriptive names.
   */
  function printFrame(frame) {
    const obj = _objectSpread2({}, frame);
    obj.type = getFrameTypeName(frame.type) + ` (${toHex(frame.type)})`;
    const flagNames = [];
    forEachObject_1(FLAGS, (flag, name) => {
      if ((frame.flags & flag) === flag) {
        flagNames.push(name);
      }
    });
    if (!flagNames.length) {
      flagNames.push('NO FLAGS');
    }
    obj.flags = flagNames.join(' | ') + ` (${toHex(frame.flags)})`;
    if (frame.type === FRAME_TYPES.ERROR) {
      obj.code =
        getErrorCodeExplanation(frame.code) + ` (${toHex(frame.code)})`;
    }
    return JSON.stringify(obj, null, 2);
  }

  function toHex(n) {
    return '0x' + n.toString(16);
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

  const MAJOR_VERSION = 1;
  const MINOR_VERSION = 0;

  /**
   * Copyright (c) 2013-present, Facebook, Inc.
   *
   * This source code is licensed under the MIT license found in the
   * LICENSE file in the root directory of this source tree.
   *
   *
   */
  function makeEmptyFunction(arg) {
    return function () {
      return arg;
    };
  }
  /**
   * This function accepts and discards inputs; it has no side effects. This is
   * primarily useful idiomatically for overridable function endpoints which
   * always need to be callable, since JS lacks a null-call idiom ala Cocoa.
   */

  var emptyFunction = function emptyFunction() {};

  emptyFunction.thatReturns = makeEmptyFunction;
  emptyFunction.thatReturnsFalse = makeEmptyFunction(false);
  emptyFunction.thatReturnsTrue = makeEmptyFunction(true);
  emptyFunction.thatReturnsNull = makeEmptyFunction(null);

  emptyFunction.thatReturnsThis = function () {
    return this;
  };

  emptyFunction.thatReturnsArgument = function (arg) {
    return arg;
  };

  var emptyFunction_1 = emptyFunction;

  /**
   * Similar to invariant but only logs a warning if the condition is not met.
   * This can be used to log issues in development environments in critical
   * paths. Removing the logging code for production environments will keep the
   * same logic and follow the same code paths.
   */

  function printWarning(format) {
    for (
      var _len = arguments.length,
        args = new Array(_len > 1 ? _len - 1 : 0),
        _key = 1;
      _key < _len;
      _key++
    ) {
      args[_key - 1] = arguments[_key];
    }

    var argIndex = 0;
    var message =
      'Warning: ' +
      format.replace(/%s/g, function () {
        return args[argIndex++];
      });

    if (typeof console !== 'undefined') {
      console.error(message);
    }

    try {
      // --- Welcome to debugging React ---
      // This error was thrown as a convenience so that you can use this stack
      // to find the callsite that caused this warning to fire.
      throw new Error(message);
    } catch (x) {}
  }

  var warning =
    process.env.NODE_ENV !== 'production'
      ? function (condition, format) {
          if (format === undefined) {
            throw new Error(
              '`warning(condition, format, ...args)` requires a warning ' +
                'message argument'
            );
          }

          if (!condition) {
            for (
              var _len2 = arguments.length,
                args = new Array(_len2 > 2 ? _len2 - 2 : 0),
                _key2 = 2;
              _key2 < _len2;
              _key2++
            ) {
              args[_key2 - 2] = arguments[_key2];
            }

            printWarning.apply(void 0, [format].concat(args));
          }
        }
      : emptyFunction_1;
  var warning_1 = warning;

  const bufferExists =
    typeof global !== 'undefined' && global.hasOwnProperty('Buffer');

  function notImplemented(msg) {
    const message = msg ? `Not implemented: ${msg}` : 'Not implemented';
    throw new Error(message);
  }

  // eslint-disable-next-line max-len
  // Taken from: https://github.com/nodejs/node/blob/ba684805b6c0eded76e5cd89ee00328ac7a59365/lib/internal/util.js#L125
  // Return undefined if there is no match.
  // Move the "slow cases" to a separate function to make sure this function gets
  // inlined properly. That prioritizes the common case.
  function normalizeEncoding(enc) {
    if (enc == null || enc === 'utf8' || enc === 'utf-8') {
      return 'utf8';
    }
    return slowCases(enc);
  }

  function isInstance(obj, type) {
    return (
      obj instanceof type ||
      (obj != null &&
        obj.constructor != null &&
        obj.constructor.name != null &&
        obj.constructor.name === type.name)
    );
  }

  // eslint-disable-next-line max-len
  // https://github.com/nodejs/node/blob/ba684805b6c0eded76e5cd89ee00328ac7a59365/lib/internal/util.js#L130
  function slowCases(enc) {
    switch (enc.length) {
      case 4:
        if (enc === 'UTF8') {
          return 'utf8';
        }
        if (enc === 'ucs2' || enc === 'UCS2') {
          return 'utf16le';
        }
        enc = `${enc}`.toLowerCase();
        if (enc === 'utf8') {
          return 'utf8';
        }
        if (enc === 'ucs2') {
          return 'utf16le';
        }
        break;
      case 3:
        if (
          enc === 'hex' ||
          enc === 'HEX' ||
          `${enc}`.toLowerCase() === 'hex'
        ) {
          return 'hex';
        }
        break;
      case 5:
        if (enc === 'ascii') {
          return 'ascii';
        }
        if (enc === 'ucs-2') {
          return 'utf16le';
        }
        if (enc === 'UTF-8') {
          return 'utf8';
        }
        if (enc === 'ASCII') {
          return 'ascii';
        }
        if (enc === 'UCS-2') {
          return 'utf16le';
        }
        enc = `${enc}`.toLowerCase();
        if (enc === 'utf-8') {
          return 'utf8';
        }
        if (enc === 'ascii') {
          return 'ascii';
        }
        if (enc === 'ucs-2') {
          return 'utf16le';
        }
        break;
      case 6:
        if (enc === 'base64') {
          return 'base64';
        }
        if (enc === 'latin1' || enc === 'binary') {
          return 'latin1';
        }
        if (enc === 'BASE64') {
          return 'base64';
        }
        if (enc === 'LATIN1' || enc === 'BINARY') {
          return 'latin1';
        }
        enc = `${enc}`.toLowerCase();
        if (enc === 'base64') {
          return 'base64';
        }
        if (enc === 'latin1' || enc === 'binary') {
          return 'latin1';
        }
        break;
      case 7:
        if (
          enc === 'utf16le' ||
          enc === 'UTF16LE' ||
          `${enc}`.toLowerCase() === 'utf16le'
        ) {
          return 'utf16le';
        }
        break;
      case 8:
        if (
          enc === 'utf-16le' ||
          enc === 'UTF-16LE' ||
          `${enc}`.toLowerCase() === 'utf-16le'
        ) {
          return 'utf16le';
        }
        break;
      default:
        if (enc === '') {
          return 'utf8';
        }
    }
  }

  const notImplementedEncodings = [
    'base64',
    'hex',
    'ascii',
    'binary',
    'latin1',
    'ucs2',
    'utf16le',
  ];

  function checkEncoding(encoding = 'utf8', strict = true) {
    if (typeof encoding !== 'string' || (strict && encoding === '')) {
      if (!strict) {
        return 'utf8';
      }
      throw new TypeError(`Unknown encoding: ${encoding}`);
    }

    const normalized = normalizeEncoding(encoding);

    if (normalized === undefined) {
      throw new TypeError(`Unknown encoding: ${encoding}`);
    }

    if (notImplementedEncodings.includes(encoding)) {
      notImplemented(`"${encoding}" encoding`);
    }

    return normalized;
  }

  // https://github.com/nodejs/node/blob/56dbe466fdbc598baea3bfce289bf52b97b8b8f7/lib/buffer.js#L598
  const encodingOps = {
    ascii: {
      byteLength: (string) => string.length,
    },

    base64: {
      byteLength: (string) => base64ByteLength(string, string.length),
    },

    hex: {
      byteLength: (string) => string.length >>> 1,
    },

    latin1: {
      byteLength: (string) => string.length,
    },

    ucs2: {
      byteLength: (string) => string.length * 2,
    },

    utf16le: {
      byteLength: (string) => string.length * 2,
    },

    utf8: {
      byteLength: (string) => utf8ToBytes(string).length,
    },
  };

  function base64ByteLength(str, bytes) {
    // Handle padding
    if (str.charCodeAt(bytes - 1) === 0x3d) {
      bytes--;
    }
    if (bytes > 1 && str.charCodeAt(bytes - 1) === 0x3d) {
      bytes--;
    }

    // Base64 ratio: 3/4
    // eslint-disable-next-line no-bitwise
    return (bytes * 3) >>> 2;
  }

  const MAX_ARGUMENTS_LENGTH = 0x1000;
  function decodeCodePointsArray(codePoints) {
    const len = codePoints.length;
    if (len <= MAX_ARGUMENTS_LENGTH) {
      return String.fromCharCode.apply(String, codePoints); // avoid extra slice()
    }

    // Decode in chunks to avoid "call stack size exceeded".
    let res = '';
    let i = 0;
    while (i < len) {
      res += String.fromCharCode.apply(
        String,
        codePoints.slice(i, (i += MAX_ARGUMENTS_LENGTH))
      );
    }
    return res;
  }

  function utf8ToBytes(str, pUnits = Infinity) {
    let units = pUnits;
    let codePoint;
    const length = str.length;
    let leadSurrogate = null;
    const bytes = [];

    for (let i = 0; i < length; ++i) {
      codePoint = str.charCodeAt(i);

      // is surrogate component
      if (codePoint > 0xd7ff && codePoint < 0xe000) {
        // last char was a lead
        if (!leadSurrogate) {
          // no lead yet
          if (codePoint > 0xdbff) {
            // unexpected trail
            if ((units -= 3) > -1) {
              bytes.push(0xef, 0xbf, 0xbd);
            }
            continue;
          } else if (i + 1 === length) {
            // unpaired lead
            if ((units -= 3) > -1) {
              bytes.push(0xef, 0xbf, 0xbd);
            }
            continue;
          }

          // valid lead
          leadSurrogate = codePoint;

          continue;
        }

        // 2 leads in a row
        if (codePoint < 0xdc00) {
          if ((units -= 3) > -1) {
            bytes.push(0xef, 0xbf, 0xbd);
          }
          leadSurrogate = codePoint;
          continue;
        }

        // valid surrogate pair
        codePoint =
          (((leadSurrogate - 0xd800) << 10) | (codePoint - 0xdc00)) + 0x10000;
      } else if (leadSurrogate) {
        // valid bmp char, but last char was a lead
        if ((units -= 3) > -1) {
          bytes.push(0xef, 0xbf, 0xbd);
        }
      }

      leadSurrogate = null;

      // encode utf8
      if (codePoint < 0x80) {
        if ((units -= 1) < 0) {
          break;
        }
        bytes.push(codePoint);
      } else if (codePoint < 0x800) {
        if ((units -= 2) < 0) {
          break;
        }
        bytes.push((codePoint >> 0x6) | 0xc0, (codePoint & 0x3f) | 0x80);
      } else if (codePoint < 0x10000) {
        if ((units -= 3) < 0) {
          break;
        }
        bytes.push(
          (codePoint >> 0xc) | 0xe0,
          ((codePoint >> 0x6) & 0x3f) | 0x80,
          (codePoint & 0x3f) | 0x80
        );
      } else if (codePoint < 0x110000) {
        if ((units -= 4) < 0) {
          break;
        }
        bytes.push(
          (codePoint >> 0x12) | 0xf0,
          ((codePoint >> 0xc) & 0x3f) | 0x80,
          ((codePoint >> 0x6) & 0x3f) | 0x80,
          (codePoint & 0x3f) | 0x80
        );
      } else {
        throw new Error('Invalid code point');
      }
    }

    return bytes;
  }

  function utf8Slice(buf, start, end) {
    end = Math.min(buf.length, end);
    const res = [];

    let i = start;
    while (i < end) {
      const firstByte = buf[i];
      let codePoint = null;
      let bytesPerSequence =
        firstByte > 0xef ? 4 : firstByte > 0xdf ? 3 : firstByte > 0xbf ? 2 : 1;

      if (i + bytesPerSequence <= end) {
        let secondByte, thirdByte, fourthByte, tempCodePoint;

        switch (bytesPerSequence) {
          case 1:
            if (firstByte < 0x80) {
              codePoint = firstByte;
            }
            break;
          case 2:
            secondByte = buf[i + 1];
            if ((secondByte & 0xc0) === 0x80) {
              tempCodePoint = ((firstByte & 0x1f) << 0x6) | (secondByte & 0x3f);
              if (tempCodePoint > 0x7f) {
                codePoint = tempCodePoint;
              }
            }
            break;
          case 3:
            secondByte = buf[i + 1];
            thirdByte = buf[i + 2];
            if ((secondByte & 0xc0) === 0x80 && (thirdByte & 0xc0) === 0x80) {
              tempCodePoint =
                ((firstByte & 0xf) << 0xc) |
                ((secondByte & 0x3f) << 0x6) |
                (thirdByte & 0x3f);
              if (
                tempCodePoint > 0x7ff &&
                (tempCodePoint < 0xd800 || tempCodePoint > 0xdfff)
              ) {
                codePoint = tempCodePoint;
              }
            }
            break;
          case 4:
            secondByte = buf[i + 1];
            thirdByte = buf[i + 2];
            fourthByte = buf[i + 3];
            if (
              (secondByte & 0xc0) === 0x80 &&
              (thirdByte & 0xc0) === 0x80 &&
              (fourthByte & 0xc0) === 0x80
            ) {
              tempCodePoint =
                ((firstByte & 0xf) << 0x12) |
                ((secondByte & 0x3f) << 0xc) |
                ((thirdByte & 0x3f) << 0x6) |
                (fourthByte & 0x3f);
              if (tempCodePoint > 0xffff && tempCodePoint < 0x110000) {
                codePoint = tempCodePoint;
              }
            }
        }
      }

      if (codePoint === null) {
        // we did not generate a valid codePoint so insert a
        // replacement char (U+FFFD) and advance only 1 byte
        codePoint = 0xfffd;
        bytesPerSequence = 1;
      } else if (codePoint > 0xffff) {
        // encode to utf16 (surrogate pair dance)
        codePoint -= 0x10000;
        res.push(((codePoint >>> 10) & 0x3ff) | 0xd800);
        codePoint = 0xdc00 | (codePoint & 0x3ff);
      }

      res.push(codePoint);
      i += bytesPerSequence;
    }

    return decodeCodePointsArray(res);
  }

  function utf8Write(buf, input, offset, length) {
    return blitBuffer(
      utf8ToBytes(input, buf.length - offset),
      buf,
      offset,
      length
    );
  }

  function blitBuffer(src, dst, offset, length) {
    let i = 0;
    for (; i < length; ++i) {
      if (i + offset >= dst.length || i >= src.length) {
        break;
      }
      dst[i + offset] = src[i];
    }
    return i;
  }

  /**
   * See also https://nodejs.org/api/buffer.html
   */
  class Buffer$1 extends Uint8Array {
    constructor(value, byteOffset, length) {
      super(value, byteOffset, length);
    }
    /**
     * Allocates a new Buffer of size bytes.
     */
    static alloc(size, fill = 0, encoding = 'utf8') {
      if (typeof size !== 'number') {
        throw new TypeError(
          `The "size" argument must be of type number. Received type ${typeof size}`
        );
      }

      const buf = new Buffer$1(size);
      if (size === 0) {
        return buf;
      }

      let bufFill;
      if (typeof fill === 'string') {
        encoding = checkEncoding(encoding);
        if (fill.length === 1 && encoding === 'utf8') {
          buf.fill(fill.charCodeAt(0));
        } else {
          bufFill = Buffer$1.from(fill, encoding);
        }
      } else if (typeof fill === 'number') {
        buf.fill(fill);
      } else if (isInstance(fill, Uint8Array)) {
        if (fill.length === 0) {
          throw new TypeError(
            `The argument "value" is invalid. Received ${fill.constructor.name} []`
          );
        }

        bufFill = fill;
      }

      if (bufFill) {
        if (bufFill.length > buf.length) {
          bufFill = bufFill.subarray(0, buf.length);
        }

        let offset = 0;
        while (offset < size) {
          buf.set(bufFill, offset);
          offset += bufFill.length;
          if (offset + bufFill.length >= size) {
            break;
          }
        }
        if (offset !== size) {
          buf.set(bufFill.subarray(0, size - offset), offset);
        }
      }

      return buf;
    }

    static allocUnsafe(size) {
      return new Buffer$1(size);
    }

    /**
     * Returns the byte length of a string when encoded. This is not the same as
     * String.prototype.length, which does not account for the encoding that is
     * used to convert the string into bytes.
     */
    static byteLength(string, encoding = 'utf8') {
      if (typeof string != 'string') {
        return string.byteLength;
      }

      encoding = normalizeEncoding(encoding) || 'utf8';
      return encodingOps[encoding].byteLength(string);
    }

    /**
     * Returns a new Buffer which is the result of concatenating all the Buffer
     * instances in the list together.
     */
    static concat(list, totalLength) {
      if (totalLength == undefined) {
        totalLength = 0;
        for (const buf of list) {
          totalLength += buf.length;
        }
      }

      const buffer = new Buffer$1(totalLength);
      let pos = 0;
      for (const buf of list) {
        buffer.set(buf, pos);
        pos += buf.length;
      }

      return buffer;
    }

    /**
     * This creates a view of the ArrayBuffer without copying the underlying
     * memory. For example, when passed a reference to the .buffer property of a
     * TypedArray instance, the newly created Buffer will share the same allocated
     * memory as the TypedArray.
     */
    //$FlowFixMe
    static from(
      value,
      byteOffsetOrEncoding,
      //$FlowFixMe
      length
    ) {
      const offset =
        typeof byteOffsetOrEncoding === 'string'
          ? undefined
          : byteOffsetOrEncoding;
      let encoding =
        typeof byteOffsetOrEncoding === 'string'
          ? byteOffsetOrEncoding
          : undefined;

      if (typeof value == 'string') {
        encoding = checkEncoding(encoding, false);
        // if (encoding === 'hex') {return new Buffer(hex.decodeString(value).buffer);}
        // if (encoding === 'base64') {return new Buffer(base64.decode(value));}

        switch (encoding) {
          case 'utf8':
            if (typeof TextEncoder !== 'undefined') {
              return new Buffer$1(new TextEncoder().encode(value).buffer);
            }
            return new Buffer$1(utf8ToBytes(value));
          default:
            throw new TypeError('Unknown encoding: ' + encoding);
        }
      }

      // workaround for https://github.com/microsoft/TypeScript/issues/38446
      return new Buffer$1(value, offset, length);
    }

    /**
     * Returns true if obj is a Buffer, false otherwise.
     */
    static isBuffer(obj) {
      return isInstance(obj, Buffer$1);
    }

    static isEncoding(encoding) {
      return (
        typeof encoding === 'string' &&
        encoding.length !== 0 &&
        normalizeEncoding(encoding) !== undefined
      );
    }

    /**
     * Copies data from a region of buf to a region in target, even if the target
     * memory region overlaps with buf.
     */
    copy(
      targetBuffer,
      targetStart = 0,
      sourceStart = 0,
      sourceEnd = this.length
    ) {
      const sourceBuffer = this.subarray(sourceStart, sourceEnd);
      targetBuffer.set(sourceBuffer, targetStart);
      return sourceBuffer.length;
    }

    /*
     * Returns true if both buf and otherBuffer have exactly the same bytes, false otherwise.
     */
    equals(otherBuffer) {
      if (!isInstance(otherBuffer, Uint8Array)) {
        throw new TypeError(
          // eslint-disable-next-line max-len
          `The "otherBuffer" argument must be an instance of Buffer or Uint8Array. Received type ${typeof otherBuffer}`
        );
      }

      if (this === otherBuffer) {
        return true;
      }
      if (this.byteLength !== otherBuffer.byteLength) {
        return false;
      }

      for (let i = 0; i < this.length; i++) {
        if (this[i] !== otherBuffer[i]) {
          return false;
        }
      }

      return true;
    }

    readDoubleBE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getFloat64(offset);
    }

    readDoubleLE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getFloat64(offset, true);
    }

    readFloatBE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getFloat32(offset);
    }

    readFloatLE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getFloat32(offset, true);
    }

    readInt8(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getInt8(offset);
    }

    readInt16BE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getInt16(offset);
    }

    readInt16LE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getInt16(offset, true);
    }

    readInt32BE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getInt32(offset);
    }

    readInt32LE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getInt32(offset, true);
    }

    readUInt8(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getUint8(offset);
    }

    readUInt16BE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getUint16(offset);
    }

    readUInt16LE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getUint16(offset, true);
    }

    readUInt32BE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getUint32(offset);
    }

    readUInt32LE(offset = 0) {
      return new DataView(
        this.buffer,
        this.byteOffset,
        this.byteLength
      ).getUint32(offset, true);
    }

    /**
     * Returns a new Buffer that references the same memory as the original, but
     * offset and cropped by the start and end indices.
     */
    // $FlowFixMe
    slice(begin = 0, end = this.length) {
      // workaround for https://github.com/microsoft/TypeScript/issues/38665
      return this.subarray(begin, end);
    }

    // $FlowFixMe
    subarray(begin = 0, end = this.length) {
      return new Buffer$1(super.subarray(begin, end));
    }

    /**
     * Returns a JSON representation of buf. JSON.stringify() implicitly calls
     * this function when stringifying a Buffer instance.
     */
    toJSON() {
      return {data: Array.from(this), type: 'Buffer'};
    }

    /**
     * Decodes buf to a string according to the specified character encoding in
     * encoding. start and end may be passed to decode only a subset of buf.
     */
    toString(encoding = 'utf8', start = 0, end = this.length) {
      encoding = checkEncoding(encoding);

      if (typeof TextDecoder !== 'undefined') {
        const b = this.subarray(start, end);
        // if (encoding === 'hex') {return hex.encodeToString(b);}
        // if (encoding === 'base64') {return base64.encode(b.buffer);}

        return new TextDecoder().decode(b);
      }

      return this.slowToString(encoding, start, end);
    }

    slowToString(encoding = 'utf8', start = 0, end = this.length) {
      if (start === undefined || start < 0) {
        start = 0;
      }

      if (start > this.length) {
        return '';
      }

      if (end === undefined || end > this.length) {
        end = this.length;
      }

      if (end <= 0) {
        return '';
      }

      // Force coersion to uint32. This will also coerce falsey/NaN values to 0.
      end >>>= 0;
      start >>>= 0;

      if (end <= start) {
        return '';
      }

      encoding = checkEncoding(encoding);
      switch (encoding) {
        case 'utf8':
          return utf8Slice(this, start, end);
        default:
          throw new TypeError('Unsupported encoding: ' + encoding);
      }
    }

    /**
     * Writes string to buf at offset according to the character encoding in
     * encoding. The length parameter is the number of bytes to write. If buf did
     * not contain enough space to fit the entire string, only part of string will
     * be written. However, partially encoded characters will not be written.
     */
    write(string, offset = 0, length = this.length, encoding = 'utf8') {
      encoding = checkEncoding(encoding);
      switch (encoding) {
        case 'utf8':
          if (typeof TextEncoder !== 'undefined') {
            // $FlowFixMe
            const resultArray = new TextEncoder().encode(string);
            this.set(resultArray, offset);

            return resultArray.byteLength > length - offset
              ? length - offset
              : resultArray.byteLength;
          }
          return utf8Write(this, string, offset, length);
        default:
          throw new TypeError('Unknown encoding: ' + encoding);
      }
    }

    writeDoubleBE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setFloat64(
        offset,
        value
      );

      return offset + 8;
    }

    writeDoubleLE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setFloat64(
        offset,
        value,
        true
      );

      return offset + 8;
    }

    writeFloatBE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setFloat32(
        offset,
        value
      );

      return offset + 4;
    }

    writeFloatLE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setFloat32(
        offset,
        value,
        true
      );

      return offset + 4;
    }

    writeInt8(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setInt8(
        offset,
        value
      );

      return offset + 1;
    }

    writeInt16BE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setInt16(
        offset,
        value
      );

      return offset + 2;
    }

    writeInt16LE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setInt16(
        offset,
        value,
        true
      );

      return offset + 2;
    }

    writeInt32BE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setUint32(
        offset,
        value
      );

      return offset + 4;
    }

    writeInt32LE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setInt32(
        offset,
        value,
        true
      );

      return offset + 4;
    }

    writeUInt8(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setUint8(
        offset,
        value
      );

      return offset + 1;
    }

    writeUInt16BE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setUint16(
        offset,
        value
      );

      return offset + 2;
    }

    writeUInt16LE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setUint16(
        offset,
        value,
        true
      );

      return offset + 2;
    }

    writeUInt32BE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setUint32(
        offset,
        value
      );

      return offset + 4;
    }

    writeUInt32LE(value, offset = 0) {
      new DataView(this.buffer, this.byteOffset, this.byteLength).setUint32(
        offset,
        value,
        true
      );

      return offset + 4;
    }
  }

  if (!bufferExists) {
    // eslint-disable-next-line no-undef
    Object.defineProperty(window, 'Buffer', {
      configurable: true,
      enumerable: false,
      value: Buffer$1,
      writable: true,
    });
  }

  const LiteBuffer = bufferExists ? global.Buffer : Buffer$1;

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
   * A Serializer transforms data between the application encoding used in
   * Payloads and the Encodable type accepted by the transport client.
   */

  // JSON serializer
  const JsonSerializer = {
    deserialize: (data) => {
      let str;
      if (data == null) {
        return null;
      } else if (typeof data === 'string') {
        str = data;
      } else if (LiteBuffer.isBuffer(data)) {
        const buffer = data;
        str = buffer.toString('utf8');
      } else {
        const buffer = LiteBuffer.from(data);
        str = buffer.toString('utf8');
      }
      return JSON.parse(str);
    },
    serialize: JSON.stringify,
  };

  const JsonSerializers = {
    data: JsonSerializer,
    metadata: JsonSerializer,
  };

  // Pass-through serializer
  const IdentitySerializer = {
    deserialize: (data) => {
      invariant_1(
        data == null ||
          typeof data === 'string' ||
          LiteBuffer.isBuffer(data) ||
          data instanceof Uint8Array,
        'RSocketSerialization: Expected data to be a string, Buffer, or ' +
          'Uint8Array. Got `%s`.',
        data
      );

      return data;
    },
    serialize: (data) => data,
  };

  const IdentitySerializers = {
    data: IdentitySerializer,
    metadata: IdentitySerializer,
  };

  /** Copyright 2015-2019 the original author or authors.
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

  class Lease {
    constructor(timeToLiveMillis, allowedRequests, metadata) {
      invariant_1(timeToLiveMillis > 0, 'Lease time-to-live must be positive');
      invariant_1(
        allowedRequests > 0,
        'Lease allowed requests must be positive'
      );
      this.timeToLiveMillis = timeToLiveMillis;
      this.allowedRequests = allowedRequests;
      this.startingAllowedRequests = allowedRequests;
      this.expiry = Date.now() + timeToLiveMillis;
      this.metadata = metadata;
    }

    expired() {
      return Date.now() > this.expiry;
    }

    valid() {
      return this.allowedRequests > 0 && !this.expired();
    }

    // todo hide
    _use() {
      if (this.expired()) {
        return false;
      }
      const allowed = this.allowedRequests;
      const success = allowed > 0;
      if (success) {
        this.allowedRequests = allowed - 1;
      }
      return success;
    }
  }

  class Leases {
    constructor() {
      _defineProperty(this, '_sender', () => rsocketFlowable.Flowable.never());
      _defineProperty(this, '_receiver', (leases) => {});
    }

    sender(sender) {
      this._sender = sender;
      return this;
    }

    receiver(receiver) {
      this._receiver = receiver;
      return this;
    }

    stats(stats) {
      this._stats = stats;
      return this;
    }
  }

  class RequesterLeaseHandler {
    /*negative value means received lease was not signalled due to missing requestN*/

    constructor(leaseReceiver) {
      _defineProperty(this, '_requestN', -1);
      leaseReceiver(
        new rsocketFlowable.Flowable((subscriber) => {
          if (this._subscriber) {
            subscriber.onError(new Error('only 1 subscriber is allowed'));
            return;
          }
          if (this.isDisposed()) {
            subscriber.onComplete();
            return;
          }
          this._subscriber = subscriber;
          subscriber.onSubscribe({
            cancel: () => {
              this.dispose();
            },
            request: (n) => {
              if (n <= 0) {
                subscriber.onError(
                  new Error(`request demand must be positive: ${n}`)
                );
              }
              if (!this.isDisposed()) {
                const curReqN = this._requestN;
                this._onRequestN(curReqN);
                this._requestN = Math.min(
                  Number.MAX_SAFE_INTEGER,
                  Math.max(0, curReqN) + n
                );
              }
            },
          });
        })
      );
    }

    use() {
      const l = this._lease;
      return l ? l._use() : false;
    }

    errorMessage() {
      return _errorMessage(this._lease);
    }

    receive(frame) {
      if (!this.isDisposed()) {
        const timeToLiveMillis = frame.ttl;
        const requestCount = frame.requestCount;
        const metadata = frame.metadata;
        this._onLease(new Lease(timeToLiveMillis, requestCount, metadata));
      }
    }

    availability() {
      const l = this._lease;
      if (l && l.valid()) {
        return l.allowedRequests / l.startingAllowedRequests;
      }
      return 0.0;
    }

    dispose() {
      if (!this._isDisposed) {
        this._isDisposed = true;
        const s = this._subscriber;
        if (s) {
          s.onComplete();
        }
      }
    }

    isDisposed() {
      return this._isDisposed;
    }

    _onRequestN(requestN) {
      const l = this._lease;
      const s = this._subscriber;
      if (requestN < 0 && l && s) {
        s.onNext(l);
      }
    }

    _onLease(lease) {
      const s = this._subscriber;
      const newReqN = this._requestN - 1;
      if (newReqN >= 0 && s) {
        s.onNext(lease);
      }
      this._requestN = Math.max(-1, newReqN);
      this._lease = lease;
    }
  }

  class ResponderLeaseHandler {
    constructor(leaseSender, stats, errorConsumer) {
      this._leaseSender = leaseSender;
      this._stats = stats;
      this._errorConsumer = errorConsumer;
    }

    use() {
      const l = this._lease;
      const success = l ? l._use() : false;
      this._onStatsEvent(success);
      return success;
    }

    errorMessage() {
      return _errorMessage(this._lease);
    }

    send(send) {
      let subscription;
      let isDisposed;

      this._leaseSender(this._stats).subscribe({
        onComplete: () => this._onStatsEvent(),
        onError: (error) => {
          this._onStatsEvent();
          const errConsumer = this._errorConsumer;
          if (errConsumer) {
            errConsumer(error);
          }
        },
        onNext: (lease) => {
          this._lease = lease;
          send(lease);
        },
        onSubscribe: (s) => {
          if (isDisposed) {
            s.cancel();
            return;
          }
          s.request(MAX_REQUEST_N);
          subscription = s;
        },
      });

      return {
        dispose() {
          if (!isDisposed) {
            isDisposed = true;
            this._onStatsEvent();
            if (subscription) {
              subscription.cancel();
            }
          }
        },

        isDisposed() {
          return isDisposed;
        },
      };
    }

    _onStatsEvent(success) {
      const s = this._stats;
      if (s) {
        const event =
          success === undefined ? 'Terminate' : success ? 'Accept' : 'Reject';
        s.onEvent(event);
      }
    }
  }

  function _errorMessage(lease) {
    if (!lease) {
      return 'Lease was not received yet';
    }
    if (lease.valid()) {
      return 'Missing leases';
    } else {
      const isExpired = lease.expired();
      const requests = lease.allowedRequests;
      return `Missing leases. Expired: ${isExpired.toString()}, allowedRequests: ${requests}`;
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

  class ResponderWrapper {
    constructor(responder) {
      this._responder = responder || {};
    }

    setResponder(responder) {
      this._responder = responder || {};
    }

    fireAndForget(payload) {
      if (this._responder.fireAndForget) {
        try {
          this._responder.fireAndForget(payload);
        } catch (error) {
          console.error('fireAndForget threw an exception', error);
        }
      }
    }

    requestResponse(payload) {
      let error;
      if (this._responder.requestResponse) {
        try {
          return this._responder.requestResponse(payload);
        } catch (_error) {
          console.error('requestResponse threw an exception', _error);
          error = _error;
        }
      }
      return rsocketFlowable.Single.error(
        error || new Error('not implemented')
      );
    }

    requestStream(payload) {
      let error;
      if (this._responder.requestStream) {
        try {
          return this._responder.requestStream(payload);
        } catch (_error) {
          console.error('requestStream threw an exception', _error);
          error = _error;
        }
      }
      return rsocketFlowable.Flowable.error(
        error || new Error('not implemented')
      );
    }

    requestChannel(payloads) {
      let error;
      if (this._responder.requestChannel) {
        try {
          return this._responder.requestChannel(payloads);
        } catch (_error) {
          console.error('requestChannel threw an exception', _error);
          error = _error;
        }
      }
      return rsocketFlowable.Flowable.error(
        error || new Error('not implemented')
      );
    }

    metadataPush(payload) {
      let error;
      if (this._responder.metadataPush) {
        try {
          return this._responder.metadataPush(payload);
        } catch (_error) {
          console.error('metadataPush threw an exception', _error);
          error = _error;
        }
      }
      return rsocketFlowable.Single.error(
        error || new Error('not implemented')
      );
    }
  }

  function createServerMachine(
    connection,
    connectionPublisher,
    keepAliveTimeout,
    serializers,
    errorHandler,
    requesterLeaseHandler,
    responderLeaseHandler
  ) {
    return new RSocketMachineImpl(
      'SERVER',
      connection,
      connectionPublisher,
      keepAliveTimeout,
      serializers,
      undefined,
      errorHandler,
      requesterLeaseHandler,
      responderLeaseHandler
    );
  }

  function createClientMachine(
    connection,
    connectionPublisher,
    keepAliveTimeout,
    serializers,
    requestHandler,
    errorHandler,
    requesterLeaseHandler,
    responderLeaseHandler
  ) {
    return new RSocketMachineImpl(
      'CLIENT',
      connection,
      connectionPublisher,
      keepAliveTimeout,
      serializers,
      requestHandler,
      errorHandler,
      requesterLeaseHandler,
      responderLeaseHandler
    );
  }

  class RSocketMachineImpl {
    constructor(
      role,
      connection,
      connectionPublisher,
      keepAliveTimeout,
      serializers,
      requestHandler,
      errorHandler,
      requesterLeaseHandler,
      responderLeaseHandler
    ) {
      _defineProperty(this, '_connectionAvailability', 1.0);
      _defineProperty(
        this,
        '_handleTransportClose',

        () => {
          this._handleError(new Error('RSocket: The connection was closed.'));
        }
      );
      _defineProperty(
        this,
        '_handleError',

        (error) => {
          // Error any open request streams
          this._receivers.forEach((receiver) => {
            receiver.onError(error);
          });
          this._receivers.clear();
          // Cancel any active subscriptions
          this._subscriptions.forEach((subscription) => {
            subscription.cancel();
          });
          this._subscriptions.clear();
          this._connectionAvailability = 0.0;
          this._dispose(
            this._requesterLeaseHandler,
            this._responderLeaseSenderDisposable
          );

          const handle = this._keepAliveTimerHandle;
          if (handle) {
            clearTimeout(handle);
            this._keepAliveTimerHandle = null;
          }
        }
      );
      _defineProperty(
        this,
        '_handleFrame',

        (frame) => {
          const {streamId} = frame;
          if (streamId === CONNECTION_STREAM_ID) {
            this._handleConnectionFrame(frame);
          } else {
            this._handleStreamFrame(streamId, frame);
          }
        }
      );
      this._connection = connection;
      this._requesterLeaseHandler = requesterLeaseHandler;
      this._responderLeaseHandler = responderLeaseHandler;
      this._nextStreamId = role === 'CLIENT' ? 1 : 2;
      this._receivers = new Map();
      this._subscriptions = new Map();
      this._serializers = serializers || IdentitySerializers;
      this._requestHandler = new ResponderWrapper(requestHandler);
      this._errorHandler = errorHandler; // Subscribe to completion/errors before sending anything
      connectionPublisher({
        onComplete: this._handleTransportClose,
        onError: this._handleError,
        onNext: this._handleFrame,
        onSubscribe: (subscription) =>
          subscription.request(Number.MAX_SAFE_INTEGER),
      });
      const responderHandler = this._responderLeaseHandler;
      if (responderHandler) {
        this._responderLeaseSenderDisposable = responderHandler.send(
          this._leaseFrameSender()
        );
      } // Cleanup when the connection closes
      this._connection.connectionStatus().subscribe({
        onNext: (status) => {
          if (status.kind === 'CLOSED') {
            this._handleTransportClose();
          } else if (status.kind === 'ERROR') {
            this._handleError(status.error);
          }
        },
        onSubscribe: (subscription) =>
          subscription.request(Number.MAX_SAFE_INTEGER),
      });
      const MIN_TICK_DURATION = 100;
      this._keepAliveLastReceivedMillis = Date.now();
      const keepAliveHandler = () => {
        const now = Date.now();
        const noKeepAliveDuration = now - this._keepAliveLastReceivedMillis;
        if (noKeepAliveDuration >= keepAliveTimeout) {
          this._handleConnectionError(
            new Error(`No keep-alive acks for ${keepAliveTimeout} millis`)
          );
        } else {
          this._keepAliveTimerHandle = setTimeout(
            keepAliveHandler,
            Math.max(MIN_TICK_DURATION, keepAliveTimeout - noKeepAliveDuration)
          );
        }
      };
      this._keepAliveTimerHandle = setTimeout(
        keepAliveHandler,
        keepAliveTimeout
      );
    }
    setRequestHandler(requestHandler) {
      this._requestHandler.setResponder(requestHandler);
    }
    close() {
      this._connection.close();
    }
    connectionStatus() {
      return this._connection.connectionStatus();
    }
    availability() {
      const r = this._requesterLeaseHandler;
      const requesterAvailability = r ? r.availability() : 1.0;
      return Math.min(this._connectionAvailability, requesterAvailability);
    }
    fireAndForget(payload) {
      if (this._useLeaseOrError(this._requesterLeaseHandler)) {
        return;
      }
      const streamId = this._getNextStreamId(this._receivers);
      const data = this._serializers.data.serialize(payload.data);
      const metadata = this._serializers.metadata.serialize(payload.metadata);
      const frame = {
        data,
        flags: payload.metadata !== undefined ? FLAGS.METADATA : 0,
        metadata,
        streamId,
        type: FRAME_TYPES.REQUEST_FNF,
      };
      this._connection.sendOne(frame);
    }
    requestResponse(payload) {
      const leaseError = this._useLeaseOrError(this._requesterLeaseHandler);
      if (leaseError) {
        return rsocketFlowable.Single.error(new Error(leaseError));
      }
      const streamId = this._getNextStreamId(this._receivers);
      return new rsocketFlowable.Single((subscriber) => {
        this._receivers.set(streamId, {
          onComplete: emptyFunction_1,
          onError: (error) => subscriber.onError(error),
          onNext: (data) => subscriber.onComplete(data),
        });
        const data = this._serializers.data.serialize(payload.data);
        const metadata = this._serializers.metadata.serialize(payload.metadata);
        const frame = {
          data,
          flags: payload.metadata !== undefined ? FLAGS.METADATA : 0,
          metadata,
          streamId,
          type: FRAME_TYPES.REQUEST_RESPONSE,
        };
        this._connection.sendOne(frame);
        subscriber.onSubscribe(() => {
          this._receivers.delete(streamId);
          const cancelFrame = {flags: 0, streamId, type: FRAME_TYPES.CANCEL};
          this._connection.sendOne(cancelFrame);
        });
      });
    }
    requestStream(payload) {
      const leaseError = this._useLeaseOrError(this._requesterLeaseHandler);
      if (leaseError) {
        return rsocketFlowable.Flowable.error(new Error(leaseError));
      }
      const streamId = this._getNextStreamId(this._receivers);
      return new rsocketFlowable.Flowable((subscriber) => {
        this._receivers.set(streamId, subscriber);
        let initialized = false;
        subscriber.onSubscribe({
          cancel: () => {
            this._receivers.delete(streamId);
            if (!initialized) {
              return;
            }
            const cancelFrame = {flags: 0, streamId, type: FRAME_TYPES.CANCEL};
            this._connection.sendOne(cancelFrame);
          },
          request: (n) => {
            if (n > MAX_REQUEST_N) {
              n = MAX_REQUEST_N;
            }
            if (initialized) {
              const requestNFrame = {
                flags: 0,
                requestN: n,
                streamId,
                type: FRAME_TYPES.REQUEST_N,
              };
              this._connection.sendOne(requestNFrame);
            } else {
              initialized = true;
              const data = this._serializers.data.serialize(payload.data);
              const metadata = this._serializers.metadata.serialize(
                payload.metadata
              );
              const requestStreamFrame = {
                data,
                flags: payload.metadata !== undefined ? FLAGS.METADATA : 0,
                metadata,
                requestN: n,
                streamId,
                type: FRAME_TYPES.REQUEST_STREAM,
              };
              this._connection.sendOne(requestStreamFrame);
            }
          },
        });
      }, MAX_REQUEST_N);
    }
    requestChannel(payloads) {
      const leaseError = this._useLeaseOrError(this._requesterLeaseHandler);
      if (leaseError) {
        return rsocketFlowable.Flowable.error(new Error(leaseError));
      }
      const streamId = this._getNextStreamId(this._receivers);
      let payloadsSubscribed = false;
      return new rsocketFlowable.Flowable((subscriber) => {
        try {
          this._receivers.set(streamId, subscriber);
          let initialized = false;
          subscriber.onSubscribe({
            cancel: () => {
              this._receivers.delete(streamId);
              if (!initialized) {
                return;
              }
              const cancelFrame = {
                flags: 0,
                streamId,
                type: FRAME_TYPES.CANCEL,
              };
              this._connection.sendOne(cancelFrame);
            },
            request: (n) => {
              if (n > MAX_REQUEST_N) {
                n = MAX_REQUEST_N;
              }
              if (initialized) {
                const requestNFrame = {
                  flags: 0,
                  requestN: n,
                  streamId,
                  type: FRAME_TYPES.REQUEST_N,
                };
                this._connection.sendOne(requestNFrame);
              } else {
                if (!payloadsSubscribed) {
                  payloadsSubscribed = true;
                  payloads.subscribe({
                    onComplete: () => {
                      this._sendStreamComplete(streamId);
                    },
                    onError: (error) => {
                      this._sendStreamError(streamId, error.message);
                    }, //Subscriber methods
                    onNext: (payload) => {
                      const data = this._serializers.data.serialize(
                        payload.data
                      );
                      const metadata = this._serializers.metadata.serialize(
                        payload.metadata
                      );
                      if (!initialized) {
                        initialized = true;
                        const requestChannelFrame = {
                          data,
                          flags:
                            payload.metadata !== undefined ? FLAGS.METADATA : 0,
                          metadata,
                          requestN: n,
                          streamId,
                          type: FRAME_TYPES.REQUEST_CHANNEL,
                        };
                        this._connection.sendOne(requestChannelFrame);
                      } else {
                        const payloadFrame = {
                          data,
                          flags:
                            FLAGS.NEXT |
                            (payload.metadata !== undefined
                              ? FLAGS.METADATA
                              : 0),
                          metadata,
                          streamId,
                          type: FRAME_TYPES.PAYLOAD,
                        };
                        this._connection.sendOne(payloadFrame);
                      }
                    },
                    onSubscribe: (subscription) => {
                      this._subscriptions.set(streamId, subscription);
                      subscription.request(1);
                    },
                  });
                } else {
                  warning_1(
                    false,
                    'RSocketClient: re-entrant call to request n before initial' +
                      ' channel established.'
                  );
                }
              }
            },
          });
        } catch (err) {
          console.warn(
            'Exception while subscribing to channel flowable:' + err
          );
        }
      }, MAX_REQUEST_N);
    }
    metadataPush(payload) {
      // TODO #18065331: implement metadataPush
      throw new Error('metadataPush() is not implemented');
    }
    _getNextStreamId(streamIds) {
      const streamId = this._nextStreamId;
      do {
        this._nextStreamId = (this._nextStreamId + 2) & MAX_STREAM_ID;
      } while (this._nextStreamId === 0 || streamIds.has(streamId));
      return streamId;
    }
    _useLeaseOrError(leaseHandler) {
      if (leaseHandler) {
        if (!leaseHandler.use()) {
          return leaseHandler.errorMessage();
        }
      }
    }
    _leaseFrameSender() {
      return (lease) =>
        this._connection.sendOne({
          flags: 0,
          metadata: lease.metadata,
          requestCount: lease.allowedRequests,
          streamId: CONNECTION_STREAM_ID,
          ttl: lease.timeToLiveMillis,
          type: FRAME_TYPES.LEASE,
        });
    }
    _dispose(...disposables) {
      disposables.forEach((d) => {
        if (d) {
          d.dispose();
        }
      });
    }
    _isRequest(frameType) {
      switch (frameType) {
        case FRAME_TYPES.REQUEST_FNF:
        case FRAME_TYPES.REQUEST_RESPONSE:
        case FRAME_TYPES.REQUEST_STREAM:
        case FRAME_TYPES.REQUEST_CHANNEL:
          return true;
        default:
          return false;
      }
    }
    /**
     * Handle the connection closing normally: this is an error for any open streams.
     */ _handleConnectionError(error) {
      this._handleError(error);
      this._connection.close();
      const errorHandler = this._errorHandler;
      if (errorHandler) {
        errorHandler(error);
      }
    }
    /**
     * Handle a frame received from the transport client.
     */ /**
     * Handle connection frames (stream id === 0).
     */ _handleConnectionFrame(frame) {
      switch (frame.type) {
        case FRAME_TYPES.ERROR:
          const error = createErrorFromFrame(frame);
          this._handleConnectionError(error);
          break;
        case FRAME_TYPES.EXT:
          // Extensions are not supported
          break;
        case FRAME_TYPES.KEEPALIVE:
          this._keepAliveLastReceivedMillis = Date.now();
          if (isRespond(frame.flags)) {
            this._connection.sendOne(
              _objectSpread2(
                _objectSpread2({}, frame),
                {},
                {
                  flags: frame.flags ^ FLAGS.RESPOND, // eslint-disable-line no-bitwise
                  lastReceivedPosition: 0,
                }
              )
            );
          }
          break;
        case FRAME_TYPES.LEASE:
          const r = this._requesterLeaseHandler;
          if (r) {
            r.receive(frame);
          }
          break;
      }
    }

    /**
     * Handle stream-specific frames (stream id !== 0).
     */
    _handleStreamFrame(streamId, frame) {
      if (this._isRequest(frame.type)) {
        const leaseError = this._useLeaseOrError(this._responderLeaseHandler);
        if (leaseError) {
          this._sendStreamError(streamId, leaseError);
          return;
        }
      }
      switch (frame.type) {
        case FRAME_TYPES.CANCEL:
          this._handleCancel(streamId, frame);
          break;
        case FRAME_TYPES.REQUEST_N:
          this._handleRequestN(streamId, frame);
          break;
        case FRAME_TYPES.REQUEST_FNF:
          this._handleFireAndForget(streamId, frame);
          break;
        case FRAME_TYPES.REQUEST_RESPONSE:
          this._handleRequestResponse(streamId, frame);
          break;
        case FRAME_TYPES.REQUEST_STREAM:
          this._handleRequestStream(streamId, frame);
          break;
        case FRAME_TYPES.REQUEST_CHANNEL:
          this._handleRequestChannel(streamId, frame);
          break;
        case FRAME_TYPES.ERROR:
          const error = createErrorFromFrame(frame);
          this._handleStreamError(streamId, error);
          break;
        case FRAME_TYPES.PAYLOAD:
          const receiver = this._receivers.get(streamId);
          if (receiver != null) {
            if (isNext(frame.flags)) {
              const payload = {
                data: this._serializers.data.deserialize(frame.data),
                metadata: this._serializers.metadata.deserialize(
                  frame.metadata
                ),
              };

              receiver.onNext(payload);
            }
            if (isComplete(frame.flags)) {
              this._receivers.delete(streamId);
              receiver.onComplete();
            }
          }
          break;
      }
    }

    _handleCancel(streamId, frame) {
      const subscription = this._subscriptions.get(streamId);
      if (subscription) {
        subscription.cancel();
        this._subscriptions.delete(streamId);
      }
    }

    _handleRequestN(streamId, frame) {
      const subscription = this._subscriptions.get(streamId);
      if (subscription) {
        subscription.request(frame.requestN);
      }
    }

    _handleFireAndForget(streamId, frame) {
      const payload = this._deserializePayload(frame);
      this._requestHandler.fireAndForget(payload);
    }

    _handleRequestResponse(streamId, frame) {
      const payload = this._deserializePayload(frame);
      this._requestHandler.requestResponse(payload).subscribe({
        onComplete: (payload) => {
          this._sendStreamPayload(streamId, payload, true);
        },
        onError: (error) => this._sendStreamError(streamId, error.message),
        onSubscribe: (cancel) => {
          const subscription = {
            cancel,
            request: emptyFunction_1,
          };

          this._subscriptions.set(streamId, subscription);
        },
      });
    }

    _handleRequestStream(streamId, frame) {
      const payload = this._deserializePayload(frame);
      this._requestHandler.requestStream(payload).subscribe({
        onComplete: () => this._sendStreamComplete(streamId),
        onError: (error) => this._sendStreamError(streamId, error.message),
        onNext: (payload) => this._sendStreamPayload(streamId, payload),
        onSubscribe: (subscription) => {
          this._subscriptions.set(streamId, subscription);
          subscription.request(frame.requestN);
        },
      });
    }

    _handleRequestChannel(streamId, frame) {
      const existingSubscription = this._subscriptions.get(streamId);
      if (existingSubscription) {
        //Likely a duplicate REQUEST_CHANNEL frame, ignore per spec
        return;
      }

      const payloads = new rsocketFlowable.Flowable((subscriber) => {
        let firstRequest = true;

        subscriber.onSubscribe({
          cancel: () => {
            this._receivers.delete(streamId);
            const cancelFrame = {
              flags: 0,
              streamId,
              type: FRAME_TYPES.CANCEL,
            };

            this._connection.sendOne(cancelFrame);
          },
          request: (n) => {
            if (n > MAX_REQUEST_N) {
              n = MAX_REQUEST_N;
            }
            if (firstRequest) {
              n--;
            }

            if (n > 0) {
              const requestNFrame = {
                flags: 0,
                requestN: n,
                streamId,
                type: FRAME_TYPES.REQUEST_N,
              };

              this._connection.sendOne(requestNFrame);
            }
            //critically, if n is 0 now, that's okay because we eagerly decremented it
            if (firstRequest && n >= 0) {
              firstRequest = false;
              //release the initial frame we received in frame form due to map operator
              subscriber.onNext(frame);
            }
          },
        });
      }, MAX_REQUEST_N);
      const framesToPayloads = new rsocketFlowable.FlowableProcessor(
        payloads,
        (frame) => this._deserializePayload(frame)
      );

      this._receivers.set(streamId, framesToPayloads);

      this._requestHandler.requestChannel(framesToPayloads).subscribe({
        onComplete: () => this._sendStreamComplete(streamId),
        onError: (error) => this._sendStreamError(streamId, error.message),
        onNext: (payload) => this._sendStreamPayload(streamId, payload),
        onSubscribe: (subscription) => {
          this._subscriptions.set(streamId, subscription);
          subscription.request(frame.requestN);
        },
      });
    }

    _sendStreamComplete(streamId) {
      this._subscriptions.delete(streamId);
      this._connection.sendOne({
        data: null,
        flags: FLAGS.COMPLETE,
        metadata: null,
        streamId,
        type: FRAME_TYPES.PAYLOAD,
      });
    }

    _sendStreamError(streamId, errorMessage) {
      this._subscriptions.delete(streamId);
      this._connection.sendOne({
        code: ERROR_CODES.APPLICATION_ERROR,
        flags: 0,
        message: errorMessage,
        streamId,
        type: FRAME_TYPES.ERROR,
      });
    }

    _sendStreamPayload(streamId, payload, complete = false) {
      let flags = FLAGS.NEXT;
      if (complete) {
        // eslint-disable-next-line no-bitwise
        flags |= FLAGS.COMPLETE;
        this._subscriptions.delete(streamId);
      }
      const data = this._serializers.data.serialize(payload.data);
      const metadata = this._serializers.metadata.serialize(payload.metadata);
      this._connection.sendOne({
        data,
        flags,
        metadata,
        streamId,
        type: FRAME_TYPES.PAYLOAD,
      });
    }

    _deserializePayload(frame) {
      return deserializePayload(this._serializers, frame);
    }

    /**
     * Handle an error specific to a stream.
     */
    _handleStreamError(streamId, error) {
      const receiver = this._receivers.get(streamId);
      if (receiver != null) {
        this._receivers.delete(streamId);
        receiver.onError(error);
      }
    }
  }

  function deserializePayload(serializers, frame) {
    return {
      data: serializers.data.deserialize(frame.data),
      metadata: serializers.metadata.deserialize(frame.metadata),
    };
  }

  class ReassemblyDuplexConnection {
    constructor(source) {
      this._source = source;
    }

    sendOne(frame) {
      this._source.sendOne(frame);
    }

    send(input) {
      this._source.send(input);
    }

    receive() {
      return this._source
        .receive()
        .lift((actual) => new ReassemblySubscriber(actual));
    }

    close() {
      this._source.close();
    }

    connect() {
      this._source.connect();
    }

    connectionStatus() {
      return this._source.connectionStatus();
    }
  }

  class ReassemblySubscriber {
    constructor(actual) {
      _defineProperty(this, '_framesReassemblyMap', new Map());
      this._actual = actual;
    }

    request(n) {
      this._subscription.request(n);
    }

    cancel() {
      this._subscription.cancel();
      this._framesReassemblyMap.clear();
    }

    onSubscribe(s) {
      if (this._subscription == null) {
        this._subscription = s;
        this._actual.onSubscribe(this);
      } else {
        s.cancel();
      }
    }

    onComplete() {
      this._actual.onComplete();
    }

    onError(error) {
      this._actual.onError(error);
    }

    onNext(frame) {
      const streamId = frame.streamId;
      if (streamId !== CONNECTION_STREAM_ID) {
        const hasFollowsFlag = isFollows(frame.flags);
        const hasCompleteFlag = isComplete(frame.flags);
        const isCancelOrError =
          frame.type === FRAME_TYPES.ERROR || frame.type === FRAME_TYPES.CANCEL;

        const storedFrame = this._framesReassemblyMap.get(streamId);
        if (storedFrame) {
          if (isCancelOrError) {
            this._framesReassemblyMap.delete(streamId);
          } else {
            if (storedFrame.metadata && frame.metadata) {
              storedFrame.metadata = concatContent(
                storedFrame.metadata,
                frame.metadata
              );
            }

            if (storedFrame.data && frame.data) {
              storedFrame.data = concatContent(storedFrame.data, frame.data);
            } else if (!storedFrame.data && frame.data) {
              storedFrame.data = frame.data;
            }

            if (!hasFollowsFlag || hasCompleteFlag) {
              if (hasCompleteFlag) {
                storedFrame.flags |= FLAGS.COMPLETE;
              }

              this._framesReassemblyMap.delete(streamId);
              this._actual.onNext(storedFrame);
            }

            return;
          }
        } else if (hasFollowsFlag && !hasCompleteFlag && !isCancelOrError) {
          this._framesReassemblyMap.set(streamId, frame);

          return;
        }
      }

      this._actual.onNext(frame);
    }
  }

  const concatContent = (a, b) => {
    switch (a.constructor.name) {
      case 'String':
        return a + b;
      case 'Uint8Array':
        const result = new Uint8Array(a.length + b.length);
        result.set(a);
        result.set(b, a.length);
        return result;
      default:
        return LiteBuffer.concat([a, b]);
    }
  };

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
   * RSocketClient: A client in an RSocket connection that will communicates with
   * the peer via the given transport client. Provides methods for establishing a
   * connection and initiating the RSocket interactions:
   * - fireAndForget()
   * - requestResponse()
   * - requestStream()
   * - requestChannel()
   * - metadataPush()
   */
  class RSocketClient {
    constructor(config) {
      this._checkConfig(config);
      this._cancel = null;
      this._config = config;
      this._connection = null;
      this._socket = null;
    }

    close() {
      this._config.transport.close();
    }

    connect() {
      invariant_1(
        !this._connection,
        'RSocketClient: Unexpected call to connect(), already connected.'
      );

      this._connection = new rsocketFlowable.Single((subscriber) => {
        const transport = this._config.transport;
        let subscription;
        transport.connectionStatus().subscribe({
          onNext: (status) => {
            if (status.kind === 'CONNECTED') {
              subscription && subscription.cancel();
              subscriber.onComplete(
                new RSocketClientSocket(
                  this._config,
                  new ReassemblyDuplexConnection(transport)
                )
              );
            } else if (status.kind === 'ERROR') {
              subscription && subscription.cancel();
              subscriber.onError(status.error);
            } else if (status.kind === 'CLOSED') {
              subscription && subscription.cancel();
              subscriber.onError(
                new Error('RSocketClient: Connection closed.')
              );
            }
          },
          onSubscribe: (_subscription) => {
            subscriber.onSubscribe(() => _subscription.cancel());
            subscription = _subscription;
            subscription.request(Number.MAX_SAFE_INTEGER);
          },
        });

        transport.connect();
      });
      return this._connection;
    }

    _checkConfig(config) {
      const setup = config.setup;
      const keepAlive = setup && setup.keepAlive;
      // wrap in try catch since in 'strict' mode the access to an unexciting window will throw
      // the ReferenceError: window is not defined exception
      try {
        // eslint-disable-next-line no-undef
        const navigator = window && window.navigator;
        if (
          keepAlive > 30000 &&
          navigator &&
          navigator.userAgent &&
          (navigator.userAgent.includes('Trident') ||
            navigator.userAgent.includes('Edg'))
        ) {
          console.warn(
            'rsocket-js: Due to a browser bug, Internet Explorer and Edge users may experience WebSocket instability with keepAlive values longer than 30 seconds.'
          );
        }
      } catch (e) {
        // ignore the error since it means that the code is running in non browser environment
      }
    }
  }

  /**
   * @private
   */
  class RSocketClientSocket {
    constructor(config, connection) {
      let requesterLeaseHandler;
      let responderLeaseHandler;

      const leasesSupplier = config.leases;
      if (leasesSupplier) {
        const lease = leasesSupplier();
        requesterLeaseHandler = new RequesterLeaseHandler(lease._receiver);
        responderLeaseHandler = new ResponderLeaseHandler(
          lease._sender,
          lease._stats
        );
      }
      const {keepAlive, lifetime} = config.setup;

      this._machine = createClientMachine(
        connection,
        (subscriber) => connection.receive().subscribe(subscriber),
        lifetime,
        config.serializers,
        config.responder,
        config.errorHandler,
        requesterLeaseHandler,
        responderLeaseHandler
      );

      // Send SETUP
      connection.sendOne(this._buildSetupFrame(config));

      // Send KEEPALIVE frames
      const keepAliveFrames = rsocketFlowable.every(keepAlive).map(() => ({
        data: null,
        flags: FLAGS.RESPOND,
        lastReceivedPosition: 0,
        streamId: CONNECTION_STREAM_ID,
        type: FRAME_TYPES.KEEPALIVE,
      }));

      connection.send(keepAliveFrames);
    }

    fireAndForget(payload) {
      this._machine.fireAndForget(payload);
    }

    requestResponse(payload) {
      return this._machine.requestResponse(payload);
    }

    requestStream(payload) {
      return this._machine.requestStream(payload);
    }

    requestChannel(payloads) {
      return this._machine.requestChannel(payloads);
    }

    metadataPush(payload) {
      return this._machine.metadataPush(payload);
    }

    close() {
      this._machine.close();
    }

    connectionStatus() {
      return this._machine.connectionStatus();
    }

    availability() {
      return this._machine.availability();
    }

    _buildSetupFrame(config) {
      const {
        dataMimeType,
        keepAlive,
        lifetime,
        metadataMimeType,
        payload,
      } = config.setup;

      const serializers = config.serializers || IdentitySerializers;
      const data = payload
        ? serializers.data.serialize(payload.data)
        : undefined;
      const metadata = payload
        ? serializers.metadata.serialize(payload.metadata)
        : undefined;
      let flags = 0;
      if (metadata !== undefined) {
        flags |= FLAGS.METADATA;
      }
      return {
        data,
        dataMimeType,
        flags: flags | (config.leases ? FLAGS.LEASE : 0),
        keepAlive,
        lifetime,
        majorVersion: MAJOR_VERSION,
        metadata,
        metadataMimeType,
        minorVersion: MINOR_VERSION,
        resumeToken: null,
        streamId: CONNECTION_STREAM_ID,
        type: FRAME_TYPES.SETUP,
      };
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

  /**
   * RSocketServer: A server in an RSocket connection that accepts connections
   * from peers via the given transport server.
   */
  class RSocketServer {
    constructor(config) {
      _defineProperty(
        this,
        '_handleTransportComplete',

        () => {
          this._handleTransportError(
            new Error('RSocketServer: Connection closed unexpectedly.')
          );
        }
      );
      _defineProperty(
        this,
        '_handleTransportError',

        (error) => {
          this._connections.forEach((connection) => {
            // TODO: Allow passing in error
            connection.close();
          });
        }
      );
      _defineProperty(
        this,
        '_handleTransportConnection',

        (connection) => {
          const swapper = new SubscriberSwapper();
          let subscription;
          connection = new ReassemblyDuplexConnection(connection);
          connection.receive().subscribe(
            swapper.swap({
              onError: (error) => console.error(error),
              onNext: (frame) => {
                switch (frame.type) {
                  case FRAME_TYPES.RESUME:
                    connection.sendOne({
                      code: ERROR_CODES.REJECTED_RESUME,
                      flags: 0,
                      message: 'RSocketServer: RESUME not supported.',
                      streamId: CONNECTION_STREAM_ID,
                      type: FRAME_TYPES.ERROR,
                    });

                    connection.close();
                    break;
                  case FRAME_TYPES.SETUP:
                    if (this._setupLeaseError(frame)) {
                      connection.sendOne({
                        code: ERROR_CODES.INVALID_SETUP,
                        flags: 0,
                        message: 'RSocketServer: LEASE not supported.',
                        streamId: CONNECTION_STREAM_ID,
                        type: FRAME_TYPES.ERROR,
                      });

                      connection.close();
                      break;
                    }
                    const serializers = this._getSerializers();

                    let requesterLeaseHandler;
                    let responderLeaseHandler;

                    const leasesSupplier = this._config.leases;
                    if (leasesSupplier) {
                      const lease = leasesSupplier();
                      requesterLeaseHandler = new RequesterLeaseHandler(
                        lease._receiver
                      );

                      responderLeaseHandler = new ResponderLeaseHandler(
                        lease._sender,
                        lease._stats
                      );
                    }
                    const serverMachine = createServerMachine(
                      connection,
                      (subscriber) => {
                        swapper.swap(subscriber);
                      },
                      frame.lifetime,
                      serializers,
                      this._config.errorHandler,
                      requesterLeaseHandler,
                      responderLeaseHandler
                    );

                    try {
                      const requestHandler = this._config.getRequestHandler(
                        serverMachine,
                        deserializePayload$1(serializers, frame)
                      );

                      serverMachine.setRequestHandler(requestHandler);
                      this._connections.add(serverMachine);
                    } catch (error) {
                      connection.sendOne({
                        code: ERROR_CODES.REJECTED_SETUP,
                        flags: 0,
                        message:
                          'Application rejected setup, reason: ' +
                          error.message,
                        streamId: CONNECTION_STREAM_ID,
                        type: FRAME_TYPES.ERROR,
                      });

                      connection.close();
                    }

                    // TODO(blom): We should subscribe to connection status
                    // so we can remove the connection when it goes away
                    break;
                  default:
                    invariant_1(
                      false,
                      'RSocketServer: Expected first frame to be SETUP or RESUME, ' +
                        'got `%s`.',
                      getFrameTypeName(frame.type)
                    );
                }
              },
              onSubscribe: (_subscription) => {
                subscription = _subscription;
                subscription.request(1);
              },
            })
          );
        }
      );
      this._config = config;
      this._connections = new Set();
      this._started = false;
      this._subscription = null;
    }
    start() {
      invariant_1(
        !this._started,
        'RSocketServer: Unexpected call to start(), already started.'
      );
      this._started = true;
      this._config.transport.start().subscribe({
        onComplete: this._handleTransportComplete,
        onError: this._handleTransportError,
        onNext: this._handleTransportConnection,
        onSubscribe: (subscription) => {
          this._subscription = subscription;
          subscription.request(Number.MAX_SAFE_INTEGER);
        },
      });
    }
    stop() {
      if (this._subscription) {
        this._subscription.cancel();
      }
      this._config.transport.stop();
      this._handleTransportError(
        new Error('RSocketServer: Connection terminated via stop().')
      );
    }

    _getSerializers() {
      return this._config.serializers || IdentitySerializers;
    }

    _setupLeaseError(frame) {
      const clientLeaseEnabled = (frame.flags & FLAGS.LEASE) === FLAGS.LEASE;
      const serverLeaseEnabled = this._config.leases;
      return clientLeaseEnabled && !serverLeaseEnabled;
    }
  }

  class SubscriberSwapper {
    constructor(target) {
      this._target = target;
    }

    swap(next) {
      this._target = next;
      if (this._subscription) {
        this._target.onSubscribe &&
          this._target.onSubscribe(this._subscription);
      }
      return this;
    }

    onComplete() {
      invariant_1(this._target, 'must have target');
      this._target.onComplete && this._target.onComplete();
    }
    onError(error) {
      invariant_1(this._target, 'must have target');
      this._target.onError && this._target.onError(error);
    }
    onNext(value) {
      invariant_1(this._target, 'must have target');
      this._target.onNext && this._target.onNext(value);
    }
    onSubscribe(subscription) {
      invariant_1(this._target, 'must have target');
      this._subscription = subscription;
      this._target.onSubscribe && this._target.onSubscribe(subscription);
    }
  }

  function deserializePayload$1(serializers, frame) {
    return {
      data: serializers.data.deserialize(frame.data),
      metadata: serializers.metadata.deserialize(frame.metadata),
    };
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

  /**
   * Mimimum value that would overflow bitwise operators (2^32).
   */
  const BITWISE_OVERFLOW = 0x100000000;

  /**
   * Read a uint24 from a buffer starting at the given offset.
   */
  function readUInt24BE(buffer, offset) {
    const val1 = buffer.readUInt8(offset) << 16;
    const val2 = buffer.readUInt8(offset + 1) << 8;
    const val3 = buffer.readUInt8(offset + 2);
    return val1 | val2 | val3;
  }

  /**
   * Writes a uint24 to a buffer starting at the given offset, returning the
   * offset of the next byte.
   */
  function writeUInt24BE(buffer, value, offset) {
    offset = buffer.writeUInt8(value >>> 16, offset); // 3rd byte
    offset = buffer.writeUInt8((value >>> 8) & 0xff, offset); // 2nd byte
    return buffer.writeUInt8(value & 0xff, offset); // 1st byte
  }

  /**
   * Read a uint64 (technically supports up to 53 bits per JS number
   * representation).
   */
  function readUInt64BE(buffer, offset) {
    const high = buffer.readUInt32BE(offset);
    const low = buffer.readUInt32BE(offset + 4);
    return high * BITWISE_OVERFLOW + low;
  }

  /**
   * Write a uint64 (technically supports up to 53 bits per JS number
   * representation).
   */
  function writeUInt64BE(buffer, value, offset) {
    const high = (value / BITWISE_OVERFLOW) | 0;
    const low = value % BITWISE_OVERFLOW;
    offset = buffer.writeUInt32BE(high, offset); // first half of uint64
    return buffer.writeUInt32BE(low, offset); // second half of uint64
  }

  /**
   * Determine the number of bytes it would take to encode the given data with the
   * given encoding.
   */
  function byteLength(data, encoding) {
    if (data == null) {
      return 0;
    }
    return LiteBuffer.byteLength(data, encoding);
  }

  /**
   * Attempts to construct a buffer from the input, throws if invalid.
   */
  function toBuffer(data) {
    // Buffer.from(buffer) copies which we don't want here
    if (data instanceof LiteBuffer) {
      return data;
    }
    invariant_1(
      data instanceof ArrayBuffer,
      'RSocketBufferUtils: Cannot construct buffer. Expected data to be an ' +
        'arraybuffer, got `%s`.',
      data
    );

    return LiteBuffer.from(data);
  }

  /**
   * Function to create a buffer of a given sized filled with zeros.
   */

  const createBuffer =
    typeof LiteBuffer.alloc === 'function'
      ? (length) => LiteBuffer.alloc(length)
      : // $FlowFixMe
        (length) => new LiteBuffer(length).fill(0);

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
   * Commonly used subset of the allowed Node Buffer Encoder types.
   */

  const UTF8Encoder = {
    byteLength: (value) => byteLength(value, 'utf8'),
    decode: (buffer, start, end) => {
      return buffer.toString('utf8', start, end);
    },
    encode: (value, buffer, start, end) => {
      invariant_1(
        typeof value === 'string',
        'RSocketEncoding: Expected value to be a string, got `%s`.',
        value
      );

      buffer.write(value, start, end - start, 'utf8');
      return end;
    },
  };

  const BufferEncoder = {
    byteLength: (value) => {
      invariant_1(
        Buffer.isBuffer(value),
        'RSocketEncoding: Expected value to be a buffer, got `%s`.',
        value
      );

      return value.length;
    },
    decode: (buffer, start, end) => {
      return buffer.slice(start, end);
    },
    encode: (value, buffer, start, end) => {
      invariant_1(
        Buffer.isBuffer(value),
        'RSocketEncoding: Expected value to be a buffer, got `%s`.',
        value
      );

      value.copy(buffer, start, 0, value.length);
      return end;
    },
  };

  /**
   * Encode all values as UTF8 strings.
   */
  const Utf8Encoders = {
    data: UTF8Encoder,
    dataMimeType: UTF8Encoder,
    message: UTF8Encoder,
    metadata: UTF8Encoder,
    metadataMimeType: UTF8Encoder,
    resumeToken: UTF8Encoder,
  };

  /**
   * Encode all values as buffers.
   */
  const BufferEncoders = {
    data: BufferEncoder,
    dataMimeType: UTF8Encoder,
    message: UTF8Encoder,
    metadata: BufferEncoder,
    metadataMimeType: UTF8Encoder,
    resumeToken: BufferEncoder,
  };

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
   * Frame header is:
   * - stream id (uint32 = 4)
   * - type + flags (uint 16 = 2)
   */
  const FRAME_HEADER_SIZE = 6;

  /**
   * Size of frame length and metadata length fields.
   */
  const UINT24_SIZE = 3;

  /**
   * Reads a frame from a buffer that is prefixed with the frame length.
   */
  function deserializeFrameWithLength(buffer, encoders) {
    const frameLength = readUInt24BE(buffer, 0);
    return deserializeFrame(
      buffer.slice(UINT24_SIZE, UINT24_SIZE + frameLength),
      encoders
    );
  }

  /**
   * Given a buffer that may contain zero or more length-prefixed frames followed
   * by zero or more bytes of a (partial) subsequent frame, returns an array of
   * the frames and a buffer of the leftover bytes.
   */
  function deserializeFrames(buffer, encoders) {
    const frames = [];
    let offset = 0;
    while (offset + UINT24_SIZE < buffer.length) {
      const frameLength = readUInt24BE(buffer, offset);
      const frameStart = offset + UINT24_SIZE;
      const frameEnd = frameStart + frameLength;
      if (frameEnd > buffer.length) {
        // not all bytes of next frame received
        break;
      }
      const frameBuffer = buffer.slice(frameStart, frameEnd);
      const frame = deserializeFrame(frameBuffer, encoders);
      frames.push(frame);
      offset = frameEnd;
    }
    return [frames, buffer.slice(offset, buffer.length)];
  }

  /**
   * Writes a frame to a buffer with a length prefix.
   */
  function serializeFrameWithLength(frame, encoders) {
    const buffer = serializeFrame(frame, encoders);
    const lengthPrefixed = createBuffer(buffer.length + UINT24_SIZE);
    writeUInt24BE(lengthPrefixed, buffer.length, 0);
    buffer.copy(lengthPrefixed, UINT24_SIZE, 0, buffer.length);
    return lengthPrefixed;
  }

  /**
   * Read a frame from the buffer.
   */
  function deserializeFrame(buffer, encoders) {
    encoders = encoders || Utf8Encoders;
    let offset = 0;
    const streamId = buffer.readInt32BE(offset);
    offset += 4;
    invariant_1(
      streamId >= 0,
      'RSocketBinaryFraming: Invalid frame, expected a positive stream id, got `%s.',
      streamId
    );

    const typeAndFlags = buffer.readUInt16BE(offset);
    offset += 2;
    const type = typeAndFlags >>> FRAME_TYPE_OFFFSET; // keep highest 6 bits
    const flags = typeAndFlags & FLAGS_MASK; // keep lowest 10 bits
    switch (type) {
      case FRAME_TYPES.SETUP:
        return deserializeSetupFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.PAYLOAD:
        return deserializePayloadFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.ERROR:
        return deserializeErrorFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.KEEPALIVE:
        return deserializeKeepAliveFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.REQUEST_FNF:
        return deserializeRequestFnfFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.REQUEST_RESPONSE:
        return deserializeRequestResponseFrame(
          buffer,
          streamId,
          flags,
          encoders
        );
      case FRAME_TYPES.REQUEST_STREAM:
        return deserializeRequestStreamFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.REQUEST_CHANNEL:
        return deserializeRequestChannelFrame(
          buffer,
          streamId,
          flags,
          encoders
        );
      case FRAME_TYPES.REQUEST_N:
        return deserializeRequestNFrame(buffer, streamId, flags);
      case FRAME_TYPES.RESUME:
        return deserializeResumeFrame(buffer, streamId, flags, encoders);
      case FRAME_TYPES.RESUME_OK:
        return deserializeResumeOkFrame(buffer, streamId, flags);
      case FRAME_TYPES.CANCEL:
        return deserializeCancelFrame(buffer, streamId, flags);
      case FRAME_TYPES.LEASE:
        return deserializeLeaseFrame(buffer, streamId, flags, encoders);
      default:
        invariant_1(
          false,
          'RSocketBinaryFraming: Unsupported frame type `%s`.',
          getFrameTypeName(type)
        );
    }
  }

  /**
   * Convert the frame to a (binary) buffer.
   */
  function serializeFrame(frame, encoders) {
    encoders = encoders || Utf8Encoders;
    switch (frame.type) {
      case FRAME_TYPES.SETUP:
        return serializeSetupFrame(frame, encoders);
      case FRAME_TYPES.PAYLOAD:
        return serializePayloadFrame(frame, encoders);
      case FRAME_TYPES.ERROR:
        return serializeErrorFrame(frame, encoders);
      case FRAME_TYPES.KEEPALIVE:
        return serializeKeepAliveFrame(frame, encoders);
      case FRAME_TYPES.REQUEST_FNF:
      case FRAME_TYPES.REQUEST_RESPONSE:
        return serializeRequestFrame(frame, encoders);
      case FRAME_TYPES.REQUEST_STREAM:
      case FRAME_TYPES.REQUEST_CHANNEL:
        return serializeRequestManyFrame(frame, encoders);
      case FRAME_TYPES.REQUEST_N:
        return serializeRequestNFrame(frame);
      case FRAME_TYPES.RESUME:
        return serializeResumeFrame(frame, encoders);
      case FRAME_TYPES.RESUME_OK:
        return serializeResumeOkFrame(frame);
      case FRAME_TYPES.CANCEL:
        return serializeCancelFrame(frame);
      case FRAME_TYPES.LEASE:
        return serializeLeaseFrame(frame, encoders);
      default:
        invariant_1(
          false,
          'RSocketBinaryFraming: Unsupported frame type `%s`.',
          getFrameTypeName(frame.type)
        );
    }
  }
  /**
   * Byte size of frame without size prefix
   */
  function sizeOfFrame(frame, encoders) {
    encoders = encoders || Utf8Encoders;
    switch (frame.type) {
      case FRAME_TYPES.SETUP:
        return sizeOfSetupFrame(frame, encoders);
      case FRAME_TYPES.PAYLOAD:
        return sizeOfPayloadFrame(frame, encoders);
      case FRAME_TYPES.ERROR:
        return sizeOfErrorFrame(frame, encoders);
      case FRAME_TYPES.KEEPALIVE:
        return sizeOfKeepAliveFrame(frame, encoders);
      case FRAME_TYPES.REQUEST_FNF:
      case FRAME_TYPES.REQUEST_RESPONSE:
        return sizeOfRequestFrame(frame, encoders);
      case FRAME_TYPES.REQUEST_STREAM:
      case FRAME_TYPES.REQUEST_CHANNEL:
        return sizeOfRequestManyFrame(frame, encoders);
      case FRAME_TYPES.REQUEST_N:
        return sizeOfRequestNFrame();
      case FRAME_TYPES.RESUME:
        return sizeOfResumeFrame(frame, encoders);
      case FRAME_TYPES.RESUME_OK:
        return sizeOfResumeOkFrame();
      case FRAME_TYPES.CANCEL:
        return sizeOfCancelFrame();
      case FRAME_TYPES.LEASE:
        return sizeOfLeaseFrame(frame, encoders);
      default:
        invariant_1(
          false,
          'RSocketBinaryFraming: Unsupported frame type `%s`.',
          getFrameTypeName(frame.type)
        );
    }
  }

  /**
   * Writes a SETUP frame into a new buffer and returns it.
   *
   * Prefix size is:
   * - version (2x uint16 = 4)
   * - keepalive (uint32 = 4)
   * - lifetime (uint32 = 4)
   * - mime lengths (2x uint8 = 2)
   */
  const SETUP_FIXED_SIZE = 14;
  const RESUME_TOKEN_LENGTH_SIZE = 2;
  function serializeSetupFrame(frame, encoders) {
    const resumeTokenLength =
      frame.resumeToken != null
        ? encoders.resumeToken.byteLength(frame.resumeToken)
        : 0;
    const metadataMimeTypeLength =
      frame.metadataMimeType != null
        ? encoders.metadataMimeType.byteLength(frame.metadataMimeType)
        : 0;
    const dataMimeTypeLength =
      frame.dataMimeType != null
        ? encoders.dataMimeType.byteLength(frame.dataMimeType)
        : 0;
    const payloadLength = getPayloadLength(frame, encoders);
    const buffer = createBuffer(
      FRAME_HEADER_SIZE +
        SETUP_FIXED_SIZE + //
        (resumeTokenLength ? RESUME_TOKEN_LENGTH_SIZE + resumeTokenLength : 0) +
        metadataMimeTypeLength +
        dataMimeTypeLength +
        payloadLength
    );

    let offset = writeHeader(frame, buffer);
    offset = buffer.writeUInt16BE(frame.majorVersion, offset);
    offset = buffer.writeUInt16BE(frame.minorVersion, offset);
    offset = buffer.writeUInt32BE(frame.keepAlive, offset);
    offset = buffer.writeUInt32BE(frame.lifetime, offset);

    if (frame.flags & FLAGS.RESUME_ENABLE) {
      offset = buffer.writeUInt16BE(resumeTokenLength, offset);
      if (frame.resumeToken != null) {
        offset = encoders.resumeToken.encode(
          frame.resumeToken,
          buffer,
          offset,
          offset + resumeTokenLength
        );
      }
    }

    offset = buffer.writeUInt8(metadataMimeTypeLength, offset);
    if (frame.metadataMimeType != null) {
      offset = encoders.metadataMimeType.encode(
        frame.metadataMimeType,
        buffer,
        offset,
        offset + metadataMimeTypeLength
      );
    }

    offset = buffer.writeUInt8(dataMimeTypeLength, offset);
    if (frame.dataMimeType != null) {
      offset = encoders.dataMimeType.encode(
        frame.dataMimeType,
        buffer,
        offset,
        offset + dataMimeTypeLength
      );
    }

    writePayload(frame, buffer, encoders, offset);
    return buffer;
  }

  function sizeOfSetupFrame(frame, encoders) {
    const resumeTokenLength =
      frame.resumeToken != null
        ? encoders.resumeToken.byteLength(frame.resumeToken)
        : 0;
    const metadataMimeTypeLength =
      frame.metadataMimeType != null
        ? encoders.metadataMimeType.byteLength(frame.metadataMimeType)
        : 0;
    const dataMimeTypeLength =
      frame.dataMimeType != null
        ? encoders.dataMimeType.byteLength(frame.dataMimeType)
        : 0;
    const payloadLength = getPayloadLength(frame, encoders);
    return (
      FRAME_HEADER_SIZE +
      SETUP_FIXED_SIZE + //
      (resumeTokenLength ? RESUME_TOKEN_LENGTH_SIZE + resumeTokenLength : 0) +
      metadataMimeTypeLength +
      dataMimeTypeLength +
      payloadLength
    );
  }

  /**
   * Reads a SETUP frame from the buffer and returns it.
   */
  function deserializeSetupFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId === 0,
      'RSocketBinaryFraming: Invalid SETUP frame, expected stream id to be 0.'
    );

    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const majorVersion = buffer.readUInt16BE(offset);
    offset += 2;
    const minorVersion = buffer.readUInt16BE(offset);
    offset += 2;

    const keepAlive = buffer.readInt32BE(offset);
    offset += 4;
    invariant_1(
      keepAlive >= 0 && keepAlive <= MAX_KEEPALIVE,
      'RSocketBinaryFraming: Invalid SETUP frame, expected keepAlive to be ' +
        '>= 0 and <= %s. Got `%s`.',
      MAX_KEEPALIVE,
      keepAlive
    );

    const lifetime = buffer.readInt32BE(offset);
    offset += 4;
    invariant_1(
      lifetime >= 0 && lifetime <= MAX_LIFETIME,
      'RSocketBinaryFraming: Invalid SETUP frame, expected lifetime to be ' +
        '>= 0 and <= %s. Got `%s`.',
      MAX_LIFETIME,
      lifetime
    );

    let resumeToken = null;
    if (flags & FLAGS.RESUME_ENABLE) {
      const resumeTokenLength = buffer.readInt16BE(offset);
      offset += 2;
      invariant_1(
        resumeTokenLength >= 0 && resumeTokenLength <= MAX_RESUME_LENGTH,
        'RSocketBinaryFraming: Invalid SETUP frame, expected resumeToken length ' +
          'to be >= 0 and <= %s. Got `%s`.',
        MAX_RESUME_LENGTH,
        resumeTokenLength
      );

      resumeToken = encoders.resumeToken.decode(
        buffer,
        offset,
        offset + resumeTokenLength
      );

      offset += resumeTokenLength;
    }

    const metadataMimeTypeLength = buffer.readUInt8(offset);
    offset += 1;
    const metadataMimeType = encoders.metadataMimeType.decode(
      buffer,
      offset,
      offset + metadataMimeTypeLength
    );

    offset += metadataMimeTypeLength;

    const dataMimeTypeLength = buffer.readUInt8(offset);
    offset += 1;
    const dataMimeType = encoders.dataMimeType.decode(
      buffer,
      offset,
      offset + dataMimeTypeLength
    );

    offset += dataMimeTypeLength;

    const frame = {
      data: null,
      dataMimeType,
      flags,
      keepAlive,
      length,
      lifetime,
      majorVersion,
      metadata: null,
      metadataMimeType,
      minorVersion,
      resumeToken,
      streamId,
      type: FRAME_TYPES.SETUP,
    };

    readPayload(buffer, frame, encoders, offset);
    return frame;
  }

  /**
   * Writes an ERROR frame into a new buffer and returns it.
   *
   * Prefix size is for the error code (uint32 = 4).
   */
  const ERROR_FIXED_SIZE = 4;
  function serializeErrorFrame(frame, encoders) {
    const messageLength =
      frame.message != null ? encoders.message.byteLength(frame.message) : 0;
    const buffer = createBuffer(
      FRAME_HEADER_SIZE + ERROR_FIXED_SIZE + messageLength
    );

    let offset = writeHeader(frame, buffer);
    offset = buffer.writeUInt32BE(frame.code, offset);
    if (frame.message != null) {
      encoders.message.encode(
        frame.message,
        buffer,
        offset,
        offset + messageLength
      );
    }
    return buffer;
  }

  function sizeOfErrorFrame(frame, encoders) {
    const messageLength =
      frame.message != null ? encoders.message.byteLength(frame.message) : 0;
    return FRAME_HEADER_SIZE + ERROR_FIXED_SIZE + messageLength;
  }

  /**
   * Reads an ERROR frame from the buffer and returns it.
   */
  function deserializeErrorFrame(buffer, streamId, flags, encoders) {
    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const code = buffer.readInt32BE(offset);
    offset += 4;
    invariant_1(
      code >= 0 && code <= MAX_CODE,
      'RSocketBinaryFraming: Invalid ERROR frame, expected code to be >= 0 and <= %s. Got `%s`.',
      MAX_CODE,
      code
    );

    const messageLength = buffer.length - offset;
    let message = '';
    if (messageLength > 0) {
      message = encoders.message.decode(buffer, offset, offset + messageLength);
      offset += messageLength;
    }

    return {
      code,
      flags,
      length,
      message,
      streamId,
      type: FRAME_TYPES.ERROR,
    };
  }

  /**
   * Writes a KEEPALIVE frame into a new buffer and returns it.
   *
   * Prefix size is for the last received position (uint64 = 8).
   */
  const KEEPALIVE_FIXED_SIZE = 8;
  function serializeKeepAliveFrame(frame, encoders) {
    const dataLength =
      frame.data != null ? encoders.data.byteLength(frame.data) : 0;
    const buffer = createBuffer(
      FRAME_HEADER_SIZE + KEEPALIVE_FIXED_SIZE + dataLength
    );

    let offset = writeHeader(frame, buffer);
    offset = writeUInt64BE(buffer, frame.lastReceivedPosition, offset);
    if (frame.data != null) {
      encoders.data.encode(frame.data, buffer, offset, offset + dataLength);
    }
    return buffer;
  }

  function sizeOfKeepAliveFrame(frame, encoders) {
    const dataLength =
      frame.data != null ? encoders.data.byteLength(frame.data) : 0;
    return FRAME_HEADER_SIZE + KEEPALIVE_FIXED_SIZE + dataLength;
  }

  /**
   * Reads a KEEPALIVE frame from the buffer and returns it.
   */
  function deserializeKeepAliveFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId === 0,
      'RSocketBinaryFraming: Invalid KEEPALIVE frame, expected stream id to be 0.'
    );

    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const lastReceivedPosition = readUInt64BE(buffer, offset);
    offset += 8;
    let data = null;
    if (offset < buffer.length) {
      data = encoders.data.decode(buffer, offset, buffer.length);
    }

    return {
      data,
      flags,
      lastReceivedPosition,
      length,
      streamId,
      type: FRAME_TYPES.KEEPALIVE,
    };
  }

  /**
   * Writes a LEASE frame into a new buffer and returns it.
   *
   * Prefix size is for the ttl (uint32) and requestcount (uint32).
   */
  const LEASE_FIXED_SIZE = 8;
  function serializeLeaseFrame(frame, encoders) {
    const metaLength =
      frame.metadata != null ? encoders.metadata.byteLength(frame.metadata) : 0;
    const buffer = createBuffer(
      FRAME_HEADER_SIZE + LEASE_FIXED_SIZE + metaLength
    );

    let offset = writeHeader(frame, buffer);
    offset = buffer.writeUInt32BE(frame.ttl, offset);
    offset = buffer.writeUInt32BE(frame.requestCount, offset);
    if (frame.metadata != null) {
      encoders.metadata.encode(
        frame.metadata,
        buffer,
        offset,
        offset + metaLength
      );
    }
    return buffer;
  }

  function sizeOfLeaseFrame(frame, encoders) {
    const metaLength =
      frame.metadata != null ? encoders.metadata.byteLength(frame.metadata) : 0;
    return FRAME_HEADER_SIZE + LEASE_FIXED_SIZE + metaLength;
  }

  /**
   * Reads a LEASE frame from the buffer and returns it.
   */
  function deserializeLeaseFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId === 0,
      'RSocketBinaryFraming: Invalid LEASE frame, expected stream id to be 0.'
    );

    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const ttl = buffer.readUInt32BE(offset);
    offset += 4;
    const requestCount = buffer.readUInt32BE(offset);
    offset += 4;
    let metadata = null;
    if (offset < buffer.length) {
      metadata = encoders.metadata.decode(buffer, offset, buffer.length);
    }
    return {
      flags,
      length,
      metadata,
      requestCount,
      streamId,
      ttl,
      type: FRAME_TYPES.LEASE,
    };
  }

  /**
   * Writes a REQUEST_FNF or REQUEST_RESPONSE frame to a new buffer and returns
   * it.
   *
   * Note that these frames have the same shape and only differ in their type.
   */
  function serializeRequestFrame(frame, encoders) {
    const payloadLength = getPayloadLength(frame, encoders);
    const buffer = createBuffer(FRAME_HEADER_SIZE + payloadLength);
    const offset = writeHeader(frame, buffer);
    writePayload(frame, buffer, encoders, offset);
    return buffer;
  }

  function sizeOfRequestFrame(frame, encoders) {
    const payloadLength = getPayloadLength(frame, encoders);
    return FRAME_HEADER_SIZE + payloadLength;
  }

  function deserializeRequestFnfFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid REQUEST_FNF frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    const frame = {
      data: null,
      flags,
      length,
      metadata: null,
      streamId,
      type: FRAME_TYPES.REQUEST_FNF,
    };

    readPayload(buffer, frame, encoders, FRAME_HEADER_SIZE);
    return frame;
  }

  function deserializeRequestResponseFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid REQUEST_RESPONSE frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    const frame = {
      data: null,
      flags,
      length,
      metadata: null,
      streamId,
      type: FRAME_TYPES.REQUEST_RESPONSE,
    };

    readPayload(buffer, frame, encoders, FRAME_HEADER_SIZE);
    return frame;
  }

  /**
   * Writes a REQUEST_STREAM or REQUEST_CHANNEL frame to a new buffer and returns
   * it.
   *
   * Note that these frames have the same shape and only differ in their type.
   *
   * Prefix size is for requestN (uint32 = 4).
   */
  const REQUEST_MANY_HEADER = 4;
  function serializeRequestManyFrame(frame, encoders) {
    const payloadLength = getPayloadLength(frame, encoders);
    const buffer = createBuffer(
      FRAME_HEADER_SIZE + REQUEST_MANY_HEADER + payloadLength
    );

    let offset = writeHeader(frame, buffer);
    offset = buffer.writeUInt32BE(frame.requestN, offset);
    writePayload(frame, buffer, encoders, offset);
    return buffer;
  }

  function sizeOfRequestManyFrame(frame, encoders) {
    const payloadLength = getPayloadLength(frame, encoders);
    return FRAME_HEADER_SIZE + REQUEST_MANY_HEADER + payloadLength;
  }

  function deserializeRequestStreamFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid REQUEST_STREAM frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const requestN = buffer.readInt32BE(offset);
    offset += 4;
    invariant_1(
      requestN > 0,
      'RSocketBinaryFraming: Invalid REQUEST_STREAM frame, expected requestN to be > 0, got `%s`.',
      requestN
    );

    const frame = {
      data: null,
      flags,
      length,
      metadata: null,
      requestN,
      streamId,
      type: FRAME_TYPES.REQUEST_STREAM,
    };

    readPayload(buffer, frame, encoders, offset);
    return frame;
  }

  function deserializeRequestChannelFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid REQUEST_CHANNEL frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const requestN = buffer.readInt32BE(offset);
    offset += 4;
    invariant_1(
      requestN > 0,
      'RSocketBinaryFraming: Invalid REQUEST_STREAM frame, expected requestN to be > 0, got `%s`.',
      requestN
    );

    const frame = {
      data: null,
      flags,
      length,
      metadata: null,
      requestN,
      streamId,
      type: FRAME_TYPES.REQUEST_CHANNEL,
    };

    readPayload(buffer, frame, encoders, offset);
    return frame;
  }

  /**
   * Writes a REQUEST_N frame to a new buffer and returns it.
   *
   * Prefix size is for requestN (uint32 = 4).
   */
  const REQUEST_N_HEADER = 4;
  function serializeRequestNFrame(frame, encoders) {
    const buffer = createBuffer(FRAME_HEADER_SIZE + REQUEST_N_HEADER);
    const offset = writeHeader(frame, buffer);
    buffer.writeUInt32BE(frame.requestN, offset);
    return buffer;
  }

  function sizeOfRequestNFrame(frame, encoders) {
    return FRAME_HEADER_SIZE + REQUEST_N_HEADER;
  }

  function deserializeRequestNFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid REQUEST_N frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    const requestN = buffer.readInt32BE(FRAME_HEADER_SIZE);
    invariant_1(
      requestN > 0,
      'RSocketBinaryFraming: Invalid REQUEST_STREAM frame, expected requestN to be > 0, got `%s`.',
      requestN
    );

    return {
      flags,
      length,
      requestN,
      streamId,
      type: FRAME_TYPES.REQUEST_N,
    };
  }

  /**
   * Writes a CANCEL frame to a new buffer and returns it.
   */
  function serializeCancelFrame(frame, encoders) {
    const buffer = createBuffer(FRAME_HEADER_SIZE);
    writeHeader(frame, buffer);
    return buffer;
  }

  function sizeOfCancelFrame(frame, encoders) {
    return FRAME_HEADER_SIZE;
  }

  function deserializeCancelFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid CANCEL frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    return {
      flags,
      length,
      streamId,
      type: FRAME_TYPES.CANCEL,
    };
  }

  /**
   * Writes a PAYLOAD frame to a new buffer and returns it.
   */
  function serializePayloadFrame(frame, encoders) {
    const payloadLength = getPayloadLength(frame, encoders);
    const buffer = createBuffer(FRAME_HEADER_SIZE + payloadLength);
    const offset = writeHeader(frame, buffer);
    writePayload(frame, buffer, encoders, offset);
    return buffer;
  }

  function sizeOfPayloadFrame(frame, encoders) {
    const payloadLength = getPayloadLength(frame, encoders);
    return FRAME_HEADER_SIZE + payloadLength;
  }

  function deserializePayloadFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId > 0,
      'RSocketBinaryFraming: Invalid PAYLOAD frame, expected stream id to be > 0.'
    );

    const length = buffer.length;
    const frame = {
      data: null,
      flags,
      length,
      metadata: null,
      streamId,
      type: FRAME_TYPES.PAYLOAD,
    };

    readPayload(buffer, frame, encoders, FRAME_HEADER_SIZE);
    return frame;
  }

  /**
   * Writes a RESUME frame into a new buffer and returns it.
   *
   * Fixed size is:
   * - major version (uint16 = 2)
   * - minor version (uint16 = 2)
   * - token length (uint16 = 2)
   * - client position (uint64 = 8)
   * - server position (uint64 = 8)
   */
  const RESUME_FIXED_SIZE = 22;
  function serializeResumeFrame(frame, encoders) {
    const resumeTokenLength = encoders.resumeToken.byteLength(
      frame.resumeToken
    );
    const buffer = createBuffer(
      FRAME_HEADER_SIZE + RESUME_FIXED_SIZE + resumeTokenLength
    );

    let offset = writeHeader(frame, buffer);
    offset = buffer.writeUInt16BE(frame.majorVersion, offset);
    offset = buffer.writeUInt16BE(frame.minorVersion, offset);
    offset = buffer.writeUInt16BE(resumeTokenLength, offset);
    offset = encoders.resumeToken.encode(
      frame.resumeToken,
      buffer,
      offset,
      offset + resumeTokenLength
    );

    offset = writeUInt64BE(buffer, frame.serverPosition, offset);
    writeUInt64BE(buffer, frame.clientPosition, offset);
    return buffer;
  }

  function sizeOfResumeFrame(frame, encoders) {
    const resumeTokenLength = encoders.resumeToken.byteLength(
      frame.resumeToken
    );
    return FRAME_HEADER_SIZE + RESUME_FIXED_SIZE + resumeTokenLength;
  }

  function deserializeResumeFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId === 0,
      'RSocketBinaryFraming: Invalid RESUME frame, expected stream id to be 0.'
    );

    const length = buffer.length;
    let offset = FRAME_HEADER_SIZE;
    const majorVersion = buffer.readUInt16BE(offset);
    offset += 2;
    const minorVersion = buffer.readUInt16BE(offset);
    offset += 2;

    const resumeTokenLength = buffer.readInt16BE(offset);
    offset += 2;
    invariant_1(
      resumeTokenLength >= 0 && resumeTokenLength <= MAX_RESUME_LENGTH,
      'RSocketBinaryFraming: Invalid SETUP frame, expected resumeToken length ' +
        'to be >= 0 and <= %s. Got `%s`.',
      MAX_RESUME_LENGTH,
      resumeTokenLength
    );

    const resumeToken = encoders.resumeToken.decode(
      buffer,
      offset,
      offset + resumeTokenLength
    );

    offset += resumeTokenLength;
    const serverPosition = readUInt64BE(buffer, offset);
    offset += 8;
    const clientPosition = readUInt64BE(buffer, offset);
    offset += 8;
    return {
      clientPosition,
      flags,
      length,
      majorVersion,
      minorVersion,
      resumeToken,
      serverPosition,
      streamId,
      type: FRAME_TYPES.RESUME,
    };
  }

  /**
   * Writes a RESUME_OK frame into a new buffer and returns it.
   *
   * Fixed size is:
   * - client position (uint64 = 8)
   */
  const RESUME_OK_FIXED_SIZE = 8;
  function serializeResumeOkFrame(frame, encoders) {
    const buffer = createBuffer(FRAME_HEADER_SIZE + RESUME_OK_FIXED_SIZE);
    const offset = writeHeader(frame, buffer);
    writeUInt64BE(buffer, frame.clientPosition, offset);
    return buffer;
  }

  function sizeOfResumeOkFrame(frame, encoders) {
    return FRAME_HEADER_SIZE + RESUME_OK_FIXED_SIZE;
  }

  function deserializeResumeOkFrame(buffer, streamId, flags, encoders) {
    invariant_1(
      streamId === 0,
      'RSocketBinaryFraming: Invalid RESUME frame, expected stream id to be 0.'
    );

    const length = buffer.length;
    const clientPosition = readUInt64BE(buffer, FRAME_HEADER_SIZE);
    return {
      clientPosition,
      flags,
      length,
      streamId,
      type: FRAME_TYPES.RESUME_OK,
    };
  }

  /**
   * Write the header of the frame into the buffer.
   */
  function writeHeader(frame, buffer) {
    const offset = buffer.writeInt32BE(frame.streamId, 0);
    // shift frame to high 6 bits, extract lowest 10 bits from flags
    return buffer.writeUInt16BE(
      (frame.type << FRAME_TYPE_OFFFSET) | (frame.flags & FLAGS_MASK),
      offset
    );
  }

  /**
   * Determine the length of the payload section of a frame. Only applies to
   * frame types that MAY have both metadata and data.
   */
  function getPayloadLength(frame, encoders) {
    let payloadLength = 0;
    if (frame.data != null) {
      payloadLength += encoders.data.byteLength(frame.data);
    }
    if (isMetadata(frame.flags)) {
      payloadLength += UINT24_SIZE;
      if (frame.metadata != null) {
        payloadLength += encoders.metadata.byteLength(frame.metadata);
      }
    }
    return payloadLength;
  }

  /**
   * Write the payload of a frame into the given buffer. Only applies to frame
   * types that MAY have both metadata and data.
   */
  function writePayload(frame, buffer, encoders, offset) {
    if (isMetadata(frame.flags)) {
      if (frame.metadata != null) {
        const metaLength = encoders.metadata.byteLength(frame.metadata);
        offset = writeUInt24BE(buffer, metaLength, offset);
        offset = encoders.metadata.encode(
          frame.metadata,
          buffer,
          offset,
          offset + metaLength
        );
      } else {
        offset = writeUInt24BE(buffer, 0, offset);
      }
    }
    if (frame.data != null) {
      encoders.data.encode(frame.data, buffer, offset, buffer.length);
    }
  }

  /**
   * Read the payload from a buffer and write it into the frame. Only applies to
   * frame types that MAY have both metadata and data.
   */
  function readPayload(buffer, frame, encoders, offset) {
    if (isMetadata(frame.flags)) {
      const metaLength = readUInt24BE(buffer, offset);
      offset += UINT24_SIZE;
      if (metaLength > 0) {
        frame.metadata = encoders.metadata.decode(
          buffer,
          offset,
          offset + metaLength
        );

        offset += metaLength;
      }
    }
    if (offset < buffer.length) {
      frame.data = encoders.data.decode(buffer, offset, buffer.length);
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

  /**
   * NOTE: This implementation conforms to an upcoming version of the RSocket protocol
   *       and will not work with version 1.0 servers.
   *
   * An implementation of the DuplexConnection interface that supports automatic
   * resumption per the RSocket protocol.
   *
   * # Example
   *
   * Create a client instance:
   * ```
   * const client = new RSocketClient({
   *   ...,
   *   transport: new RSocketResumableTransport(
   *     () => new RSocketWebSocketClient(...), // provider for low-level transport instances
   *     {
   *       bufferSize: 10, // max number of sent & pending frames to buffer before failing
   *       resumeToken: 'abc123', // string to uniquely identify the session across connections
   *     }
   *   ),
   * })
   *
   * Open the connection. After this if the connection dies it will be auto-resumed:
   * ```
   * client.connect().subscribe(...);
   * ```
   *
   * Optionally, subscribe to the status of the connection:
   * ```
   * client.connectionStatus().subscribe(...);
   * ```
   *
   * # Implementation Notes
   *
   * This transport maintains:
   * - _currentConnection: a current low-level transport, which is null when not
   *   connected
   * - _sentFrames: a buffer of frames written to a low-level transport (which
   *   may or may not have been received by the server)
   * - _pendingFrames: a buffer of frames not yet written to the low-level
   *   connection, because they were sent while not connected.
   *
   * The initial connection is simple: connect using the low-level transport and
   * flush any _pendingFrames (write them and add them to _sentFrames).
   *
   * Thereafter if the low-level transport drops, this transport attempts resumption.
   * It obtains a fresh low-level transport from the given transport `source`
   * and attempts to connect. Once connected, it sends a RESUME frame and waits.
   * If RESUME_OK is received, _sentFrames and _pendingFrames are adjusted such
   * that:
   * - any frames the server has received are removed from _sentFrames
   * - the remaining frames are merged (in correct order) into _pendingFrames
   *
   * Then the connection proceeds as above, where all pending frames are flushed.
   * If anything other than RESUME_OK is received, resumption is considered to
   * have failed and the connection is set to the ERROR status.
   */
  class RSocketResumableTransport {
    constructor(source, options, encoders) {
      invariant_1(
        options.bufferSize >= 0,
        'RSocketResumableTransport: bufferSize option must be >= 0, got `%s`.',
        options.bufferSize
      );

      this._encoders = encoders;
      this._bufferSize = options.bufferSize;
      this._sentFramesSize = 0;
      this._position = {
        client: 0,
        server: 0,
      };

      this._currentConnection = null;
      this._statusSubscription = null;
      this._receiveSubscription = null;
      this._receivers = new Set();
      this._resumeToken = options.resumeToken;
      this._sessionTimeoutMillis = options.sessionDurationSeconds * 1000;
      this._sessionTimeoutHandle = null;
      this._senders = new Set();
      this._sentFrames = [];
      this._setupFrame = null;
      this._source = source;
      this._status = rsocketTypes.CONNECTION_STATUS.NOT_CONNECTED;
      this._statusSubscribers = new Set();
    }

    close() {
      this._close();
    }

    connect() {
      invariant_1(
        !this._isTerminated(),
        'RSocketResumableTransport: Cannot connect(), connection terminated (%s: %s).',
        this._status.kind,
        this._status.kind === 'ERROR'
          ? this._status.error.message
          : 'no message'
      );

      try {
        this._disconnect();
        this._currentConnection = null;
        this._receiveSubscription = null;
        this._statusSubscription = null;
        this._setConnectionStatus(rsocketTypes.CONNECTION_STATUS.CONNECTING);
        const connection = this._source();
        connection.connectionStatus().subscribe({
          onNext: (status) => {
            if (status.kind === this._status.kind) {
              return;
            }
            if (status.kind === 'CONNECTED') {
              if (this._sessionTimeoutHandle) {
                clearTimeout(this._sessionTimeoutHandle);
                this._sessionTimeoutHandle = null;
              }
              //Setup
              if (this._setupFrame == null) {
                this._handleConnected(connection);
                //Resume
              } else {
                this._handleResume(connection);
              }
            } else if (this._isTerminationStatus(status)) {
              if (!this._sessionTimeoutHandle) {
                this._sessionTimeoutHandle = setTimeout(
                  () => this._close(this._resumeTimeoutError()),
                  this._sessionTimeoutMillis
                );
              }
              this._disconnect();
              this._setConnectionStatus(
                rsocketTypes.CONNECTION_STATUS.NOT_CONNECTED
              );
            }
          },
          onSubscribe: (subscription) => {
            this._statusSubscription = subscription;
            subscription.request(Number.MAX_SAFE_INTEGER);
          },
        });

        connection.connect();
      } catch (error) {
        this._close(error);
      }
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
        let added = false;
        subject.onSubscribe({
          cancel: () => {
            this._receivers.delete(subject);
          },
          request: () => {
            if (!added) {
              added = true;
              this._receivers.add(subject);
            }
          },
        });
      });
    }

    sendOne(frame) {
      try {
        this._writeFrame(frame);
      } catch (error) {
        this._close(error);
      }
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
      if (this._isTerminated()) {
        return;
      }
      if (error) {
        this._setConnectionStatus({error, kind: 'ERROR'});
      } else {
        this._setConnectionStatus(rsocketTypes.CONNECTION_STATUS.CLOSED);
      }
      const receivers = this._receivers;
      receivers.forEach((r) => r.onComplete());
      receivers.clear();

      const senders = this._senders;
      senders.forEach((s) => s.cancel());
      senders.clear();
      this._sentFrames.length = 0;

      this._disconnect();
    }

    _disconnect() {
      if (this._statusSubscription) {
        this._statusSubscription.cancel();
        this._statusSubscription = null;
      }
      if (this._receiveSubscription) {
        this._receiveSubscription.cancel();
        this._receiveSubscription = null;
      }
      if (this._currentConnection) {
        this._currentConnection.close();
        this._currentConnection = null;
      }
    }

    _handleConnected(connection) {
      this._currentConnection = connection;
      this._flushFrames();
      this._setConnectionStatus(rsocketTypes.CONNECTION_STATUS.CONNECTED);
      connection.receive().subscribe({
        onNext: (frame) => {
          try {
            this._receiveFrame(frame);
          } catch (error) {
            this._close(error);
          }
        },
        onSubscribe: (subscription) => {
          this._receiveSubscription = subscription;
          subscription.request(Number.MAX_SAFE_INTEGER);
        },
      });
    }

    _handleResume(connection) {
      connection
        .receive()
        .take(1)
        .subscribe({
          onNext: (frame) => {
            try {
              if (frame.type === FRAME_TYPES.RESUME_OK) {
                const {clientPosition} = frame;
                // clientPosition indicates which frames the server is missing:
                // - anything after that still needs to be sent
                // - anything before that can be discarded
                if (clientPosition < this._position.client) {
                  // Invalid RESUME_OK frame: server asked for an older
                  // client frame than is available
                  this._close(this._nonResumableStateError());
                  return;
                }
                // remove tail frames of total length = remoteImpliedPos-localPos
                let removeSize = clientPosition - this._position.client;
                let index = 0;
                while (removeSize > 0) {
                  const frameSize = this._onReleasedTailFrame(
                    this._sentFrames[index]
                  );

                  if (!frameSize) {
                    this._close(this._absentLengthError(frame));
                    return;
                  }
                  removeSize -= frameSize;
                  index++;
                }
                if (removeSize !== 0) {
                  this._close(this._inconsistentImpliedPositionError());
                  return;
                }
                // Drop sent frames that the server has received
                if (index > 0) {
                  this._sentFrames.splice(0, index);
                }
                // Continue connecting, which will flush pending frames
                this._handleConnected(connection);
              } else {
                const error =
                  frame.type === FRAME_TYPES.ERROR
                    ? createErrorFromFrame(frame)
                    : new Error(
                        'RSocketResumableTransport: Resumption failed for an ' +
                          'unspecified reason.'
                      );

                this._close(error);
              }
            } catch (error) {
              this._close(error);
            }
          },
          onSubscribe: (subscription) => {
            this._receiveSubscription = subscription;
            subscription.request(1);
          },
        });

      const setupFrame = this._setupFrame;
      invariant_1(
        setupFrame,
        'RSocketResumableTransport: Cannot resume, setup frame has not been sent.'
      );

      connection.sendOne({
        clientPosition: this._position.client,
        flags: 0,
        majorVersion: setupFrame.majorVersion,
        minorVersion: setupFrame.minorVersion,
        resumeToken: this._resumeToken,
        serverPosition: this._position.server,
        streamId: CONNECTION_STREAM_ID,
        type: FRAME_TYPES.RESUME,
      });
    }

    _absentLengthError(frame) {
      return new Error(
        'RSocketResumableTransport: absent frame.length for type ' + frame.type
      );
    }

    _inconsistentImpliedPositionError() {
      return new Error(
        'RSocketResumableTransport: local frames are inconsistent with remote implied position'
      );
    }

    _nonResumableStateError() {
      return new Error(
        'RSocketResumableTransport: resumption failed, server is ' +
          'missing frames that are no longer in the client buffer.'
      );
    }

    _resumeTimeoutError() {
      return new Error(
        'RSocketResumableTransport: resumable session timed out'
      );
    }

    _isTerminated() {
      return this._isTerminationStatus(this._status);
    }

    _isTerminationStatus(status) {
      const kind = status.kind;
      return kind === 'CLOSED' || kind === 'ERROR';
    }

    _setConnectionStatus(status) {
      if (status.kind === this._status.kind) {
        return;
      }
      this._status = status;
      this._statusSubscribers.forEach((subscriber) =>
        subscriber.onNext(status)
      );
    }

    _receiveFrame(frame) {
      if (isResumePositionFrameType(frame.type)) {
        if (frame.length) {
          this._position.server += frame.length;
        }
      }
      // TODO: trim _sentFrames on KEEPALIVE frame
      this._receivers.forEach((subscriber) => subscriber.onNext(frame));
    }

    _flushFrames() {
      this._sentFrames.forEach((frame) => {
        const connection = this._currentConnection;
        if (connection) {
          connection.sendOne(frame);
        }
      });
    }

    _onReleasedTailFrame(frame) {
      const removedFrameSize = frame.length;
      if (removedFrameSize) {
        this._sentFramesSize -= removedFrameSize;
        this._position.client += removedFrameSize;
        return removedFrameSize;
      }
    }

    _writeFrame(frame) {
      // Ensure that SETUP frames contain the resume token
      if (frame.type === FRAME_TYPES.SETUP) {
        frame = _objectSpread2(
          _objectSpread2({}, frame),
          {},
          {
            flags: frame.flags | FLAGS.RESUME_ENABLE, // eslint-disable-line no-bitwise
            resumeToken: this._resumeToken,
          }
        );

        this._setupFrame = frame; // frame can only be a SetupFrame
      }
      frame.length = sizeOfFrame(frame, this._encoders);
      // If connected, immediately write frames to the low-level transport
      // and consider them "sent". The resumption protocol will figure out
      // which frames may not have been received and recover.
      if (isResumePositionFrameType(frame.type)) {
        let available = this._bufferSize - this._sentFramesSize;
        const frameSize = frame.length;
        if (frameSize) {
          // remove tail until there is space for new frame
          while (available < frameSize) {
            const removedFrame = this._sentFrames.shift();
            if (removedFrame) {
              const removedFrameSize = this._onReleasedTailFrame(removedFrame);
              if (!removedFrameSize) {
                this._close(this._absentLengthError(frame));
                return;
              }
              available += removedFrameSize;
            } else {
              break;
            }
          }
          if (available >= frameSize) {
            this._sentFrames.push(frame);
            this._sentFramesSize += frameSize;
          } else {
            this._position.client += frameSize;
          }
        } else {
          this._close(this._absentLengthError(frame));
          return;
        }
      }
      const currentConnection = this._currentConnection;
      if (currentConnection) {
        currentConnection.sendOne(frame);
      }
    }
  }

  class WellKnownMimeType {
    constructor(str, identifier) {
      this._string = str;
      this._identifier = identifier;
    }

    /**
     * Find the {@link WellKnownMimeType} for the given identifier (as an {@code int}). Valid
     * identifiers are defined to be integers between 0 and 127, inclusive. Identifiers outside of
     * this range will produce the {@link #UNPARSEABLE_MIME_TYPE}. Additionally, some identifiers in
     * that range are still only reserved and don't have a type associated yet: this method returns
     * the {@link #UNKNOWN_RESERVED_MIME_TYPE} when passing such an identifier, which lets call sites
     * potentially detect this and keep the original representation when transmitting the associated
     * metadata buffer.
     *
     * @param id the looked up identifier
     * @return the {@link WellKnownMimeType}, or {@link #UNKNOWN_RESERVED_MIME_TYPE} if the id is out
     *     of the specification's range, or {@link #UNKNOWN_RESERVED_MIME_TYPE} if the id is one that
     *     is merely reserved but unknown to this implementation.
     */
    static fromIdentifier(id) {
      if (id < 0x00 || id > 0x7f) {
        return UNPARSEABLE_MIME_TYPE;
      }
      return TYPES_BY_MIME_ID[id];
    }

    /**
     * Find the {@link WellKnownMimeType} for the given {@link String} representation. If the
     * representation is {@code null} or doesn't match a {@link WellKnownMimeType}, the {@link
     * #UNPARSEABLE_MIME_TYPE} is returned.
     *
     * @param mimeType the looked up mime type
     * @return the matching {@link WellKnownMimeType}, or {@link #UNPARSEABLE_MIME_TYPE} if none
     *     matches
     */
    static fromString(mimeType) {
      if (!mimeType) {
        throw new Error('type must be non-null');
      }

      // force UNPARSEABLE if by chance UNKNOWN_RESERVED_MIME_TYPE's text has been used
      if (mimeType === UNKNOWN_RESERVED_MIME_TYPE.string) {
        return UNPARSEABLE_MIME_TYPE;
      }

      return TYPES_BY_MIME_STRING.get(mimeType) || UNPARSEABLE_MIME_TYPE;
    }

    /** @return the byte identifier of the mime type, guaranteed to be positive or zero. */
    get identifier() {
      return this._identifier;
    }

    /**
     * @return the mime type represented as a {@link String}, which is made of US_ASCII compatible
     *     characters only
     */
    get string() {
      return this._string;
    }

    /** @see #getString() */
    toString() {
      return this._string;
    }
  }

  const UNPARSEABLE_MIME_TYPE = new WellKnownMimeType(
    'UNPARSEABLE_MIME_TYPE_DO_NOT_USE',
    -2
  );

  const UNKNOWN_RESERVED_MIME_TYPE = new WellKnownMimeType(
    'UNKNOWN_YET_RESERVED_DO_NOT_USE',
    -1
  );

  const APPLICATION_AVRO = new WellKnownMimeType('application/avro', 0x00);

  const APPLICATION_CBOR = new WellKnownMimeType('application/cbor', 0x01);

  const APPLICATION_GRAPHQL = new WellKnownMimeType(
    'application/graphql',
    0x02
  );

  const APPLICATION_GZIP = new WellKnownMimeType('application/gzip', 0x03);

  const APPLICATION_JAVASCRIPT = new WellKnownMimeType(
    'application/javascript',
    0x04
  );

  const APPLICATION_JSON = new WellKnownMimeType('application/json', 0x05);

  const APPLICATION_OCTET_STREAM = new WellKnownMimeType(
    'application/octet-stream',
    0x06
  );

  const APPLICATION_PDF = new WellKnownMimeType('application/pdf', 0x07);

  const APPLICATION_THRIFT = new WellKnownMimeType(
    'application/vnd.apache.thrift.binary',
    0x08
  );

  const APPLICATION_PROTOBUF = new WellKnownMimeType(
    'application/vnd.google.protobuf',
    0x09
  );

  const APPLICATION_XML = new WellKnownMimeType('application/xml', 0x0a);

  const APPLICATION_ZIP = new WellKnownMimeType('application/zip', 0x0b);

  const AUDIO_AAC = new WellKnownMimeType('audio/aac', 0x0c);

  const AUDIO_MP3 = new WellKnownMimeType('audio/mp3', 0x0d);

  const AUDIO_MP4 = new WellKnownMimeType('audio/mp4', 0x0e);

  const AUDIO_MPEG3 = new WellKnownMimeType('audio/mpeg3', 0x0f);

  const AUDIO_MPEG = new WellKnownMimeType('audio/mpeg', 0x10);

  const AUDIO_OGG = new WellKnownMimeType('audio/ogg', 0x11);

  const AUDIO_OPUS = new WellKnownMimeType('audio/opus', 0x12);

  const AUDIO_VORBIS = new WellKnownMimeType('audio/vorbis', 0x13);

  const IMAGE_BMP = new WellKnownMimeType('image/bmp', 0x14);

  const IMAGE_GIG = new WellKnownMimeType('image/gif', 0x15);

  const IMAGE_HEIC_SEQUENCE = new WellKnownMimeType(
    'image/heic-sequence',
    0x16
  );

  const IMAGE_HEIC = new WellKnownMimeType('image/heic', 0x17);

  const IMAGE_HEIF_SEQUENCE = new WellKnownMimeType(
    'image/heif-sequence',
    0x18
  );

  const IMAGE_HEIF = new WellKnownMimeType('image/heif', 0x19);

  const IMAGE_JPEG = new WellKnownMimeType('image/jpeg', 0x1a);

  const IMAGE_PNG = new WellKnownMimeType('image/png', 0x1b);

  const IMAGE_TIFF = new WellKnownMimeType('image/tiff', 0x1c);

  const MULTIPART_MIXED = new WellKnownMimeType('multipart/mixed', 0x1d);

  const TEXT_CSS = new WellKnownMimeType('text/css', 0x1e);

  const TEXT_CSV = new WellKnownMimeType('text/csv', 0x1f);

  const TEXT_HTML = new WellKnownMimeType('text/html', 0x20);

  const TEXT_PLAIN = new WellKnownMimeType('text/plain', 0x21);

  const TEXT_XML = new WellKnownMimeType('text/xml', 0x22);

  const VIDEO_H264 = new WellKnownMimeType('video/H264', 0x23);

  const VIDEO_H265 = new WellKnownMimeType('video/H265', 0x24);

  const VIDEO_VP8 = new WellKnownMimeType('video/VP8', 0x25);

  const APPLICATION_HESSIAN = new WellKnownMimeType(
    'application/x-hessian',
    0x26
  );

  const APPLICATION_JAVA_OBJECT = new WellKnownMimeType(
    'application/x-java-object',
    0x27
  );

  const APPLICATION_CLOUDEVENTS_JSON = new WellKnownMimeType(
    'application/cloudevents+json',
    0x28
  );

  // ... reserved for future use ...
  const MESSAGE_RSOCKET_MIMETYPE = new WellKnownMimeType(
    'message/x.rsocket.mime-type.v0',
    0x7a
  );

  const MESSAGE_RSOCKET_ACCEPT_MIMETYPES = new WellKnownMimeType(
    'message/x.rsocket.accept-mime-types.v0',
    0x7b
  );

  const MESSAGE_RSOCKET_AUTHENTICATION = new WellKnownMimeType(
    'message/x.rsocket.authentication.v0',
    0x7c
  );

  const MESSAGE_RSOCKET_TRACING_ZIPKIN = new WellKnownMimeType(
    'message/x.rsocket.tracing-zipkin.v0',
    0x7d
  );

  const MESSAGE_RSOCKET_ROUTING = new WellKnownMimeType(
    'message/x.rsocket.routing.v0',
    0x7e
  );

  const MESSAGE_RSOCKET_COMPOSITE_METADATA = new WellKnownMimeType(
    'message/x.rsocket.composite-metadata.v0',
    0x7f
  );

  const TYPES_BY_MIME_ID = new Array(128);
  const TYPES_BY_MIME_STRING = new Map();

  const ALL_MIME_TYPES = [
    UNPARSEABLE_MIME_TYPE,
    UNKNOWN_RESERVED_MIME_TYPE,
    APPLICATION_AVRO,
    APPLICATION_CBOR,
    APPLICATION_GRAPHQL,
    APPLICATION_GZIP,
    APPLICATION_JAVASCRIPT,
    APPLICATION_JSON,
    APPLICATION_OCTET_STREAM,
    APPLICATION_PDF,
    APPLICATION_THRIFT,
    APPLICATION_PROTOBUF,
    APPLICATION_XML,
    APPLICATION_ZIP,
    AUDIO_AAC,
    AUDIO_MP3,
    AUDIO_MP4,
    AUDIO_MPEG3,
    AUDIO_MPEG,
    AUDIO_OGG,
    AUDIO_OPUS,
    AUDIO_VORBIS,
    IMAGE_BMP,
    IMAGE_GIG,
    IMAGE_HEIC_SEQUENCE,
    IMAGE_HEIC,
    IMAGE_HEIF_SEQUENCE,
    IMAGE_HEIF,
    IMAGE_JPEG,
    IMAGE_PNG,
    IMAGE_TIFF,
    MULTIPART_MIXED,
    TEXT_CSS,
    TEXT_CSV,
    TEXT_HTML,
    TEXT_PLAIN,
    TEXT_XML,
    VIDEO_H264,
    VIDEO_H265,
    VIDEO_VP8,
    APPLICATION_HESSIAN,
    APPLICATION_JAVA_OBJECT,
    APPLICATION_CLOUDEVENTS_JSON,
    MESSAGE_RSOCKET_MIMETYPE,
    MESSAGE_RSOCKET_ACCEPT_MIMETYPES,
    MESSAGE_RSOCKET_AUTHENTICATION,
    MESSAGE_RSOCKET_TRACING_ZIPKIN,
    MESSAGE_RSOCKET_ROUTING,
    MESSAGE_RSOCKET_COMPOSITE_METADATA,
  ];

  TYPES_BY_MIME_ID.fill(UNKNOWN_RESERVED_MIME_TYPE);

  for (const value of ALL_MIME_TYPES) {
    if (value.identifier >= 0) {
      TYPES_BY_MIME_ID[value.identifier] = value;
      TYPES_BY_MIME_STRING.set(value.string, value);
    }
  }

  if (Object.seal) {
    Object.seal(TYPES_BY_MIME_ID);
  }

  class CompositeMetadata {
    constructor(buffer) {
      this._buffer = buffer;
    }
    // $FlowFixMe
    [Symbol.iterator]() {
      return entriesIterator(this._buffer);
    }
  }

  /**
   * Encode a new sub-metadata information into a composite metadata {@link CompositeByteBuf
   * buffer}, without checking if the {@link String} can be matched with a well known compressable
   * mime type. Prefer using this method and {@link #encodeAndAddMetadata(CompositeByteBuf,
   * ByteBufAllocator, WellKnownMimeType, ByteBuf)} if you know in advance whether or not the mime
   * is well known. Otherwise use {@link #encodeAndAddMetadataWithCompression(CompositeByteBuf,
   * ByteBufAllocator, String, ByteBuf)}
   *
   * @param compositeMetaData the buffer that will hold all composite metadata information.
   * @param allocator the {@link ByteBufAllocator} to use to create intermediate buffers as needed.
   * @param customMimeType the custom mime type to encode.
   * @param metadata the metadata value to encode.
   */
  // see #encodeMetadataHeader(ByteBufAllocator, String, int)
  function encodeAndAddCustomMetadata(
    compositeMetaData,
    customMimeType,
    metadata
  ) {
    return LiteBuffer.concat([
      compositeMetaData,
      encodeCustomMetadataHeader(customMimeType, metadata.byteLength),
      metadata,
    ]);
  }

  /**
   * Encode a new sub-metadata information into a composite metadata {@link CompositeByteBuf
   * buffer}.
   *
   * @param compositeMetaData the buffer that will hold all composite metadata information.
   * @param allocator the {@link ByteBufAllocator} to use to create intermediate buffers as needed.
   * @param knownMimeType the {@link WellKnownMimeType} to encode.
   * @param metadata the metadata value to encode.
   */
  // see #encodeMetadataHeader(ByteBufAllocator, byte, int)
  function encodeAndAddWellKnownMetadata(
    compositeMetaData,
    knownMimeType,
    metadata
  ) {
    let mimeTypeId;

    if (Number.isInteger(knownMimeType)) {
      mimeTypeId = knownMimeType;
    } else {
      mimeTypeId = knownMimeType.identifier;
    }

    return LiteBuffer.concat([
      compositeMetaData,
      encodeWellKnownMetadataHeader(mimeTypeId, metadata.byteLength),
      metadata,
    ]);
  }

  /**
   * Decode the next metadata entry (a mime header + content pair of {@link ByteBuf}) from a {@link
   * ByteBuf} that contains at least enough bytes for one more such entry. These buffers are
   * actually slices of the full metadata buffer, and this method doesn't move the full metadata
   * buffer's {@link ByteBuf#readerIndex()}. As such, it requires the user to provide an {@code
   * index} to read from. The next index is computed by calling {@link #computeNextEntryIndex(int,
   * ByteBuf, ByteBuf)}. Size of the first buffer (the "header buffer") drives which decoding method
   * should be further applied to it.
   *
   * <p>The header buffer is either:
   *
   * <ul>
   *   <li>made up of a single byte: this represents an encoded mime id, which can be further
   *       decoded using {@link #decodeMimeIdFromMimeBuffer(ByteBuf)}
   *   <li>made up of 2 or more bytes: this represents an encoded mime String + its length, which
   *       can be further decoded using {@link #decodeMimeTypeFromMimeBuffer(ByteBuf)}. Note the
   *       encoded length, in the first byte, is skipped by this decoding method because the
   *       remaining length of the buffer is that of the mime string.
   * </ul>
   *
   * @param compositeMetadata the source {@link ByteBuf} that originally contains one or more
   *     metadata entries
   * @param entryIndex the {@link ByteBuf#readerIndex()} to start decoding from. original reader
   *     index is kept on the source buffer
   * @param retainSlices should produced metadata entry buffers {@link ByteBuf#slice() slices} be
   *     {@link ByteBuf#retainedSlice() retained}?
   * @return a {@link ByteBuf} array of length 2 containing the mime header buffer
   *     <strong>slice</strong> and the content buffer <strong>slice</strong>, or one of the
   *     zero-length error constant arrays
   */
  function decodeMimeAndContentBuffersSlices(compositeMetadata, entryIndex) {
    const mimeIdOrLength = compositeMetadata.readInt8(entryIndex);
    let mime;
    let toSkip = entryIndex;
    if (
      (mimeIdOrLength & STREAM_METADATA_KNOWN_MASK) ===
      STREAM_METADATA_KNOWN_MASK
    ) {
      mime = compositeMetadata.slice(toSkip, toSkip + 1);
      toSkip += 1;
    } else {
      // M flag unset, remaining 7 bits are the length of the mime
      const mimeLength = (mimeIdOrLength & 0xff) + 1;

      if (compositeMetadata.byteLength > toSkip + mimeLength) {
        // need to be able to read an extra mimeLength bytes (we have already read one so byteLength should be strictly more)
        // here we need a way for the returned ByteBuf to differentiate between a
        // 1-byte length mime type and a 1 byte encoded mime id, preferably without
        // re-applying the byte mask. The easiest way is to include the initial byte
        // and have further decoding ignore the first byte. 1 byte buffer == id, 2+ byte
        // buffer == full mime string.
        mime = compositeMetadata.slice(toSkip, toSkip + mimeLength + 1);

        // we thus need to skip the bytes we just sliced, but not the flag/length byte
        // which was already skipped in initial read
        toSkip += mimeLength + 1;
      } else {
        throw new Error(
          'Metadata is malformed. Inappropriately formed Mime Length'
        );
      }
    }

    if (compositeMetadata.byteLength >= toSkip + 3) {
      // ensures the length medium can be read
      const metadataLength = readUInt24BE(compositeMetadata, toSkip);
      toSkip += 3;
      if (compositeMetadata.byteLength >= metadataLength + toSkip) {
        const metadata = compositeMetadata.slice(
          toSkip,
          toSkip + metadataLength
        );
        return [mime, metadata];
      } else {
        throw new Error(
          'Metadata is malformed. Inappropriately formed Metadata Length or malformed content'
        );
      }
    } else {
      throw new Error(
        'Metadata is malformed. Metadata Length is absent or malformed'
      );
    }
  }

  /**
   * Decode a {@link CharSequence} custome mime type from a {@link ByteBuf}, assuming said buffer
   * properly contains such a mime type.
   *
   * <p>The buffer must at least have two readable bytes, which distinguishes it from the {@link
   * #decodeMimeIdFromMimeBuffer(ByteBuf) compressed id} case. The first byte is a size and the
   * remaining bytes must correspond to the {@link CharSequence}, encoded fully in US_ASCII. As a
   * result, the first byte can simply be skipped, and the remaining of the buffer be decoded to the
   * mime type.
   *
   * <p>If the mime header buffer is less than 2 bytes long, returns {@code null}.
   *
   * @param flyweightMimeBuffer the mime header {@link ByteBuf} that contains length + custom mime
   *     type
   * @return the decoded custom mime type, as a {@link CharSequence}, or null if the input is
   *     invalid
   * @see #decodeMimeIdFromMimeBuffer(ByteBuf)
   */
  function decodeMimeTypeFromMimeBuffer(flyweightMimeBuffer) {
    if (flyweightMimeBuffer.length < 2) {
      throw new Error('Unable to decode explicit MIME type');
    }
    // the encoded length is assumed to be kept at the start of the buffer
    // but also assumed to be irrelevant because the rest of the slice length
    // actually already matches _decoded_length
    return flyweightMimeBuffer.toString('ascii', 1);
  }

  function encodeCustomMetadataHeader(customMime, metadataLength) {
    const metadataHeader = createBuffer(4 + customMime.length);
    // reserve 1 byte for the customMime length
    // /!\ careful not to read that first byte, which is random at this point
    // int writerIndexInitial = metadataHeader.writerIndex();
    // metadataHeader.writerIndex(writerIndexInitial + 1);

    // write the custom mime in UTF8 but validate it is all ASCII-compatible
    // (which produces the right result since ASCII chars are still encoded on 1 byte in UTF8)
    const customMimeLength = metadataHeader.write(customMime, 1);
    if (!isAscii(metadataHeader, 1)) {
      throw new Error('Custom mime type must be US_ASCII characters only');
    }
    if (customMimeLength < 1 || customMimeLength > 128) {
      throw new Error(
        'Custom mime type must have a strictly positive length that fits on 7 unsigned bits, ie 1-128'
      );
    }
    // encoded length is one less than actual length, since 0 is never a valid length, which gives
    // wider representation range
    metadataHeader.writeUInt8(customMimeLength - 1);

    writeUInt24BE(metadataHeader, metadataLength, customMimeLength + 1);

    return metadataHeader;
  }

  /**
   * Encode a {@link WellKnownMimeType well known mime type} and a metadata value length into a
   * newly allocated {@link ByteBuf}.
   *
   * <p>This compact representation encodes the mime type via its ID on a single byte, and the
   * unsigned value length on 3 additional bytes.
   *
   * @param allocator the {@link ByteBufAllocator} to use to create the buffer.
   * @param mimeType a byte identifier of a {@link WellKnownMimeType} to encode.
   * @param metadataLength the metadata length to append to the buffer as an unsigned 24 bits
   *     integer.
   * @return the encoded mime and metadata length information
   */
  function encodeWellKnownMetadataHeader(mimeType, metadataLength) {
    const buffer = LiteBuffer.alloc(4);

    buffer.writeUInt8(mimeType | STREAM_METADATA_KNOWN_MASK);
    writeUInt24BE(buffer, metadataLength, 1);

    return buffer;
  }

  function* entriesIterator(buffer) {
    const length = buffer.byteLength;
    let entryIndex = 0;

    while (entryIndex < length) {
      const headerAndData = decodeMimeAndContentBuffersSlices(
        buffer,
        entryIndex
      );

      const header = headerAndData[0];
      const data = headerAndData[1];

      entryIndex = computeNextEntryIndex(entryIndex, header, data);

      if (!isWellKnownMimeType(header)) {
        const typeString = decodeMimeTypeFromMimeBuffer(header);
        if (!typeString) {
          throw new Error('MIME type cannot be null');
        }

        yield new ExplicitMimeTimeEntry(data, typeString);
        continue;
      }

      const id = decodeMimeIdFromMimeBuffer(header);
      const type = WellKnownMimeType.fromIdentifier(id);
      if (UNKNOWN_RESERVED_MIME_TYPE === type) {
        yield new ReservedMimeTypeEntry(data, id);
        continue;
      }

      yield new WellKnownMimeTypeEntry(data, type);
    }
  }

  class ExplicitMimeTimeEntry {
    constructor(content, type) {
      this._content = content;
      this._type = type;
    }

    get content() {
      return this._content;
    }

    get mimeType() {
      return this._type;
    }
  }

  class ReservedMimeTypeEntry {
    constructor(content, type) {
      this._content = content;
      this._type = type;
    }

    get content() {
      return this._content;
    }

    /**
     * {@inheritDoc} Since this entry represents a compressed id that couldn't be decoded, this is
     * always {@code null}.
     */
    get mimeType() {
      return undefined;
    }

    /**
     * Returns the reserved, but unknown {@link WellKnownMimeType} for this entry. Range is 0-127
     * (inclusive).
     *
     * @return the reserved, but unknown {@link WellKnownMimeType} for this entry
     */
    get type() {
      return this._type;
    }
  }

  class WellKnownMimeTypeEntry {
    constructor(content, type) {
      this._content = content;
      this._type = type;
    }

    get content() {
      return this._content;
    }

    get mimeType() {
      return this._type.string;
    }

    /**
     * Returns the {@link WellKnownMimeType} for this entry.
     *
     * @return the {@link WellKnownMimeType} for this entry
     */
    get type() {
      return this._type;
    }
  }

  /**
   * Decode a {@code byte} compressed mime id from a {@link ByteBuf}, assuming said buffer properly
   * contains such an id.
   *
   * <p>The buffer must have exactly one readable byte, which is assumed to have been tested for
   * mime id encoding via the {@link #STREAM_METADATA_KNOWN_MASK} mask ({@code firstByte &
   * STREAM_METADATA_KNOWN_MASK) == STREAM_METADATA_KNOWN_MASK}).
   *
   * <p>If there is no readable byte, the negative identifier of {@link
   * WellKnownMimeType#UNPARSEABLE_MIME_TYPE} is returned.
   *
   * @param mimeBuffer the buffer that should next contain the compressed mime id byte
   * @return the compressed mime id, between 0 and 127, or a negative id if the input is invalid
   * @see #decodeMimeTypeFromMimeBuffer(ByteBuf)
   */
  function decodeMimeIdFromMimeBuffer(mimeBuffer) {
    if (!isWellKnownMimeType(mimeBuffer)) {
      return UNPARSEABLE_MIME_TYPE.identifier;
    }
    return mimeBuffer.readInt8() & STREAM_METADATA_LENGTH_MASK;
  }

  function computeNextEntryIndex(currentEntryIndex, headerSlice, contentSlice) {
    return (
      currentEntryIndex +
      headerSlice.byteLength + // this includes the mime length byte
      3 + // 3 bytes of the content length, which are excluded from the slice
      contentSlice.byteLength
    );
  }

  function isWellKnownMimeType(header) {
    return header.byteLength === 1;
  }

  const STREAM_METADATA_KNOWN_MASK = 0x80; // 1000 0000
  const STREAM_METADATA_LENGTH_MASK = 0x7f; // 0111 1111

  function isAscii(buffer, offset) {
    let isAscii = true;
    for (let i = offset, length = buffer.length; i < length; i++) {
      if (buffer[i] > 127) {
        isAscii = false;
        break;
      }
    }

    return isAscii;
  }

  exports.APPLICATION_AVRO = APPLICATION_AVRO;
  exports.APPLICATION_CBOR = APPLICATION_CBOR;
  exports.APPLICATION_CLOUDEVENTS_JSON = APPLICATION_CLOUDEVENTS_JSON;
  exports.APPLICATION_GRAPHQL = APPLICATION_GRAPHQL;
  exports.APPLICATION_GZIP = APPLICATION_GZIP;
  exports.APPLICATION_HESSIAN = APPLICATION_HESSIAN;
  exports.APPLICATION_JAVASCRIPT = APPLICATION_JAVASCRIPT;
  exports.APPLICATION_JAVA_OBJECT = APPLICATION_JAVA_OBJECT;
  exports.APPLICATION_JSON = APPLICATION_JSON;
  exports.APPLICATION_OCTET_STREAM = APPLICATION_OCTET_STREAM;
  exports.APPLICATION_PDF = APPLICATION_PDF;
  exports.APPLICATION_PROTOBUF = APPLICATION_PROTOBUF;
  exports.APPLICATION_THRIFT = APPLICATION_THRIFT;
  exports.APPLICATION_XML = APPLICATION_XML;
  exports.APPLICATION_ZIP = APPLICATION_ZIP;
  exports.AUDIO_AAC = AUDIO_AAC;
  exports.AUDIO_MP3 = AUDIO_MP3;
  exports.AUDIO_MP4 = AUDIO_MP4;
  exports.AUDIO_MPEG = AUDIO_MPEG;
  exports.AUDIO_MPEG3 = AUDIO_MPEG3;
  exports.AUDIO_OGG = AUDIO_OGG;
  exports.AUDIO_OPUS = AUDIO_OPUS;
  exports.AUDIO_VORBIS = AUDIO_VORBIS;
  exports.BufferEncoder = BufferEncoder;
  exports.BufferEncoders = BufferEncoders;
  exports.CONNECTION_STREAM_ID = CONNECTION_STREAM_ID;
  exports.CompositeMetadata = CompositeMetadata;
  exports.ERROR_CODES = ERROR_CODES;
  exports.ERROR_EXPLANATIONS = ERROR_EXPLANATIONS;
  exports.ExplicitMimeTimeEntry = ExplicitMimeTimeEntry;
  exports.FLAGS = FLAGS;
  exports.FLAGS_MASK = FLAGS_MASK;
  exports.FRAME_TYPES = FRAME_TYPES;
  exports.FRAME_TYPE_OFFFSET = FRAME_TYPE_OFFFSET;
  exports.IMAGE_BMP = IMAGE_BMP;
  exports.IMAGE_GIG = IMAGE_GIG;
  exports.IMAGE_HEIC = IMAGE_HEIC;
  exports.IMAGE_HEIC_SEQUENCE = IMAGE_HEIC_SEQUENCE;
  exports.IMAGE_HEIF = IMAGE_HEIF;
  exports.IMAGE_HEIF_SEQUENCE = IMAGE_HEIF_SEQUENCE;
  exports.IMAGE_JPEG = IMAGE_JPEG;
  exports.IMAGE_PNG = IMAGE_PNG;
  exports.IMAGE_TIFF = IMAGE_TIFF;
  exports.IdentitySerializer = IdentitySerializer;
  exports.IdentitySerializers = IdentitySerializers;
  exports.JsonSerializer = JsonSerializer;
  exports.JsonSerializers = JsonSerializers;
  exports.Lease = Lease;
  exports.Leases = Leases;
  exports.MAX_CODE = MAX_CODE;
  exports.MAX_KEEPALIVE = MAX_KEEPALIVE;
  exports.MAX_LIFETIME = MAX_LIFETIME;
  exports.MAX_MIME_LENGTH = MAX_MIME_LENGTH;
  exports.MAX_RESUME_LENGTH = MAX_RESUME_LENGTH;
  exports.MAX_STREAM_ID = MAX_STREAM_ID;
  exports.MAX_VERSION = MAX_VERSION;
  exports.MESSAGE_RSOCKET_ACCEPT_MIMETYPES = MESSAGE_RSOCKET_ACCEPT_MIMETYPES;
  exports.MESSAGE_RSOCKET_AUTHENTICATION = MESSAGE_RSOCKET_AUTHENTICATION;
  exports.MESSAGE_RSOCKET_COMPOSITE_METADATA = MESSAGE_RSOCKET_COMPOSITE_METADATA;
  exports.MESSAGE_RSOCKET_MIMETYPE = MESSAGE_RSOCKET_MIMETYPE;
  exports.MESSAGE_RSOCKET_ROUTING = MESSAGE_RSOCKET_ROUTING;
  exports.MESSAGE_RSOCKET_TRACING_ZIPKIN = MESSAGE_RSOCKET_TRACING_ZIPKIN;
  exports.MULTIPART_MIXED = MULTIPART_MIXED;
  exports.RSocketClient = RSocketClient;
  exports.RSocketResumableTransport = RSocketResumableTransport;
  exports.RSocketServer = RSocketServer;
  exports.ReservedMimeTypeEntry = ReservedMimeTypeEntry;
  exports.TEXT_CSS = TEXT_CSS;
  exports.TEXT_CSV = TEXT_CSV;
  exports.TEXT_HTML = TEXT_HTML;
  exports.TEXT_PLAIN = TEXT_PLAIN;
  exports.TEXT_XML = TEXT_XML;
  exports.UNKNOWN_RESERVED_MIME_TYPE = UNKNOWN_RESERVED_MIME_TYPE;
  exports.UNPARSEABLE_MIME_TYPE = UNPARSEABLE_MIME_TYPE;
  exports.UTF8Encoder = UTF8Encoder;
  exports.Utf8Encoders = Utf8Encoders;
  exports.VIDEO_H264 = VIDEO_H264;
  exports.VIDEO_H265 = VIDEO_H265;
  exports.VIDEO_VP8 = VIDEO_VP8;
  exports.WellKnownMimeType = WellKnownMimeType;
  exports.WellKnownMimeTypeEntry = WellKnownMimeTypeEntry;
  exports.byteLength = byteLength;
  exports.createBuffer = createBuffer;
  exports.createErrorFromFrame = createErrorFromFrame;
  exports.deserializeFrame = deserializeFrame;
  exports.deserializeFrameWithLength = deserializeFrameWithLength;
  exports.deserializeFrames = deserializeFrames;
  exports.encodeAndAddCustomMetadata = encodeAndAddCustomMetadata;
  exports.encodeAndAddWellKnownMetadata = encodeAndAddWellKnownMetadata;
  exports.getErrorCodeExplanation = getErrorCodeExplanation;
  exports.isComplete = isComplete;
  exports.isIgnore = isIgnore;
  exports.isLease = isLease;
  exports.isMetadata = isMetadata;
  exports.isNext = isNext;
  exports.isRespond = isRespond;
  exports.isResumeEnable = isResumeEnable;
  exports.printFrame = printFrame;
  exports.readUInt24BE = readUInt24BE;
  exports.serializeFrame = serializeFrame;
  exports.serializeFrameWithLength = serializeFrameWithLength;
  exports.toBuffer = toBuffer;
  exports.writeUInt24BE = writeUInt24BE;

  Object.defineProperty(exports, '__esModule', {value: true});

  return exports;
})({}, rsocketFlowable, rsocketTypes);
