package com.tugalsan.api.thread.server.async.core;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeLst;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> {

    private static class InnerScope<T> implements AutoCloseable {

        private final StructuredTaskScope.ShutdownOnSuccess<T> innerScope = new StructuredTaskScope.ShutdownOnSuccess();
        public volatile boolean timeout = false;
        public final TS_ThreadSafeLst<Future<T>> futures = new TS_ThreadSafeLst();

        public InnerScope<T> join() throws InterruptedException {
            innerScope.join();
            return this;
        }

        public InnerScope<T> joinUntil(Instant until) throws InterruptedException {
            try {
                innerScope.joinUntil(until);
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

        public T resultIfAnySuccessful() throws ExecutionException {
            return timeout ? null : innerScope.result();
        }
    }

    private TS_ThreadAsyncCoreParallelUntilFirstSuccess(TS_ThreadSafeTrigger killTrigger, Duration duration, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables) {
        try (var scope = new InnerScope<T>()) {
            List<Callable<T>> callablesWrapped = new ArrayList();
            callables.forEach(c -> callablesWrapped.add(() -> c.call(killTrigger)));
            callablesWrapped.forEach(c -> scope.fork(c));
            callablesWrapped.forEach(c -> scope.fork(c));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            if (scope.timeout) {
                exceptions.add(new TS_ThreadAsyncCoreTimeoutException());
            }
            resultIfAnySuccessful = scope.resultIfAnySuccessful();
            states = TGS_StreamUtils.toLst(scope.futures.stream().map(f -> f.state()));
        } catch (InterruptedException | ExecutionException e) {
            exceptions.add(e);
        }
    }

    public boolean timeout() {
        return exceptions.stream()
                .filter(e -> e instanceof TS_ThreadAsyncCoreTimeoutException)
                .findAny().isPresent();
    }
    public List<State> states;
    public List<Exception> exceptions = TGS_ListUtils.of();
    public T resultIfAnySuccessful;

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> of(TS_ThreadSafeTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadSafeTrigger>... callables) {
        return of(killTrigger, duration, List.of(callables));
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> of(TS_ThreadSafeTrigger killTrigger, Duration duration, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables) {
        return new TS_ThreadAsyncCoreParallelUntilFirstSuccess(killTrigger, duration, callables);
    }
}
