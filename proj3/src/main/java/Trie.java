import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    //前缀树的节点
    static class TrieNode {
        private Map<Character, TrieNode> children;
        //用于储存当前前缀对应的名称链表，一个key可能有多个name
        private List<String> names;
        //表示当前节点是否是一个完整单词的结尾
        private boolean isWord;

        public TrieNode() {
            this.children = new HashMap<>();
            this.names = new ArrayList<>();
        }

        public Map<Character, TrieNode> getChildren() {
            return children;
        }

        public List<String> getNames() {
            return names;
        }

        public boolean isWord() {
            return isWord;
        }
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void add(String key, String name) {
        TrieNode cur = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (cur.children.containsKey(c)) {
                cur = cur.children.get(c);
            } else {
                TrieNode t = new TrieNode();
                cur.children.put(c, t);
                cur = t;
            }
        }
        cur.names.add(name);
        cur.isWord = true;
    }

    public TrieNode findNode(String key) {
        TrieNode cur = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            TrieNode node = cur.children.get(c);
            if (node == null) {
                return null;
            }
            cur = node;
        }
        return cur;
    }
}