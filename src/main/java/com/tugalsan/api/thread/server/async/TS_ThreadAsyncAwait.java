package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallelUntilFirstFail;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallelUntilFirstSuccess;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallel;
import com.tugalsan.api.runnable.client.TGS_Runnable;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class TS_ThreadAsyncAwait {

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callSingle(Duration duration, Callable<T> callable) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, callable);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(Duration duration, Callable<T>... callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(Duration duration, Callable<T> fetcher, Callable<Void>... throwingValidators) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, fetcher, throwingValidators);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(Duration duration, Callable<T> fetcher, List<Callable<Void>> throwingValidators) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, fetcher, throwingValidators);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(Duration duration, List<Callable<T>> fetchers, Callable<Void>... throwingValidators) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(Duration duration, List<Callable<T>> fetchers, List<Callable<Void>> throwingValidators) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(Duration duration, Callable<T>... callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstSuccess.of(duration, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstSuccess.of(duration, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> callParallel(Duration duration, Callable<T>... callables) {
        return TS_ThreadAsyncCoreParallel.of(duration, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> callParallel(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadAsyncCoreParallel.of(duration, callables);
    }

    public static TS_ThreadAsyncCoreParallelUntilFirstFail<Void> runUntil(Duration duration, TGS_Runnable exe) {
        return TS_ThreadAsyncAwait.callSingle(duration, () -> {
            exe.run();
            return null;
        });
    }
}
