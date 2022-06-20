package com.tugalsan.api.thread.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.*;
import com.tugalsan.api.executable.client.*;

public class TGC_ThreadUtils {

    public static class TGC_Thread {

        final private Timer t;

        public TGC_Thread(Timer t) {
            this.t = t;
        }

        public void execute_afterSeconds(float seconds) {
            t.schedule((int) (seconds * 1000f));
        }

        public void execute_everySeconds(float seconds) {
            t.scheduleRepeating((int) (seconds * 1000f));
        }
    }

    public static void execute_afterGUIUpdate(TGS_Executable exe) {
        Scheduler.get().scheduleDeferred(() -> exe.execute());
    }

    public static TGC_Thread create_afterGUIUpdate(TGS_ExecutableType1<TGC_Thread> exe) {
        return new TGC_Thread(new Timer() {
            @Override
            public void run() {
                execute_afterGUIUpdate(() -> exe.execute(new TGC_Thread(this)));
            }
        });
    }

    public static TGC_Thread create(TGS_ExecutableType1<TGC_Thread> exe) {
        return new TGC_Thread(new Timer() {
            @Override
            public void run() {
                exe.execute(new TGC_Thread(this));
            }
        });
    }

    public static TGC_Thread execute_afterSeconds(TGS_ExecutableType1<TGC_Thread> exe, float seconds) {
        var t = create(exe);
        t.execute_afterSeconds(seconds);
        return t;
    }

    public static TGC_Thread execute_afterSeconds_afterGUIUpdate(TGS_ExecutableType1<TGC_Thread> exe, float seconds) {
        var t = create_afterGUIUpdate(exe);
        t.execute_afterSeconds(seconds);
        return t;
    }

    public static TGC_Thread execute_everySeconds(TGS_ExecutableType1<TGC_Thread> exe, float seconds) {
        var t = create(exe);
        t.execute_everySeconds(seconds);
        return t;
    }

    public static TGC_Thread execute_everySeconds_afterGUIUpdate(TGS_ExecutableType1<TGC_Thread> exe, float seconds) {
        var t = create_afterGUIUpdate(exe);
        t.execute_everySeconds(seconds);
        return t;
    }
}
