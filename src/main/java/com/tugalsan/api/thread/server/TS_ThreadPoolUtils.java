package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.*;
import com.tugalsan.api.list.client.*;
import java.util.concurrent.*;

public class TS_ThreadPoolUtils {

    final private static TGS_ListSyncItem<ExecutorService> IMMEDIATE = new TGS_ListSyncItem(Executors.newSingleThreadExecutor());
    final private static TGS_ListSyncItem<ScheduledExecutorService> SCHEDULED = new TGS_ListSyncItem(Executors.newScheduledThreadPool(1));

    public static void shutdown() {
        IMMEDIATE.get().shutdown();
        SCHEDULED.get().shutdown();
    }

    private static void execute(Runnable exe) {
        IMMEDIATE.get().execute(exe);
    }

    private static void scheduleAtFixedRate(Runnable r, long initialDelay, long period, TimeUnit unit) {
        SCHEDULED.get().scheduleAtFixedRate(r, initialDelay, period, unit);
    }

    public static void execute(TGS_Executable exe) {
        Runnable r = () -> exe.execute();
        execute(r);
    }

    public static void scheduleAtFixedRate(TGS_Executable exe, long initialDelay, long period, TimeUnit unit) {
        Runnable r = () -> exe.execute();
        scheduleAtFixedRate(r, initialDelay, period, unit);
    }
}
