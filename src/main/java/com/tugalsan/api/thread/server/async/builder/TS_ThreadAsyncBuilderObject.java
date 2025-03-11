package com.tugalsan.api.thread.server.async.builder;

import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.async.run.TS_ThreadAsyncRun;
import com.tugalsan.api.thread.server.async.await.TS_ThreadAsyncAwait;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.function.client.TGS_FuncUtils;
import com.tugalsan.api.function.client.maythrow.uncheckedexceptions.TGS_FuncMTUCE_OutBool_In2;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TS_ThreadAsyncBuilderObject<T> {

    public static TS_Log d = TS_Log.of(false, TS_ThreadAsyncBuilderObject.class);

    private TS_ThreadAsyncBuilderObject(TS_ThreadSyncTrigger killTrigger, String name,
            TS_ThreadAsyncBuilderCallableTimed<T> init, TS_ThreadAsyncBuilderRunnableTimedType2<T> main, TS_ThreadAsyncBuilderRunnableTimedType1<T> fin,
            Optional<TGS_FuncMTUCE_OutBool_In2<TS_ThreadSyncTrigger, T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        this.killTrigger_wt = TS_ThreadSyncTrigger.of(d.className, killTrigger).newChild(name);
        this.name = name;
        this.init = init;
        this.main = main;
        this.fin = fin;
        this.valCycleMain = valCycleMain;
        this.durPeriodCycle = durPeriodCycle;
        this.dead = TS_ThreadSyncTrigger.of(name + ".dead");
        this.started = TS_ThreadSyncTrigger.of(name + ".started");
    }
    final public TS_ThreadSyncTrigger killTrigger_wt;
    final public String name;
    final public TS_ThreadAsyncBuilderCallableTimed<T> init;
    final public TS_ThreadAsyncBuilderRunnableTimedType2<T> main;
    final public TS_ThreadAsyncBuilderRunnableTimedType1<T> fin;
    final public Optional<Duration> durPeriodCycle;
    final public Optional<TGS_FuncMTUCE_OutBool_In2<TS_ThreadSyncTrigger, T>> valCycleMain;
    final public AtomicReference<T> initObject = new AtomicReference(null);

    @Override
    public String toString() {
        return TS_ThreadAsyncBuilderObject.class.getSimpleName() + "{" + "name=" + name + ", init=" + init + ", main=" + main + ", fin=" + fin + ", durPeriodCycle=" + durPeriodCycle + ", valCycleMain=" + valCycleMain + ", killTriggered=" + killTrigger_wt + ", dead=" + dead + ", started=" + started + '}';
    }

    public void kill() {
        killTrigger_wt.trigger("builder_kill");
    }

    public boolean isKillTriggered() {
        return killTrigger_wt.hasTriggered();
    }

    public boolean isNotDead() {
        return !isDead();
    }

    public boolean isDead() {
        return dead.hasTriggered();
    }
    private final TS_ThreadSyncTrigger dead;

    public boolean isStarted() {
        return started.hasTriggered();
    }
    private final TS_ThreadSyncTrigger started;

    public boolean hasError() {
        return !exceptions.isEmpty();
    }
    public List<Throwable> exceptions = TGS_ListUtils.of();

    private void _run_init() {
        d.ci(name, "#init");
        if (init.call.isPresent()) {
            d.ci(name, "#init.call.isPresent()");
            if (init.max.isPresent()) {
                d.ci(name, "#init.max.isPresent()");
                var await = TS_ThreadAsyncAwait.runUntil(killTrigger_wt.newChild("_run_init"), init.max.get(), kt -> initObject.set(init.call.get().call()));
                if (await.hasError()) {
                    d.ci(name, "#init.await.hasError()");
                    exceptions.add(await.exceptionIfFailed.get());
                    if (d.infoEnable) {
                        d.ce(name, exceptions);
                    }
                } else {
                    d.ci(name, "#init.await.!hasError()");
                }
            } else {
                d.ci(name, "#init.max.!isPresent()");
                initObject.set(init.call.get().call());
                if (hasError()) {
                    d.ci(name, "#init.run.!hasError()");
                } else {
                    d.ci(name, "#init.run.!hasError()");
                }
            }
        }
    }

    private void _run_main() {
        d.ci(name, "#main");
        if (main.run.isPresent()) {
            d.ci(name, "#main.run.isPresent()");
            while (true) {
                if (d.infoEnable) {
                    d.ci(name, "#main.tick." + TGS_Time.toString_timeOnly_now());
                }
                var msBegin = System.currentTimeMillis();
                if (isKillTriggered()) {
                    break;
                }
                if (valCycleMain.isPresent()) {
                    d.ci(name, "#main.valCycleMain.isPresent()");
                    if (!valCycleMain.get().validate(killTrigger_wt.newChild("valCycleMain"), initObject.get())) {
                        d.ci(name, "#main.!valCycleMain.get().validate(initObject.get())");
                        break;
                    }
                }
                if (main.max.isPresent()) {
                    var await = TS_ThreadAsyncAwait.runUntil(killTrigger_wt.newChild("_run_main.await"), main.max.get(), kt -> main.run.get().run(kt, initObject.get()));
                    if (await.hasError()) {
                        d.ci(name, "#main.await.hasError()");
                        exceptions.add(await.exceptionIfFailed.get());
                        if (d.infoEnable) {
                            d.ce(name, exceptions);
                        }
                        return;
                    } else {
                        d.ci(name, "#main.await.!hasError()");
                    }
                } else {
                    main.run.get().run(killTrigger_wt.newChild("_run_main.run"), initObject.get());
                    if (hasError()) {// DO NOT STOP FINILIZE
                        d.ci(name, "#main.run.hasError()");
                        return;
                    } else {
                        d.ci(name, "#main.run.!hasError()");
                    }
                }
                if (!durPeriodCycle.isPresent() && !valCycleMain.isPresent()) {
                    d.ci(name, "#main.!durPeriodCycle.isPresent() && !valCycleMain.isPresent()");
                    break;
                } else {
                    d.ci(name, "#main.will continue");
                }
                if (durPeriodCycle.isPresent()) {
                    var msLoop = durPeriodCycle.get().toMillis();
                    var msEnd = System.currentTimeMillis();
                    var msSleep = msLoop - (msEnd - msBegin);
                    if (msSleep > 0) {
                        d.ci(name, "#main.later");
                        try {
                            Thread.sleep(msSleep);
                        } catch (InterruptedException ex) {
                            TGS_FuncUtils.throwIfInterruptedException(ex);
                        }
                    } else {
                        d.ci(name, "#main.now");
                    }
                    Thread.yield();
                }
            }
        }
    }

    private void _run_fin() {
        d.ci(name, "#fin");
        if (fin.run.isPresent()) {
            d.ci(name, "#fin.run.isPresent()");
            if (fin.max.isPresent()) {
                d.ci(name, "#fin.max.isPresent()");
                var await = TS_ThreadAsyncAwait.runUntil(killTrigger_wt.newChild("_run_fin.await"), fin.max.get(), kt -> fin.run.get().run(initObject.get()));
                if (await.hasError()) {
                    d.ci(name, "#fin.await.hasError()");
                    exceptions.add(await.exceptionIfFailed.get());
                    if (d.infoEnable) {
                        d.ce(name, exceptions);
                    }
                } else {
                    d.ci(name, "#fin.await.!hasError()");
                }
            } else {
                d.ci(name, "fin.max.!isPresent()");
                fin.run.get().run(initObject.get());
                if (hasError()) {
                    d.ci(name, "#fin.run.hasError()");
                } else {
                    d.ci(name, "#fin.run.!hasError()");
                }
            }
        }
    }

    private void _run() {
        _run_init();
        _run_main();
        _run_fin();
        dead.trigger("builder_run[dead]");
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRun() {
        if (isStarted()) {
            return this;
        }
        started.trigger("builder_asyncRun()[started]");
        TS_ThreadAsyncRun.now(killTrigger_wt.newChild("asyncRun()"), kt -> _run());
        return this;
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRun(Duration until) {
        if (isStarted()) {
            return this;
        }
        started.trigger("builder_asyncRun(dur)[started]");
        TS_ThreadAsyncRun.until(killTrigger_wt.newChild("asyncRun(dur)"), until, kt -> _run());
        return this;
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait() {
        return asyncRunAwait(null);
    }

    public TS_ThreadAsyncBuilderObject<T> asyncRunAwait(Duration until) {
        if (isStarted()) {
            return this;
        }
        started.trigger("builder_asyncRunAwait(dur)[started]");
        TS_ThreadAsyncAwait.runUntil(killTrigger_wt.newChild("asyncRunAwait(dur)"), until, kt -> _run());
        return this;
    }

    public static <T> TS_ThreadAsyncBuilderObject<T> of(TS_ThreadSyncTrigger killTrigger, String name,
            TS_ThreadAsyncBuilderCallableTimed<T> init, TS_ThreadAsyncBuilderRunnableTimedType2<T> main, 
            TS_ThreadAsyncBuilderRunnableTimedType1<T> fin,
            Optional<TGS_FuncMTUCE_OutBool_In2<TS_ThreadSyncTrigger, T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return new TS_ThreadAsyncBuilderObject(killTrigger, name, init, main, fin, valCycleMain, durPeriodCycle);
    }
}
