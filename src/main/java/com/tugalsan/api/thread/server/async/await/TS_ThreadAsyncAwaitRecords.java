package com.tugalsan.api.thread.server.async.await;

import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

public class TS_ThreadAsyncAwaitRecords {

    private TS_ThreadAsyncAwaitRecords() {

    }

    public static record AllAwait<R>(TS_ThreadSyncTrigger killTrigger, Duration timeoutDuration, Optional<StructuredTaskScope.TimeoutException> timeoutException, List<StructuredTaskScope.Subtask<R>> resultsFailedOrUnavailable, List<R> resultsSuccessful) {

        public boolean timeout() {
            return timeoutException.isPresent();
        }

        public List<Throwable> exceptions() {
            if (timeoutException.isPresent()) {
                return List.of(timeoutException.orElseThrow());
            }
            return resultsFailedOrUnavailable.stream().filter(r -> r.state() == State.FAILED).map(StructuredTaskScope.Subtask::exception).toList();
        }

        public boolean hasError() {
            return resultsSuccessful.isEmpty() || timeout() || !resultsFailedOrUnavailable.isEmpty();
        }
    }

    public static record AnySuccessfulOrThrow<R>(TS_ThreadSyncTrigger killTrigger, Duration timeoutDuration, Optional<StructuredTaskScope.TimeoutException> timeoutException, Optional<StructuredTaskScope.FailedException> failedException, Optional<R> result) {

        public boolean timeout() {
            return timeoutException.isPresent();
        }

        public boolean hasError() {
            return result.isEmpty() || timeout() || !failedException.isEmpty();
        }

        public Optional<Throwable> exceptionIfFailed() {
            if (timeoutException.isPresent()) {
                return Optional.of(timeoutException.orElseThrow());
            }
            if (failedException.isPresent()) {
                return Optional.of(failedException.orElseThrow());
            }
            return Optional.empty();
        }
    }

    public static record AllSuccessfulOrThrow<R>(TS_ThreadSyncTrigger killTrigger, Duration timeoutDuration, Optional<StructuredTaskScope.TimeoutException> timeoutException, Optional<StructuredTaskScope.FailedException> failedException, List<R> results) {

        public boolean timeout() {
            return timeoutException.isPresent();
        }

        public boolean hasError() {
            return results.isEmpty() || timeout() || !failedException.isEmpty();
        }

        public Optional<Throwable> exceptionIfFailed() {
            if (timeoutException.isPresent()) {
                return Optional.of(timeoutException.orElseThrow());
            }
            if (failedException.isPresent()) {
                return Optional.of(failedException.orElseThrow());
            }
            return Optional.empty();
        }
    }

    public static record SingleSuccessfulOrThrow<R>(TS_ThreadSyncTrigger killTrigger, Duration timeoutDuration, Optional<StructuredTaskScope.TimeoutException> timeoutException, Optional<StructuredTaskScope.FailedException> failedException, Optional<R> result) {

        public boolean timeout() {
            return timeoutException.isPresent();
        }

        public boolean hasError() {
            return result.isEmpty() || timeout() || !failedException.isEmpty();
        }

        public Optional<Throwable> exceptionIfFailed() {
            if (timeoutException.isPresent()) {
                return Optional.of(timeoutException.orElseThrow());
            }
            if (failedException.isPresent()) {
                return Optional.of(failedException.orElseThrow());
            }
            return Optional.empty();
        }
    }
}
