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

    // Adds an item to the end (rear) of the queue
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            // If the queue is empty, front and rear both point to the new node
            front = rear = newNode;
        } else {
            // Otherwise, add the new node after the current rear and move rear
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Removes and returns the item at the front of the queue
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");

        T data = front.data;
        front = front.next;

        // If queue becomes empty after dequeue, set rear to null
        if (front == null) {
            rear = null;
        }

        size--;
        return data;
    }

    // Returns the item at the front of the queue without removing it
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        return front.data;
    }

    // Checks if the queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the number of items in the queue
    public int size() {
        return size;
    }
}

