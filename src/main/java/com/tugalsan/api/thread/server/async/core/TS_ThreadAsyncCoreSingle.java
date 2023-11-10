package com.tugalsan.api.thread.server.async.core;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.server.TS_TimeElapsed;
import com.tugalsan.api.time.server.TS_TimeUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import jdk.incubator.concurrent.StructuredTaskScope;

//IMPLEMENTATION OF https://www.youtube.com/watch?v=_fRN7tpLyPk
public class TS_ThreadAsyncCoreSingle<T> {

    private static class InnerScope<T> extends StructuredTaskScope<T> {

        @Override
        protected void handleComplete(Future<T> future) {
            switch (future.state()) {
                case RUNNING ->
                    throw new IllegalStateException("State should not be running!");
                case SUCCESS -> {
                    var result = future.resultNow();
                    if (result != null) {
                        this.resultIfSuccessful.set(result);
                    }
                }
                case FAILED ->
                    this.exceptionIfFailed.set(future.exceptionNow());
                case CANCELLED -> {
                }
            }
        }
        public final AtomicReference<T> resultIfSuccessful = new AtomicReference();
        public final AtomicReference<Throwable> exceptionIfFailed = new AtomicReference();

        @Override
        public InnerScope<T> joinUntil(Instant deadline) throws InterruptedException {
            try {
                super.joinUntil(deadline);
            } catch (TimeoutException e) {
                super.shutdown();
                exceptionIfFailed.set(new TS_ThreadAsyncCoreTimeoutException());
            }
            return this;
        }
    }

    //until: Instant.now().plusMillis(10)
    private TS_ThreadAsyncCoreSingle(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadSyncTrigger> callable) {
        var elapsed = TS_TimeElapsed.of();
        try (var scope = new InnerScope<T>()) {
            scope.fork(() -> callable.call(killTrigger));
            if (duration == null) {
                scope.join();
            } else {
                scope.joinUntil(TS_TimeUtils.toInstant(duration));
            }
            resultIfSuccessful = scope.resultIfSuccessful.get() == null ? Optional.empty() : Optional.of(scope.resultIfSuccessful.get());
            exceptionIfFailed = scope.exceptionIfFailed.get() == null ? Optional.empty() : Optional.of(scope.exceptionIfFailed.get());
        } catch (InterruptedException e) {
            exceptionIfFailed = Optional.of(e);
        } finally {
            this.elapsed = elapsed.elapsed_now();
        }
    }
    final public Duration elapsed;

    public boolean timeout() {
        return exceptionIfFailed.isPresent() && exceptionIfFailed.get() instanceof TS_ThreadAsyncCoreTimeoutException;
    }
    public Optional<T> resultIfSuccessful;
    public Optional<Throwable> exceptionIfFailed;

    public boolean hasError() {
        return exceptionIfFailed.isPresent();
    }

    public static <T> TS_ThreadAsyncCoreSingle<T> of(TS_ThreadSyncTrigger killTrigger, Duration duration, TGS_CallableType1<T, TS_ThreadSyncTrigger> callable) {
        return new TS_ThreadAsyncCoreSingle(killTrigger, duration, callable);
    }
}
