package leaderboard_matchmaking.matchmaking;

import static org.junit.jupiter.api.Assertions.*;

import org.seng.leaderboard_matchmaking.matchmaking.Queue;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class QueueTest {

    // ----- Tests for enqueue() -----

    @Test
    public void testEnqueueOnEmpty() {
        Queue<Integer> q = new Queue<>();
        assertTrue(q.isEmpty(), "Queue should be empty initially");
        q.enqueue(1);
        assertFalse(q.isEmpty());
        assertEquals(1, q.size());
        assertEquals(1, q.peek(), "Peek should return the first enqueued element");
    }

    @Test
    public void testEnqueueMultipleElementsOrder() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        List<Integer> expected = Arrays.asList(10, 20, 30);
        assertEquals(expected, q.toList(), "Queue should maintain FIFO order");
    }

    @Test
    public void testEnqueueAfterDequeue() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(1, q.dequeue(), "Dequeued element should be the first enqueued");
        q.enqueue(4);
        List<Integer> expected = Arrays.asList(2, 3, 4);
        assertEquals(expected, q.toList(), "Queue should correctly update after dequeue and enqueue");
    }

    @Test
    public void testEnqueueSizeConsistency() {
        Queue<String> q = new Queue<>();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        assertEquals(3, q.size(), "Queue size should match number of enqueued elements");
        q.dequeue();
        assertEquals(2, q.size(), "Size should decrement after dequeue");
    }

    @Test
    public void testEnqueueThenClear() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(100);
        q.enqueue(200);
        q.clear();
        assertTrue(q.isEmpty(), "Queue should be empty after clear");
        assertEquals(0, q.size(), "Size should be 0 after clear");
    }

    // ----- Tests for dequeue() -----

    @Test
    public void testDequeueEmptyQueueThrowsException() {
        Queue<Integer> q = new Queue<>();
        Exception exception = assertThrows(IllegalStateException.class, q::dequeue,
                "Dequeue on empty queue should throw an exception");
        assertEquals("Queue is empty", exception.getMessage());
    }

    @Test
    public void testDequeueOrderFIFO() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);
        assertEquals(5, q.dequeue(), "Dequeued element should be the front element");
        assertEquals(6, q.dequeue(), "Dequeued element should follow FIFO order");
        assertEquals(7, q.dequeue(), "Dequeued element should be the last one");
    }

    @Test
    public void testDequeueSizeUpdate() {
        Queue<String> q = new Queue<>();
        q.enqueue("x");
        q.enqueue("y");
        q.enqueue("z");
        q.dequeue();
        assertEquals(2, q.size(), "Size should update correctly after dequeue");
    }

    @Test
    public void testDequeueAfterMixedOperations() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.dequeue(); // removes 1
        q.enqueue(3);
        q.enqueue(4);
        assertEquals(2, q.dequeue(), "After several operations, dequeue should return the correct element");
    }

    @Test
    public void testDequeueUntilEmpty() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.dequeue();
        assertTrue(q.isEmpty(), "Queue should be empty after removing its only element");
        assertThrows(IllegalStateException.class, q::dequeue, "Dequeue on empty queue should throw exception");
    }

    // ----- Tests for remove(T target) -----

    @Test
    public void testRemoveFromEmptyQueue() {
        Queue<Integer> q = new Queue<>();
        assertFalse(q.remove(99), "Removing from an empty queue should return false");
    }

    @Test
    public void testRemoveFrontElement() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertTrue(q.remove(1), "Removing the front element should succeed");
        List<Integer> expected = Arrays.asList(2, 3);
        assertEquals(expected, q.toList(), "Queue order should update after removing front element");
    }

    @Test
    public void testRemoveMiddleElement() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        assertTrue(q.remove(3), "Removing a middle element should succeed");
        List<Integer> expected = Arrays.asList(1, 2, 4);
        assertEquals(expected, q.toList(), "Queue order should update after removing a middle element");
    }

    @Test
    public void testRemoveRearElement() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        assertTrue(q.remove(30), "Removing the rear element should succeed");
        List<Integer> expected = Arrays.asList(10, 20);
        assertEquals(expected, q.toList(), "Queue order should update after removing the rear element");
    }

    @Test
    public void testRemoveNonExistentElement() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(100);
        q.enqueue(200);
        assertFalse(q.remove(300), "Removing a non-existent element should return false");
        List<Integer> expected = Arrays.asList(100, 200);
        assertEquals(expected, q.toList(), "Queue should remain unchanged after a failed removal");
    }

    // ----- Tests for peek() -----

    @Test
    public void testPeekEmptyQueueThrowsException() {
        Queue<Integer> q = new Queue<>();
        Exception exception = assertThrows(IllegalStateException.class, q::peek,
                "Peek on empty queue should throw an exception");
        assertEquals("Queue is empty", exception.getMessage());
    }

    @Test
    public void testPeekDoesNotRemoveElement() {
        Queue<String> q = new Queue<>();
        q.enqueue("hello");
        String peeked = q.peek();
        assertEquals("hello", peeked, "Peek should return the front element");
        assertEquals(1, q.size(), "Peek should not remove the element");
    }

    @Test
    public void testPeekWithMultipleElements() {
        Queue<String> q = new Queue<>();
        q.enqueue("first");
        q.enqueue("second");
        q.enqueue("third");
        assertEquals("first", q.peek(), "Peek should always return the first element enqueued");
    }

    @Test
    public void testPeekAfterDequeue() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(5);
        q.enqueue(6);
        q.dequeue();
        assertEquals(6, q.peek(), "After dequeue, peek should return the new front element");
    }

    @Test
    public void testPeekAfterRemove() {
        Queue<String> q = new Queue<>();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        q.remove("A");
        assertEquals("B", q.peek(), "After removing the front element, peek should return the next element");
    }

    // ----- Tests for contains(T target) -----

    @Test
    public void testContainsOnEmptyQueue() {
        Queue<Integer> q = new Queue<>();
        assertFalse(q.contains(1), "Empty queue should not contain any element");
    }

    @Test
    public void testContainsExistingElements() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        assertTrue(q.contains(10), "Queue should contain the first element");
        assertTrue(q.contains(20), "Queue should contain a middle element");
        assertTrue(q.contains(30), "Queue should contain the rear element");
    }

    @Test
    public void testContainsAfterRemoval() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.remove(2);
        assertFalse(q.contains(2), "Queue should not contain an element that was removed");
    }

    @Test
    public void testContainsWithDuplicates() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(5);
        q.enqueue(5);
        assertTrue(q.contains(5), "Queue should report true if duplicate elements exist");
    }

    @Test
    public void testContainsAfterClear() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(100);
        q.clear();
        assertFalse(q.contains(100), "Queue should not contain any element after clear");
    }

    // ----- Tests for isEmpty() and size() -----

    @Test
    public void testIsEmptyAndSizeOnNewQueue() {
        Queue<String> q = new Queue<>();
        assertTrue(q.isEmpty(), "Newly created queue should be empty");
        assertEquals(0, q.size(), "Size of a new queue should be 0");
    }

    @Test
    public void testIsEmptyAndSizeAfterEnqueues() {
        Queue<String> q = new Queue<>();
        q.enqueue("one");
        q.enqueue("two");
        q.enqueue("three");
        assertFalse(q.isEmpty(), "Queue should not be empty after enqueues");
        assertEquals(3, q.size(), "Size should reflect the number of enqueued elements");
    }

    @Test
    public void testSizeAfterDequeueAndRemove() {
        Queue<String> q = new Queue<>();
        q.enqueue("one");
        q.enqueue("two");
        q.enqueue("three");
        q.dequeue();
        assertEquals(2, q.size(), "Size should decrement after dequeue");
        q.remove("three");
        assertEquals(1, q.size(), "Size should decrement after removal");
    }

    @Test
    public void testSizeAfterMultipleOperations() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(100);
        q.enqueue(200);
        q.dequeue();
        q.enqueue(300);
        q.remove(300);
        assertEquals(1, q.size(), "Size should be correct after a sequence of operations");
    }

    @Test
    public void testIsEmptyAfterAllElementsRemoved() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.remove(10);
        assertTrue(q.isEmpty(), "Queue should be empty after removing its only element");
    }

    // ----- Tests for clear() -----

    @Test
    public void testClearOnNonEmptyQueue() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.clear();
        assertTrue(q.isEmpty(), "Queue should be empty after clear");
        assertEquals(0, q.size(), "Size should be 0 after clear");
    }

    @Test
    public void testClearOnEmptyQueue() {
        Queue<String> q = new Queue<>();
        q.clear(); // Should work without error on an already empty queue
        assertTrue(q.isEmpty(), "Clearing an empty queue should leave it empty");
        assertEquals(0, q.size(), "Size should be 0 after clearing an empty queue");
    }

    @Test
    public void testClearResetsInternalState() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.enqueue(20);
        q.dequeue();
        q.clear();
        assertThrows(IllegalStateException.class, q::peek, "Peek should throw on cleared queue");
        assertThrows(IllegalStateException.class, q::dequeue, "Dequeue should throw on cleared queue");
    }

    @Test
    public void testClearAndEnqueueAgain() {
        Queue<String> q = new Queue<>();
        q.enqueue("A");
        q.enqueue("B");
        q.clear();
        q.enqueue("C");
        assertFalse(q.isEmpty(), "Queue should not be empty after enqueue post-clear");
        assertEquals(1, q.size(), "Size should reflect new element after clear");
        assertEquals("C", q.peek(), "Queue should behave normally after clear");
    }

    @Test
    public void testMultipleClearCalls() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(5);
        q.clear();
        q.clear();
        assertTrue(q.isEmpty(), "Queue should remain empty after multiple clear calls");
    }

    // ----- Tests for toList() -----

    @Test
    public void testToListOnEmptyQueue() {
        Queue<Integer> q = new Queue<>();
        List<Integer> list = q.toList();
        assertTrue(list.isEmpty(), "toList should return an empty list for an empty queue");
    }

    @Test
    public void testToListMaintainsOrder() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, q.toList(), "toList should return elements in FIFO order");
    }

    @Test
    public void testToListAfterDequeue() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        q.dequeue(); // removes 10
        List<Integer> expected = Arrays.asList(20, 30);
        assertEquals(expected, q.toList(), "toList should reflect the queue state after a dequeue");
    }

    @Test
    public void testToListAfterRemove() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(100);
        q.enqueue(200);
        q.enqueue(300);
        q.remove(200);
        List<Integer> expected = Arrays.asList(100, 300);
        assertEquals(expected, q.toList(), "toList should reflect the queue state after a removal");
    }

    @Test
    public void testToListAfterClear() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.clear();
        List<Integer> list = q.toList();
        assertTrue(list.isEmpty(), "toList should return an empty list after clear");
    }
}
