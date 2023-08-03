package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import com.tugalsan.api.thread.server.struct.TS_ThreadStructBuilder;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//USE TS_ThreadStructBuilder with killTrigger if possible
public class TS_ThreadAsyncScheduled {

    final private static ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());

    public static void destroy() {
        SCHEDULED.shutdown();
    }

    private static void _scheduleAtFixedRate(TS_ThreadSafeTrigger killTrigger, Runnable exe, long initialDelay, long period, TimeUnit unit) {
        var future = SCHEDULED.scheduleAtFixedRate(exe, initialDelay, period, unit);
        TS_ThreadStructBuilder.of()
                .main(kt -> {
                    if (killTrigger.hasTriggered()) {
                        future.cancel(false);
                    }
                }).cycle_mainValidation_mainDuration(o -> !future.isCancelled() && !future.isDone(), Duration.ofMinutes(1))
                .asyncRun();
    }

    private static void _scheduleAtFixedRate(TS_ThreadSafeTrigger killTrigger, TGS_RunnableType1<TS_ThreadSafeTrigger> exe, long initialDelay, long period, TimeUnit unit) {
        Runnable exe2 = () -> exe.run(killTrigger);
        _scheduleAtFixedRate(killTrigger, exe2, initialDelay, period, unit);
    }

    public static void everySeconds(TS_ThreadSafeTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static void everyMinutes(TS_ThreadSafeTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static void everyHours(TS_ThreadSafeTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static void everyDays(TS_ThreadSafeTrigger killTrigger, boolean startNow, long initialDelayAndPeriod, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        _scheduleAtFixedRate(killTrigger, exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }
}
