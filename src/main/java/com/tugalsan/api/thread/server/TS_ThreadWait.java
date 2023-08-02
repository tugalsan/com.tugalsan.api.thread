package com.tugalsan.api.thread.server;

import com.tugalsan.api.log.server.*;
import com.tugalsan.api.random.server.TS_RandomUtils;
import com.tugalsan.api.unsafe.client.*;
import java.time.Duration;
import com.tugalsan.api.thread.server.safe.*;
import com.tugalsan.api.thread.server.struct.builder_core.TS_ThreadStruct;

public class TS_ThreadWait {

    final private static TS_Log d = TS_Log.of(TS_ThreadWait.class);

    public static void secondsBtw(TS_ThreadStruct killable, float minSeconds, float maxSecons) {
        seconds(killable, TS_RandomUtils.nextFloat(minSeconds, maxSecons));
    }

    public static void secondsBtw(TS_ThreadSafeRunnable killable, float minSeconds, float maxSecons) {
        seconds(killable, TS_RandomUtils.nextFloat(minSeconds, maxSecons));
    }

    public static void days(TS_ThreadStruct killable, float days) {
        hours(killable, days * 24);
    }

    public static void days(TS_ThreadSafeRunnable killable, float days) {
        hours(killable, days * 24);
    }

    public static void hours(TS_ThreadStruct killable, float hours) {
        minutes(killable, hours * 60);
    }

    public static void hours(TS_ThreadSafeRunnable killable, float hours) {
        minutes(killable, hours * 60);
    }

    public static void minutes(TS_ThreadStruct killable, float minutes) {
        seconds(killable, minutes * 60);
    }

    public static void minutes(TS_ThreadSafeRunnable killable, float minutes) {
        seconds(killable, minutes * 60);
    }

    public static void seconds(TS_ThreadStruct killable, float seconds) {
        var gap = 3;
        if (seconds <= gap) {
            seconds(seconds);
            return;
        }
        var total = 0;
        while (total < seconds) {
            if (killable != null && killable.isKillTriggered()) {
                return;
            }
            d.ci("seconds", killable == null ? "null" : killable.toString(), "...");
            seconds(gap);
            total += gap;
        }
    }

    public static void seconds(TS_ThreadSafeRunnable killable, float seconds) {
        var gap = 3;
        if (seconds <= gap) {
            seconds(seconds);
            return;
        }
        var total = 0;
        while (total < seconds) {
            if (killable != null && killable.killMe) {
                return;
            }
            d.ci("seconds", killable == null ? "null" : killable.toString(), "...");
            seconds(gap);
            total += gap;
        }
    }

    private static void seconds(float seconds) {
        milliseconds((long) (seconds * 1000f));
    }

    public static void millisecondsBtw(long minMilliSeconds, long maxMilliSecons) {
        milliseconds(TS_RandomUtils.nextLong(minMilliSeconds, maxMilliSecons));
    }

    public static void milliseconds(long milliSeconds) {
        of(Duration.ofMillis(milliSeconds));
    }

    public static void of(Duration duration) {
        Thread.yield();
        TGS_UnSafe.run(() -> Thread.sleep(duration), e -> TGS_UnSafe.runNothing());
    }
}
