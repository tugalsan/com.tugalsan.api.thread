package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallelUntilFirstFail;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallelUntilFirstSuccess;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallel;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.thread.server.TS_ThreadKillTrigger;
import java.time.*;
import java.util.*;

@Deprecated //USE TS_ThreadStructBuilder with killTrigger if possible
public class TS_ThreadAsyncAwait {

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callSingle(TS_ThreadKillTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadKillTrigger> callable) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, callable);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadKillTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadKillTrigger>... callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadKillTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, callables);
    }

//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadKillTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadKillTrigger> fetcher, Callable<Void>... throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, fetcher, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadKillTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadKillTrigger> fetcher, List<Callable<Void>> throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, fetcher, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadKillTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> fetchers, Callable<Void>... throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, fetchers, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadKillTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> fetchers, List<Callable<Void>> throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(until, fetchers, throwingValidators);
//    }
    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(TS_ThreadKillTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadKillTrigger>... callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstSuccess.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(TS_ThreadKillTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstSuccess.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> callParallel(TS_ThreadKillTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadKillTrigger>... callables) {
        return TS_ThreadAsyncCoreParallel.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> callParallel(TS_ThreadKillTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadKillTrigger>> callables) {
        return TS_ThreadAsyncCoreParallel.of(killTrigger, until, callables);
    }

    public static TS_ThreadAsyncCoreParallelUntilFirstFail<Void> runUntil(TS_ThreadKillTrigger killTrigger, Duration until, TGS_RunnableType1<TS_ThreadKillTrigger> exe) {
        return callSingle(killTrigger, until, kt -> {
            exe.run(kt);
            return null;
        });
    }
}
