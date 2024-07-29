package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private Maze maze;
    private int targetX;
    private int targetY;

    private PriorityQueue<Node> pq = new PriorityQueue<>(new NodeCmp());

    private class Node {
        private int v;
        private int priority;

        public Node(int v) {
            this.v = v;
            this.priority = distTo[v] + h(v);
        }
    }

    private class NodeCmp implements Comparator<Node> {
        @Override
        public int compare(Node p1, Node p2) {
            return p1.priority - p2.priority;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        this.targetX = targetX;
        this.targetY = targetY;
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return pq.peek().v;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // TODO
        Node curNode = new Node(s);
        pq.add(curNode);
        marked[s] = true;
        while (!pq.isEmpty()) {
            curNode = pq.poll();
            for (int w : maze.adj(curNode.v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = curNode.v;
                    distTo[w] = distTo[curNode.v] + 1;
                    announce();
                    if (w == t) {
                        return;
                    } else {
                        pq.add(new Node(w));
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}
