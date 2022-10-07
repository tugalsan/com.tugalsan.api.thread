package com.tugalsan.api.thread.server;

import com.tugalsan.api.list.client.TGS_ListUtils;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;

//A STRUCTURED VERSION OF TS_ThreadFetchAll with validate. BUT SLOW!
public class TS_ThreadFetchValidated<T> {

    public static record ValidationError(String name, Throwable error) {

    }

    public static <T> TS_ThreadFetchValidated<T> of(Instant until, List<Callable<T>> valRequest, List<Callable<ValidationError>> preRequets) {
        List<Callable<Object>> callables = TGS_ListUtils.of();
        valRequest.forEach(item -> callables.add(() -> item.call()));
        preRequets.forEach(item -> callables.add(() -> item.call()));
        var fetchAll = TS_ThreadFetchAll.of(until, callables);
        TS_ThreadFetchValidated<T> fetchValidated = new TS_ThreadFetchValidated();
        fetchValidated.timeout = fetchAll.timeout();
        fetchValidated.exceptions = fetchAll.exceptionLst();
        fetchValidated.validationErrorLst = TGS_ListUtils.of();
        fetchValidated.resultLst = TGS_ListUtils.of();
        fetchAll.resultLst().stream()
                .forEach(result -> {
                    if (result instanceof ValidationError validationError) {
                        fetchValidated.validationErrorLst.add(validationError);
                        return;
                    }
                    fetchValidated.resultLst.add((T) result);
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

    public List<T> resultLst() {
        return resultLst;
    }
    private List<T> resultLst;

    public List<T> resultLstIfValidated() {
        return isValidated() ? resultLst : null;
    }

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
