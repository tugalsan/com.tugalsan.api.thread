package com.tugalsan.api.thread.server;

import com.tugalsan.api.stream.client.TGS_StreamUtils;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

public class TS_ThreadFetchFirst<T> {

    private static class FetchFirstScope<T> implements AutoCloseable {

        public final TS_ThreadSafeLst<Future<T>> futures = new TS_ThreadSafeLst();
        private final StructuredTaskScope.ShutdownOnSuccess<T> innerScope = new StructuredTaskScope.ShutdownOnSuccess();
        public volatile boolean timeout = false;

        public FetchFirstScope<T> join() throws InterruptedException {
            innerScope.join();
            return this;
        }

        public FetchFirstScope<T> joinUntil(Instant deadline) throws InterruptedException {
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
            return innerScope.result();
        }
    }

    //until: Instant.now().plusMillis(10)
    private TS_ThreadFetchFirst(Instant until, Callable<T>... callables) {
        try ( var scope = new FetchFirstScope<T>()) {
            futures = scope.futures;
            List.of(callables).forEach(c -> scope.fork(c));
            if (until == null) {
                scope.join();
            } else {
                scope.joinUntil(until);
            }
            timeout = scope.timeout;
            if (!timeout) {
                result = scope.result();
            }
        } catch (Exception e) {
            if (futures == null) {
                futures = new TS_ThreadSafeLst();
            }
            exception = e;
        }
    }

    public boolean timeout() {
        return timeout;
    }
    private boolean timeout;

    public List<State> states() {
        return TGS_StreamUtils.toLst(futures.stream().map(f -> f.state()));
    }
    private TS_ThreadSafeLst<Future<T>> futures;

    public Exception exception() {
        return exception;
    }
    private Exception exception;

    public T result() {
        return result;
    }
    private T result;

    public static <T> TS_ThreadFetchFirst<T> of(Instant until, Callable<T>... callables) {
        return new TS_ThreadFetchFirst(until, callables);
    }
}
