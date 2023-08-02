package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.async.TS_ThreadAsync;

public class TS_ThreadStructDestroyer {

    public static void destroy() {
        TS_ThreadAsync.destroy();;
    }
}
