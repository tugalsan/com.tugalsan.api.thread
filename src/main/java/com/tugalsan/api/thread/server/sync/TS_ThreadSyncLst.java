package com.tugalsan.api.thread.server.sync;

import module com.tugalsan.api.function;
import module com.tugalsan.api.list;
import module com.tugalsan.api.stream;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class TS_ThreadSyncLst<T> {

//    final static private TS_Log d = TS_Log.of(TS_ThreadSyncLst.class);
    private TS_ThreadSyncLst(boolean slowWrite) {
        strategyIsSlowWrite = slowWrite;
        if (strategyIsSlowWrite) {
            listSlowRead = null;
            listSlowWrite = new CopyOnWriteArrayList();
        } else {
            listSlowRead = new ConcurrentLinkedQueue();
            listSlowWrite = null;
        }
    }
    private final ConcurrentLinkedQueue<T> listSlowRead;
    private final CopyOnWriteArrayList<T> listSlowWrite;
    public final boolean strategyIsSlowWrite;

    public static <T> TS_ThreadSyncLst<T> ofSlowRead() {
        return new TS_ThreadSyncLst(false);
    }

    public static <T> TS_ThreadSyncLst<T> ofSlowWrite() {
        return new TS_ThreadSyncLst(true);
    }

    public Optional<CopyOnWriteArrayList<T>> getDriver_slowWrite() {
        return strategyIsSlowWrite ? Optional.of(listSlowWrite) : Optional.empty();
    }

    public Optional<ConcurrentLinkedQueue<T>> getDriver_slowRead() {
        return strategyIsSlowWrite ? Optional.empty() : Optional.of(listSlowRead);
    }

//---------------------------------  TO LIST -----------------------------------
    @Deprecated //NOT GWT DTO SAFE
    public List<T> toList_fast() {
        if (strategyIsSlowWrite) {
            return listSlowWrite.stream().toList();
        }
        List<T> o = TGS_ListUtils.of();
        forEach(false, item -> o.add(item));
        return o;
    }

    public List<T> toList_modifiable() {
        if (strategyIsSlowWrite) {
            return TGS_StreamUtils.toLst(listSlowWrite.stream());
        }
        List<T> o = TGS_ListUtils.of();
        forEach(false, item -> o.add(item));
        return o;
    }

    @Deprecated //NOT GWT DTO SAFE
    public List<T> toList_unmodifiable() {
        if (strategyIsSlowWrite) {
            return listSlowWrite.stream().toList();
        }
        List<T> o = TGS_ListUtils.of();
        forEach(false, item -> o.add(item));
        return Collections.unmodifiableList(o);
    }

//---------------------------------  STREAM  -----------------------------------    
    public Stream<T> stream() {
        if (strategyIsSlowWrite) {
            return listSlowWrite.stream();
        }
        return listSlowRead.stream();
    }

    public TS_ThreadSyncLst<T> forEach(boolean parallelIfPossible, TGS_FuncMTU_In1<T> item) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                listSlowWrite.stream().parallel()
                        .forEach(nextItem -> item.run(nextItem));
            } else {
                listSlowWrite
                        .forEach(nextItem -> item.run(nextItem));
            }
            return this;
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            item.run(iterator.next());
        }
        return this;
    }

    public TS_ThreadSyncLst<T> forEach(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition, TGS_FuncMTU_In1<T> item) {
        return forEach(parallelIfPossible, nextItem -> {
            if (condition.validate(nextItem)) {
                item.run(nextItem);
            }
        });
    }

