package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;
import java.util.concurrent.*;

public class TS_ThreadRunUtils {

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
