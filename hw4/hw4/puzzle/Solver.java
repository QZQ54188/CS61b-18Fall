package hw4.puzzle;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private Deque<WorldState> sol = new ArrayDeque<>();
    private int costs;

    // 这个哈希表存储当前状态对应到目标状态所需要的预估代价
    private Map<WorldState, Integer> stepsToGoal = new HashMap<>();

    /*
     * 封装一个Node类 ，储存节点在搜索中的关键信息，例如WorldState，父节点（便于回溯路径），
     * 以及当前消耗,这样的代码也可以方便后续添加节点的属性
     */
    private class Node {
        WorldState worlds;
        Node parentNode;
        int cost;

        public Node(WorldState world, Node pNode, int co) {
            worlds = world;
            parentNode = pNode;
            cost = co;
        }
    }

    // 计算节点到终点的预估距离
    private int getDis(Node p) {
        if (!stepsToGoal.containsKey(p.worlds)) {
            stepsToGoal.put(p.worlds, p.worlds.estimatedDistanceToGoal());
        }
        return stepsToGoal.get(p.worlds);
    }

    // 计算节点之间的实际距离,返回一个比较器，用于搜索节点
    private Comparator<Node> disCmp = new Comparator<Solver.Node>() {
        @Override
        public int compare(Node p1, Node p2) {
            int dis1 = getDis(p1);
            int dis2 = getDis(p2);
            return p1.cost + dis1 - p2.cost - dis2;
        }
    };

    public int moves() {
        return costs;
    }

    public Iterable<WorldState> solution() {
        return sol;
    }

    public Solver(WorldState init) {
        // 初始化队列
        final MinPQ<Node> pq = new MinPQ<>(1, disCmp);
        Node start = new Node(init, null, 0);
        pq.insert(start);
        costs = solverHelper(pq);
    }

    private int solverHelper(MinPQ<Node> pq) {
        while (!pq.isEmpty()) {
            Node cur = pq.delMin();
            // 如果cur已经是目标节点的话，就根据父节点回溯路径
            if (cur.worlds.isGoal()) {
                Node temp = cur;
                while (temp != null) {
                    sol.addFirst(temp.worlds);
                    temp = temp.parentNode;
                }
                return cur.cost;
            }

            for (WorldState neighbor : cur.worlds.neighbors()) {
                // 确保不会造成循环添加
                if (cur.parentNode == null || !cur.parentNode.worlds.equals(neighbor)) {
                    Node p = new Node(neighbor, cur, cur.cost + 1); // 修改这里，使用cur.cost而不是costs
                    pq.insert(p);
                }
            }
        }
        return -1; // 如果没有找到解决方案
    }

}