//---------------------------------  COMMON -----------------------------------    
    public TS_ThreadSyncLst<T> clear() {
        if (strategyIsSlowWrite) {
            listSlowWrite.clear();
            return this;
        }
        listSlowRead.clear();
        return this;
    }

    public int size() {
        if (strategyIsSlowWrite) {
            return listSlowWrite.size();
        }
        return listSlowRead.size();
    }

    public long count(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return listSlowWrite.stream().parallel()
                        .filter(nextItem -> condition.validate(nextItem)).count();
            } else {
                return listSlowWrite.stream()
                        .filter(nextItem -> condition.validate(nextItem)).count();
            }
        }
        var count = 0L;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var nextItem = iterator.next();
            if (condition.validate(nextItem)) {
                count++;
            }
        }
        return count;
    }

    public boolean isEmpty(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        return count(parallelIfPossible, condition) == 0;
    }

    public boolean isPresent(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        return !isEmpty(parallelIfPossible, condition);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isPresent() {
        return !isEmpty();
    }

    public T add(T item) {
        if (item == null) {
            return null;
        }
        if (strategyIsSlowWrite) {
            listSlowWrite.add(item);
            return item;
        }
        listSlowRead.add(item);
        return item;
    }

    public List<T> add(List<T> items) {
        var _items = items.stream().filter(itemNext -> itemNext != null).toList();
        if (strategyIsSlowWrite) {
            listSlowWrite.addAll(_items);
            return _items;
        }
        listSlowRead.addAll(_items);
        return _items;
    }

    public T[] add(T[] items) {
        add(Arrays.stream(items).toList());
        return items;
    }

    public List<T> set(List<T> items) {
        clear();
        return add(items);
    }

    public T[] set(T[] items) {
        clear();
        return add(items);
    }

    public boolean set(int idx, T newItem) {
        if (newItem == null) {
            return false;
        }
        if (idx < 0) {
            return false;
        }
        if (idx >= size()) {
            return false;
        }
        if (strategyIsSlowWrite) {
            try {
                listSlowWrite.set(idx, newItem);
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }
        var offset = 0;
        var iterator = listSlowRead.iterator();
        LinkedList<T> tmp = new LinkedList<>();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (offset == idx) {
                tmp.add(newItem);
                iterator.remove();
            }
            if (offset > idx) {
                tmp.add(item);
                iterator.remove();
            }
            offset++;
        }
        listSlowRead.addAll(tmp);
        return true;
    }

//---------------------------------  GET -----------------------------------    
    public T get(int idx) {
        if (strategyIsSlowWrite) {
            try {
                return listSlowWrite.get(idx);
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        var offset = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (offset == idx) {
                return item;
            }
            offset++;
        }
        return null;
    }

    public T getFirst() {
        if (strategyIsSlowWrite) {
            try {
                return listSlowWrite.get(0);//getfirst: UNSUUPORTED OPERATION
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        return listSlowRead.peek();
    }

    public T getLast() {
        if (strategyIsSlowWrite) {
            try {
                return listSlowWrite.get(size() - 1);//getLast: UNSUUPORTED OPERATION
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        T lastItem = null;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {
            lastItem = iterator.next();
        }
        return lastItem;
    }

    //---------------------------------  find -----------------------------------    
    public T findFirst(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            return listSlowWrite.stream()
                    .filter(nextItem -> condition.validate(nextItem))
                    .findFirst().orElse(null);
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                return item;
            }
        }
        return null;
    }

    public T findLast(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            var reverseIterator = TGS_StreamReverseIterableFromList.of(listSlowWrite).iterator();
            while (reverseIterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = reverseIterator.next();
                if (condition.validate(item)) {
                    try {
                        return item;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                }
            }
            return null;
        }
        T lastValidItem = null;
        var offset = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var nextItem = iterator.next();
            if (condition.validate(nextItem)) {
                lastValidItem = nextItem;
            }
            offset++;
        }
        return lastValidItem;
    }

    public T findAny(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return listSlowWrite.stream().parallel()
                        .filter(nextItem -> condition.validate(nextItem))
                        .findAny().orElse(null);
            } else {
                return listSlowWrite.stream()
                        .filter(nextItem -> condition.validate(nextItem))
                        .findAny().orElse(null);
            }
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                return item;
            }
        }
        return null;
    }

    public List<T> findAll_modifiable(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return TGS_StreamUtils.toLst(listSlowWrite.stream().parallel().filter(nextItem -> condition.validate(nextItem)));
            } else {
                return TGS_StreamUtils.toLst(listSlowWrite.stream().filter(nextItem -> condition.validate(nextItem)));
            }
        }
        List<T> validItems = TGS_ListUtils.of();
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                validItems.add(item);
            }
        }
        return validItems;
    }

    public List<T> findAll_unmodifiable(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return listSlowWrite.stream().parallel().filter(nextItem -> condition.validate(nextItem)).toList();
            } else {
                return listSlowWrite.stream().filter(nextItem -> condition.validate(nextItem)).toList();
            }
        }
        List<T> validItems = TGS_ListUtils.of();
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                validItems.add(item);
            }
        }
        return Collections.unmodifiableList(validItems);
    }

    public List<T> findAll_fast(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return listSlowWrite.stream().parallel().filter(nextItem -> condition.validate(nextItem)).toList();
            } else {
                return listSlowWrite.stream().filter(nextItem -> condition.validate(nextItem)).toList();
            }
        }
        List<T> validItems = TGS_ListUtils.of();
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                validItems.add(item);
            }
        }
        return validItems;
    }
//--------------------------------- CONTAINS -----------------------------------    

    public boolean contains(boolean parallelIfPossible, T item) {
        return findAny(parallelIfPossible, o -> Objects.equals(o, item)) != null;
    }

