package com.tugalsan.api.thread.server.killable;

import com.tugalsan.api.thread.server.safe.TS_ThreadSafeLst;
import java.util.Objects;

public class TS_ThreadKillablePool {

    public TS_ThreadSafeLst<TS_ThreadKillable> pool = new TS_ThreadSafeLst();

    public TS_ThreadKillable get(TS_ThreadKillable tk) {
        return get(tk.name);
    }

    public TS_ThreadKillable get(String name) {
        return pool.findFirst(itm -> Objects.equals(name, itm.name));
    }

    public TS_ThreadKillablePool add(TS_ThreadKillable tk) {
        pool.add(tk);
        return this;
    }

    public TS_ThreadKillablePool remove(String name) {
        pool.removeAll(itm -> Objects.equals(name, itm.name));
        return this;
    }

    public TS_ThreadKillablePool remove(TS_ThreadKillable tk) {
        return remove(tk.name);
    }

    public TS_ThreadKillablePool clearDead() {
        pool.removeAll(itm -> itm.isDead());
        return this;
    }
}
