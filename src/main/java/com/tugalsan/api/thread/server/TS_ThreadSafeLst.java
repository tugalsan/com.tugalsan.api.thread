package com.tugalsan.api.thread.server;

import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.validator.client.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class TS_ThreadSafeLst<T> {

    private final ConcurrentLinkedQueue<T> list = new ConcurrentLinkedQueue();

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
    public TS_ThreadSafeLst<T> forEach(TGS_RunnableType1<T> item) {
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            item.run(iterator.next());
        }
        return this;
    }

    public TS_ThreadSafeLst<T> forEach(TGS_ValidatorType1<T> condition, TGS_RunnableType1<T> item) {
        return forEach(nextItem -> {
            if (condition.validate(nextItem)) {
                item.run(nextItem);
            }
        });
    }

    public TS_ThreadSafeLst<T> clear() {
        list.clear();
        return this;
    }

    public int size() {
        return list.size();
    }

    public int count(TGS_ValidatorType1<T> condition) {
        var count = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                count++;
            }
        }
//      USE ITERATOR!!!
//      return list.stream()
//        .filter(item -> (condition.validate(item)))
//        .count();
        return count;
    }

    public boolean isEmpty(TGS_ValidatorType1<T> condition) {
        return count(condition) == 0;
    }

    public boolean isPresent(TGS_ValidatorType1<T> condition) {
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

    public void cropToLength_byRemovingFirstItems_fast(int len) {
        if (len < 1) {
            clear();
            return;
        }
        var count = count(val -> true);
        while (count > len) {
            removeFirst(val -> true);
            count--;
        }
    }

    public void cropToLength_byRemovingFirstItems(int len) {
        if (len < 1) {
            clear();
            return;
        }
        while (count(val -> true) > len) {
            removeFirst(val -> true);
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

    public T findFirst(TGS_ValidatorType1<T> condition) {
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                return item;
            }
        }
        //USE THREAD SAFE ITERATOR!!!
        return null;
    }

    public int idxFirst(TGS_ValidatorType1<T> condition) {
        var idx = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                return idx;
            }
            idx++;
        }
        //USE THREAD SAFE ITERATOR!!!
        return -1;
    }

    public List<T> findAll(TGS_ValidatorType1<T> condition) {
        List<T> foundItems = TGS_ListUtils.of();
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(item);
            }
        }
        //USE THREAD SAFE ITERATOR!!!
        return foundItems;
    }

    public List<Integer> idxAll(TGS_ValidatorType1<T> condition) {
        List<Integer> foundItems = TGS_ListUtils.of();
        var idx = 0;
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(idx);
            }
            idx++;
        }
        //USE THREAD SAFE ITERATOR!!!
        return foundItems;
    }

    public boolean removeFirst(T item) {
        return removeFirst(o -> Objects.equals(o, item));
    }

    public boolean removeAll(T item) {
        return removeAll(o -> Objects.equals(o, item));
    }

    public boolean removeAll(TGS_ValidatorType1<T> condition) {
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

    public boolean removeFirst(TGS_ValidatorType1<T> condition) {
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
}
