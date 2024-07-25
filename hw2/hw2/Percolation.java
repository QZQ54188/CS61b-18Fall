package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private boolean[][] map;
    private int count;// 表示已经打开的方块数量
    private int topSite;// 表示顶端的虚拟节点，连接所有顶行节点
    private int bottomSite;// 表示底端的虚拟节点，连接所有底端节点
    // 有了这两个变量，实现boolean percolates() 操作时只需检测topSite与bottomSite是否连接
    private WeightedQuickUnionUF sites;
    // 检测是否存在虚假渗透
    private WeightedQuickUnionUF sitesHelper;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        // 以下两个值已经越界，在地图中无法找到，是虚拟节点
        topSite = N * N;
        bottomSite = N * N + 1;
        sites = new WeightedQuickUnionUF(N * N + 2);// 地图节点数加两个虚拟节点
        sitesHelper = new WeightedQuickUnionUF(N * N + 1);// 地图节点数加顶端虚拟节点
        // 将上下边界点分别连接到上下虚拟节点
        for (int i = 0; i < N; i++) {
            sites.union(topSite, xyToID(0, i));
            sites.union(bottomSite, xyToID(N - 1, i));
        }
        for (int i = 0; i < N; i++) {
            sitesHelper.union(topSite, xyToID(0, i));
        }
        // 初始化地图
        map = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] = false;
            }
        }
    }

    // 将坐标转化为单个数字，方便并查集合并与查找
    private int xyToID(int row, int col) {
        return row * N + col;
    }

    public void open(int row, int col) {
        validateRange(row, col);
        // 如果该方块已经被打开了，直接返回
        if (map[row][col]) {
            return;
        }
        // 打开方块，注意要与上下左右已经打开的方块合并
        map[row][col] = true;
        count++;
        unionOpenNeighbor(row, col, row + 1, col);
        unionOpenNeighbor(row, col, row - 1, col);
        unionOpenNeighbor(row, col, row, col - 1);
        unionOpenNeighbor(row, col, row, col + 1);
    }

    private void validateRange(int row, int col) {
        if (row < 0 || row > N - 1 || col < 0 || col > N - 1) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void unionOpenNeighbor(int row, int col, int newRow, int newCol) {
        if (newRow < 0 || newRow > N - 1 || newCol < 0 || newCol > N - 1) {
            return;
        }
        if (map[newRow][newCol]) {
            sites.union(xyToID(row, col), xyToID(newRow, newCol));
            sitesHelper.union(xyToID(row, col), xyToID(newRow, newCol));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRange(row, col);
        return map[row][col];
    }

    // number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        if (count == 0) {
            return false;
        }
        return sites.connected(topSite, bottomSite);
    }

    public boolean isFull(int row, int col) {
        validateRange(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        // 使用sitesHelper可以防止虚假渗透
        return sitesHelper.connected(topSite, xyToID(row, col));
    }

    // use for unit testing (not required)
    public static void main(String[] args) {

    }
}
