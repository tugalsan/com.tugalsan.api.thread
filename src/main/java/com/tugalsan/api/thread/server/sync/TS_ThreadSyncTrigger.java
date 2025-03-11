package com.tugalsan.api.thread.server.sync;

import com.tugalsan.api.log.server.TS_Log;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TS_ThreadSyncTrigger {

    final public static TS_Log d = new TS_Log(TS_ThreadSyncTrigger.class);

    private TS_ThreadSyncTrigger(String name) {
        this.name = name;
    }
    final public String name;

    public static TS_ThreadSyncTrigger of(String name, TS_ThreadSyncTrigger... parents) {
        if (parents == null || parents.length == 0) {
            return new TS_ThreadSyncTrigger(name);
        }
        var parents_name = Arrays.stream(parents).map(p -> p.name).collect(Collectors.joining("|"));
        var t = new TS_ThreadSyncTrigger(parents_name + ">" + name);
        Arrays.stream(parents).forEach(p -> t.parents.add(p));
        return t;
    }
    final public TS_ThreadSyncLst<TS_ThreadSyncTrigger> parents = TS_ThreadSyncLst.ofSlowWrite();
    final private AtomicBoolean value = new AtomicBoolean(false);

    @Deprecated //USE NULL SAFE ofParent
    public TS_ThreadSyncTrigger newChild(String name) {
        return TS_ThreadSyncTrigger.of(name, this);
    }

    public void trigger(String reason) {
        d.ci("trigger", name, reason);
        value.set(true);
    }

    public boolean hasTriggered() {
        var result = false;
        if (value.get()) {
            result = true;
        } else {
            result = parents.stream().anyMatch(t -> t.hasTriggered());
        }
        d.ci("hasTriggered", name, result);
        return result;
    }

    public boolean hasNotTriggered() {
        var result = false;
        if (value.get()) {
            result = false;
        } else {
            result = parents.stream().allMatch(t -> t.hasNotTriggered());
        }
        d.ci("hasNotTriggered", name, result);
        return result;
    }
}
