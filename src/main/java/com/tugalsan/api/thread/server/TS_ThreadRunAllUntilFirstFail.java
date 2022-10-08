package com.tugalsan.api.thread.server;

import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

public class TS_ThreadRunAllUntilFirstFail<T> {

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

        public List<T> results() {
            return TGS_StreamUtils.toLst(futures.stream()
                    .filter(f -> f.state() == State.SUCCESS)
                    .map(f -> f.resultNow())
            );
        }

        public List<State> states() {
            return TGS_StreamUtils.toLst(futures.stream()
                    .map(f -> f.state())
            );
        }
    }

    private TS_ThreadRunAllUntilFirstFail(Instant until, List<Callable<T>> callables) {
        try ( var scope = new InnerScope<T>()) {
            callables.forEach(c -> scope.fork(c));
            if (until == null) {
                scope.join();
            } else {
                scope.joinUntil(until);
            }
            timeout = scope.timeout;
            if (scope.exception() != null) {
                exceptions.add(scope.exception());
            }
            results = scope.results();
            states = scope.states();
        } catch (InterruptedException e) {
            exceptions.add(e);
        }
    }

    public boolean timeout;
    public List<State> states = TGS_ListUtils.of();
    public List<Throwable> exceptions = TGS_ListUtils.of();
    public List<T> results = TGS_ListUtils.of();

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public RuntimeException exceptionPack() {
        var re = new RuntimeException();
        exceptions.forEach(e -> re.addSuppressed(e));
        return re;
    }

    public static <T> TS_ThreadRunAllUntilFirstFail<T> of(Instant until, Callable<T>... callables) {
        return of(until, List.of(callables));
    }

    public static <T> TS_ThreadRunAllUntilFirstFail<T> of(Instant until, List<Callable<T>> callables) {
        return new TS_ThreadRunAllUntilFirstFail(until, callables);
    }
}
