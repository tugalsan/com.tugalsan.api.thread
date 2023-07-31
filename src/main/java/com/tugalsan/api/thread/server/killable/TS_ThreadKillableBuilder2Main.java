package com.tugalsan.api.thread.server.killable;

public class TS_ThreadKillableBuilder2Main<T> {

    protected TS_ThreadKillableBuilder2Main(String name,
            TS_ThreadKillableCallableTimed<T> init, TS_ThreadKillableRunnableTimedType2<T> main) {
        this.name = name;
        this.init = init;
        this.main = main;
    }
    final private String name;
    final private TS_ThreadKillableCallableTimed<T> init;
    final private TS_ThreadKillableRunnableTimedType2<T> main;

    public <T> TS_ThreadKillableBuilder3Fin<T> fin(TS_ThreadKillableRunnableTimedType1<T> fin) {
        return new TS_ThreadKillableBuilder3Fin(name, init, main, fin);
    }
}
