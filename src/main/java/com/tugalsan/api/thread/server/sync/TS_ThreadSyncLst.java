package com.tugalsan.api.thread.server.sync;


import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.function.client.TGS_Func_OutBool_In1;
import com.tugalsan.api.list.client.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class TS_ThreadSyncLst<T> {

    private TS_ThreadSyncLst() {

    }
    private final ConcurrentLinkedQueue<T> list = new ConcurrentLinkedQueue();

    public static <T> TS_ThreadSyncLst<T> of() {
        return new TS_ThreadSyncLst();
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public List<T> toList() {
        List<T> o = TGS_ListUtils.of();
        forEach(item -> o.add(item));
        return o;
    }

//    public List<T> toListUnmodifiable() {//GWT does not like u; check on 2.10 version again!
//        return Collections.unmodifiableList(toListLinked());
//    }
    public TS_ThreadSyncLst<T> forEach(TGS_Func_In1<T> item) {
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            item.run(iterator.next());
        }
        return this;
    }

    public TS_ThreadSyncLst<T> forEach(TGS_Func_OutBool_In1<T> condition, TGS_Func_In1<T> item) {
        return forEach(nextItem -> {
            if (condition.validate(nextItem)) {
                item.run(nextItem);
            }
        });
    }

    public TS_ThreadSyncLst<T> clear() {
        list.clear();
        return this;
    }

    public int size() {
        return list.size();
    }

    public int count(TGS_Func_OutBool_In1<T> condition) {
        var count = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                count++;
            }
        }
        return count;
    }

    public boolean isEmpty(TGS_Func_OutBool_In1<T> condition) {
        return count(condition) == 0;
    }

    public boolean isPresent(TGS_Func_OutBool_In1<T> condition) {
        return !isEmpty(condition);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isPresent() {
        return !isEmpty();
    }

    public T add(T item) {
        list.add(item);
        return item;
    }

    public List<T> add(List<T> items) {
        items.forEach(item -> add(item));
        return items;
    }

    public void cropToLength_byRemovingFirstItems(int len) {
        if (len < 1) {
            clear();
            return;
        }
        var count = count(val -> true);
        while (count > len) {
            removeFirst(val -> true);//NO WORRY REMOVE IS SAFE :)
            count--;
        }
    }

    public void cropToLength_byRemovingLastItems(int len) {
        if (len < 1) {
            clear();
            return;
        }
        var count = count(val -> true);
        while (count > len) {
            removeLast(val -> true);//NO WORRY REMOVE IS SAFE :)
            count--;
        }
    }

    public T[] add(T[] items) {
        Arrays.stream(items).forEach(item -> add(item));
        return items;
    }

    public List<T> set(List<T> items) {
        list.clear();
        return add(items);
    }

    public T[] set(T[] items) {
        list.clear();
        return add(items);
    }

    public boolean contains(T item) {
        return findFirst(o -> Objects.equals(o, item)) != null;
    }

    public T findFirst(TGS_Func_OutBool_In1<T> condition) {
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                return item;
            }
        }
        return null;
    }

    public T popFirst() {
        return list.poll();
    }

    public T popFirst(TGS_Func_OutBool_In1<T> condition) {
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return item;
            }
        }
        //USE THREAD SAFE ITERATOR!!!
        return null;
    }

    public int idxFirst(TGS_Func_OutBool_In1<T> condition) {
        var idx = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    public List<T> findAll(TGS_Func_OutBool_In1<T> condition) {
        List<T> foundItems = TGS_ListUtils.of();
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    public List<Integer> idxAll(TGS_Func_OutBool_In1<T> condition) {
        List<Integer> foundItems = TGS_ListUtils.of();
        var idx = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(idx);
            }
            idx++;
        }
        return foundItems;
    }

    public boolean removeFirst(T item) {
        return removeFirst(o -> Objects.equals(o, item));
    }

    public boolean removeAll(T item) {
        return removeAll(o -> Objects.equals(o, item));
    }

    public boolean removeAll(TGS_Func_OutBool_In1<T> condition) {
        var result = false;
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                result = true;
            }
        }
        return result;
    }

    public boolean removeFirst(TGS_Func_OutBool_In1<T> condition) {
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public T popLast(TGS_Func_OutBool_In1<T> condition) {
        var lastIdx = -1;
        var i = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                lastIdx = i;
            }
            i++;
        }
        if (lastIdx == -1) {
            return null;
        }
        i = 0;
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (i == lastIdx && condition.validate(item)) {
                iterator.remove();
                return item;
            }
            if (i > lastIdx) {
                return null;
            }
        }
        return null;
    }

    public T findLast(TGS_Func_OutBool_In1<T> condition) {
        var lastIdx = -1;
        var i = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                lastIdx = i;
            }
            i++;
        }
        if (lastIdx == -1) {
            return null;
        }
        i = 0;
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (i == lastIdx && condition.validate(item)) {
                return item;
            }
            if (i > lastIdx) {
                return null;
            }
        }
        return null;
    }

    public boolean removeLast(TGS_Func_OutBool_In1<T> condition) {
        var lastIdx = -1;
        var i = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                lastIdx = i;
            }
            i++;
        }
        if (lastIdx == -1) {
            return false;
        }
        i = 0;
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (i == lastIdx && condition.validate(item)) {
                iterator.remove();
                return true;
            }
            if (i > lastIdx) {
                return false;
            }
        }
        return false;
    }
}
