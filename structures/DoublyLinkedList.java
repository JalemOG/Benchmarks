package structures;

import benchmark.LinearStructure;

public class DoublyLinkedList<T> implements LinearStructure<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    @Override
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
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
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
    }

    @Override
    public void insert(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            addFirst(value);
            return;
        }

        if (index == size) {
            addLast(value);
            return;
        }

        Node<T> current = getNode(index);
        Node<T> previous = current.prev;
        Node<T> newNode = new Node<>(value);

        previous.next = newNode;
        newNode.prev = previous;
        newNode.next = current;
        current.prev = newNode;

        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        T removed = head.data;

        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }

        size--;
        return removed;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        T removed = tail.data;

        if (size == 1) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }

        size--;
        return removed;
    }

    @Override
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            return removeFirst();
        }

        if (index == size - 1) {
            return removeLast();
        }

        Node<T> current = getNode(index);
        T removed = current.data;

        current.prev.next = current.next;
        current.next.prev = current.prev;

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

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> current;

        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}