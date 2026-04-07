package structures;

import benchmark.LinearStructure;

public class CircularLinkedList<T> implements LinearStructure<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> tail;
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            tail = newNode;
            tail.next = tail;
        } else {
            newNode.next = tail.next;
            tail.next = newNode;
        }

        size++;
    }

    @Override
    public void addLast(T value) {
        addFirst(value);
        tail = tail.next;
    }

    @Override
    public void insert(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        if (index == 0) {
            addFirst(value);
            return;
        }

        if (index == size) {
            addLast(value);
            return;
        }

        Node<T> previous = getNode(index - 1);
        Node<T> newNode = new Node<>(value);

        newNode.next = previous.next;
        previous.next = newNode;
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("La lista está vacía");
        }

        Node<T> head = tail.next;
        T removed = head.data;

        if (size == 1) {
            tail = null;
        } else {
            tail.next = head.next;
        }

        size--;
        return removed;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("La lista está vacía");
        }

        if (size == 1) {
            return removeFirst();
        }

        Node<T> previous = getNode(size - 2);
        T removed = tail.data;
        previous.next = tail.next;
        tail = previous;
        size--;
        return removed;
    }

    @Override
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        if (index == 0) {
            return removeFirst();
        }

        if (index == size - 1) {
            return removeLast();
        }

        Node<T> previous = getNode(index - 1);
        T removed = previous.next.data;
        previous.next = previous.next.next;
        size--;
        return removed;
    }

    @Override
    public T get(int index) {
        return getNode(index).data;
    }

    @Override
    public void set(int index, T value) {
        getNode(index).data = value;
    }

    @Override
    public int indexOf(T value) {
        if (isEmpty()) {
            return -1;
        }

        Node<T> current = tail.next;
        for (int i = 0; i < size; i++) {
            if ((current.data == null && value == null) ||
                (current.data != null && current.data.equals(value))) {
                return i;
            }
            current = current.next;
        }

        return -1;
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("La lista está vacía");
        }
        return tail.next.data;
    }

    public T getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("La lista está vacía");
        }
        return tail.data;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        Node<T> current = tail.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current;
    }
}