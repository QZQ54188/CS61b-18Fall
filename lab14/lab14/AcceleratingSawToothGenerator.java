package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
    }

    @Override
    public double next() {
        if (state == period - 1) {
            state = 0;
            period *= factor;
        }
        return normalize(state++);
    }

    private double normalize(int state) {
        double res = -1;
        res += (2.0 / (period - 1)) * state;
        return res;
    }
}
