import edu.princeton.cs.algs4.Queue;
import static org.junit.Assert.*;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest
     * item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param q1 A Queue in sorted order from least to greatest.
     * @param q2 A Queue in sorted order from least to greatest.
     * @return The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> res = new Queue<>();
        for (Item item : items) {
            Queue<Item> temp = new Queue<>();
            temp.enqueue(item);
            res.enqueue(temp);
        }
        return res;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and
     * q2. After
     * running this method, q1 and q2 will be empty, and all of their items will be
     * in the
     * returned queue.
     *
     * @param q1 A Queue in sorted order from least to greatest.
     * @param q2 A Queue in sorted order from least to greatest.
     * @return A Queue containing all of the q1 and q2 in sorted order, from least
     *         to
     *         greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> merge = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            merge.enqueue(getMin(q1, q2));
        }
        return merge;
    }

    /**
     * Returns a Queue that contains the given items sorted from least to greatest.
     */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items.size() <= 1) {
            return items;
        }
        // Queue<Queue<Item>> singleItemQueues = makeSingleItemQueues(items);
        int mid = items.size() / 2;
        Queue<Item> left = new Queue<>();
        Queue<Item> right = new Queue<>();
        for (Item i : items) {
            if (mid > 0) {
                left.enqueue(i);
            } else {
                right.enqueue(i);
            }
            mid--;
        }

        Queue<Item> leftSortedQueue = mergeSort(left);
        Queue<Item> rightSortedQueue = mergeSort(right);
        Queue<Item> res = mergeSortedQueues(leftSortedQueue, rightSortedQueue);

        return res;
    }

    public static void main(String[] args) {
        Queue<String> students = new Queue<>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Alan");
        students.enqueue("Ethan");
        students.enqueue("Harry");
        students.enqueue("Bob");
        students.enqueue("Ethan");

        System.out.println("Before sorting:");
        for (String stu : students) {
            System.out.println(stu);
        }

        // sorted by the lexicographic order
        students = mergeSort(students);

        System.out.println("\nAfter sorting:");
        for (String stu : students) {
            System.out.println(stu);
        }

        Queue<String> expected = new Queue<>();
        expected.enqueue("Alan");
        expected.enqueue("Alice");
        expected.enqueue("Bob");
        expected.enqueue("Ethan");
        expected.enqueue("Ethan");
        expected.enqueue("Harry");
        expected.enqueue("Vanessa");

        int size = expected.size();

        for (int i = 0; i < size; i++) {
            assertEquals(expected.dequeue(), students.dequeue());
        }
        System.out.println("\ntest passed!");
    }

}
