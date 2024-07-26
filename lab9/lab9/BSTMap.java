package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root; /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.key.equals(key)) {
            return p.value;
        }
        if (p.key.compareTo(key) < 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        if (p.key.equals(key)) {
            p.value = value;
        }
        if (p.key.compareTo(key) < 0) {
            // put进右子树，并且将右子树设为put完之后的子树
            p.right = putHelper(key, value, p.right);
        } else {
            // 同理
            p.left = putHelper(key, value, p.left);
        }
        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        KeySetHelper(set, root);
        return set;
    }

    private void KeySetHelper(Set<K> set, Node root) {
        if (root == null) {
            return;
        }
        set.add(root.key);
        KeySetHelper(set, root.left);
        KeySetHelper(set, root.right);
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        V res = get(key);
        if (res == null) {
            return null;
        }
        root = removeHelper(key, root);
        return res;
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value. Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V res = get(key);
        if (res != null) {
            return null;
        }
        if (size == 1) {
            root = null;
        }
        root = removeHelper(key, root);
        return res;
    }

    private Node removeHelper(K key, Node root) {
        if (root == null) {
            return null;
        }
        // 找到了要删除的节点
        if (root.key.equals(key)) {
            size--;
            // 假设该节点为叶节点
            if (root.left == null && root.right == null) {
                root = null;
                return root;
            } else if (root.left != null && root.right != null) {
                // 假设该节点有两个子树，该情况较为复杂
                Node pre = findPre(root);
                if (pre == null) {
                    // 保证在更新节点中不会丢失对原右节点的引用
                    Node pRight = root.right;
                    root.key = root.left.key;
                    root.value = root.left.value;
                    root.left = root.left.left;
                    root.right = pRight;
                } else {
                    root.value = pre.value;
                    root.key = pre.key;
                }
                return root;
            } else if (root.left != null) {
                // 该节点只有左子树
                root.value = root.left.value;
                root.key = root.left.key;
                root.right = root.left.right;
                root.left = root.left.left;
                return root;
            } else if (root.right != null) {
                // 该节点只有右子树
                root.value = root.right.value;
                root.key = root.right.key;
                root.left = root.right.left;
                root.right = root.right.right;
                return root;
            }
        }
        if (root.key.compareTo(key) < 0) {
            root.right = removeHelper(key, root.right);
        } else if (root.key.compareTo(key) > 0) {
            root.left = removeHelper(key, root.left);
        }
        return root;
    }

    // 返回p节点的前驱节点
    private Node findPre(Node p) {
        p = p.left;
        Node pre = p;
        while (p.right != null) {
            pre = p;
            p = p.right;
        }
        // 如果p没有右子树，那么p没有前驱节点
        if (p == pre) {
            return null;
        }
        pre.right = null;
        return p;
    }

    @Override
    public Iterator<K> iterator() {
        Set<K> set = keySet();
        return set.iterator();
    }
}
