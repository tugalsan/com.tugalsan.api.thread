package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.builder_core.TS_ThreadStructRunnableTimedType1;
import com.tugalsan.api.thread.server.struct.builder_core.TS_ThreadStructRunnableTimedType2;
import com.tugalsan.api.thread.server.struct.builder_core.TS_ThreadStructCallableTimed;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.thread.server.struct.async.TS_ThreadAsync;
import com.tugalsan.api.thread.server.struct.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TS_ThreadStruct<T> {

    private static TS_Log d = TS_Log.of( TS_ThreadStruct.class);

    private TS_ThreadStruct(String name,
            TS_ThreadStructCallableTimed<T> init, TS_ThreadStructRunnableTimedType2<T> main, TS_ThreadStructRunnableTimedType1<T> fin,
            Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        this.name = name;
        this.init = init;
        this.main = main;
        this.fin = fin;
        this.valCycleMain = valCycleMain;
        this.durPeriodCycle = durPeriodCycle;
    }
    final public String name;

    final public TS_ThreadStructCallableTimed<T> init;
    final public TS_ThreadStructRunnableTimedType2<T> main;
    final public TS_ThreadStructRunnableTimedType1<T> fin;
    final public Optional<Duration> durPeriodCycle;
    final public Optional<TGS_ValidatorType1<T>> valCycleMain;
    final public AtomicReference<T> initObject = new AtomicReference(null);

    @Override
    public String toString() {
        return TS_ThreadStruct.class.getSimpleName() + "{" + "name=" + name + ", init=" + init + ", main=" + main + ", fin=" + fin + ", durPeriodCycle=" + durPeriodCycle + ", valCycleMain=" + valCycleMain + ", killTriggered=" + killTriggered + ", dead=" + dead + ", started=" + started + '}';
    }

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

    public boolean hasError() {
        return !exceptions.isEmpty();
    }
    public List<Throwable> exceptions = TGS_ListUtils.of();

    public TS_ThreadStruct<T> asyncRun() {
        if (isStarted()) {
            return this;
        }
        started.set(true);
        TS_ThreadAsync.now(() -> {
            d.ci(name, "#init");
            if (init.call.isPresent()) {
                d.ci(name, "#init.call.isPresent()");
                if (init.max.isPresent()) {
                    d.ci(name, "#init.max.isPresent()");
                    var await = TS_ThreadAsyncAwait.runUntil(init.max.get(), () -> initObject.set(init.call.get().call()));
                    if (await.hasError()) {
                        exceptions.addAll(await.exceptions);
                        d.ce(name, exceptions);
                        dead.set(true);
                        return;
                    } else {
                        d.ci(name, "#init.await.!hasError()");
                    }
                } else {
                    d.ci(name, "#init.max.!isPresent()");
                    initObject.set(init.call.get().call());
                }
            }
            d.ci(name, "#main");
            if (main.run.isPresent()) {
                d.ci(name, "#main.run.isPresent()");
                while (true) {
                    var msBegin = System.currentTimeMillis();
                    if (isKillTriggered()) {
                        break;
                    }
                    if (valCycleMain.isPresent()) {
                        d.ci(name, "#main.valCycleMain.isPresent()");
                        if (!valCycleMain.get().validate(initObject.get())) {
                            d.ci(name, "#main.!valCycleMain.get().validate(initObject.get())");
                            break;
                        }
                    }
                    if (main.max.isPresent()) {
                        var await = TS_ThreadAsyncAwait.runUntil(main.max.get(), () -> main.run.get().run(killTriggered, initObject.get()));
                        if (await.hasError()) {
                            exceptions.addAll(await.exceptions);
                            dead.set(true);
                            return;
                        }
                    } else {
                        main.run.get().run(killTriggered, initObject.get());
                    }
                    if (!durPeriodCycle.isPresent() && !valCycleMain.isPresent()) {
                        d.ci(name, "#main.!durPeriodCycle.isPresent() && !valCycleMain.isPresent()");
                        break;
                    }
                    if (durPeriodCycle.isPresent()) {
                        var msLoop = durPeriodCycle.get().toMillis();
                        var msEnd = System.currentTimeMillis();
                        var msSleep = msLoop - (msEnd - msBegin);
                        if (msSleep > 0) {
                            TGS_UnSafe.run(() -> Thread.sleep(msSleep));
                        }
                        Thread.yield();
                    }
                }
            }
            d.ci(name, "#fin");
            if (fin.run.isPresent()) {
                d.ci(name, "#fin.run.isPresent()");
                if (fin.max.isPresent()) {
                    d.ci(name, "#fin.max.isPresent()");
                    var await = TS_ThreadAsyncAwait.runUntil(fin.max.get(), () -> fin.run.get().run(initObject.get()));
                    if (await.hasError()) {
                        exceptions.addAll(await.exceptions);
                        dead.set(true);
                        return;
                    }
                } else {
                    d.ci(name, "fin.max.!isPresent()");
                    fin.run.get().run(initObject.get());
                }
            }
            d.ci(name, "#dead");
            dead.set(true);
        });
        return this;
    }

    public static <T> TS_ThreadStruct of(String name,
            TS_ThreadStructCallableTimed<T> init, TS_ThreadStructRunnableTimedType2<T> main, TS_ThreadStructRunnableTimedType1<T> fin,
            Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return new TS_ThreadStruct(name, init, main, fin, valCycleMain, durPeriodCycle);
    }
}
