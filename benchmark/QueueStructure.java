package benchmark;

public interface QueueStructure<T> {
    void enqueue(T value);
    T dequeue();
    T peek();
    int size();
    boolean isEmpty();
}