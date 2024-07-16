public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int first;
    private int last;
    private T[] arr;
    private int capacity;

    public ArrayDeque() {
        arr = (T[]) new Object[8];
        this.capacity = arr.length;
        first = 0;
        last = 1;
        size = 0;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resizing(int capacity) {
        T[] Newarr = (T[]) new Object[capacity];
        for (int i = 1; i <= size; i++) {
            Newarr[i] = arr[(++first) % this.capacity];
        }
        this.capacity = capacity;
        // 两个指针在Newarr中的位置
        last = size + 1;
        first = 0;
        arr = Newarr;
    }

    @Override
    public void addFirst(T item) {
        if (size == this.capacity) {
            resizing(capacity * 2);
        }
        arr[first] = item;
        size++;
        // first越界的情况
        first = first == 0 ? capacity - 1 : first - 1;
    }

    @Override
    public void addLast(T item) {
        if (size == this.capacity) {
            resizing(capacity * 2);
        }
        arr[last] = item;
        size++;
        // last越界的情况
        last = last == capacity - 1 ? 0 : last + 1;
    }

    @Override
    public void printDeque() {
        for (int i = (first + 1) % capacity; i != last - 1; i = (i + 1) % capacity) {
            System.out.println(arr[i] + " ");
        }
        System.out.println(arr[last - 1]);
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return arr[(first + 1 + index) % this.capacity];
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        first = (first + 1) % capacity;
        T temp = arr[first];
        arr[first] = null;
        size--;
        if (capacity >= 16 && size < capacity / 4) {
            resizing(capacity / 2);
        }
        return temp;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        last = last == 0 ? capacity - 1 : last - 1;
        T temp = arr[last];
        arr[last] = null;
        size--;
        if (capacity >= 16 && size < capacity / 4) {
            resizing(capacity / 2);
        }
        return temp;
    }
}
