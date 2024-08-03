package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    @Override
    public double next() {
        return normalize(++state);
    }

    private double normalize(int state) {
        double res = -1;
        int weirdState = state & (state >> 3) % period;
        res += (2.0 / period) * weirdState;
        return res;
    }
}
