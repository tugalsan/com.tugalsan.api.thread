package com.tugalsan.api.thread.server;

import com.tugalsan.api.log.server.*;

public class TS_ThreadWaitUtils {

    final private static TS_Log d = TS_Log.of(TS_ThreadWaitUtils.class.getSimpleName());

//    public static void seconds(float minSeconds, float maxSecons) {
//        seconds(TGS_RandomUtils.nextFloat(minSeconds, maxSecons));
//    }
//
//    public static void days(float days) {
//        hours(days * 24);
//    }
//
//    public static void hours(float hours) {
//        minutes(hours * 60);
//    }
//
//    public static void minutes(float minutes) {
//        seconds(minutes * 60);
//    }
    //    private static void milliseconds(long minMilliseconds, long maxMilliseconds) {
//        milliseconds(TGS_RandomUtils.nextLong(minMilliseconds, maxMilliseconds));
//    }
    public static void seconds(TS_ThreadKillableInterface killable, float seconds, CharSequence label) {
        var gap = 3;
        if (seconds <= gap) {
            seconds(seconds);
            return;
        }
        var total = 0;
        while (total < seconds) {
            if (killable != null && killable.isKillMe()) {
                return;
            }
            d.ci("seconds", label, "...");
            seconds(gap);
            total += gap;
        }
    }

    private static void seconds(float seconds) {
        milliseconds((long) (seconds * 1000f));
    }

    private static void milliseconds(long milliSeconds) {
        Thread.yield();
        try {
            Thread.sleep(milliSeconds);
        } catch (Exception e) {
            //DO NOTHING
        }
        Thread.yield();
    }
}
