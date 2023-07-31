package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.callable.client.TGS_Callable;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.api.thread.server.async.TS_ThreadAsync;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadKillable<T> {

    private TS_ThreadKillable(String name, Duration durLag, Duration durMax, Duration durLoop, TGS_Callable<T> runInit, TGS_ValidatorType1<T> valPeriodic, TGS_RunnableType1<T> runMain, TGS_RunnableType1<T> runFinal) {
        this.name = name;
        this.durLag = durLag;
        this.durMax = durMax;
        this.durLoop = durLoop;
        this.runInit = runInit;
        this.valPeriodic = valPeriodic;
        this.runMain = runMain;
        this.runFinal = runFinal;
    }
    final public String name;
    final public Duration durLag;
    final public Duration durMax;
    final public Duration durLoop;
    final public TGS_Callable<T> runInit;
    final public TGS_ValidatorType1<T> valPeriodic;
    final public TGS_RunnableType1<T> runMain;
    final public TGS_RunnableType1<T> runFinal;

    public void kill() {
        killTriggered.set(true);
    }

    public boolean isKillTriggered() {
        return killTriggered.get();
    }
    private final AtomicBoolean killTriggered = new AtomicBoolean(false);

    public boolean isDead() {
        return dead.get();
    }
    private final AtomicBoolean dead = new AtomicBoolean(false);

    public boolean isStarted() {
        return started.get();
    }
    private final AtomicBoolean started = new AtomicBoolean(false);

    public boolean start() {
        if (isStarted()) {
            return false;
        }
        started.set(true);
        TS_ThreadAsync.now(() -> {
            TS_ThreadAsyncAwait.runUntil(durMax, () -> {
                TS_ThreadWait.of(durLag);
                T o = null;
                if (runInit != null) {
                    o = runInit.call();
                }
                if (valPeriodic == null) {
                    if (runMain != null) {
                        runMain.run(o);
                    }
                } else {
                    while (valPeriodic.validate(o)) {
                        if (runMain != null) {
                            var msLoop = durLoop.toMillis();
                            var msBegin = System.currentTimeMillis();
                            runMain.run(o);
                            var msEnd = System.currentTimeMillis();
                            var msSleep = msLoop - (msEnd - msBegin);
                            if (msSleep > 0) {
                                TGS_UnSafe.run(() -> Thread.sleep(msSleep));
                            }
                            Thread.yield();
                        }
                    }
                }
                if (runFinal != null) {
                    runFinal.run(o);
                }
            });
            dead.set(true);
        });
        return true;
    }

    public static <T> TS_ThreadKillable of(String name, Duration durLag, Duration maxTime, Duration durLoop, TGS_Callable<T> runInit, TGS_ValidatorType1<T> valPeriodic, TGS_RunnableType1<T> runMain, TGS_RunnableType1<T> runFinal) {
        return new TS_ThreadKillable(name, durLag, maxTime, durLoop, runInit, valPeriodic, runMain, runFinal);
    }
}
