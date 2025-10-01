package com.tugalsan.api.thread.server.async.await.core;

import module com.tugalsan.api.function;
import module com.tugalsan.api.thread;
import java.time.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.Optional;

public class TS_ThreadAsyncAwaitCore {

    private TS_ThreadAsyncAwaitCore() {

    }

    public static <R> TS_ThreadAsyncAwaitRecords.AllAwait<R> allAwait(TS_ThreadSyncTrigger killTrigger, Duration timeout, List<TGS_FuncMTU_OutTyped_In1<R, TS_ThreadSyncTrigger>> callables) {
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<R>awaitAll(), cf -> cookConfiguration(cf, killTrigger.name, timeout))) {
            var subTasks = callables.stream().map(c -> scope.fork(() -> c.call(killTrigger))).toList();
            scope.join();
            var resultsSuccessful = subTasks.stream().filter(st -> st.state() == StructuredTaskScope.Subtask.State.SUCCESS).map(StructuredTaskScope.Subtask::get).toList();
            var resultsFailedOrUnavailable = subTasks.stream().filter(st -> st.state() == StructuredTaskScope.Subtask.State.FAILED || st.state() == StructuredTaskScope.Subtask.State.UNAVAILABLE).toList();
            return new TS_ThreadAsyncAwaitRecords.AllAwait(killTrigger, timeout, Optional.empty(), resultsFailedOrUnavailable, resultsSuccessful);
        } catch (InterruptedException | StructuredTaskScope.TimeoutException e) {
            if (e instanceof StructuredTaskScope.TimeoutException et) {
                killTrigger.trigger("TimeoutException");
                return new TS_ThreadAsyncAwaitRecords.AllAwait(killTrigger, timeout, Optional.of(et), List.of(), List.of());
            }
            killTrigger.trigger("InterruptedException");
            return TGS_FuncUtils.throwIfInterruptedException(e);
        }
    }

    public static <R> TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow<R> anySuccessfulOrThrow(TS_ThreadSyncTrigger killTrigger, Duration timeout, List<TGS_FuncMTU_OutTyped_In1<R, TS_ThreadSyncTrigger>> callables) {
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<R>anySuccessfulResultOrThrow(), cf -> cookConfiguration(cf, killTrigger.name, timeout))) {
            callables.stream().map(c -> scope.fork(() -> c.call(killTrigger))).toList();
            return new TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow(killTrigger, timeout, Optional.empty(), Optional.empty(), Optional.ofNullable(scope.join()));
        } catch (InterruptedException | StructuredTaskScope.TimeoutException | StructuredTaskScope.FailedException e) {
            if (e instanceof StructuredTaskScope.TimeoutException et) {
                killTrigger.trigger("TimeoutException");
                return new TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow(killTrigger, timeout, Optional.of(et), Optional.empty(), Optional.empty());
            }
            if (e instanceof StructuredTaskScope.FailedException ef) {
                killTrigger.trigger("FailedException");
                return new TS_ThreadAsyncAwaitRecords.AnySuccessfulOrThrow(killTrigger, timeout, Optional.empty(), Optional.of(ef), Optional.empty());
            }
            killTrigger.trigger("InterruptedException");
            return TGS_FuncUtils.throwIfInterruptedException(e);
        }
    }

    public static <R> TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow<R> allSuccessfulOrThrow(TS_ThreadSyncTrigger killTrigger, Duration timeout, List<TGS_FuncMTU_OutTyped_In1<R, TS_ThreadSyncTrigger>> callables) {
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<R>allSuccessfulOrThrow(), cf -> cookConfiguration(cf, killTrigger.name, timeout))) {
            callables.stream().map(c -> scope.fork(() -> c.call(killTrigger))).toList();
            return new TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow(killTrigger, timeout, Optional.empty(), Optional.empty(), scope.join().map(StructuredTaskScope.Subtask::get).toList());
        } catch (InterruptedException | StructuredTaskScope.TimeoutException | StructuredTaskScope.FailedException e) {
            if (e instanceof StructuredTaskScope.TimeoutException et) {
                killTrigger.trigger("TimeoutException");
                return new TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow(killTrigger, timeout, Optional.of(et), Optional.empty(), List.of());
            }
            if (e instanceof StructuredTaskScope.FailedException ef) {
                killTrigger.trigger("FailedException");
                return new TS_ThreadAsyncAwaitRecords.AllSuccessfulOrThrow(killTrigger, timeout, Optional.empty(), Optional.of(ef), List.of());
            }
            killTrigger.trigger("InterruptedException");
            return TGS_FuncUtils.throwIfInterruptedException(e);
        }
    }

    private static StructuredTaskScope.Configuration cookConfiguration(StructuredTaskScope.Configuration configuration, String name, Duration timeout) {
        if (name != null && timeout != null) {
            return configuration.withName(name).withTimeout(timeout);
        }
        if (timeout != null) {
            return configuration.withTimeout(timeout);
        }
        if (name != null) {
            return configuration.withName(name);
        }
        return configuration;
    }
}
