package com.tugalsan.api.thread.server.sync;

import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.function.client.TGS_Func_OutBool_In1;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.stream.client.TGS_StreamReverseIterableFromList;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TS_ThreadSyncLst<T> {

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
    public List<T> toList_fast() {
        if (strategyIsSlowWrite) {
            listSlowWrite.stream().toList();
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

    public List<T> toList_unmodifiable() {
        if (strategyIsSlowWrite) {
            listSlowWrite.stream().toList();
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

    public TS_ThreadSyncLst<T> forEach(boolean parallelIfPossible, TGS_Func_In1<T> item) {
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

    public TS_ThreadSyncLst<T> forEach(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition, TGS_Func_In1<T> item) {
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

    public long count(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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

    public boolean isEmpty(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
        return count(parallelIfPossible, condition) == 0;
    }

    public boolean isPresent(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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
            listSlowWrite.set(idx, newItem);
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
            return listSlowWrite.get(idx);
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
            return listSlowWrite.getFirst();
        }
        return listSlowRead.peek();
    }

    public T getLast() {
        if (strategyIsSlowWrite) {
            return listSlowWrite.getLast();
        }
        T lastItem = null;
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {
            lastItem = iterator.next();
        }
        return lastItem;
    }

    //---------------------------------  find -----------------------------------    
    public T findFirst(TGS_Func_OutBool_In1<T> condition) {
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

    public T findLast(TGS_Func_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            return listSlowWrite.stream()
                    .filter(nextItem -> condition.validate(nextItem))
                    .findFirst().orElse(null);
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

    public T findAny(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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

    public List<T> findAll_modifiable(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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

    public List<T> findAll_unmodifiable(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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

    public List<T> findAll_fast(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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
    public T removeFirst() {
        if (strategyIsSlowWrite) {
            return TGS_UnSafe.call(() -> {
                return listSlowWrite.removeFirst();
            }, e -> null);
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            iterator.remove();
            return item;
        }
        return null;
    }

    public boolean removeLast() {
        if (strategyIsSlowWrite) {
            return TGS_UnSafe.call(() -> {
                listSlowWrite.removeLast();
                return true;
            }, e -> false);
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            if (iterator.hasNext()) {
                continue;
            }
            iterator.remove();
            return true;
        }
        return false;
    }

    public boolean removeFirst(TGS_Func_OutBool_In1<T> condition) {
        var iterator = strategyIsSlowWrite ? listSlowWrite.iterator() : listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeFirst(T item) {
        return removeFirst(o -> Objects.equals(o, item));
    }

    public boolean removeLast(TGS_Func_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            var idx = 0;
            var reverseIterator = TGS_StreamReverseIterableFromList.of(listSlowWrite).iterator();
            while (reverseIterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = reverseIterator.next();
                if (condition.validate(item)) {
                    reverseIterator.remove();
                    return true;
                }
                idx++;
            }
            return false;
        }
        var iterator = listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeLast(T item) {
        return removeLast(o -> Objects.equals(o, item));
    }

    public T popFirst() {
        if (strategyIsSlowWrite) {
            return TGS_UnSafe.call(() -> {
                var first = listSlowWrite.getFirst();
                listSlowWrite.removeFirst();
                return first;
            }, e -> null);
        }
        return listSlowRead.poll();
    }

    public T popFirst(TGS_Func_OutBool_In1<T> condition) {
        var iterator = strategyIsSlowWrite ? listSlowWrite.iterator() : listSlowRead.iterator();
        while (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            if (condition.validate(item)) {
                iterator.remove();
                return item;
            }
        }
        return null;
    }

    public T popLast() {
        if (strategyIsSlowWrite) {
            var reverseIterator = TGS_StreamReverseIterableFromList.of(listSlowWrite).iterator();
            if (reverseIterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = reverseIterator.next();
                reverseIterator.remove();
                return item;
            }
            return null;
        }
        var iterator = listSlowRead.iterator();
        if (iterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
            var item = iterator.next();
            iterator.remove();
            return item;
        }
        return null;
    }

    public T popLast(TGS_Func_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            var idx = 0;
            var reverseIterator = TGS_StreamReverseIterableFromList.of(listSlowWrite).iterator();
            while (reverseIterator.hasNext()) {//USE THREAD SAFE ITERATOR!!!
                var item = reverseIterator.next();
                if (condition.validate(item)) {
                    reverseIterator.remove();
                    return item;
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

    public boolean removeAll(TGS_Func_OutBool_In1<T> condition) {
        if (strategyIsSlowWrite) {
            return listSlowWrite.removeIf(nextItem -> condition.validate(nextItem));
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
            removeFirst();//NO WORRY REMOVE IS SAFE :)
        }
    }

    public void cropToLength_byRemovingLastItems(int len) {
        if (len < 1) {
            clear();
            return;
        }
        while (size() > len) {
            removeLast();//NO WORRY REMOVE IS SAFE :)
        }
    }

    //-------------------- NOT A GOOD IDEA -------------------------------------------
    @Deprecated //NOT A GOOD IDEA
    public int idxLast(TGS_Func_OutBool_In1<T> condition) {
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
    public int idxFirst(TGS_Func_OutBool_In1<T> condition) {
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
    public List<Integer> idxAll_unmodifiable(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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
    public List<Integer> idxAll_modifiable(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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
    public List<Integer> idxAll_fast(boolean parallelIfPossible, TGS_Func_OutBool_In1<T> condition) {
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
