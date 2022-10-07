package com.tugalsan.api.thread.server;

import com.tugalsan.api.list.client.TGS_ListUtils;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;

public class TS_ThreadFetchValidated<T> {

    public static record ValidationError(String name, Throwable error) {

    }

    private static <T> Callable<Object> toCallableObject(Callable<T> callableT) {
        return () -> callableT.call();
    }

    private static <T, R> List<Callable<Object>> toLstCallableObject(Callable<T> valRequest, List<Callable<R>> preRequets) {
        List<Callable<Object>> callables = TGS_ListUtils.of();
        callables.add(toCallableObject(valRequest));
        preRequets.forEach(callable -> callables.add(toCallableObject(callable)));
        return callables;
    }

    public static <T> TS_ThreadFetchValidated<T> of(Instant until, Callable<T> valRequest, Callable<ValidationError>... preRequets) {
        return of(until, valRequest, List.of(preRequets));
    }

    public static <T> TS_ThreadFetchValidated<T> of(Instant until, Callable<T> valRequest, List<Callable<ValidationError>> preRequets) {
        var fetchAll = TS_ThreadFetchAll.of(until, toLstCallableObject(valRequest, preRequets));
        TS_ThreadFetchValidated<T> fetchValidated = new TS_ThreadFetchValidated();
        fetchValidated.timeout = fetchAll.timeout();
        fetchValidated.exceptions = fetchAll.exceptionLst();
        fetchValidated.validationErrorLst = TGS_ListUtils.of();
        fetchAll.resultLst().stream()
//                .filter(r -> r != null)
                .forEach(result -> {
                    if (result instanceof ValidationError validationError) {
                        fetchValidated.validationErrorLst.add(validationError);
                        return;
                    }
                    fetchValidated.result = (T) result;
                });
        return fetchValidated;
    }

    public boolean isValidated() {
        if (!validationErrorLst.isEmpty()) {
            return false;
        }
        if (hasError()) {
            return false;
        }
        if (timeout) {
            return false;
        }
        return true;
    }

    public List<ValidationError> validationErrorLst() {
        return validationErrorLst;
    }
    private List<ValidationError> validationErrorLst;

    public T resultIfValidated() {
        return isValidated() ? result : null;
    }

    public T result() {
        return result;
    }
    private T result;

    public boolean timeout() {
        return timeout;
    }
    private boolean timeout;

    public TS_ThreadSafeLst<Throwable> exceptionLst() {
        return exceptions;
    }
    private TS_ThreadSafeLst<Throwable> exceptions;

    public TS_ThreadExceptionPck exceptionPack() {
        return new TS_ThreadExceptionPck(exceptions);
    }

    public boolean hasError() {
        return !exceptions.isEmpty();
    }
}
