package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;
import com.tugalsan.api.thread.server.core.*;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

public class TS_ThreadRun {

    public static <T> TS_ThreadRunParallel<T> parallel(Duration duration, Callable<T>... callables) {
        return TS_ThreadRunParallel.of(duration, callables);
    }

    public static <T> TS_ThreadRunParallel<T> parallel(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadRunParallel.of(duration, callables);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T> callable) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, callable);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T>... callables) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, callables);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, callables);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T> fetcher, Callable<Void>... throwingValidators) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, fetcher, throwingValidators);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T> fetcher, List<Callable<Void>> throwingValidators) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, fetcher, throwingValidators);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, List<Callable<T>> fetchers, Callable<Void>... throwingValidators) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadRunParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, List<Callable<T>> fetchers, List<Callable<Void>> throwingValidators) {
        return TS_ThreadRunParallelUntilFirstFail.of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadRunParallelUntilFirstSuccess<T> of(Duration duration, Callable<T>... callables) {
        return TS_ThreadRunParallelUntilFirstSuccess.of(duration, callables);
    }

    public static <T> TS_ThreadRunParallelUntilFirstSuccess<T> of(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadRunParallelUntilFirstSuccess.of(duration, callables);
    }

    public static Thread now(TGS_Executable exe) {
        return Thread.startVirtualThread(() -> exe.execute());
    }

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static void scheduleAtFixedRate(Runnable r, long initialDelay, long period, TimeUnit unit) {
        SCHEDULED.scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    private static void scheduleAtFixedRate(TGS_Executable exe, long initialDelay, long period, TimeUnit unit) {
        Runnable r = () -> exe.execute();
        scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    public static void everySeconds(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static void everyMinutes(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static void everyHours(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static void everyDays(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }
}
