package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    private int[][] tiles;
    private int size;
    private static final int BLANK = 0;

    public Board(int[][] tile) {
        size = tile.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(tile[i], 0, this.tiles[i], 0, size);
        }
    }

    public int tileAt(int i, int j) {
        if (i >= size || j >= size || i < 0 || j < 0) {
            throw new IndexOutOfBoundsException("the input i or j is out of bound!");
        }
        return tiles[i][j];
    }

    public int size() {
        return size;
    }

    /**
     * Returns neighbors of this board.
     * SPOILERZ: This is the answer.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int wrongPos = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                } else if (tiles[i][j] != i * size + (j + 1)) {
                    wrongPos++;
                }
            }
        }
        return wrongPos;
    }

    public int manhattan() {
        int dis = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (tiles[i][j] != i * size + (j + 1)) {
                    int xdis = (tiles[i][j] - 1) / size;
                    int ydis = tiles[i][j] - xdis * size - 1;
                    dis += (int) Math.abs(j - ydis) + (int) Math.abs(i - xdis);
                }
            }
        }
        return dis;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        } else if (y == null) {
            return true;
        } else if (this.getClass() != y.getClass()) {
            return false;
        }
        Board temp = (Board) y;
        if (this.size() != temp.size()) {
            return false;
        }
        int len = tiles.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (this.tiles[i][j] != temp.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
