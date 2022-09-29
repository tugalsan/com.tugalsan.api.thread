package com.tugalsan.api.thread.server;

import com.tugalsan.api.thread.server.pool.TS_ThreadPoolUtils;
import com.tugalsan.api.executable.client.*;
import java.util.concurrent.*;

public class TS_ThreadRunUtils {

    //TODO ENABLE VIRTUAL THREAD
    public static void now(TGS_Executable exe) {
//        Thread. startVirtualThread(() -> exe.execute());
        new Thread(() -> exe.execute()).start();
    }

    @Deprecated //AFTER VIRTUAL THREAD INTEGRATION, NO NEED FOR LAZY EXECUTION, JUST USE now
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
