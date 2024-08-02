public class KdTree {
    private KdNode root;
    private int size;

    private static class KdNode {
        private final GraphDB.Node point;
        private boolean compareX;
        private KdNode leftBottom;
        private KdNode rightTop;

        KdNode(GraphDB.Node p, boolean compareX) {
            this.point = p;
            this.compareX = compareX;
        }

        //决定往x方向搜索还是y方向搜索
        public boolean isRightOrTopOf(GraphDB.Node q) {
            return (compareX && point.lon > q.lon || (!compareX && point.lat > q.lat));
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public void insert(GraphDB.Node p) {
        if (root == null) {
            root = new KdNode(p, true);
            size++;
            return;
        }
        //先找到要插入节点的位置
        KdNode pre = null;
        KdNode cur = root;
        while(cur != null){
            if (cur.point.id == p.id) {
                return;
            }
            pre = cur;
            cur = cur.isRightOrTopOf(p) ? cur.leftBottom : cur.rightTop;
        }
        //在pre位置插入子节点
        if (pre.isRightOrTopOf(p)) {
            pre.leftBottom = new KdNode(p, !pre.compareX);
        } else {
            pre.rightTop = new KdNode(p, !pre.compareX);
        }
        size++;
    }

    //两个重载函数调用相关接口，找到距离(lon，lat)或者节点p最近的点
    public long nearest(double lon, double lat) {
        return nearest(new GraphDB.Node(0, lon, lat)).id;
    }

    public GraphDB.Node nearest(GraphDB.Node p) {
        return nearest(p, root, Double.MAX_VALUE);
    }

    private GraphDB.Node nearest(GraphDB.Node p, KdTree.KdNode node, double minDist) {
        if (p == null) {
            throw new NullPointerException();
        }
        if (node.point == null) {
            return null;
        }
        if (node.point.equals(p)) {
            return node.point;
        }
        GraphDB.Node bestPoint = null;
        double bestDist = minDist;
        double nodeDist = GraphDB.distance(node.point.lon, node.point.lat, p.lon, p.lat);
        if (nodeDist < minDist) {
            bestPoint = node.point;
            bestDist = nodeDist;
        }
        KdNode first = node.rightTop, second = node.leftBottom;
        if ((node.compareX && p.lon <= node.point.lon)
                || (!node.compareX && p.lat <= node.point.lat)) {
            first = node.leftBottom;
            second = node.rightTop;
        }
        if (first != null) {
            //递归，找到子树中距离最近的节点
            GraphDB.Node firstBestPoint = nearest(p, first, bestDist);
            //如果最近节点在子树中，那么更新答案
            if (firstBestPoint != null) {
                bestPoint = firstBestPoint;
                bestDist = GraphDB.distance(bestPoint.lon, bestPoint.lat, p.lon, p.lat);
            }
        }
        if (second == null) {
            return bestPoint;
        }
        if (second == node.leftBottom) {
            if (node.compareX && p.lon - node.point.lon >= bestDist) {
                return bestPoint;
            }
            if (!node.compareX && p.lat - node.point.lat >= bestDist) {
                return bestPoint;
            }
        } else if (second == node.rightTop) {
            if (node.compareX && node.point.lon - p.lon >= bestDist) {
                return bestPoint;
            }
            if (!node.compareX && node.point.lat - p.lat >= bestDist) {
                return bestPoint;
            }
        }
        GraphDB.Node secondBestPoint = nearest(p, second, bestDist);
        if (secondBestPoint != null) {
            bestPoint = secondBestPoint;
        }
        return bestPoint;
    }
}
