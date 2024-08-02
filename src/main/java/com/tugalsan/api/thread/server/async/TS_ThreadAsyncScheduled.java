package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Deprecated//USE TS_ThreadAsyncBuilder with killTrigger if possible & --ENABLE PREWIEW NEEEEEEEDED!!!!
public class TS_ThreadAsyncScheduled {

    final private static TS_Log d = TS_Log.of(false, TS_ThreadAsyncScheduled.class);

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static TGS_UnionExcuseVoid _scheduleAtFixedRate(TS_ThreadSyncTrigger killTrigger, Runnable exe, long initialDelay, long period, TimeUnit unit) {
        if (period <= 0) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "_scheduleAtFixedRate", "ERROR: period <= 0.period:" + period);
            d.ce("_scheduleAtFixedRate", u.excuse().getMessage());
            return u;
        }
        var future = SCHEDULED.scheduleAtFixedRate(exe, initialDelay, period, unit);
        TS_ThreadAsyncBuilder.of(killTrigger).mainDummyForCycle()
                .fin(() -> future.cancel(false))
                .cycle_mainValidation_mainPeriod(o -> !future.isCancelled() && !future.isDone(), Duration.ofMinutes(1))
                .asyncRun();
        return TGS_UnionExcuseVoid.ofVoid();
    }

    private static TGS_UnionExcuseVoid _scheduleAtFixedRate(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_Func_In1<TS_ThreadSyncTrigger> exe, long initialDelay, long period, TimeUnit unit) {
        Runnable exe2 = () -> {
            TS_ThreadAsyncAwait.runUntil(killTrigger, until, kt -> {
                exe.run(kt);
            });
        };
        return _scheduleAtFixedRate(killTrigger, exe2, initialDelay, period, unit);
    }

    public static TGS_UnionExcuseVoid every(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, Duration initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return everySeconds(killTrigger, until, startNow, initialDelayAndPeriod.toSeconds(), exe);
    }

    public static TGS_UnionExcuseVoid everySeconds(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static TGS_UnionExcuseVoid everyMinutes_whenSecondShow(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenSecondShow, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        var now = TGS_Time.of();
        var now_second = now.getSecond();
        if (whenSecondShow == now_second) {
            d.cr("everySeconds_whenSecondShow", "will not wait");
        } else {
            var wait_seconds = 0;
            if (whenSecondShow > now_second) {
                wait_seconds = whenSecondShow - now_second;
            }
            if (whenSecondShow < now_second) {
                wait_seconds = whenSecondShow + 60 - now_second;
            }
            now.incrementSecond(wait_seconds);
            d.cr("everySeconds_whenSecondShow", "waiting seconds...", wait_seconds, now);
            TS_ThreadWait.seconds("everySeconds_whenSecondShow", killTrigger, wait_seconds);
        }
        if (killTrigger != null && killTrigger.hasTriggered()) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "everySeconds_whenSecondShow", "WARNING: killTrigger triggered before scheduling started.Hence killed early.");
            d.ce("everySeconds_whenSecondShow", u.excuse().getMessage());
            return u;
        }
        d.cr("everySeconds_whenSecondShow", "will schedule now");
        return everyMinutes(killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }

    public static TGS_UnionExcuseVoid everyMinutes(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static TGS_UnionExcuseVoid everyHours(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static TGS_UnionExcuseVoid everyHours_whenMinuteShow(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenMinuteShow, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
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
        if (killTrigger != null && killTrigger.hasTriggered()) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "everyHours_whenMinuteShow", "WARNING: killTrigger triggered before scheduling started.Hence killed early.");
            d.ce("everyHours_whenMinuteShow", u.excuse().getMessage());
            return u;
        }
        d.cr("everyHours_whenMinuteShow", "will schedule now");
        return everyHours(killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }

    public static TGS_UnionExcuseVoid everyDays(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }

    public static TGS_UnionExcuseVoid everyDays_whenHourShow(TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenHourShow, TGS_Func_In1<TS_ThreadSyncTrigger> exe) {
        var now = TGS_Time.of();
        var now_hours = now.getHour();
        if (whenHourShow == now_hours) {
            d.cr("everyDays_whenHourShow", "will not wait");
        } else {
            var wait_hours = 0;
            if (whenHourShow > now_hours) {
                wait_hours = whenHourShow - now_hours;
            }
            if (whenHourShow < now_hours) {
                wait_hours = whenHourShow + 24 - now_hours;
            }
            now.incrementHour(wait_hours);
            d.cr("everyDays_whenHourShow", "waiting hour...", wait_hours, now);
            TS_ThreadWait.hours("everyDays_whenHourShow", killTrigger, wait_hours);
        }
        if (killTrigger != null && killTrigger.hasTriggered()) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "everyDays_whenHourShow", "WARNING: killTrigger triggered before scheduling started.Hence killed early.");
            d.ce("everyDays_whenHourShow", u.excuse().getMessage());
            return u;
        }
        d.cr("everyDays_whenHourShow", "will schedule now");
        return everyHours(killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }
}
