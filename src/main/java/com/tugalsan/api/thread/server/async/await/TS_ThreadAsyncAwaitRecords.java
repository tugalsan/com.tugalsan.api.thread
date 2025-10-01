package com.tugalsan.api.thread.server.async.await;

import module com.tugalsan.api.thread;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

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
            return resultsFailedOrUnavailable.stream().filter(r -> r.state() == StructuredTaskScope.Subtask.State.FAILED).map(StructuredTaskScope.Subtask::exception).toList();
        }

        public boolean hasError() {
            return timeout() || !resultsFailedOrUnavailable.isEmpty();
        }
    }

    public static record AnySuccessfulOrThrow<R>(TS_ThreadSyncTrigger killTrigger, Duration timeoutDuration, Optional<StructuredTaskScope.TimeoutException> timeoutException, Optional<StructuredTaskScope.FailedException> failedException, Optional<R> result) {

        public boolean timeout() {
            return timeoutException.isPresent();
        }

        public boolean hasError() {
            return timeout() || !failedException.isEmpty();
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
            return timeout() || !failedException.isEmpty();
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
