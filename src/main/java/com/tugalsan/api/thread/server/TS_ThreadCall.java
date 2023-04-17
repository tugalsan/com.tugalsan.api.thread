package com.tugalsan.api.thread.server;

import com.tugalsan.api.thread.server.core.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class TS_ThreadCall {

    public static <T> TS_ThreadCallParallel<T> parallel(Duration duration, Callable<T>... callables) {
        return TS_ThreadCallParallel.of(duration, callables);
    }

    public static <T> TS_ThreadCallParallel<T> parallel(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadCallParallel.of(duration, callables);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T> callable) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, callable);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T>... callables) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, callables);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, callables);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T> fetcher, Callable<Void>... throwingValidators) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, fetcher, throwingValidators);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, Callable<T> fetcher, List<Callable<Void>> throwingValidators) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, fetcher, throwingValidators);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, List<Callable<T>> fetchers, Callable<Void>... throwingValidators) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadCallParallelUntilFirstFail<T> parallelUntilFirstFail(Duration duration, List<Callable<T>> fetchers, List<Callable<Void>> throwingValidators) {
        return TS_ThreadCallParallelUntilFirstFail.of(duration, fetchers, throwingValidators);
    }

    public static <T> TS_ThreadCallParallelUntilFirstSuccess<T> parallelUntilFirstSuccess(Duration duration, Callable<T>... callables) {
        return TS_ThreadCallParallelUntilFirstSuccess.of(duration, callables);
    }

    public static <T> TS_ThreadCallParallelUntilFirstSuccess<T> parallelUntilFirstSuccess(Duration duration, List<Callable<T>> callables) {
        return TS_ThreadCallParallelUntilFirstSuccess.of(duration, callables);
    }
}
