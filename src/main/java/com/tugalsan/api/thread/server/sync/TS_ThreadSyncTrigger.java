package com.tugalsan.api.thread.server.sync;

import com.tugalsan.api.log.server.TS_Log;
import java.util.concurrent.atomic.AtomicBoolean;

public class TS_ThreadSyncTrigger {

    final private static TS_Log d = new TS_Log(true, TS_ThreadSyncTrigger.class);

    private TS_ThreadSyncTrigger(String name) {
        this.name = name;
    }
    final public String name;

    public static TS_ThreadSyncTrigger ofParent(TS_ThreadSyncTrigger parent, String name) {
        if (parent == null) {
            return new TS_ThreadSyncTrigger(name);
        }
        var t = new TS_ThreadSyncTrigger(name);
        t.parents.add(parent);
        return t;
    }

    public static TS_ThreadSyncTrigger of(String name) {
        return new TS_ThreadSyncTrigger(name);
    }
    final public TS_ThreadSyncLst<TS_ThreadSyncTrigger> parents = TS_ThreadSyncLst.ofSlowWrite();
    final private AtomicBoolean value = new AtomicBoolean(false);

    @Deprecated //USE NULL SAFE ofParent
    public TS_ThreadSyncTrigger newChild(String name) {
        return TS_ThreadSyncTrigger.ofParent(this, name);
    }

    public void trigger() {
        d.ci("trigger", name);
        value.set(true);
    }

    public boolean hasTriggered() {
        d.ci("hasTriggered", name);
        if (value.get()) {
            return true;
        }
        return parents.stream().anyMatch(t -> t.hasTriggered());
    }

    public boolean hasNotTriggered() {
        d.ci("hasNotTriggered", name);
        if (value.get()) {
            return false;
        }
        return parents.stream().allMatch(t -> t.hasNotTriggered());
    }
}
