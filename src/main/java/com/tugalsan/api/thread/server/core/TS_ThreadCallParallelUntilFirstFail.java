package com.tugalsan.api.thread.server.core;

import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.thread.server.TS_ThreadSafeLst;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

public class TS_ThreadCallParallelUntilFirstFail<T> {

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnFailure innerScope = new StructuredTaskScope.ShutdownOnFailure();
        public volatile boolean timeout = false;
        public final TS_ThreadSafeLst<Future<T>> futures = new TS_ThreadSafeLst();

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

        public Future<T> fork(Callable<? extends T> task) {
            Future<T> future = innerScope.fork(task);
            futures.add(future);
            return future;
        }

        public void shutdown() {
            innerScope.shutdown();
        }

        @Override
        public void close() {
            innerScope.close();
        }

        public Throwable exception() {
            return innerScope.exception().orElse(null);
        }

        public List<T> resultsForSuccessfulOnes() {
            return TGS_StreamUtils.toLst(futures.stream()
                    .filter(f -> f.state() == State.SUCCESS)
                    .map(f -> f.resultNow())
                    .filter(r -> r != null)
            );
        }

        public List<State> states() {
            return TGS_StreamUtils.toLst(futures.stream()
                    .map(f -> f.state())
            );
        }
    }

    private TS_ThreadCallParallelUntilFirstFail(Duration duration, List<Callable<T>> callables) {
        try ( var scope = new InnerScope<T>()) {
            callables.forEach(c -> scope.fork(c));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            if (scope.timeout) {
                exceptions.add(new TS_ThreadCallParallelTimeoutException());
            }
            if (scope.exception() != null) {
                exceptions.add(scope.exception());
            }
            resultsForSuccessfulOnes = scope.resultsForSuccessfulOnes();
            states = scope.states();
        } catch (InterruptedException e) {
            exceptions.add(e);
        }
    }

    public boolean timeout() {
        return exceptions.stream()
                .filter(e -> e instanceof TS_ThreadCallParallelTimeoutException)
                .findAny().isPresent();
    }
    public List<State> states = TGS_ListUtils.of();
    public List<Throwable> exceptions = TGS_ListUtils.of();
    public List<T> resultsForSuccessfulOnes = TGS_ListUtils.of();

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, Callable<T> callable) {
        return of(duration, List.of(callable));
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, Callable<T>... callables) {
        return of(duration, List.of(callables));
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, List<Callable<T>> callables) {
        return new TS_ThreadCallParallelUntilFirstFail(duration, callables);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, Callable<T> fetcher, Callable<Void>... throwingValidators) {
        return of(duration, fetcher, List.of(throwingValidators));
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, Callable<T> fetcher, List<Callable<Void>> throwingValidators) {
        List<Callable<T>> fetchers = TGS_ListUtils.of();
        fetchers.add(fetcher);
        return of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, List<Callable<T>> fetchers, Callable<Void>... throwingValidators) {
        return of(duration, fetchers, List.of(throwingValidators));
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> of(Duration duration, List<Callable<T>> fetchers, List<Callable<Void>> throwingValidators) {
        List<Callable<T>> callables = TGS_ListUtils.of();
        callables.addAll(fetchers);
        throwingValidators.forEach(tv -> callables.add(() -> {
            tv.call();
            return null;
        }));
        return TS_ThreadCallParallelUntilFirstFail.of(duration, callables);
    }
}
