package com.tugalsan.api.thread.server.async.core;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class TS_ThreadAsyncCoreSingle<T> {

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnFailure innerScope = new StructuredTaskScope.ShutdownOnFailure();
        public volatile boolean timeout = false;
        public final AtomicReference<StructuredTaskScope.Subtask<T>> subTask = new AtomicReference();

        public void throwIfFailed() throws ExecutionException {
            innerScope.throwIfFailed();
        }

        public InnerScope<T> join() throws InterruptedException {
            innerScope.join();
            return this;
        }

        public InnerScope<T> joinUntil(Instant deadline) throws InterruptedException {
            try {
                innerScope.joinUntil(deadline);
            } catch (TimeoutException e) {
                innerScope.shutdown();
                timeout = true;
            }
            return this;
        }

        public StructuredTaskScope.Subtask<T> fork(Callable<? extends T> task) {
            subTask.set(innerScope.fork(task));
            return subTask.get();
        }

        public void shutdown() {
            innerScope.shutdown();
        }

        @Override
        public void close() {
            innerScope.close();
        }

        public Optional<Throwable> exceptionIfFailed() {
            return timeout ? Optional.of(new TS_ThreadAsyncCoreTimeoutException()) : innerScope.exception();
        }

        public Optional<T> resultIfSuccessful() {
            var task = subTask.get();
            if (task == null || task.state() != StructuredTaskScope.Subtask.State.SUCCESS || task.get() == null) {
                return Optional.empty();
            }
            return Optional.of(task.get());
        }
    }

    private TS_ThreadAsyncCoreSingle(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadSyncTrigger> callable) {
        var elapsedTracker = TS_TimeElapsed.of();
        try (var scope = new InnerScope<T>()) {
            scope.fork(() -> callable.call(killTrigger));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            scope.throwIfFailed();
            resultIfSuccessful = scope.resultIfSuccessful();
            exceptionIfFailed = scope.exceptionIfFailed();
        } catch (InterruptedException | ExecutionException e) {
            exceptionIfFailed = Optional.of(e);
        } finally {
            this.elapsed = elapsedTracker.elapsed_now();
        }
    }
    final public Duration elapsed;

    public boolean timeout() {
        return exceptionIfFailed.isPresent() && exceptionIfFailed.get() instanceof TS_ThreadAsyncCoreTimeoutException;
    }
    public Optional<T> resultIfSuccessful;
    public Optional<Throwable> exceptionIfFailed;

    public boolean hasError() {
        return exceptionIfFailed.isPresent();
    }

    public static <T> TS_ThreadAsyncCoreSingle<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadSyncTrigger> callable) {
        return new TS_ThreadAsyncCoreSingle(killTrigger, duration, callable);
    }
}
