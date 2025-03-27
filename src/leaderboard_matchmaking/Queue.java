package leaderboard_matchmaking;

// A generic Queue implementation using a singly linked list.
// Supports enqueue (add to end), dequeue (remove from front), peek, size, and isEmpty.

public class Queue<T> {

    // Node class representing each element in the queue
    private static class Node<T> {
        T data;         // The value stored in the node
        Node<T> next;   // Reference to the next node

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> front; // Points to the front (head) of the queue
    private Node<T> rear;  // Points to the rear (tail) of the queue
    private int size;      // Tracks the number of elements in the queue

    // Constructor initializes an empty queue
    public Queue() {
        front = rear = null;
        size = 0;
    }


}

