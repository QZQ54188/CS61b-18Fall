package lab11.graphs;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Queue;

/**
 * @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /*
     * Inherits public fields:
     * public int[] distTo;
     * public int[] edgeTo;
     * public boolean[] marked;
     */

    private int s;
    private int t;
    private boolean targetFound = false;
    private Queue<Integer> queue = new Queue<>();

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        // bfs初始化
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        queue.enqueue(s);
        marked[s] = true;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        while (!queue.isEmpty()) {
            int cur = queue.dequeue();
            if (cur == t) {
                targetFound = true;
                break;
            }
            for (int neighbor : maze.adj(cur)) {
                if (!marked[neighbor]) {
                    queue.enqueue(neighbor);
                    marked[neighbor] = true;
                    edgeTo[neighbor] = cur;
                    distTo[neighbor] = distTo[cur] + 1;
                }
            }
            announce();
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}