//--------------------------------- ADVANCED REMOVE -----------------------------------    
    public T removeAndPopFirst() {
        if (strategyIsSlowWrite) {
            try {
                return listSlowWrite.remove(0);// listSlowWrite.removeFirst();//NOT SUPPORTED
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        return listSlowRead.poll();
    }

    public T removeAndPopLast() {
        if (strategyIsSlowWrite) {
            try {
                return listSlowWrite.remove(listSlowWrite.size() - 1);// listSlowWrite.removeLast();//NOT SUPPORTED
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (iterator.hasNext()) {
                continue;
            }
            iterator.remove();
            return item;
        }
        return null;
    }

    public T removeAndPopFirst(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            var idx = 0;
            var iterator = listSlowWrite.iterator();
            while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = iterator.next();
                if (condition.validate(item)) {
                    try {
                        return listSlowWrite.remove(idx);//iterator.remove();//UNSUPPORTED OP FOR listSlowWrite!!
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                }
                idx++;
            }
            return null;
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return item;
            }
        }
        return null;
    }

    public T removeAndPopFirst(T item) {
        return removeAndPopFirst(o -> Objects.equals(o, item));
    }

    public T removeAndPopLast(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            var idx = 0;
            var reverseIterator = TGS_StreamReverseIterableFromList.of(listSlowWrite).iterator();
            while (reverseIterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = reverseIterator.next();
                if (condition.validate(item)) {
                    try {
                        return listSlowWrite.remove(size() - 1 - idx);//reverseIterator.remove();// UNSUPPORTED OP!!!
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                }
                idx++;
            }
            return null;
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return item;
            }
        }
        return null;
    }

    public T removeAndPopLast(T item) {
        return removeAndPopLast(o -> Objects.equals(o, item));
    }

    public boolean removeAll(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            try {
                return listSlowWrite.removeIf(nextItem -> condition.validate(nextItem));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        var result = false;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                result = true;
            }
        }
        return result;
    }

    public boolean removeAll(T item) {
        return removeAll(o -> Objects.equals(o, item));
    }

    //-------------------- CROP ----------------------------
    public void cropToLength_byRemovingFirstItems(int len) {
        if (len < 1) {
            clear();
            return;
        }
        while (size() > len) {
            removeAndPopFirst();//NO WORRY REMOVE IS SAFE :)
        }
    }

    public void cropToLength_byRemovingLastItems(int len) {
        if (len < 1) {
            clear();
            return;
        }
        while (size() > len) {
            removeAndPopLast();//NO WORRY REMOVE IS SAFE :)
        }
    }

    //-------------------- NOT A GOOD IDEA -------------------------------------------
    @Deprecated //NOT A GOOD IDEA
    public int idxLast(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            var idx = 0;
            var reverseIterator = TGS_StreamReverseIterableFromList.of(listSlowWrite).iterator();
            while (reverseIterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = reverseIterator.next();
                if (condition.validate(item)) {
                    return idx;
                }
                idx++;
            }
            return -1;
        }
        var idx = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    @Deprecated //NOT A GOOD IDEA
    public int idxFirst(TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            return IntStream.range(0, listSlowWrite.size())
                    .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                    .findFirst().orElse(-1);
        }
        var idx = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    @Deprecated //NOT A GOOD IDEA
    public List<Integer> idxAll_unmodifiable(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return IntStream.range(0, listSlowWrite.size()).parallel()
                        .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                        .boxed().toList();
            } else {
                return IntStream.range(0, listSlowWrite.size())
                        .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                        .boxed().toList();
            }
        }
        List<Integer> foundItems = TGS_ListUtils.of();
        var idx = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(idx);
            }
            idx++;
        }
        return Collections.unmodifiableList(foundItems);
    }

    @Deprecated //NOT A GOOD IDEA
    public List<Integer> idxAll_modifiable(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return TGS_StreamUtils.toLst(IntStream.range(0, listSlowWrite.size()).parallel()
                        .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                        .boxed());
            } else {
                return TGS_StreamUtils.toLst(IntStream.range(0, listSlowWrite.size())
                        .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                        .boxed());
            }
        }
        List<Integer> foundItems = TGS_ListUtils.of();
        var idx = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(idx);
            }
            idx++;
        }
        return foundItems;
    }

    @Deprecated //NOT A GOOD IDEA
    public List<Integer> idxAll_fast(boolean parallelIfPossible, TGS_FuncMTU_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            if (parallelIfPossible) {
                return IntStream.range(0, listSlowWrite.size()).parallel()
                        .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                        .boxed().toList();
            } else {
                return IntStream.range(0, listSlowWrite.size())
                        .filter(nextItem -> condition.validate(listSlowWrite.get(nextItem)))
                        .boxed().toList();
            }
        }
        List<Integer> foundItems = TGS_ListUtils.of();
        var idx = 0;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                foundItems.add(idx);
            }
            idx++;
        }
        return foundItems;
    }
}
