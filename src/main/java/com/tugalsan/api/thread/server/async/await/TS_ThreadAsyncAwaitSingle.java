package com.tugalsan.api.thread.server.async.await;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In1;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import com.tugalsan.api.function.client.TGS_FuncUtils;
import com.tugalsan.api.log.server.TS_Log;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class TS_ThreadAsyncAwaitSingle<T> {

    private static TS_Log d = TS_Log.of(TS_ThreadAsyncAwaitSingle.class);

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnFailure innerScope = new StructuredTaskScope.ShutdownOnFailure();
        public volatile TimeoutException timeoutException = null;
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
            } catch (TimeoutException te) {
                setTimeout(true, te);
            }
            return this;
        }

        public StructuredTaskScope.Subtask<T> fork(Callable<? extends T> task) {
            StructuredTaskScope.Subtask<T> _subTask = innerScope.fork(task);
            subTask.set(_subTask);
            return _subTask;
        }

        public void shutdown() {
            innerScope.shutdown();
        }

        @Override
        public void close() {
            innerScope.close();
        }

        public Optional<Throwable> exceptionIfFailed() {
            return timeoutException == null ? innerScope.exception() : Optional.of(timeoutException);
        }

        public Optional<T> resultIfSuccessful() {
            var task = subTask.get();
            if (task == null || task.state() != StructuredTaskScope.Subtask.State.SUCCESS || task.get() == null) {
                return Optional.empty();
            }
            return Optional.of(task.get());
        }

        public void setTimeout(boolean triggerShutDown, TimeoutException te) {
            if (triggerShutDown) {
                innerScope.shutdown();
            }
            timeoutException = te;
        }
    }

    private TS_ThreadAsyncAwaitSingle(TS_ThreadSyncTrigger _killTrigger, Duration duration, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger> callable) {
        TS_ThreadSyncTrigger killTrigger = _killTrigger.newChild();
        var elapsedTracker = TS_TimeElapsed.of();
        InnerScope<T> scope = new InnerScope();
        try {
            scope.fork(() -> callable.call(killTrigger));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            scope.throwIfFailed();
            resultIfSuccessful = scope.resultIfSuccessful();
            exceptionIfFailed = scope.exceptionIfFailed();
        } catch (InterruptedException | ExecutionException | IllegalStateException e) {
            resultIfSuccessful = Optional.empty();
//            if (e instanceof TimeoutException te) {
//                scope.setTimeout(true, te);
//                exceptionIfFailed = Optional.of(te);
//            }
            if (e instanceof IllegalStateException ei && ei.getMessage().contains("Owner did not join after forking subtasks")) {
                var te = new TimeoutException(ei.getMessage());
                exceptionIfFailed = Optional.of(te);
                scope.setTimeout(false, te);
            } else {
                exceptionIfFailed = Optional.of(e);
            }
            TGS_FuncUtils.throwIfInterruptedException(e);
        } finally {
            killTrigger.trigger();
            scope.shutdown();
            this.elapsed = elapsedTracker.elapsed_now();
        }
    }
    final public Duration elapsed;

    public boolean timeout() {
        var timeoutExists = exceptionIfFailed.isPresent() && exceptionIfFailed.get() instanceof TimeoutException;
        var shutdownBugExists = exceptionIfFailed.isPresent() && exceptionIfFailed.get() instanceof IllegalStateException ei && ei.getMessage().contains("Owner did not join after forking subtasks");
        return timeoutExists || shutdownBugExists;
    }
    public Optional<T> resultIfSuccessful;
    public Optional<Throwable> exceptionIfFailed;

    public boolean hasError() {
        return exceptionIfFailed.isPresent();
    }

    protected static <T> TS_ThreadAsyncAwaitSingle<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger> callable) {
        return new TS_ThreadAsyncAwaitSingle(killTrigger, duration, callable);
    }
}
