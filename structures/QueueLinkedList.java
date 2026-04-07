package structures;

import benchmark.QueueStructure;

public class QueueLinkedList<T> implements QueueStructure<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    @Override
    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("La queue está vacía");
        }

        T value = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        size--;
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La queue está vacía");
        }

        return front.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }
}