package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.thread.server.TS_ThreadKillTrigger;
import java.time.Duration;
import java.util.concurrent.*;

@Deprecated //USE TS_ThreadStructBuilder with killTrigger if possible
public class TS_ThreadAsync {

    public static Thread now(TS_ThreadKillTrigger killTrigger, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        return until(killTrigger, null, exe);
    }

    public static Thread until(TS_ThreadKillTrigger killTrigger, Duration until, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        if (until == null) {
            return Thread.startVirtualThread(() -> exe.run(killTrigger));
        } else {
            return now(killTrigger, kt2 -> TS_ThreadAsyncAwait.runUntil(kt2, until, kt1 -> exe.run(kt1)));
        }
    }

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static void _scheduleAtFixedRate(Runnable exe, long initialDelay, long period, TimeUnit unit) {
        SCHEDULED.scheduleAtFixedRate(exe, initialDelay, period, unit);
    }

    private static void _scheduleAtFixedRate(TS_ThreadKillTrigger killTrigger, TGS_RunnableType1<TS_ThreadKillTrigger> exe, long initialDelay, long period, TimeUnit unit) {
        Runnable r = () -> exe.run(killTrigger);
        _scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    public static void everySeconds(TS_ThreadKillTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static void everyMinutes(TS_ThreadKillTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static void everyHours(TS_ThreadKillTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static void everyDays(TS_ThreadKillTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }
}
