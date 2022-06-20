package com.tugalsan.api.thread.server;

import com.tugalsan.api.os.server.*;

public class TS_ThreadCountUtils {

    public static int max() {
        return TS_ProcessorUtils.count() * 2;
    }
}
