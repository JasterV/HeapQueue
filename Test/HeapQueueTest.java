import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HeapQueueTest {
    private HeapQueue<String, Integer> heapQueue = new HeapQueue<>();

    @Test
    void test_add_remove(){
        heapQueue = new HeapQueue<>();
        heapQueue.add("Victor", 2);
        heapQueue.add("Jordi", 6);
        heapQueue.add("Manel", 6);
        heapQueue.add("Fran", 10);
        heapQueue.add("Albert", 13);
        heapQueue.add("Pedro", null);
        test_add();
        test_remove();
    }

    void test_add(){
        assertFalse(heapQueue.hasLeft(5) || heapQueue.hasRight(5));
        assertTrue(heapQueue.hasLeft(1) && heapQueue.hasRight(1));
        assertTrue(heapQueue.hasLeft(2) && !heapQueue.hasRight(2));
        assertTrue(heapQueue.hasParent(1));
        assertTrue(heapQueue.hasParent(3));
    }

    void test_remove(){
        assertEquals(heapQueue.remove(), "Albert");
        assertEquals(heapQueue.remove(), "Fran");
        assertEquals(heapQueue.remove(), "Jordi");
        assertEquals(heapQueue.remove(), "Manel");
        assertEquals(heapQueue.remove(), "Victor");
        assertEquals(heapQueue.remove(), "Pedro");
        try{
            heapQueue.remove();
        }catch (NoSuchElementException e){
            assertEquals(e.getMessage(), "Empty queue!");
        }
    }
}