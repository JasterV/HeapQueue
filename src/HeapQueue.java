import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class HeapQueue<V, P extends Comparable<? super P>> implements PriorityQueue<V, P> {

    private final ArrayList<TSPair<V, P>> pairs = new ArrayList<>();
    private long nextTimeStamp = 0L;

    /**
     * ---PRIORITY QUEUE IMPLEMENTATION---
     */
    @Override
    public void add(V value, P priority) {
        TSPair<V, P> child = new TSPair<>(value, priority, nextTimeStamp);
        pairs.add(child);
        add(size() - 1);
        nextTimeStamp += 1;
    }

    public void add(int index) {
        if (hasParent(index)) {
            TSPair<V, P> parent = pairs.get(parent(index));
            TSPair<V, P> child = pairs.get(index);
            if (child.compareTo(parent) > 0) {
                Collections.swap(pairs, index, parent(index));
            }
            add(parent(index));
        }
    }

    @Override
    public V remove() {
        V n = element();
        Collections.swap(pairs, size() - 1, 0);
        pairs.remove(size() - 1);
        remove(0);
        return n;

    }

    void remove(int head) {
        int ro = head;
        int l = left(head);
        int r = right(head);
        if (hasLeft(head) && hasRight(head)) {
            leftAndRight(ro, l, r, head);
        } else if (hasLeft(head) && !hasRight(head)) {
            onlyLeft(ro, l, head);
        }
    }

    private void leftAndRight(int ro, int l, int r, int head) {
        TSPair<V, P> root = pairs.get(ro);
        TSPair<V, P> left = pairs.get(l);
        TSPair<V, P> right = pairs.get(r);

        if (hasLeft(head) && left.compareTo(root) > 0) {
            ro = l;
        }
        if (hasRight(head) && right.compareTo(left) > 0) {
            ro = r;
        }
        if (ro != head) {
            Collections.swap(pairs, head, ro);
            remove(ro);
        }
    }

    private void onlyLeft(int ro, int l, int head) {
        TSPair<V, P> root = pairs.get(ro);
        TSPair<V, P> left = pairs.get(l);
        if (hasLeft(head) && left.compareTo(root) > 0) {
            ro = l;
        }
        if (ro != head) {
            Collections.swap(pairs, head, ro);
            remove(ro);
        }
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
