package com.tugalsan.api.thread.server.async.core;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.thread.server.TS_ThreadKillTrigger;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeLst;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadAsyncCoreParallel<T> {

    private static class InnerScope<T> extends StructuredTaskScope<T> {

        @Override
        protected void handleComplete(Future<T> future) {
            switch (future.state()) {
                case RUNNING ->
                    throw new IllegalStateException("State should not be running!");
                case SUCCESS -> {
                    var result = future.resultNow();
                    if (result != null) {
                        this.resultsForSuccessfulOnes.add(result);
                    }
                }
                case FAILED ->
                    this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {
                }
            }
        }
        public final TS_ThreadSafeLst<T> resultsForSuccessfulOnes = new TS_ThreadSafeLst();
        public final TS_ThreadSafeLst<Throwable> exceptions = new TS_ThreadSafeLst();

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
    private TS_ThreadAsyncCoreParallel(TS_ThreadKillTrigger killTrigger, Duration duration, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> callables) {
        try (var scope = new InnerScope<T>()) {
            List<Callable<T>> callablesWrapped = new ArrayList();
            callables.forEach(c -> callablesWrapped.add(() -> c.call(killTrigger)));
            callablesWrapped.forEach(c -> scope.fork(c));
            resultsForSuccessfulOnes = scope.resultsForSuccessfulOnes.toList();
            exceptions = scope.exceptions.toList();
            callablesWrapped.forEach(c -> scope.fork(c));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
        } catch (InterruptedException e) {
            if (resultsForSuccessfulOnes == null) {
                resultsForSuccessfulOnes = TGS_ListUtils.of();
            }
            if (exceptions == null) {
                exceptions = TGS_ListUtils.of();
            }
            exceptions.add(e);
        }
    }

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

    public static <T> TS_ThreadAsyncCoreParallel<T> of(TS_ThreadKillTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadKillTrigger>... callables) {
        return of(killTrigger, duration, List.of(callables));
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> of(TS_ThreadKillTrigger killTrigger, Duration duration, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> callables) {
        return new TS_ThreadAsyncCoreParallel(killTrigger, duration, callables);
    }
}
