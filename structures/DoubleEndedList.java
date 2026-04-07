package structures;

import benchmark.LinearStructure;

public class DoubleEndedList<T> implements LinearStructure<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
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
        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }

        size++;
    }

    @Override
    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
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

        T removed = head.data;
        head = head.next;

        if (head == null) {
            tail = null;
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
        tail = previous;
        tail.next = null;
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
        Node<T> current = head;
        int index = 0;

        while (current != null) {
            if ((current.data == null && value == null) ||
                (current.data != null && current.data.equals(value))) {
                return index;
            }

            current = current.next;
            index++;
        }

        return -1;
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("La lista está vacía");
        }
        return head.data;
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

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current;
    }
}