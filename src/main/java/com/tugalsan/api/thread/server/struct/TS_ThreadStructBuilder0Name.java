package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.TS_ThreadStructCallableTimed;
import com.tugalsan.api.callable.client.TGS_Callable;
import java.time.Duration;

public class TS_ThreadStructBuilder0Name {

    protected TS_ThreadStructBuilder0Name(String name) {
        this.name = name;
    }
    final private String name;

    public TS_ThreadStructBuilder1Init<Object> initEmpty() {
        return new TS_ThreadStructBuilder1Init(name, TS_ThreadStructCallableTimed.of());
    }

    public <T> TS_ThreadStructBuilder1Init<T> init(TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder1Init(name, TS_ThreadStructCallableTimed.of(call));
    }

    public <T> TS_ThreadStructBuilder1Init<T> initTimed(Duration max, TGS_Callable<T> call) {
        return new TS_ThreadStructBuilder1Init(name, TS_ThreadStructCallableTimed.of(max, call));
    }

}
