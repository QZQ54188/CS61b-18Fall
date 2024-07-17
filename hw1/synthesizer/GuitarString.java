
package synthesizer;

public class GuitarString {
    private static final int SR = 44100;
    private static final double DECAY = .996;

    private BoundedQueue<Double> buffer;

    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buffer.enqueue(0.0);
        }
    }

    public void pluck() {
        for (int i = 0; i < buffer.capacity(); i++) {
            double random = Math.random() - 0.5;
            buffer.dequeue();
            buffer.enqueue(random);
        }
    }

    public void tic() {
        if (buffer.isFull()) {
            Double nextNote = buffer.dequeue();
            nextNote = DECAY * 0.5 * (nextNote + buffer.peek());
            buffer.enqueue(nextNote);
        }
    }

    public double sample() {
        return buffer.peek();
    }
}
