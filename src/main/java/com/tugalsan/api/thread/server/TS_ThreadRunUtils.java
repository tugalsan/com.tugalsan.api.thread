package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;
import java.util.concurrent.*;

public class TS_ThreadRunUtils {

    public static void now(TGS_Executable exe) {
        new Thread(() -> exe.execute()).start();
    }

    public static void once(TGS_Executable exe) {
        TS_ThreadPoolUtils.execute(exe);
    }

    public static void everySeconds(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        TS_ThreadPoolUtils.scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.SECONDS);
    }

    public static void everyMinutes(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        TS_ThreadPoolUtils.scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.MINUTES);
    }

    public static void everyHours(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        TS_ThreadPoolUtils.scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.HOURS);
    }

    public static void everyDays(boolean startNow, long initialDelayAndPeriod, TGS_Executable exe) {
        TS_ThreadPoolUtils.scheduleAtFixedRate(exe, startNow ? 0 : initialDelayAndPeriod, initialDelayAndPeriod, TimeUnit.DAYS);
    }
}
