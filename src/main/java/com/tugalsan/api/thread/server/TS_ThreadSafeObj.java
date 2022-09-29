package com.tugalsan.api.thread.server;

import com.tugalsan.api.executable.client.TGS_Executable;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.concurrent.locks.ReentrantLock;

public class TS_ThreadSafeObj<A> {

    final private ReentrantLock lock = new ReentrantLock();

    public void onSafeState(TGS_Executable exe) {
        TGS_UnSafe.execute(() -> {
            lock.lock();
            exe.execute();
        }, ex -> TGS_UnSafe.catchMeIfUCan(ex), () -> lock.unlock());
    }

    private TS_ThreadSafeObj() {

    }

    private TS_ThreadSafeObj(A value0) {
        this.value0 = value0;
    }
    public volatile A value0;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value0 + "]";
    }

    public boolean isEmpty() {
        return value0 == null;
    }

    public boolean isPresent() {
        return !isEmpty();
    }

    public static <A> TS_ThreadSafeObj<A> of(A value0) {
        return new TS_ThreadSafeObj(value0);
    }

    public static <String> TS_ThreadSafeObj<String> ofStr() {
        return new TS_ThreadSafeObj();
    }

    public static <Boolean> TS_ThreadSafeObj<Boolean> ofBool() {
        return new TS_ThreadSafeObj();
    }

    public static <Integer> TS_ThreadSafeObj<Integer> ofInt() {
        return new TS_ThreadSafeObj();
    }

    public static <Long> TS_ThreadSafeObj<Long> ofLng() {
        return new TS_ThreadSafeObj();
    }

    public static <Double> TS_ThreadSafeObj<Double> ofDbl() {
        return new TS_ThreadSafeObj();
    }
}
