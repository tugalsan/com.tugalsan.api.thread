package com.tugalsan.api.thread.server.sync;

import module com.tugalsan.api.function;
import module com.tugalsan.api.log;
import module com.tugalsan.api.os;
import module com.tugalsan.api.random;
import java.time.*;

public class TS_ThreadSyncWait {

    private TS_ThreadSyncWait() {

    }

    final public static TS_Log d = TS_Log.of(TS_ThreadSyncWait.class);

    public static boolean waitForMemory_returnTrueIfSafe(TS_ThreadSyncTrigger killTrigger, int memUsedThreashold, int secGap, int times, boolean showLog) {
        var maxGap = secGap * times;
        var secCnt = 0;
        while (maxGap > secCnt) {
            var memUsedPercent = TS_OsRamUtils.getPercentageUsed();
            if (memUsedPercent < memUsedThreashold) {
                if (showLog) {
                    d.cr("waitForMemory_returnTrueIfSafe", "memUsedPercent", String.format("%.1f", Math.round(10 * memUsedPercent) / 10d));
                }
                break;
            } else {
                if (showLog) {
                    d.ce("waitForMemory_returnTrueIfSafe", "memUsedPercent", String.format("%.1f", Math.round(10 * memUsedPercent) / 10d));
                }
            }
            TS_ThreadSyncWait.seconds("waitForMemory_returnTrueIfSafe", killTrigger, secGap);
            secCnt += secGap;
        }
        var passed = maxGap > secCnt;
        if (showLog) {
            if (passed) {
                d.cr("waitForMemory_returnTrueIfSafe", "pass_mark", "passed");
            } else {
                d.ce("waitForMemory_returnTrueIfSafe", "pass_mark", "failed");
            }
        }
        return passed;
    }

    public static void secondsBtw(String name, TS_ThreadSyncTrigger killTrigger, double minSeconds, double maxSecons) {
        seconds(name, killTrigger, TS_RandomUtils.nextDouble(minSeconds, maxSecons));
    }

    public static void days(String name, TS_ThreadSyncTrigger killTrigger, double days) {
        hours(name, killTrigger, days * 24);
    }

    public static void hours(String name, TS_ThreadSyncTrigger killTrigger, double hours) {
        minutes(name, killTrigger, hours * 60);
    }

    public static void minutes(String name, TS_ThreadSyncTrigger killTrigger, double minutes) {
        seconds(name, killTrigger, minutes * 60);
    }

    public static void seconds(String name, TS_ThreadSyncTrigger killTrigger, double seconds) {
        var gap = 3;
        if (seconds <= gap) {
            _seconds(seconds);
            return;
        }
        var total = 0;
        while (total < seconds) {
            if (killTrigger != null && killTrigger.hasTriggered()) {
                return;
            }
            d.ci("seconds", name, "...");
            _seconds(gap);
            total += gap;
        }
    }

    private static void _seconds(double seconds) {
        _milliseconds((long) (seconds * 1000f));
    }

    private static void _milliseconds(long milliSeconds) {
        Thread.yield();
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException ex) {
            if (ex instanceof InterruptedException) {// U NEED THIS SO STRUCTURED SCOPE CAN ABLE TO SHUT DOWN
                Thread.currentThread().interrupt();
                TGS_FuncMTUUtils.thrw(ex);
            }
        }
    }

    public static void milliseconds20() {
        _milliseconds(20);
    }

    public static void milliseconds100() {
        _milliseconds(100);
    }

    public static void milliseconds200() {
        _milliseconds(200);
    }

    public static void milliseconds500() {
        _milliseconds(500);
    }

    public static void of(String name, TS_ThreadSyncTrigger killTrigger, Duration duration) {
        var millis = duration.toMillis();
        if (millis < 1000L) {
            _milliseconds(millis);
            return;
        }
        seconds(name, killTrigger, duration.toSeconds());
    }
}
