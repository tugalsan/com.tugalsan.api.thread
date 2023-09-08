package com.tugalsan.api.thread.server.async.core;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadAsyncCoreParallelUntilAllDone<T> {

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
        public final TS_ThreadSyncLst<T> resultsForSuccessfulOnes = new TS_ThreadSyncLst();
        public final TS_ThreadSyncLst<Throwable> exceptions = new TS_ThreadSyncLst();

        @Override
        public InnerScope<T> joinUntil(Instant deadline) throws InterruptedException {
            try {
                super.joinUntil(deadline);
            } catch (TimeoutException e) {
                super.shutdown();
                exceptions.add(new TS_ThreadAsyncCoreTimeoutException());
            }
            return this;
        }
    }

    //until: Instant.now().plusMillis(10)
    private TS_ThreadAsyncCoreParallelUntilAllDone(TS_ThreadSyncTrigger killTrigger, Duration duration, List<TGS_CallableType1<T, TS_ThreadSyncTrigger>> callables) {
        var elapsedTracker = TS_TimeElapsed.of();
        try (var scope = new InnerScope<T>()) {
            callables.forEach(c -> scope.fork(() -> c.call(killTrigger)));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            resultsForSuccessfulOnes = scope.resultsForSuccessfulOnes.toList();
            exceptions = scope.exceptions.toList();
        } catch (InterruptedException e) {
            if (resultsForSuccessfulOnes == null) {
                resultsForSuccessfulOnes = TGS_ListUtils.of();
            }
            if (exceptions == null) {
                exceptions = TGS_ListUtils.of();
            }
            exceptions.add(e);
        } finally {
            this.elapsed = elapsedTracker.elapsed_now();
        }
    }
    final public Duration elapsed;

    public boolean timeout() {
        return exceptions.stream()
                .filter(e -> e instanceof TS_ThreadAsyncCoreTimeoutException)
                .findAny().isPresent();
    }
    public List<T> resultsForSuccessfulOnes;
    public List<Throwable> exceptions;

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public T findAny() {
        return resultsForSuccessfulOnes.stream().findAny().orElse(null);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilAllDone<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadSyncTrigger>... callables) {
        return of(killTrigger, duration, List.of(callables));
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilAllDone<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, List<TGS_CallableType1<T, TS_ThreadSyncTrigger>> callables) {
        return new TS_ThreadAsyncCoreParallelUntilAllDone(killTrigger, duration, callables);
    }
}
