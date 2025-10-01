package com.tugalsan.api.thread.server.async.scheduled;

import module com.tugalsan.api.function;
import module com.tugalsan.api.log;
import module com.tugalsan.api.thread;
import module com.tugalsan.api.time;
import module com.tugalsan.api.union;
import java.time.*;
import java.util.concurrent.*;

@Deprecated//USE TS_ThreadAsyncBuilder with killTrigger if possible & --ENABLE PREWIEW NEEEEEEEDED!!!!
public class TS_ThreadAsyncScheduled {

    private TS_ThreadAsyncScheduled() {

    }

    final private static TS_Log d = TS_Log.of(false, TS_ThreadAsyncScheduled.class);

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static TGS_UnionExcuseVoid _scheduleAtFixedRate(String name, TS_ThreadSyncTrigger killTrigger, Runnable exe, long initialDelay, long period, TimeUnit unit) {
        if (period <= 0) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "_scheduleAtFixedRate", "ERROR: period <= 0.period:" + period);
            d.ce("_scheduleAtFixedRate", u.excuse().getMessage());
            return u;
        }
        var future = SCHEDULED.scheduleAtFixedRate(exe, initialDelay, period, unit);
        TS_ThreadAsyncBuilder.of(killTrigger.newChild(d.className()).newChild("_scheduleAtFixedRate")).name(name).mainDummyForCycle()
                .fin(() -> future.cancel(false))
                .cycle_mainValidation_mainPeriod((kt, o) -> !future.isCancelled() && !future.isDone(), Duration.ofMinutes(1))
                .asyncRun();
        return TGS_UnionExcuseVoid.ofVoid();
    }

    private static TGS_UnionExcuseVoid _scheduleAtFixedRate(String name, TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe, long initialDelay, long period, TimeUnit unit) {
        Runnable exe2 = () -> {
            TS_ThreadAsyncAwait.runUntil(killTrigger.newChild(d.className()).newChild("exe2"), until, kt -> {
                exe.run(kt);
            });
        };
        return _scheduleAtFixedRate(name, killTrigger, exe2, initialDelay, period, unit);
    }

    public static TGS_UnionExcuseVoid every(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, Duration initialDelayAndPeriod, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return everySeconds(name, killTrigger, until, startNow, initialDelayAndPeriod.toSeconds(), exe);
    }

    public static TGS_UnionExcuseVoid everySeconds(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(name, killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static Thread everyMinutes_whenSecondShow(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenSecondShow, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return TS_ThreadAsyncRun.now(killTrigger, __ -> {
            var u = everyMinutes_whenSecondShow_willBlockAtFirst(name, killTrigger.newChild(d.className()), until, startNow, initialDelayAndPeriod, whenSecondShow, exe);
            if (u.isExcuse()) {
                d.ct("everyMinutes_whenSecondShow", u.excuse());
            }
        });
    }

    private static TGS_UnionExcuseVoid everyMinutes_whenSecondShow_willBlockAtFirst(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenSecondShow, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        var now = TGS_Time.of();
        var now_second = now.getSecond();
        if (whenSecondShow == now_second) {
            d.cr("everyMinutes_whenSecondShow_willBlockAtFirst", "will not wait");
        } else {
            var wait_seconds = 0;
            if (whenSecondShow > now_second) {
                wait_seconds = whenSecondShow - now_second;
            }
            if (whenSecondShow < now_second) {
                wait_seconds = whenSecondShow + 60 - now_second;
            }
            now.incrementSecond(wait_seconds);
            d.cr("everyMinutes_whenSecondShow_willBlockAtFirst", "waiting seconds...", wait_seconds, now);
            TS_ThreadSyncWait.seconds("everySeconds_whenSecondShow", killTrigger, wait_seconds);
        }
        if (killTrigger != null && killTrigger.hasTriggered()) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "everySeconds_whenSecondShow", "WARNING: killTrigger triggered before scheduling started.Hence killed early.");
            d.ce("everyMinutes_whenSecondShow_willBlockAtFirst", u.excuse().getMessage());
            return u;
        }
        d.cr("everyMinutes_whenSecondShow_willBlockAtFirst", "will schedule now");
        return everyMinutes(name, killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }

    public static TGS_UnionExcuseVoid everyMinutes(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(name, killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static TGS_UnionExcuseVoid everyHours(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(name, killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static Thread everyHours_whenMinuteShow(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenMinuteShow, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return TS_ThreadAsyncRun.now(killTrigger.newChild(d.className()), __ -> {
            var u = everyHours_whenMinuteShow_willBlockAtFirst(name, killTrigger, until, startNow, initialDelayAndPeriod, whenMinuteShow, exe);
            if (u.isExcuse()) {
                d.ct("everyHours_whenMinuteShow", u.excuse());
            }
        });
    }

    private static TGS_UnionExcuseVoid everyHours_whenMinuteShow_willBlockAtFirst(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenMinuteShow, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        var now = TGS_Time.of();
        var now_minutes = now.getMinute();
        if (whenMinuteShow == now_minutes) {
            d.cr("everyHours_whenMinuteShow_willBlockAtFirst", "will not wait");
        } else {
            var wait_minutes = 0;
            if (whenMinuteShow > now_minutes) {
                wait_minutes = whenMinuteShow - now_minutes;
            }
            if (whenMinuteShow < now_minutes) {
                wait_minutes = whenMinuteShow + 60 - now_minutes;
            }
            now.incrementMinute(wait_minutes);
            d.cr("everyHours_whenMinuteShow_willBlockAtFirst", "waiting minutes...", wait_minutes, now);
            TS_ThreadSyncWait.minutes("everyHours_whenMinuteShow_willBlockAtFirst", killTrigger, wait_minutes);
        }
        if (killTrigger != null && killTrigger.hasTriggered()) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "everyHours_whenMinuteShow_willBlockAtFirst", "WARNING: killTrigger triggered before scheduling started.Hence killed early.");
            d.ce("everyHours_whenMinuteShow_willBlockAtFirst", u.excuse().getMessage());
            return u;
        }
        d.cr("everyHours_whenMinuteShow_willBlockAtFirst", "will schedule now");
        return everyHours(name, killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }

    public static TGS_UnionExcuseVoid everyDays(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return _scheduleAtFixedRate(name, killTrigger, until, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }

    public static Thread everyDays_whenHourShow(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenHourShow, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return TS_ThreadAsyncRun.now(killTrigger.newChild(d.className()), __ -> {
            var u = everyDays_whenHourShow_willBlockAtFirst(name, killTrigger, until, startNow, initialDelayAndPeriod, whenHourShow, exe);
            if (u.isExcuse()) {
                d.ct("everyDays_whenHourShow", u.excuse());
            }
        });
    }

    private static TGS_UnionExcuseVoid everyDays_whenHourShow_willBlockAtFirst(String name, TS_ThreadSyncTrigger killTrigger, Duration until, boolean startNow, long initialDelayAndPeriod, int whenHourShow, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        var now = TGS_Time.of();
        var now_hours = now.getHour();
        if (whenHourShow == now_hours) {
            d.cr("everyDays_whenHourShow_willBlockAtFirst", "will not wait");
        } else {
            var wait_hours = 0;
            if (whenHourShow > now_hours) {
                wait_hours = whenHourShow - now_hours;
            }
            if (whenHourShow < now_hours) {
                wait_hours = whenHourShow + 24 - now_hours;
            }
            now.incrementHour(wait_hours);
            d.cr("everyDays_whenHourShow_willBlockAtFirst", "waiting hour...", wait_hours, now);
            TS_ThreadSyncWait.hours("everyDays_whenHourShow_willBlockAtFirst", killTrigger, wait_hours);
        }
        if (killTrigger != null && killTrigger.hasTriggered()) {
            var u = TGS_UnionExcuseVoid.ofExcuse(TS_ThreadAsyncScheduled.class.getSimpleName(), "everyDays_whenHourShow_willBlockAtFirst", "WARNING: killTrigger triggered before scheduling started.Hence killed early.");
            d.ce("everyDays_whenHourShow_willBlockAtFirst", u.excuse().getMessage());
            return u;
        }
        d.cr("everyDays_whenHourShow_willBlockAtFirst", "will schedule now");
        return everyHours(name, killTrigger, until, startNow, initialDelayAndPeriod, exe);
    }
}
