package com.tugalsan.api.thread.server.async.await;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import com.tugalsan.api.function.client.TGS_FuncUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadAsyncAwaitParallelUntilAllDone<T> {

    private static class InnerScope<T> extends StructuredTaskScope<T> {

        @Override
        protected void handleComplete(StructuredTaskScope.Subtask<? extends T> subTask) {
            if (subTask.state().equals(StructuredTaskScope.Subtask.State.UNAVAILABLE)) {
                throw new IllegalStateException("State should not be running!");
            }
            if (subTask.state().equals(StructuredTaskScope.Subtask.State.SUCCESS)) {
                var result = subTask.get();
                if (result == null) {
                    return;
                }
                resultsForSuccessfulOnes.add(result);
                return;
            }
            if (subTask.state().equals(StructuredTaskScope.Subtask.State.FAILED)) {
                exceptions.add(subTask.exception());
            }
        }
        public final TS_ThreadSyncLst<T> resultsForSuccessfulOnes = TS_ThreadSyncLst.ofSlowRead();
        public final TS_ThreadSyncLst<Throwable> exceptions = TS_ThreadSyncLst.ofSlowRead();

        @Override
        public InnerScope<T> joinUntil(Instant deadline) throws InterruptedException {
            try {
                super.joinUntil(deadline);
            } catch (TimeoutException te) {
                super.shutdown();
                exceptions.add(te);
            }
            return this;
        }
    }

    //until: Instant.now().plusMillis(10)
    private TS_ThreadAsyncAwaitParallelUntilAllDone(TS_ThreadSyncTrigger _killTrigger, Duration duration, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        TS_ThreadSyncTrigger killTrigger = _killTrigger.newChild();
        var elapsedTracker = TS_TimeElapsed.of();
        try (var scope = new InnerScope<T>()) {
            callables.forEach(c -> scope.fork(() -> c.call(killTrigger)));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            resultsForSuccessfulOnes = scope.resultsForSuccessfulOnes.toList_modifiable();
            exceptions = scope.exceptions.toList_modifiable();
        } catch (InterruptedException e) {
            if (resultsForSuccessfulOnes == null) {
                resultsForSuccessfulOnes = TGS_ListUtils.of();
            }
            if (exceptions == null) {
                exceptions = TGS_ListUtils.of();
            }
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
                .filter(e -> e instanceof TimeoutException)
                .findAny().isPresent();
        var shutdownBugExists = exceptions.stream()
                .filter(e -> e instanceof IllegalStateException ei && ei.getMessage().contains("Owner did not join after forking subtasks"))
                .findAny().isPresent();
        return timeoutExists || shutdownBugExists;
    }
    public List<T> resultsForSuccessfulOnes;
    public List<Throwable> exceptions;

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public T findAny() {
        return resultsForSuccessfulOnes.stream().findAny().orElse(null);
    }

    protected static <T> TS_ThreadAsyncAwaitParallelUntilAllDone<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return of(killTrigger, duration, List.of(callables));
    }

    protected static <T> TS_ThreadAsyncAwaitParallelUntilAllDone<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return new TS_ThreadAsyncAwaitParallelUntilAllDone(killTrigger, duration, callables);
    }
}
