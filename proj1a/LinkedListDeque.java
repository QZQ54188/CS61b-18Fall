public class LinkedListDeque<T> {
    private int size;

    private class Node {
        private T elem;
        private Node pre;
        private Node next;

        public Node(T _elem, Node _pre, Node _next) {
            elem = _elem;
            pre = _pre;
            next = _next;
        }

    }

    // 创造一个虚拟节点，避免调用函数时讨论节点为空的情况
    private Node sentinel = new Node(null, null, null);

    // 双端链表的默认构造函数
    public LinkedListDeque() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0 ? true : false;
    }

    public void addFirst(T item) {
        if (sentinel.next == null) {
            sentinel.next = new Node(item, sentinel, sentinel);
            sentinel.pre = sentinel.next;
        } else {
            sentinel.next = new Node(item, sentinel, sentinel.next);
            sentinel.next.next.pre = sentinel.next;
        }
        // 不在函数一开始调用size++的是为了保障异常安全
        size++;
    }

    public void addLast(T item) {
        if (sentinel.next == null) {
            sentinel.next = new Node(item, sentinel, sentinel);
            sentinel.pre = sentinel.next;
        } else {
            sentinel.pre = new Node(item, sentinel.pre, sentinel);
            sentinel.pre.pre.next = sentinel.pre;
        }
        // 不在函数一开始调用size++的是为了保障异常安全
        size++;
    }

    public void printDeque() {
        Node ptr = sentinel.next;
        for (int i = 0; i <= size - 1; i++) {
            System.out.println(ptr.elem + " ");
            ptr = ptr.next;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T res = sentinel.next.elem;
        Node temp = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.pre = sentinel;
        temp.pre = null;
        temp.next = null;
        size--;
        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T res = sentinel.pre.elem;
        Node temp = sentinel.pre;
        sentinel.pre = sentinel.pre.pre;
        sentinel.pre.next = sentinel;
        temp.next = null;
        temp.pre = null;
        size--;
        return res;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node ptr = sentinel.next;
        for (int i = 0; i <= index - 1; i++) {
            ptr = ptr.next;
        }
        return ptr.elem;
    }

    public T getHelper(int index, Node ptr) {
        if (index == 0) {
            return ptr.elem;
        }
        return getHelper(index - 1, ptr.next);
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getHelper(index, this.sentinel.next);
    }
}
