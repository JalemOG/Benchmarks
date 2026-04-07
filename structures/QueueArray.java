package structures;

import benchmark.QueueStructure;

@SuppressWarnings("unchecked")
public class QueueArray<T> implements QueueStructure<T> {

    private T[] data;
    private int front;
    private int rear;
    private int size;

    public QueueArray() {
        data = (T[]) new Object[10];
        front = 0;
        rear = 0;
        size = 0;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            T[] newData = (T[]) new Object[data.length * 2];

            for (int i = 0; i < size; i++) {
                newData[i] = data[(front + i) % data.length];
            }

            data = newData;
            front = 0;
            rear = size;
        }
    }

    @Override
    public void enqueue(T value) {
        ensureCapacity();
        data[rear] = value;
        rear = (rear + 1) % data.length;
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("La queue está vacía");
        }

        T value = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La queue está vacía");
        }

        return data[front];
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