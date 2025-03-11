package com.tugalsan.api.thread.server.async.await;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import com.tugalsan.api.function.client.TGS_FuncUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

public class TS_ThreadAsyncAwaitParallelUntilFirstFail<T> {

    final private static TS_Log d = TS_Log.of(false, TS_ThreadAsyncAwaitParallelUntilFirstFail.class);

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnFailure innerScope = new StructuredTaskScope.ShutdownOnFailure();
        public volatile TimeoutException timeoutException = null;
        public final TS_ThreadSyncLst<StructuredTaskScope.Subtask<T>> subTasks = TS_ThreadSyncLst.ofSlowRead();

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
                setTimeout(true, e);
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

        public Optional<Throwable> exception() {
            var e = innerScope.exception().orElse(null);
            if (e != null && e.getCause() != null) {
                e = e.getCause();
            }
            return e == null ? Optional.empty() : Optional.of(e);
        }

        public List<T> resultsForSuccessfulOnes() {
            return TGS_StreamUtils.toLst(subTasks.stream()
                    .filter(st -> st.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                    .map(f -> f.get())
                    .filter(r -> r != null)
            );
        }

        public List<StructuredTaskScope.Subtask.State> states() {
            return TGS_StreamUtils.toLst(subTasks.stream()
                    .map(st -> st.state())
            );
        }

        public void setTimeout(boolean triggerShutDown, TimeoutException te) {
            if (triggerShutDown) {
                innerScope.shutdown();
            }
            timeoutException = te == null ? new TimeoutException("TS_ThreadAsyncAwaitParallelUntilFirstFail.TimeoutException") : te;
        }
    }

    private TS_ThreadAsyncAwaitParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration duration, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        var killTrigger_wt = TS_ThreadSyncTrigger.of(d.className, killTrigger);
        var elapsedTracker = TS_TimeElapsed.of();
        var o = new Object() {
            InnerScope<T> scope = null;
        };
        try (var scope = new InnerScope<T>()) {
            o.scope = scope;
            callables.forEach(c -> scope.fork(() -> c.call(killTrigger_wt)));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            scope.throwIfFailed();
            if (scope.timeoutException != null) {
                exceptions.add(new TimeoutException());
            }
            if (scope.exception().isPresent()) {
                exceptions.add(scope.exception().orElseThrow());
            }
            resultsForSuccessfulOnes = scope.resultsForSuccessfulOnes();
            states = scope.states();
        } catch (InterruptedException | ExecutionException | IllegalStateException e) {
//            if (e instanceof TimeoutException te) {
//                o.scope.setTimeout(true, te);
//            }
            if (e instanceof IllegalStateException ei && ei.getMessage().contains("Owner did not join after forking subtasks")) {
                var te = new TimeoutException(ei.getMessage());
                o.scope.setTimeout(false, te);
                exceptions.add(te);
            } else {
                exceptions.add(e);
            }
            TGS_FuncUtils.throwIfInterruptedException(e);
        } finally {
            killTrigger_wt.trigger("ff_inawait_finally");
            this.elapsed = elapsedTracker.elapsed_now();
        }
    }
    final public Duration elapsed;

    public boolean timeout() {
        var timeoutExists = exceptions.stream()
                .anyMatch(TimeoutException.class::isInstance);
        var shutdownBugExists = exceptions.stream()
                .anyMatch(e -> e instanceof IllegalStateException ei && ei.getMessage().contains("Owner did not join after forking subtasks"));
        return timeoutExists || shutdownBugExists;
    }
    public List<StructuredTaskScope.Subtask.State> states = TGS_ListUtils.of();
    public List<Throwable> exceptions = TGS_ListUtils.of();
    public List<T> resultsForSuccessfulOnes = TGS_ListUtils.of();

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger> callable) {
//        return of(killTrigger, duration, List.of(callable));
//    }
    protected static <T> TS_ThreadAsyncAwaitParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return of(killTrigger, duration, List.of(callables));
    }

    protected static <T> TS_ThreadAsyncAwaitParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return new TS_ThreadAsyncAwaitParallelUntilFirstFail(killTrigger, duration, callables);
    }

//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, Callable<T> fetcher, Callable<Void>... throwingValidators) {
//        return of(killTrigger, duration, fetcher, List.of(throwingValidators));
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, Callable<T> fetcher, List<Callable<Void>> throwingValidators) {
//        List<Callable<T>> fetchers = TGS_ListUtils.of();
//        fetchers.add(fetcher);
//        return of(killTrigger, duration, fetchers, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, List<Callable<T>> fetchers, Callable<Void>... throwingValidators) {
//        return of(killTrigger, duration, fetchers, List.of(throwingValidators));
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, List<Callable<T>> fetchers, List<Callable<Void>> throwingValidators) {
//        List<Callable<T>> callables = TGS_ListUtils.of();
//        callables.addAll(fetchers);
//        throwingValidators.forEach(tv -> callables.add(() -> {
//            tv.call();
//            return TGS_CallableVoid.of();
//        }));
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, duration, callables);
//    }
}
