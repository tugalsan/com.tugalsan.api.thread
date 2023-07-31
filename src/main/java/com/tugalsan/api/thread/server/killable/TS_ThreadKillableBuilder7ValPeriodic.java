package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;

public class TS_ThreadKillableBuilder7ValPeriodic<T> {

    protected TS_ThreadKillableBuilder7ValPeriodic(Class<T> clazz, String name, Duration durLag, Duration durMainMax, Duration durLoop, TGS_Callable<T> runInit, TGS_ValidatorType1<T> valPeriodic) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMainMax = durMainMax;
        this.durLoop = durLoop;
        this.runInit = runInit;
        this.valPeriodic = valPeriodic;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMainMax;
    protected Duration durLoop;
    protected TGS_Callable<T> runInit;
    protected TGS_ValidatorType1<T> valPeriodic;

    @Deprecated //NANI?
    public TS_ThreadKillableBuilder8RunMain<T> runMainNone() {
        return runMain(null);
    }

    public TS_ThreadKillableBuilder8RunMain<T> runMain(TGS_RunnableType1<T> runMain) {
        return new TS_ThreadKillableBuilder8RunMain(clazz, name, durLag, durMainMax, durLoop, runInit, valPeriodic, runMain);
    }
}
