package com.tugalsan.api.thread.server;

import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import jdk.incubator.concurrent.StructuredTaskScope;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadRunAll<T> {

    private static class CompileAllScope<T> extends StructuredTaskScope<T> {

        @Override
        protected void handleComplete(Future<T> future) {
            switch (future.state()) {
                case RUNNING ->
                    throw new IllegalStateException("State should not be running!");
                case SUCCESS -> {
                    var result = future.resultNow();
                    if (result != null) {
                        this.resultsNotNull.add(result);
                    }
                }
                case FAILED ->
                    this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {
                }
            }
        }
        public final TS_ThreadSafeLst<T> resultsNotNull = new TS_ThreadSafeLst();
        public final TS_ThreadSafeLst<Throwable> exceptions = new TS_ThreadSafeLst();

        @Override
        public CompileAllScope<T> joinUntil(Instant deadline) throws InterruptedException {
            try {
                super.joinUntil(deadline);
            } catch (TimeoutException e) {
                super.shutdown();
                exceptions.add(new TS_ThreadRunAllTimeoutException());
            }
            return this;
        }
    }

    //until: Instant.now().plusMillis(10)
    private TS_ThreadRunAll(Duration duration, List<Callable<T>> callables) {
        try ( var scope = new CompileAllScope<T>()) {
            resultsNotNull = scope.resultsNotNull.toList();
            exceptions = scope.exceptions.toList();
            callables.forEach(c -> scope.fork(c));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
        } catch (InterruptedException e) {
            if (resultsNotNull == null) {
                resultsNotNull = TGS_ListUtils.of();
            }
            if (exceptions == null) {
                exceptions = TGS_ListUtils.of();
            }
            exceptions.add(e);
        }
    }

    public boolean timeout() {
        return exceptions.stream()
                .filter(e -> e instanceof TS_ThreadRunAllTimeoutException)
                .findAny().isPresent();
    }
    public List<T> resultsNotNull;
    public List<Throwable> exceptions;

    public boolean hasError() {
        return !exceptions.isEmpty();
    }

    public T findAny() {
        return resultsNotNull.stream().findAny().orElse(null);
    }

    public static <T> TS_ThreadRunAll<T> of(Duration duration, Callable<T>... callables) {
        return of(duration, List.of(callables));
    }

    public static <T> TS_ThreadRunAll<T> of(Duration duration, List<Callable<T>> callables) {
        return new TS_ThreadRunAll(duration, callables);
    }
}
