package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.validator.client.TGS_ValidatorType1;
import java.time.Duration;
import java.util.Optional;

public class TS_ThreadKillableBuilder1Init<T> {

    protected TS_ThreadKillableBuilder1Init(String name, TS_ThreadKillableCallableTimed<T> init) {
        this.name = name;
        this.init = init;
    }
    final private String name;
    final private TS_ThreadKillableCallableTimed<T> init;

    public <T> TS_ThreadKillableBuilder2Main<T> main(TS_ThreadKillableRunnableTimedType2<T> main) {
        return new TS_ThreadKillableBuilder2Main(name, init, main);
    }
}
