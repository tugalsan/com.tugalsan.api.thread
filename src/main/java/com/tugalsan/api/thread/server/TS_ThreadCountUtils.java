package com.tugalsan.api.thread.server;

import com.tugalsan.api.os.server.*;

@Deprecated //JUST USE VIRTUAL THREAD
public class TS_ThreadCountUtils {

    @Deprecated //JUST USE VIRTUAL THREAD
    public static int max() {
        return TS_ProcessorUtils.count() * 2;
    }
}
