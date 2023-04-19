package com.tugalsan.api.thread.server;

import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.thread.server.core.TS_ThreadCallParallelUntilFirstFail;
import java.time.Duration;
import java.util.concurrent.*;

public class TS_ThreadRun {

    public static TS_ThreadCallParallelUntilFirstFail<Void> until(Duration duration, TGS_Runnable exe) {
        return TS_ThreadCall.single(duration, () -> {
            exe.run();
            return null;
        });
    }

    public static Thread now(TGS_Runnable exe) {
        return Thread.startVirtualThread(() -> exe.run());
    }

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static void scheduleAtFixedRate(Runnable r, long initialDelay, long period, TimeUnit unit) {
        SCHEDULED.scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    private static void scheduleAtFixedRate(TGS_Runnable exe, long initialDelay, long period, TimeUnit unit) {
        Runnable r = () -> exe.run();
        scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    public static void everySeconds(boolean startNow, long initialDelayAndPeriod, TGS_Runnable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static void everyMinutes(boolean startNow, long initialDelayAndPeriod, TGS_Runnable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static void everyHours(boolean startNow, long initialDelayAndPeriod, TGS_Runnable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static void everyDays(boolean startNow, long initialDelayAndPeriod, TGS_Runnable exe) {
        scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }
}
