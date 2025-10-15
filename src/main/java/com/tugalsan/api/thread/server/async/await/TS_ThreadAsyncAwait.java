package com.tugalsan.api.thread.server.async.await;

import module com.tugalsan.api.function;
import module com.tugalsan.api.list;
import module com.tugalsan.api.stream;
import module com.tugalsan.api.thread;
import java.time.*;
import java.util.*;

//USE TS_ThreadAsyncBuilder with killTrigger if possible
public class TS_ThreadAsyncAwait {

    private TS_ThreadAsyncAwait() {

    }

    //AllSuccessfulOrThrow
    @Deprecated//Optional[java.util.concurrent.StructuredTaskScope$FailedException: java.util.NoSuchElementException: No value present
    public static <T> TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger> callable, T objectAsInVoidButNotNull, TGS_FuncMTU_In1<TS_ThreadSyncTrigger>... throwingValidators) {
        List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> allCallables = TGS_ListUtils.of();
        allCallables.add(callable);
        Arrays.stream(throwingValidators).forEach(tv -> allCallables.add(TGS_FuncMTUUtils.toR(objectAsInVoidButNotNull, tv)));
        var fullResults = callParallelUntilFirstFail(
                killTrigger, until, allCallables
        );
        if (fullResults.hasError()) {
            return fullResults;
        }
        return new TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow(
                fullResults.killTrigger(),
                fullResults.timeoutDuration(),
                fullResults.timeoutException(),
                fullResults.failedException(),
                fullResults.results().stream().filter(r -> !r.equals(objectAsInVoidButNotNull)).toList()
        );
    }

    @Deprecated//Optional[java.util.concurrent.StructuredTaskScope$FailedException: java.util.NoSuchElementException: No value present
    public static <T> TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables, T objectAsInVoidButNotNull, TGS_FuncMTU_In1<TS_ThreadSyncTrigger>... throwingValidators) {
        return callParallelUntilFirstFail(killTrigger, until, callables, objectAsInVoidButNotNull, List.of(throwingValidators));
    }

    @Deprecated//Optional[java.util.concurrent.StructuredTaskScope$FailedException: java.util.NoSuchElementException: No value present
    public static <T> TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables, T objectAsInVoidButNotNull, List<TGS_FuncMTU_In1<TS_ThreadSyncTrigger>> throwingValidators) {
        List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> allCallables = TGS_ListUtils.of();
        allCallables.addAll(callables);
        throwingValidators.forEach(tv -> allCallables.add(TGS_FuncMTUUtils.toR(objectAsInVoidButNotNull, tv)));
        var fullResults = callParallelUntilFirstFail(
                killTrigger, until, allCallables
        );
        if (fullResults.hasError()) {
            return fullResults;
        }
        return new TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow(
                fullResults.killTrigger(),
                fullResults.timeoutDuration(),
                fullResults.timeoutException(),
                fullResults.failedException(),
                fullResults.results().stream().filter(r -> !r.equals(objectAsInVoidButNotNull)).toList()
        );
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return callParallelUntilFirstFail(killTrigger, until, List.of(callables));
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return TS_ThreadAsyncAwaitCore.allSuccessfulOrThrow(killTrigger, until, callables);
    }

    //AnySuccessfulOrThrow
    public static <T> TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow<T> callParallelUntilFirstSuccess(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return callParallelUntilFirstSuccess(killTrigger, until, List.of(callables));
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow<T> callParallelUntilFirstSuccess(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return TS_ThreadAsyncAwaitCore.anySuccessfulOrThrow(killTrigger, until, callables);
    }

    //AllAwait
    public static <T> TS_ThreadAsyncAwaitRecords.AllAwait<T> callParallel(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return callParallel(killTrigger, until, List.of(callables));
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AllAwait<T> callParallel(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return callParallelRateLimited(killTrigger, 0, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AllAwait<T> callParallelRateLimited(TS_ThreadSyncTrigger killTrigger, int rateLimit, Duration until, TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return callParallelRateLimited(killTrigger, rateLimit, until, List.of(callables));
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AllAwait<T> callParallelRateLimited(TS_ThreadSyncTrigger killTrigger, int rateLimit, Duration until, List<TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        if (rateLimit < 1) {
            return TS_ThreadAsyncAwaitCore.allAwait(killTrigger, until, callables);
        }
        var rateLimitor = TS_ThreadSyncRateLimitedCallType1.<T, TS_ThreadSyncTrigger>of(rateLimit);
        var rateLimitedCallables = TGS_StreamUtils.toLst(
                callables.stream().map(c -> {
                    TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger> cs = kt -> rateLimitor.callUntil(c, until, kt).orElse(null);
                    return cs;
                })
        );
        return TS_ThreadAsyncAwaitCore.allAwait(killTrigger, until, rateLimitedCallables);
    }

    //AnySuccessfulOrThrow
    public static TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow<Void> runUntil(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_In1<TS_ThreadSyncTrigger> exe) {
        return callSingle(killTrigger, until, exe);
    }

    public static <T> TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow<T> callSingle(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTU_OutTyped_In1<T, TS_ThreadSyncTrigger> callable) {
        return TS_ThreadAsyncAwaitCore.anySuccessfulOrThrow(killTrigger, until, List.of(callable));
    }
}
