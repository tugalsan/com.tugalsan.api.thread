package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.validator.client.TGS_Validator;
import java.util.concurrent.*;

public class TS_ThreadAsync {

//    public static <T> Thread nowWhile(TGS_Validator prePeriodicValidate, TGS_RunnableType1<T> periodicRun) {
//        return nowWhile(null, prePeriodicValidate, periodicRun, null);
//    }
//
//    public static <T> Thread nowWhile(TGS_Callable<T> init, TGS_Validator prePeriodicValidate, TGS_RunnableType1<T> periodicRun) {
//        return nowWhile(init, periodicRun, periodicRun, null);
//    }
//
//    public static <T> Thread nowWhile(TGS_Validator prePeriodicValidate, TGS_RunnableType1<T> periodicRun, TGS_RunnableType1<T> finalize) {
//        return nowWhile(null, periodicRun, periodicRun, finalize);
//    }

//    public static <T> Thread nowWhile(TGS_Callable<T> init, TGS_Validator prePeriodicValidate, TGS_RunnableType1<T> periodicRun, TGS_RunnableType1<T> finalize) {
//        //TODO
//        now(() -> {
//            var o = init.call();
//            while (prePeriodicValidate.validate()) {
//                var begin = System.currentTimeMillis();
//                images.add(TS_InputScreenUtils.shotPictures(r, rect));
//                var end = System.currentTimeMillis();
//                TGS_UnSafe.run(() -> Thread.sleep(gif.timeBetweenFramesMS - (end - begin)));
//                Thread.yield();
//            }
//            finalize.run(o);
//            captureAlive.set(false);
//        });
//    }

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
