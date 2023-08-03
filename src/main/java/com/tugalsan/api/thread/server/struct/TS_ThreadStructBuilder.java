package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import com.tugalsan.api.thread.server.struct.core.TS_ThreadStructBuilder0Kill;

public class TS_ThreadStructBuilder {

    public static TS_ThreadStructBuilder0Kill of() {
        return new TS_ThreadStructBuilder0Kill(TS_ThreadSafeTrigger.of());
    }

    public static TS_ThreadStructBuilder0Kill of(TS_ThreadSafeTrigger killTrigger) {
        return new TS_ThreadStructBuilder0Kill(killTrigger);
    }
}
