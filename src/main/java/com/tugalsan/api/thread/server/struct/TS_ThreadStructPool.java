package com.tugalsan.api.thread.server.struct;

import com.tugalsan.api.thread.server.struct.builder_core.TS_ThreadStruct;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeLst;
import java.util.Objects;

@Deprecated//DO YOU REALLY NEED IT
public class TS_ThreadStructPool {

    public TS_ThreadSafeLst<TS_ThreadStruct> pool = new TS_ThreadSafeLst();

    public TS_ThreadStruct get(TS_ThreadStruct tk) {
        return get(tk.name);
    }

    public TS_ThreadStruct get(String name) {
        return pool.findFirst(itm -> Objects.equals(name, itm.name));
    }

    public TS_ThreadStructPool add(TS_ThreadStruct tk) {
        pool.add(tk);
        return this;
    }

    public TS_ThreadStructPool remove(String name) {
        pool.removeAll(itm -> Objects.equals(name, itm.name));
        return this;
    }

    public TS_ThreadStructPool remove(TS_ThreadStruct tk) {
        return remove(tk.name);
    }

    public TS_ThreadStructPool clearDead() {
        pool.removeAll(itm -> itm.isDead());
        return this;
    }

    public void asyncRunAll() {
        pool.forEach(itm -> itm.asyncRun());
    }

    public void killAll() {
        pool.forEach(itm -> itm.kill());
    }
}
