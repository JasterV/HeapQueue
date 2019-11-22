import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class HeapQueue<V, P extends Comparable<? super P>> implements PriorityQueue<V, P> {

    private final ArrayList<TSPair<V, P>> pairs = new ArrayList<>();
    private long nextTimeStamp = 0L;

    /**---PRIORITY QUEUE IMPLEMENTATION---*/
    @Override
    public void add(V value, P priority) {
        TSPair<V, P> pair = new TSPair<>(value, priority, nextTimeStamp);
        pairs.add(pair);
        onePathSort();
        nextTimeStamp += 1;
    }

    @Override
    public V remove() {
        V value = element();
        Collections.swap(pairs, 0, size() - 1);
        pairs.remove(size() - 1);
        maxHeapSort();
        return value;
    }

    @Override
    public V element() {
        if (size() == 0)
            throw new NoSuchElementException("Empty queue!");
        return pairs.get(0).value;
    }

    @Override
    public int size() {
        return pairs.size();
    }

    /**--- SORT ALGORITHM FOR ADD METHOD ---*/
    private void onePathSort() {
        int lastPairIndex = size() - 1;
        onePathSort(lastPairIndex);
    }

    private void onePathSort(int index) {
        if (hasParent(index)) {
            TSPair<V, P> parent = pairs.get(parent(index));
            TSPair<V, P> child = pairs.get(index);
            if (child.compareTo(parent) > 0) {
                Collections.swap(pairs, index, parent(index));
                onePathSort(parent(index));
            }
        }
    }

    /**--- SORT ALGORITHM FOR REMOVE METHOD ---*/
    private void maxHeapSort() {
        int parentIndex = 0;
        maxHeapSort(parentIndex);
    }

    private void maxHeapSort(int index) {
        int left = left(index);
        int right = right(index);
        if (hasLeft(index) && hasRight(index)) {
            int maxPairIndex = (pairs.get(left).compareTo(pairs.get(right)) > 0) ? left : right;
            checkMaxPair(maxPairIndex, index);
        } else if (hasLeft(index)) {
            checkMaxPair(left, index);
        } else if (hasRight(index)) {
            checkMaxPair(right, index);
        }
    }

    private void checkMaxPair(int child, int parent) {
        if (pairs.get(child).compareTo(pairs.get(parent)) > 0) {
            Collections.swap(pairs, child, parent);
            maxHeapSort(child);
        }
    }

    boolean hasParent(int index) {
        return index > 0;
    }

    boolean hasLeft(int index) {
        return isValid(left(index));
    }

    boolean hasRight(int index) {
        return isValid(right(index));
    }

    private int parent(int index) {
        if (index % 2 != 0)
            return (index + 1) / 2 - 1;
        else
            return index / 2 - 1;
    }

    private int left(int index) {
        return 2 * (index + 1) - 1;
    }

    private int right(int index) {
        return 2 * (index + 1);
    }

    private boolean isValid(int index) {
        return 0 <= index && index < size();
    }

    private static class TSPair<V, P extends Comparable<? super P>> implements Comparable<TSPair<V, P>> {
        private final V value;
        private final P priority;
        private final long timeStamp;

        TSPair(V value, P priority, long timeStamp) {
            this.value = value;
            this.priority = priority;
            this.timeStamp = timeStamp;
        }

        @Override
        public int compareTo(TSPair<V, P> tsPair) {
            if (tsPair.priority == null && this.priority == null)
                return 0;
            else if (tsPair.priority == null)
                return 1;
            else if (this.priority == null)
                return -1;
            else
                return priority.compareTo(tsPair.priority) > 0 ? 1
                        : priority.compareTo((tsPair.priority)) < 0 ? -1
                        : Long.compare(tsPair.timeStamp, timeStamp);
        }
    }
}
