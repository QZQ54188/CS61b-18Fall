package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int T;// 表示重复实验的次数
    private double[] openSiteFraction;// 表示每次实验打开的方块数量

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        openSiteFraction = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = pf.make(N);
            // 当地图未被渗透时就继续循环，直到随机打开的方块使地图被渗透
            while (!percolation.percolates()) {
                int x, y;
                // 确保随机生成的方块合理,如果该位置的方块已经打开了就重新生成
                do {
                    x = StdRandom.uniform(N);
                    y = StdRandom.uniform(N);
                } while (percolation.isOpen(x, y));
                percolation.open(x, y);
            }
            // 计算占比
            openSiteFraction[i] = (double) percolation.numberOfOpenSites() / (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSiteFraction);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSiteFraction);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return this.mean() - 1.96 * this.stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return this.mean() + 1.96 * this.stddev() / Math.sqrt(T);
    }
}
