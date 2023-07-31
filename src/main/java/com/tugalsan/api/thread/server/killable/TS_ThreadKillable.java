package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.thread.server.async.TS_ThreadAsync;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TS_ThreadKillable<T> {

    private TS_ThreadKillable(String name,
            TS_ThreadKillableCallableTimed<T> init, TS_ThreadKillableRunnableTimedType2<T> main, TS_ThreadKillableRunnableTimedType1<T> fin,
            Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        this.name = name;
        this.init = init;
        this.main = main;
        this.fin = fin;
        this.valCycleMain = valCycleMain;
        this.durPeriodCycle = durPeriodCycle;
    }
    final public String name;

    final public TS_ThreadKillableCallableTimed<T> init;
    final public TS_ThreadKillableRunnableTimedType2<T> main;
    final public TS_ThreadKillableRunnableTimedType1<T> fin;
    final public Optional<Duration> durPeriodCycle;
    final public Optional<TGS_ValidatorType1<T>> valCycleMain;
    final public AtomicReference<T> initObject = new AtomicReference(null);

    @Override
    public String toString() {
        return TS_ThreadKillable.class.getSimpleName() + "{" + "name=" + name + ", init=" + init + ", main=" + main + ", fin=" + fin + ", durPeriodCycle=" + durPeriodCycle + ", valCycleMain=" + valCycleMain + ", killTriggered=" + killTriggered + ", dead=" + dead + ", started=" + started + '}';
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

    public TS_ThreadKillable<T> start() {
        if (isStarted()) {
            return this;
        }
        started.set(true);
        TS_ThreadAsync.now(() -> {
            if (init.call.isPresent()) {
                if (init.max.isPresent()) {
                    var await = TS_ThreadAsyncAwait.runUntil(init.max.get(), () -> initObject.set(init.call.get().call()));
                    if (await.hasError()) {
                        exceptions.addAll(await.exceptions);
                        dead.set(true);
                        return;
                    }
                } else {
                    initObject.set(init.call.get().call());
                }
            }
            if (main.run.isPresent()) {
                while (true) {
                    var msBegin = System.currentTimeMillis();
                    if (isKillTriggered()) {
                        break;
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
                        break;
                    }
                    if (valCycleMain.isPresent()) {
                        if (valCycleMain.get().validate(initObject.get())) {
                            break;
                        }
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
            if (fin.run.isPresent()) {
                if (fin.max.isPresent()) {
                    var await = TS_ThreadAsyncAwait.runUntil(fin.max.get(), () -> fin.run.get().run(initObject.get()));
                    if (await.hasError()) {
                        exceptions.addAll(await.exceptions);
                        dead.set(true);
                        return;
                    }
                } else {
                    fin.run.get().run(initObject.get());
                }
            }
            dead.set(true);
        });
        return this;
    }

    public static <T> TS_ThreadKillable of(String name,
            TS_ThreadKillableCallableTimed<T> init, TS_ThreadKillableRunnableTimedType2<T> main, TS_ThreadKillableRunnableTimedType1<T> fin,
            Optional<TGS_ValidatorType1<T>> valCycleMain, Optional<Duration> durPeriodCycle) {
        return new TS_ThreadKillable(name, init, main, fin, valCycleMain, durPeriodCycle);
    }
}
