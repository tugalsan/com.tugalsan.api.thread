package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.builder_core.TS_ThreadStructBuilder0Kill;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadStructBuilder {

    public static TS_ThreadStructBuilder0Kill of() {
        return new TS_ThreadStructBuilder0Kill(new AtomicBoolean(false));
    }

    public static TS_ThreadStructBuilder0Kill of(AtomicBoolean killTrigger) {
        return new TS_ThreadStructBuilder0Kill(killTrigger);
    }
}
