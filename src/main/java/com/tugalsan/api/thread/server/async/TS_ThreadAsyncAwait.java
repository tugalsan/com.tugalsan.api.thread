package com.tugalsan.api.thread.server.async;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallelUntilFirstFail;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallelUntilFirstSuccess;
import com.tugalsan.api.thread.server.async.core.TS_ThreadAsyncCoreParallel;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import java.time.*;
import java.util.*;

//USE TS_ThreadStructBuilder with killTrigger if possible
public class TS_ThreadAsyncAwait {

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger> caller, TGS_RunnableType1<TS_ThreadSafeTrigger>... throwingValidators) {
        List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables = TGS_ListUtils.of();
        callables.add(caller);
        Arrays.stream(throwingValidators).forEach(tv -> callables.add(kt -> {
            tv.run(kt);
            return null;
        }));
        return TS_ThreadAsyncAwait.callParallelUntilFirstFail(
                killTrigger, until, callables
        );
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callers, TGS_RunnableType1<TS_ThreadSafeTrigger>... throwingValidators) {
        List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables = TGS_ListUtils.of();
        callables.addAll(callers);
        Arrays.stream(throwingValidators).forEach(tv -> callables.add(kt -> {
            tv.run(kt);
            return null;
        }));
        return TS_ThreadAsyncAwait.callParallelUntilFirstFail(
                killTrigger, until, callables
        );
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callSingle(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger> callable) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, callable);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger>... callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, callables);
    }

//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger> fetcher, Callable<Void>... throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, fetcher, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger> fetcher, List<Callable<Void>> throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, fetcher, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> fetchers, Callable<Void>... throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(killTrigger, until, fetchers, throwingValidators);
//    }
//
//    public static <T> TS_ThreadAsyncCoreParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSafeTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> fetchers, List<Callable<Void>> throwingValidators) {
//        return TS_ThreadAsyncCoreParallelUntilFirstFail.of(until, fetchers, throwingValidators);
//    }
    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger>... callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstSuccess.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(TS_ThreadSafeTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables) {
        return TS_ThreadAsyncCoreParallelUntilFirstSuccess.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> callParallel(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_CallableType1<T, TS_ThreadSafeTrigger>... callables) {
        return TS_ThreadAsyncCoreParallel.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncCoreParallel<T> callParallel(TS_ThreadSafeTrigger killTrigger, Duration until, List<TGS_CallableType1<T, TS_ThreadSafeTrigger>> callables) {
        return TS_ThreadAsyncCoreParallel.of(killTrigger, until, callables);
    }

    public static TS_ThreadAsyncCoreParallelUntilFirstFail<Void> runUntil(TS_ThreadSafeTrigger killTrigger, Duration until, TGS_RunnableType1<TS_ThreadSafeTrigger> exe) {
        return callSingle(killTrigger, until, kt -> {
            exe.run(kt);
            return null;
        });
    }
}
