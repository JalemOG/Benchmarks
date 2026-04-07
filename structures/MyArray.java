package structures;

import benchmark.LinearStructure;

@SuppressWarnings("unchecked")
public class MyArray<T> implements LinearStructure<T> {

    private T[] data;
    private int size;

    public MyArray() {
        data = (T[]) new Object[10];
        size = 0;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            T[] newData = (T[]) new Object[data.length * 2];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    public void addFirst(T value) {
        insert(0, value);
    }

    public void addLast(T value) {
        ensureCapacity();
        data[size++] = value;
    }

    public void insert(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensureCapacity();
        for (int i = size; i > index; i--) data[i] = data[i - 1];
        data[index] = value;
        size++;
    }

    public T removeFirst() {
        return removeAt(0);
    }

    public T removeLast() {
        if (size == 0) throw new IllegalStateException();
        T val = data[--size];
        data[size] = null;
        return val;
    }

    public T removeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T val = data[index];
        for (int i = index; i < size - 1; i++) data[i] = data[i + 1];
        data[--size] = null;
        return val;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return data[index];
    }

    public void set(int index, T value) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        data[index] = value;
    }

    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if ((data[i] == null && value == null) || (data[i] != null && data[i].equals(value))) {
                return i;
            }
        }
        return -1;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }
}