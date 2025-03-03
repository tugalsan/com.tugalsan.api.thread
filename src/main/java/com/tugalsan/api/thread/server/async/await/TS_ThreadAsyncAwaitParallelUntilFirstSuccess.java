package com.tugalsan.api.thread.server.async.await;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import com.tugalsan.api.function.client.TGS_FuncUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadAsyncAwaitParallelUntilFirstSuccess<T> {

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnSuccess<T> innerScope = new StructuredTaskScope.ShutdownOnSuccess();
        public volatile TimeoutException timeoutException = null;
        public final TS_ThreadSyncLst<StructuredTaskScope.Subtask<T>> subTasks = TS_ThreadSyncLst.ofSlowRead();

        public InnerScope<T> join() throws InterruptedException {
            innerScope.join();
            return this;
        }

        public InnerScope<T> joinUntil(Instant until) throws InterruptedException {
            try {
                innerScope.joinUntil(until);
            } catch (TimeoutException e) {
                innerScope.shutdown();
                timeoutException = e;
            }
            return this;
        }

        public StructuredTaskScope.Subtask<T> fork(Callable<? extends T> task) {
            StructuredTaskScope.Subtask<T> subTask = innerScope.fork(task);
            subTasks.add(subTask);
            return subTask;
        }

        public void shutdown() {
            innerScope.shutdown();
        }

        @Override
        public void close() {
            innerScope.close();
        }

        public T resultIfAnySuccessful() throws ExecutionException {
            return timeoutException == null ? innerScope.result() : null;
        }
    }

    private TS_ThreadAsyncAwaitParallelUntilFirstSuccess(TS_ThreadSyncTrigger _killTrigger, Duration duration, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        TS_ThreadSyncTrigger killTrigger = _killTrigger.newChild();
        var elapsedTracker = TS_TimeElapsed.of();
        try (var scope = new InnerScope<T>()) {
            callables.forEach(c -> scope.fork(() -> c.call(killTrigger)));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            if (scope.timeoutException != null) {
                exceptions.add(scope.timeoutException);
            }
            resultIfAnySuccessful = scope.resultIfAnySuccessful() == null ? Optional.empty() : Optional.of(scope.resultIfAnySuccessful());
            states = TGS_StreamUtils.toLst(
                    scope.subTasks.stream().map(st -> st.state())
            );
        } catch (InterruptedException | ExecutionException e) {
            exceptions.add(e);
            TGS_FuncUtils.throwIfInterruptedException(e);
        } finally {
            killTrigger.trigger();
            this.elapsed = elapsedTracker.elapsed_now();
        }
    }
    final public Duration elapsed;

    public boolean timeout() {
        var timeoutExists = exceptions.stream()
                .anyMatch(e -> e instanceof TimeoutException);
        var shutdownBugExists = exceptions.stream()
                .anyMatch(e -> e instanceof IllegalStateException ei && ei.getMessage().contains("Owner did not join after forking subtasks"));
        return timeoutExists || shutdownBugExists;
    }
    public List<StructuredTaskScope.Subtask.State> states;
    public List<Exception> exceptions = TGS_ListUtils.of();
    public Optional<T> resultIfAnySuccessful;

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    protected static <T> TS_ThreadAsyncAwaitParallelUntilFirstSuccess<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return of(killTrigger, duration, List.of(callables));
    }

    protected static <T> TS_ThreadAsyncAwaitParallelUntilFirstSuccess<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return new TS_ThreadAsyncAwaitParallelUntilFirstSuccess(killTrigger, duration, callables);
    }
}
