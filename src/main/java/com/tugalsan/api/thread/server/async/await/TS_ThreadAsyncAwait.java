package com.tugalsan.api.thread.server.async.await;

import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutTyped_In1;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_In1;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCEUtils;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.sync.rateLimited.TS_ThreadSyncRateLimitedCallType1;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.time.*;
import java.util.*;

//USE TS_ThreadAsyncBuilder with killTrigger if possible
public class TS_ThreadAsyncAwait {
    
    private TS_ThreadAsyncAwait(){
        
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger> callable, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger>... throwingValidators) {
        List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> allCallables = TGS_ListUtils.of();
        allCallables.add(callable);
        Arrays.stream(throwingValidators).forEach(tv -> allCallables.add(TGS_FuncMTUCEUtils.toR(tv)));
        return TS_ThreadAsyncAwait.callParallelUntilFirstFail(
                killTrigger, until, allCallables
        );
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger>... throwingValidators) {
        List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> allCallables = TGS_ListUtils.of();
        allCallables.addAll(callables);
        Arrays.stream(throwingValidators).forEach(tv -> allCallables.add(TGS_FuncMTUCEUtils.toR(tv)));
        return TS_ThreadAsyncAwait.callParallelUntilFirstFail(
                killTrigger, until, allCallables
        );
    }

    public static <T> TS_ThreadAsyncAwaitSingle<T> callSingle(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger> callable) {
        return TS_ThreadAsyncAwaitSingle.of(killTrigger, until, callable);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return TS_ThreadAsyncAwaitParallelUntilFirstFail.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilFirstFail<T> callParallelUntilFirstFail(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return TS_ThreadAsyncAwaitParallelUntilFirstFail.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return TS_ThreadAsyncAwaitParallelUntilFirstSuccess.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilFirstSuccess<T> callParallelUntilFirstSuccess(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return TS_ThreadAsyncAwaitParallelUntilFirstSuccess.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilAllDone<TGS_UnionExcuse<T>> callParallelRateLimited(TS_ThreadSyncTrigger killTrigger, int rateLimit, Duration until, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        var rateLimitor = TS_ThreadSyncRateLimitedCallType1.<T, TS_ThreadSyncTrigger>of(rateLimit);
        var rateLimitedCallables = TGS_StreamUtils.toLst(
                Arrays.stream(callables).map(c -> {
                    TGS_FuncMTUCE_OutTyped_In1<TGS_UnionExcuse<T>, TS_ThreadSyncTrigger> cs = kt -> rateLimitor.callUntil(c, until, kt);
                    return cs;
                })
        );
        return TS_ThreadAsyncAwaitParallelUntilAllDone.of(killTrigger, until, rateLimitedCallables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilAllDone<T> callParallel(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>... callables) {
        return TS_ThreadAsyncAwaitParallelUntilAllDone.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilAllDone<T> callParallel(TS_ThreadSyncTrigger killTrigger, Duration until, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        return TS_ThreadAsyncAwaitParallelUntilAllDone.of(killTrigger, until, callables);
    }

    public static <T> TS_ThreadAsyncAwaitParallelUntilAllDone<T> callParallelRateLimited(TS_ThreadSyncTrigger killTrigger, int rateLimit, Duration until, List<TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger>> callables) {
        var rateLimitor = TS_ThreadSyncRateLimitedCallType1.<T, TS_ThreadSyncTrigger>of(rateLimit);
        var rateLimitedCallables = TGS_StreamUtils.toLst(
                callables.stream().map(c -> {
                    TGS_FuncMTUCE_OutTyped_In1<T, TS_ThreadSyncTrigger> cs = kt -> rateLimitor.callUntil(c, until, kt).orElse(null);
                    return cs;
                })
        );
        return TS_ThreadAsyncAwaitParallelUntilAllDone.of(killTrigger, until, rateLimitedCallables);
    }

    public static TS_ThreadAsyncAwaitSingle<Void> runUntil(TS_ThreadSyncTrigger killTrigger, Duration until, TGS_FuncMTUCE_In1<TS_ThreadSyncTrigger> exe) {
        return callSingle(killTrigger, until, exe);
    }
}
