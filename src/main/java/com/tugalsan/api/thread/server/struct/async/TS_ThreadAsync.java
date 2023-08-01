package com.tugalsan.api.thread.server.struct.async;

import com.tugalsan.api.runnable.client.*;
import java.util.concurrent.*;

@Deprecated //USE TS_ThreadStructBuilder with killTrigger if possible
public class TS_ThreadAsync {

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
