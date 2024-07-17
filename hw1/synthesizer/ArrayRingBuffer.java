package synthesizer;

import java.util.Iterator;

import javax.management.RuntimeErrorException;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    private int first;
    private int last;
    private T[] rb;

    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
        first = 0;
        last = 0;
        fillCount = 0;
    }

    public void enqueue(T x) {
        if (x == null) {
            return;
        } else if (fillCount == capacity) {
            throw new RuntimeException("Ring buffer overflow");
        } else if (last == capacity) {
            last = 0;
        }
        rb[last] = x;
        last++;
        fillCount++;
    }

    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring buffer underflow");
        } else if (first == capacity) {
            first = 0;
        }
        T temp = rb[first];
        rb[first] = null;
        first++;
        fillCount--;
        return temp;
    }

    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring buffer is empty");
        } else if (first == capacity) {
            first = 0;
        }
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<T> {
        private int pos;

        public ArrayRingBufferIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < fillCount;
        }

        @Override
        public T next() {
            T temp = rb[pos];
            pos++;
            return temp;
        }
    }

}
