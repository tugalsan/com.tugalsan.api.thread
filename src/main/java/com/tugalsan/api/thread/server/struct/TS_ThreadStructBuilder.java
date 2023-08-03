package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.TS_ThreadKillTrigger;
import com.tugalsan.api.thread.server.struct.core.TS_ThreadStructBuilder0Kill;

public class TS_ThreadStructBuilder {

    public static TS_ThreadStructBuilder0Kill of() {
        return new TS_ThreadStructBuilder0Kill(TS_ThreadKillTrigger.of());
    }

    public static TS_ThreadStructBuilder0Kill of(TS_ThreadKillTrigger killTrigger) {
        return new TS_ThreadStructBuilder0Kill(killTrigger);
    }
}
