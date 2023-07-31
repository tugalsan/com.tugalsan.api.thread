package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;

public class TS_ThreadKillableBuilder8RunMain<T> {

    protected TS_ThreadKillableBuilder8RunMain(Class<T> clazz, String name, Duration durLag, Duration durMax, Duration durLoop, TGS_Callable<T> runInit, TGS_ValidatorType1<T> valPeriodic, TGS_RunnableType1<T> runMain) {
        this.clazz = clazz;
        this.name = name;
        this.durLag = durLag;
        this.durMax = durMax;
        this.durLoop = durLoop;
        this.runInit = runInit;
        this.valPeriodic = valPeriodic;
        this.runMain = runMain;
    }
    protected Class<T> clazz;
    protected String name;
    protected Duration durLag;
    protected Duration durMax;
    protected Duration durLoop;
    protected TGS_Callable<T> runInit;
    protected TGS_ValidatorType1<T> valPeriodic;
    protected TGS_RunnableType1<T> runMain;

    public TS_ThreadKillable<T> runFinalNone() {
        return runFinal(null);
    }

    public TS_ThreadKillable<T> runFinal(TGS_RunnableType1<T> runFinal) {
        return TS_ThreadKillable.of(name, durLag, durMax, durLoop, runInit, valPeriodic, runMain, runFinal);
    }
}
