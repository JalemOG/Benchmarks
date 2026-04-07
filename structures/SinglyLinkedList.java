package structures;

import benchmark.LinearStructure;

public class SinglyLinkedList<T> implements LinearStructure<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T d) { data = d; }
    }

    private Node<T> head, tail;
    private int size;

    public void addFirst(T v) {
        Node<T> n = new Node<>(v);
        n.next = head;
        head = n;
        if (tail == null) tail = n;
        size++;
    }

    public void addLast(T v) {
        Node<T> n = new Node<>(v);
        if (isEmpty()) head = tail = n;
        else { tail.next = n; tail = n; }
        size++;
    }

    public void insert(int i, T v) {
        if (i < 0 || i > size) throw new IndexOutOfBoundsException();
        if (i == 0) { addFirst(v); return; }
        if (i == size) { addLast(v); return; }

        Node<T> prev = getNode(i - 1);
        Node<T> n = new Node<>(v);
        n.next = prev.next;
        prev.next = n;
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new IllegalStateException();
        T val = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return val;
    }

    public T removeLast() {
        if (size == 1) return removeFirst();
        Node<T> prev = getNode(size - 2);
        T val = tail.data;
        tail = prev;
        tail.next = null;
        size--;
        return val;
    }

    public T removeAt(int i) {
        if (i == 0) return removeFirst();
        if (i == size - 1) return removeLast();
        Node<T> prev = getNode(i - 1);
        T val = prev.next.data;
        prev.next = prev.next.next;
        size--;
        return val;
    }

    public T get(int i) { return getNode(i).data; }

    public void set(int i, T v) { getNode(i).data = v; }

    public int indexOf(T v) {
        Node<T> c = head;
        for (int i = 0; i < size; i++) {
            if ((c.data == null && v == null) || (c.data != null && c.data.equals(v)))
                return i;
            c = c.next;
        }
        return -1;
    }

    private Node<T> getNode(int i) {
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
        Node<T> c = head;
        for (int j = 0; j < i; j++) c = c.next;
        return c;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }
}