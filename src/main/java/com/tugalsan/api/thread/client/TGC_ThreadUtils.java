package com.tugalsan.api.thread.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.*;
import com.tugalsan.api.function.client.TGS_Func;
import com.tugalsan.api.function.client.TGS_Func_In1;


public class TGC_ThreadUtils {

    public static class TGC_Thread {

        final private Timer t;

        public TGC_Thread(Timer t) {
            this.t = t;
        }

        public void run_afterSeconds(float seconds) {
            t.schedule((int) (seconds * 1000f));
        }

        public void run_everySeconds(float seconds) {
            t.scheduleRepeating((int) (seconds * 1000f));
        }
    }

    public static void run_afterGUIUpdate(TGS_Func exe) {
        Scheduler.get().scheduleDeferred(() -> exe.run());
    }

    public static TGC_Thread create_afterGUIUpdate(TGS_Func_In1<TGC_Thread> exe) {
        return new TGC_Thread(new Timer() {
            @Override
            public void run() {
                run_afterGUIUpdate(() -> exe.run(new TGC_Thread(this)));
            }
        });
    }

    public static TGC_Thread create(TGS_Func_In1<TGC_Thread> exe) {
        return new TGC_Thread(new Timer() {
            @Override
            public void run() {
                exe.run(new TGC_Thread(this));
            }
        });
    }

    public static TGC_Thread run_afterSeconds(TGS_Func_In1<TGC_Thread> exe, float seconds) {
        var t = create(exe);
        t.run_afterSeconds(seconds);
        return t;
    }

    public static TGC_Thread run_afterSeconds_afterGUIUpdate(TGS_Func_In1<TGC_Thread> exe, float seconds) {
        var t = create_afterGUIUpdate(exe);
        t.run_afterSeconds(seconds);
        return t;
    }

    public static TGC_Thread run_everySeconds(TGS_Func_In1<TGC_Thread> exe, float seconds) {
        var t = create(exe);
        t.run_everySeconds(seconds);
        return t;
    }

    public static TGC_Thread run_everySeconds_afterGUIUpdate(TGS_Func_In1<TGC_Thread> exe, float seconds) {
        var t = create_afterGUIUpdate(exe);
        t.run_everySeconds(seconds);
        return t;
    }
}
