package structures;

import benchmark.StackStructure;

@SuppressWarnings("unchecked")
public class StackArray<T> implements StackStructure<T> {

    private T[] data;
    private int top;

    public StackArray() {
        data = (T[]) new Object[10];
        top = -1;
    }

    private void ensureCapacity() {
        if (top + 1 == data.length) {
            T[] newData = (T[]) new Object[data.length * 2];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    @Override
    public void push(T value) {
        ensureCapacity();
        data[++top] = value;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("El stack está vacío");
        }

        T value = data[top];
        data[top] = null;
        top--;
        return value;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("El stack está vacío");
        }

        return data[top];
    }

    @Override
    public int size() {
        return top + 1;
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }
}