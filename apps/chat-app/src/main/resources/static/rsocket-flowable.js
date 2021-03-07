var rsocketFlowable = (function (exports) {
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
  var nullthrows = function nullthrows(x) {
    if (x != null) {
      return x;
    }

    throw new Error('Got unexpected null or undefined');
  };

  var nullthrows_1 = nullthrows;

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
   * An operator that acts like Array.map, applying a given function to
   * all values provided by its `Subscription` and passing the result to its
   * `Subscriber`.
   */
  class FlowableMapOperator {
    constructor(subscriber, fn) {
      this._fn = fn;
      this._subscriber = subscriber;
      this._subscription = null;
    }

    onComplete() {
      this._subscriber.onComplete();
    }

    onError(error) {
      this._subscriber.onError(error);
    }

    onNext(t) {
      try {
        this._subscriber.onNext(this._fn(t));
      } catch (e) {
        nullthrows_1(this._subscription).cancel();
        this._subscriber.onError(e);
      }
    }

    onSubscribe(subscription) {
      this._subscription = subscription;
      this._subscriber.onSubscribe(subscription);
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
   * An operator that requests a fixed number of values from its source
   * `Subscription` and forwards them to its `Subscriber`, cancelling the
   * subscription when the requested number of items has been reached.
   */
  class FlowableTakeOperator {
    constructor(subscriber, toTake) {
      this._subscriber = subscriber;
      this._subscription = null;
      this._toTake = toTake;
    }

    onComplete() {
      this._subscriber.onComplete();
    }

    onError(error) {
      this._subscriber.onError(error);
    }

    onNext(t) {
      try {
        this._subscriber.onNext(t);
        if (--this._toTake === 0) {
          this._cancelAndComplete();
        }
      } catch (e) {
        nullthrows_1(this._subscription).cancel();
        this._subscriber.onError(e);
      }
    }

    onSubscribe(subscription) {
      this._subscription = subscription;
      this._subscriber.onSubscribe(subscription);
      if (this._toTake <= 0) {
        this._cancelAndComplete();
      }
    }

    _cancelAndComplete() {
      nullthrows_1(this._subscription).cancel();
      this._subscriber.onComplete();
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

  // $FlowFixMe
  class FlowableAsyncIterable {
    constructor(source, prefetch = 256) {
      this._source = source;
      this._prefetch = prefetch;
    }

    asyncIterator() {
      const asyncIteratorSubscriber = new AsyncIteratorSubscriber(
        this._prefetch
      );
      this._source.subscribe(asyncIteratorSubscriber);
      return asyncIteratorSubscriber;
    }

    // $FlowFixMe
    [Symbol.asyncIterator]() {
      return this.asyncIterator();
    }
  }

  // $FlowFixMe
  class AsyncIteratorSubscriber {
    constructor(prefetch = 256) {
      this._prefetch = prefetch;
      this._values = [];
      this._limit =
        prefetch === Number.MAX_SAFE_INTEGER
          ? Number.MAX_SAFE_INTEGER
          : prefetch - (prefetch >> 2);
      this._produced = 0;
    }

    onSubscribe(subscription) {
      this._subscription = subscription;
      subscription.request(this._prefetch);
    }

    onNext(value) {
      const resolve = this._resolve;
      if (resolve) {
        this._resolve = undefined;
        this._reject = undefined;

        if (++this._produced === this._limit) {
          this._produced = 0;
          this._subscription.request(this._limit);
        }

        resolve({done: false, value});
        return;
      }

      this._values.push(value);
    }

    onComplete() {
      this._done = true;

      const resolve = this._resolve;
      if (resolve) {
        this._resolve = undefined;
        this._reject = undefined;

        resolve({done: true});
      }
    }

    onError(error) {
      this._done = true;
      this._error = error;

      const reject = this._reject;
      if (reject) {
        this._resolve = undefined;
        this._reject = undefined;

        reject(error);
      }
    }

    next() {
      const value = this._values.shift();
      if (value) {
        if (++this._produced === this._limit) {
          this._produced = 0;
          this._subscription.request(this._limit);
        }

        return Promise.resolve({done: false, value});
      } else if (this._done) {
        if (this._error) {
          return Promise.reject(this._error);
        } else {
          return Promise.resolve({done: true});
        }
      } else {
        return new Promise((resolve, reject) => {
          this._resolve = resolve;
          this._reject = reject;
        });
      }
    }

    return() {
      this._subscription.cancel();
      return Promise.resolve({done: true});
    }

    // $FlowFixMe
    [Symbol.asyncIterator]() {
      return this;
    }
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
   * Implements the ReactiveStream `Publisher` interface with Rx-style operators.
   */
  class Flowable {
    static just(...values) {
      return new Flowable((subscriber) => {
        let cancelled = false;
        let i = 0;
        subscriber.onSubscribe({
          cancel: () => {
            cancelled = true;
          },
          request: (n) => {
            while (!cancelled && n > 0 && i < values.length) {
              subscriber.onNext(values[i++]);
              n--;
            }
            if (!cancelled && i == values.length) {
              subscriber.onComplete();
            }
          },
        });
      });
    }

    static error(error) {
      return new Flowable((subscriber) => {
        subscriber.onSubscribe({
          cancel: () => {},
          request: () => {
            subscriber.onError(error);
          },
        });
      });
    }

    static never() {
      return new Flowable((subscriber) => {
        subscriber.onSubscribe({
          cancel: emptyFunction_1,
          request: emptyFunction_1,
        });
      });
    }

    constructor(source, max = Number.MAX_SAFE_INTEGER) {
      this._max = max;
      this._source = source;
    }

    subscribe(subscriberOrCallback) {
      let partialSubscriber;
      if (typeof subscriberOrCallback === 'function') {
        partialSubscriber = this._wrapCallback(subscriberOrCallback);
      } else {
        partialSubscriber = subscriberOrCallback;
      }
      const subscriber = new FlowableSubscriber(partialSubscriber, this._max);
      this._source(subscriber);
    }

    lift(onSubscribeLift) {
      return new Flowable((subscriber) =>
        this._source(onSubscribeLift(subscriber))
      );
    }

    map(fn) {
      return this.lift((subscriber) => new FlowableMapOperator(subscriber, fn));
    }

    take(toTake) {
      return this.lift(
        (subscriber) => new FlowableTakeOperator(subscriber, toTake)
      );
    }

    toAsyncIterable(prefetch = 256) {
      return new FlowableAsyncIterable(this, prefetch);
    }

    _wrapCallback(callback) {
      const max = this._max;
      return {
        onNext: callback,
        onSubscribe(subscription) {
          subscription.request(max);
        },
      };
    }
  }

  /**
   * @private
   */
  class FlowableSubscriber {
    constructor(subscriber, max) {
      _defineProperty(
        this,
        '_cancel',

        () => {
          if (!this._active) {
            return;
          }
          this._active = false;
          if (this._subscription) {
            this._subscription.cancel();
          }
        }
      );
      _defineProperty(
        this,
        '_request',

        (n) => {
          invariant_1(
            Number.isInteger(n) && n >= 1,
            'Flowable: Expected request value to be an integer with a value greater than 0, got `%s`.',
            n
          );

          if (!this._active) {
            return;
          }
          if (n >= this._max) {
            this._pending = this._max;
          } else {
            this._pending += n;
            if (this._pending >= this._max) {
              this._pending = this._max;
            }
          }
          if (this._subscription) {
            this._subscription.request(n);
          }
        }
      );
      this._active = false;
      this._max = max;
      this._pending = 0;
      this._started = false;
      this._subscriber = subscriber || {};
      this._subscription = null;
    }
    onComplete() {
      if (!this._active) {
        warning_1(
          false,
          'Flowable: Invalid call to onComplete(): %s.',
          this._started
            ? 'onComplete/onError was already called'
            : 'onSubscribe has not been called'
        );
        return;
      }
      this._active = false;
      this._started = true;
      try {
        if (this._subscriber.onComplete) {
          this._subscriber.onComplete();
        }
      } catch (error) {
        if (this._subscriber.onError) {
          this._subscriber.onError(error);
        }
      }
    }
    onError(error) {
      if (this._started && !this._active) {
        warning_1(
          false,
          'Flowable: Invalid call to onError(): %s.',
          this._active
            ? 'onComplete/onError was already called'
            : 'onSubscribe has not been called'
        );
        return;
      }
      this._active = false;
      this._started = true;
      this._subscriber.onError && this._subscriber.onError(error);
    }
    onNext(data) {
      if (!this._active) {
        warning_1(
          false,
          'Flowable: Invalid call to onNext(): %s.',
          this._active
            ? 'onComplete/onError was already called'
            : 'onSubscribe has not been called'
        );
        return;
      }
      if (this._pending === 0) {
        warning_1(
          false,
          'Flowable: Invalid call to onNext(), all request()ed values have been ' +
            'published.'
        );
        return;
      }
      if (this._pending !== this._max) {
        this._pending--;
      }
      try {
        this._subscriber.onNext && this._subscriber.onNext(data);
      } catch (error) {
        if (this._subscription) {
          this._subscription.cancel();
        }
        this.onError(error);
      }
    }
    onSubscribe(subscription) {
      if (this._started) {
        warning_1(
          false,
          'Flowable: Invalid call to onSubscribe(): already called.'
        );
        return;
      }
      this._active = true;
      this._started = true;
      this._subscription = subscription;
      try {
        this._subscriber.onSubscribe &&
          this._subscriber.onSubscribe({
            cancel: this._cancel,
            request: this._request,
          });
      } catch (error) {
        this.onError(error);
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

  /**
   * Represents a lazy computation that will either produce a value of type T
   * or fail with an error. Calling `subscribe()` starts the
   * computation and returns a subscription object, which has an `unsubscribe()`
   * method that can be called to prevent completion/error callbacks from being
   * invoked and, where supported, to also cancel the computation.
   * Implementations may optionally implement cancellation; if they do not
   * `cancel()` is a no-op.
   *
   * Note: Unlike Promise, callbacks (onComplete/onError) may be invoked
   * synchronously.
   *
   * Example:
   *
   * ```
   * const value = new Single(subscriber => {
   *   const id = setTimeout(
   *     () => subscriber.onComplete('Hello!'),
   *     250
   *   );
   *   // Optional: Call `onSubscribe` with a cancellation callback
   *   subscriber.onSubscribe(() => clearTimeout(id));
   * });
   *
   * // Start the computation. onComplete will be called after the timeout
   * // with 'hello'  unless `cancel()` is called first.
   * value.subscribe({
   *   onComplete: value => console.log(value),
   *   onError: error => console.error(error),
   *   onSubscribe: cancel => ...
   * });
   * ```
   */
  class Single {
    static of(value) {
      return new Single((subscriber) => {
        subscriber.onSubscribe();
        subscriber.onComplete(value);
      });
    }

    static error(error) {
      return new Single((subscriber) => {
        subscriber.onSubscribe();
        subscriber.onError(error);
      });
    }

    static never() {
      return new Single((subscriber) => {
        subscriber.onSubscribe();
      });
    }

    constructor(source) {
      this._source = source;
    }

    subscribe(partialSubscriber) {
      const subscriber = new FutureSubscriber(partialSubscriber);
      try {
        this._source(subscriber);
      } catch (error) {
        subscriber.onError(error);
      }
    }

    flatMap(fn) {
      return new Single((subscriber) => {
        let currentCancel;
        const cancel = () => {
          currentCancel && currentCancel();
          currentCancel = null;
        };
        this._source({
          onComplete: (value) => {
            fn(value).subscribe({
              onComplete: (mapValue) => {
                subscriber.onComplete(mapValue);
              },
              onError: (error) => subscriber.onError(error),
              onSubscribe: (_cancel) => {
                currentCancel = _cancel;
              },
            });
          },
          onError: (error) => subscriber.onError(error),
          onSubscribe: (_cancel) => {
            currentCancel = _cancel;
            subscriber.onSubscribe(cancel);
          },
        });
      });
    }

    /**
     * Return a new Single that resolves to the value of this Single applied to
     * the given mapping function.
     */
    map(fn) {
      return new Single((subscriber) => {
        return this._source({
          onComplete: (value) => subscriber.onComplete(fn(value)),
          onError: (error) => subscriber.onError(error),
          onSubscribe: (cancel) => subscriber.onSubscribe(cancel),
        });
      });
    }

    then(successFn, errorFn) {
      this.subscribe({
        onComplete: successFn || emptyFunction_1,
        onError: errorFn || emptyFunction_1,
      });
    }
  }

  /**
   * @private
   */
  class FutureSubscriber {
    constructor(subscriber) {
      this._active = false;
      this._started = false;
      this._subscriber = subscriber || {};
    }

    onComplete(value) {
      if (!this._active) {
        warning_1(
          false,
          'Single: Invalid call to onComplete(): %s.',
          this._started
            ? 'onComplete/onError was already called'
            : 'onSubscribe has not been called'
        );

        return;
      }
      this._active = false;
      this._started = true;
      try {
        if (this._subscriber.onComplete) {
          this._subscriber.onComplete(value);
        }
      } catch (error) {
        if (this._subscriber.onError) {
          this._subscriber.onError(error);
        }
      }
    }

    onError(error) {
      if (this._started && !this._active) {
        warning_1(
          false,
          'Single: Invalid call to onError(): %s.',
          this._active
            ? 'onComplete/onError was already called'
            : 'onSubscribe has not been called'
        );

        return;
      }
      this._active = false;
      this._started = true;
      this._subscriber.onError && this._subscriber.onError(error);
    }

    onSubscribe(cancel) {
      if (this._started) {
        warning_1(
          false,
          'Single: Invalid call to onSubscribe(): already called.'
        );
        return;
      }
      this._active = true;
      this._started = true;
      try {
        this._subscriber.onSubscribe &&
          this._subscriber.onSubscribe(() => {
            if (!this._active) {
              return;
            }
            this._active = false;
            cancel && cancel();
          });
      } catch (error) {
        this.onError(error);
      }
    }
  }

  class FlowableProcessor {
    constructor(source, fn) {
      this._source = source;
      this._transformer = fn;
      this._done = false;
      this._mappers = []; //mappers for map function
    }

    onSubscribe(subscription) {
      this._subscription = subscription;
    }

    onNext(t) {
      if (!this._sink) {
        warning_1('Warning, premature onNext for processor, dropping value');
        return;
      }

      let val = t;
      if (this._transformer) {
        val = this._transformer(t);
      }
      const finalVal = this._mappers.reduce(
        (interimVal, mapper) => mapper(interimVal),
        val
      );

      this._sink.onNext(finalVal);
    }

    onError(error) {
      this._error = error;
      if (!this._sink) {
        warning_1(
          'Warning, premature onError for processor, marking complete/errored'
        );
      } else {
        this._sink.onError(error);
      }
    }

    onComplete() {
      this._done = true;
      if (!this._sink) {
        warning_1('Warning, premature onError for processor, marking complete');
      } else {
        this._sink.onComplete();
      }
    }

    subscribe(subscriber) {
      if (this._source.subscribe) {
        this._source.subscribe(this);
      }
      this._sink = subscriber;
      this._sink.onSubscribe(this);

      if (this._error) {
        this._sink.onError(this._error);
      } else if (this._done) {
        this._sink.onComplete();
      }
    }

    map(fn) {
      this._mappers.push(fn);
      return this;
    }

    request(n) {
      this._subscription && this._subscription.request(n);
    }

    cancel() {
      this._subscription && this._subscription.cancel();
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
   * Returns a Publisher that provides the current time (Date.now()) every `ms`
   * milliseconds.
   *
   * The timer is established on the first call to `request`: on each
   * interval a value is published if there are outstanding requests,
   * otherwise nothing occurs for that interval. This approach ensures
   * that the interval between `onNext` calls is as regular as possible
   * and means that overlapping `request` calls (ie calling again before
   * the previous values have been vended) behaves consistently.
   */
  function every(ms) {
    return new Flowable((subscriber) => {
      let intervalId = null;
      let pending = 0;
      subscriber.onSubscribe({
        cancel: () => {
          if (intervalId != null) {
            clearInterval(intervalId);
            intervalId = null;
          }
        },
        request: (n) => {
          if (n < Number.MAX_SAFE_INTEGER) {
            pending += n;
          } else {
            pending = Number.MAX_SAFE_INTEGER;
          }
          if (intervalId != null) {
            return;
          }
          intervalId = setInterval(() => {
            if (pending > 0) {
              if (pending !== Number.MAX_SAFE_INTEGER) {
                pending--;
              }
              subscriber.onNext(Date.now());
            }
          }, ms);
        },
      });
    });
  }

  exports.Flowable = Flowable;
  exports.FlowableProcessor = FlowableProcessor;
  exports.Single = Single;
  exports.every = every;

  Object.defineProperty(exports, '__esModule', {value: true});

  return exports;
})({});
