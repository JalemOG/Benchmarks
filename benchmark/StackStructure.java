package benchmark;

public interface StackStructure<T> {
    void push(T value);
    T pop();
    T peek();
    int size();
    boolean isEmpty();
}