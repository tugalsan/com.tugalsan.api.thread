package com.tugalsan.api.thread.server;

import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadRunAllUntilFirstSuccess<T> {

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnSuccess<T> innerScope = new StructuredTaskScope.ShutdownOnSuccess();
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

        public T result() throws ExecutionException {
            return timeout ? null : innerScope.result();
        }
    }

    private TS_ThreadRunAllUntilFirstSuccess(Instant until, List<Callable<T>> callables) {
        try ( var scope = new InnerScope<T>()) {
            callables.forEach(c -> scope.fork(c));
            if (until == null) {
                scope.join();
            } else {
                scope.joinUntil(until);
            }
            timeout = scope.timeout;
            result = scope.result();
            states = TGS_StreamUtils.toLst(scope.futures.stream().map(f -> f.state()));
        } catch (InterruptedException | ExecutionException e) {
            exception = e;
        }
    }

    public boolean timeout;
    public List<State> states;
    public Exception exception;
    public T result;

    public boolean hasError() {
        return exception != null;
    }

    public static <T> TS_ThreadRunAllUntilFirstSuccess<T> of(Instant until, Callable<T>... callables) {
        return of(until, List.of(callables));
    }

    public static <T> TS_ThreadRunAllUntilFirstSuccess<T> of(Instant until, List<Callable<T>> callables) {
        return new TS_ThreadRunAllUntilFirstSuccess(until, callables);
    }
}