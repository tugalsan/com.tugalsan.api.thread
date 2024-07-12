package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.TS_ThreadWait;

import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.client.TGS_Time;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//USE TS_ThreadAsyncBuilder with killTrigger if possible
public class TS_ThreadAsyncScheduled {

    final private static TS_Log d = TS_Log.of(false, TS_ThreadAsyncScheduled.class);

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static boolean _scheduleAtFixedRate(TS_ThreadSyncTrigger killTrigger, Runnable exe, long initialDelay, long period, TimeUnit unit) {
        if (period <= 0) {
            d.ce("_scheduleAtFixedRate", "ERROR: period <= 0", "period", period);
            return false;
        }
        var future = SCHEDULED.scheduleAtFixedRate(exe, initialDelay, period, unit);
        TS_ThreadAsyncBuilder.of(killTrigger).mainDummyForCycle()
                .fin(() -> future.cancel(false))
                .cycle_mainValidation_mainPeriod(o -> !future.isCancelled() && !future.isDone(), Duration.ofMinutes(1))
                .asyncRun();
        return true;
    }

    private static boolean _scheduleAtFixedRate(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_Func_In1<TS_ThreadSyncTrigger> exe, long initialDelay, long period, TimeUnit unit) {
        Runnable exe2 = () -> {
            TS_ThreadAsyncAwait.runUntil(killTrigger, until, kt -> {
                exe.run(kt);
            });
        };
        return _scheduleAtFixedRate(killTrigger, exe2, initialDelay, period, unit);
    }

    public static boolean every(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, Duration initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return everySeconds(killTrigger, until, startNow, initialDelayAndPeriod.toSeconds(), exe);
    }

    public static boolean everySeconds(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static boolean everyMinutes(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static boolean everyHours(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static boolean everyHours_whenMinuteShow(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenMinuteShow, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        var now = TGS_Time.of();
        var now_minutes = now.getMinute();
        if (whenMinuteShow == now_minutes) {
            d.cr("everyHours_whenMinuteShow", "will not wait");
        } else {
            var wait_minutes = 0;
            if (whenMinuteShow > now_minutes) {
                wait_minutes = whenMinuteShow - now_minutes;
            }
            if (whenMinuteShow < now_minutes) {
                wait_minutes = whenMinuteShow + 60 - now_minutes;
            }
            now.incrementMinute(wait_minutes);
            d.cr("everyHours_whenMinuteShow", "waiting minutes...", wait_minutes, now);
            TS_ThreadWait.minutes("everyHours_whenMinuteShow", killTrigger, wait_minutes);
        }
        if (killTrigger != null && killTrigger.hasTriggered()){
            return true;
        }
        d.cr("everyHours_whenMinuteShow", "will schedule now");
        return everyHours(killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }

    public static boolean everyDays(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }
}
