import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * This class represents a queue based on a heap structure.
 * @param <V> The value we want to store into the heapQueue its type can be anything
 * @param <P> The value priority whose type has to be comparable
 */
public class HeapQueue<V, P extends Comparable<? super P>> implements PriorityQueue<V, P> {

    private final ArrayList<TSPair<V, P>> pairs = new ArrayList<>();
    private long nextTimeStamp = 0L;

    /**
     * Adds a value with his priority into the queue.
     * @param value The value that TSPair will store
     * @param priority The value priority
     */
    @Override
    public void add(V value, P priority) {
        TSPair<V, P> pair = new TSPair<>(value, priority, nextTimeStamp);
        pairs.add(pair);
        toMaxHeapFromBottom();
        nextTimeStamp += 1;
    }

    /**
     * Removes the value with the highest priority
     * @return the value removed.
     */
    @Override
    public V remove() {
        V value = element();
        Collections.swap(pairs, 0, size() - 1);
        pairs.remove(size() - 1);
        toMaxHeapFromTop();
        return value;
    }

    /**
     * Returns the value with the highest priority
     * @return the value with the highest priority
     */
    @Override
    public V element() {
        if (size() == 0)
            throw new NoSuchElementException("Empty queue!");
        return pairs.get(0).value;
    }

    /**
     * Returns the heapQueue size.
     * @return the heapQueue size
     */
    @Override
    public int size() {
        return pairs.size();
    }


    /** ------------ AUXILIARY METHODS ------------- */

    private void toMaxHeapFromBottom() {
        int lastPairIndex = size() - 1;
        toMaxHeapFromBottom(lastPairIndex);
    }

    private void toMaxHeapFromBottom(int index) {
        if (hasParent(index)) {
            TSPair<V, P> parent = pairs.get(parent(index));
            TSPair<V, P> child = pairs.get(index);
            if (child.compareTo(parent) > 0) {
                Collections.swap(pairs, index, parent(index));
                toMaxHeapFromBottom(parent(index));
            }
        }
    }

    private void toMaxHeapFromTop() {
        int parentIndex = 0;
        toMaxHeapFromTop(parentIndex);
    }

    private void toMaxHeapFromTop(int index) {
        int left = left(index);
        int right = right(index);
        if (hasLeft(index) && hasRight(index)) {
            int maxPairIndex = (pairs.get(left).compareTo(pairs.get(right)) > 0) ? left : right;
            checkMaxPair(maxPairIndex, index);
        } else if (hasLeft(index)) {
            checkMaxPair(left, index);
        }
    }

    private void checkMaxPair(int child, int parent) {
        if (pairs.get(child).compareTo(pairs.get(parent)) > 0) {
            Collections.swap(pairs, child, parent);
            toMaxHeapFromTop(child);
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

    private static int parent(int index) {
        if (index % 2 != 0)
            return (index + 1) / 2 - 1;
        else
            return index / 2 - 1;
    }

    private static int left(int index) {
        return 2 * (index + 1) - 1;
    }

    private static int right(int index) {
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
