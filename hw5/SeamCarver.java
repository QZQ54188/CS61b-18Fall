import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    // 表示在水平方向还是垂直方向寻找
    private boolean isVertical = true;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.height = picture.height();
        this.width = picture.width();
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        Color up, down, left, right;
        // 垂直方向寻找
        if (isVertical) {
            up = y > 0 ? picture.get(x, y - 1) : picture.get(x, height - 1);
            down = y < height - 1 ? picture.get(x, y + 1) : picture.get(x, 0);
            left = x > 0 ? picture.get(x - 1, y) : picture.get(width - 1, y);
            right = x < width - 1 ? picture.get(x + 1, y) : picture.get(0, y);
        } // 水平方向寻找
        else {
            up = x > 0 ? picture.get(x - 1, y) : picture.get(height - 1, y);
            down = x < height - 1 ? picture.get(x + 1, y) : picture.get(0, y);
            left = y > 0 ? picture.get(x, y - 1) : picture.get(x, width - 1);
            right = y < width - 1 ? picture.get(x, y + 1) : picture.get(x, 0);
        }

        int rx = left.getRed() - right.getRed();
        int gx = left.getGreen() - right.getGreen();
        int bx = left.getBlue() - right.getBlue();
        int ry = up.getRed() - down.getRed();
        int gy = up.getGreen() - down.getGreen();
        int by = up.getBlue() - down.getBlue();

        return rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by;
    }

    private void swap() {
        int temp = width;
        width = height;
        height = width;
    }

    public int[] findVerticalSeam() {
        int[][] path = new int[width][height];
        double[][] cost = new double[width][height];
        for (int i = 0; i < width; i++) {
            double e = isVertical ? energy(i, 0) : energy(0, i);
            cost[i][0] = e;
            path[i][height - 1] = i;
        }

        for (int j = 1; j < height; j++) {
            for (int i = 1; i < width; i++) {
                double e = isVertical ? energy(i, j) : energy(j, i);
                cost[i][j] = e + getMinCost(i, j, path, cost);
            }
        }
        int[] res = new int[height];
        double min = Double.MAX_VALUE;
        int minPos = 0;
        for (int i = 0; i < width; i++) {
            if (cost[i][height - 1] < min) {
                min = cost[i][height - 1];
                minPos = i;
            }
        }

        for (int j = height - 1; j >= 0; j--) {
            res[j] = path[minPos][j];
            minPos = res[j];
        }
        return res;
    }

    private double getMinCost(int i, int j, int[][] path, double[][] cost) {
        double[] v = new double[3];
        v[1] = cost[i][j - 1];
        if (i > 0) {
            v[0] = cost[i - 1][j - 1];
        } else {
            v[0] = Double.MAX_VALUE;
        }
        if (i < width - 1) {
            v[2] = cost[i + 1][j - 1];
        } else {
            v[2] = Double.MAX_VALUE;
        }
        double res = Double.MAX_VALUE;
        int pos = 0;
        for (int m = 0; m < 3; m++) {
            if (v[m] < res) {
                res = v[m];
                pos = m;
            }
        }
        path[i][j - 1] = pos + i - 1;
        return res;
    }

    public int[] findHorizontalSeam() {
        isVertical = false;
        swap();
        int[] res = findVerticalSeam();
        swap();
        isVertical = true;
        return res;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(picture, seam);
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 0, j = 1; j < seam.length; i++, j++) {
            if (Math.abs(seam[i] - seam[j]) > 1) {
                return false;
            }
        }
        return true;
    }
}
