package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;

public class TS_ThreadKillableBuilder6RunInit<T> {

    protected TS_ThreadKillableBuilder6RunInit(Class<T> clazz, String name, Duration durLag, Duration durMax, Duration durLoop, TGS_Callable<T> runInit) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMax = durMax;
        this.durLoop = durLoop;
        this.runInit = runInit;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMax;
    protected Duration durLoop;
    protected TGS_Callable<T> runInit;

    public TS_ThreadKillableBuilder7ValPeriodic<T> valPeriodicNone() {
        return valPeriodic(null);
    }

    public TS_ThreadKillableBuilder7ValPeriodic<T> valPeriodic(TGS_ValidatorType1<T> valPeriodic) {
        return new TS_ThreadKillableBuilder7ValPeriodic(clazz, name, durLag, durMax, durLoop, runInit, valPeriodic);
    }
}
