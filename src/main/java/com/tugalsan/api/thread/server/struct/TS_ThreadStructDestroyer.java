package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.async.TS_ThreadAsyncScheduled;

public class TS_ThreadStructDestroyer {

    public static void destroy() {
        TS_ThreadAsyncScheduled.destroy();;
    }
}
