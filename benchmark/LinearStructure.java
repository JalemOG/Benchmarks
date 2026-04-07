package benchmark;

public interface LinearStructure<T> {
    void addFirst(T value);
    void addLast(T value);
    void insert(int index, T value);

    T removeFirst();
    T removeLast();
    T removeAt(int index);

    T get(int index);
    void set(int index, T value);

    int indexOf(T value);

    int size();
    boolean isEmpty();
}