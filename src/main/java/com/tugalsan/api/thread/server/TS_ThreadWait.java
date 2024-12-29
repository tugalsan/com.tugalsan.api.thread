package com.tugalsan.api.thread.server;

import com.tugalsan.api.log.server.*;
import com.tugalsan.api.os.server.TS_OsRamUtils;
import com.tugalsan.api.random.server.TS_RandomUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import java.time.Duration;

public class TS_ThreadWait {

    final public static TS_Log d = TS_Log.of(TS_ThreadWait.class);

    public static boolean waitForMemory_returnTrueIfSafe(TS_ThreadSyncTrigger killTrigger, int memUsedThreashold, int secGap, int times, boolean showLog) {
        var maxGap = secGap * times;
        var secCnt = 0;
        while (maxGap > secCnt) {
            var memUsedPer = TS_OsRamUtils.getPercentageUsed();
            if (memUsedPer < memUsedThreashold) {
                if (showLog) {
                    d.cr("memory", "getPercentageUsed", String.format("%.1f", Math.round(10 * memUsedPer) / 10d));
                }
                break;
            } else {
                if (showLog) {
                    d.ce("memory", "getPercentageUsed", String.format("%.1f", Math.round(10 * memUsedPer) / 10d));
                }
            }
            TS_ThreadWait.seconds("create", killTrigger, secGap);
            secCnt += secGap;
        }
        if (showLog) {
            d.cr("memory", "getPercentageUsed", "passed");
        }
        return maxGap > secCnt;
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
                throw new RuntimeException(ex);
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
