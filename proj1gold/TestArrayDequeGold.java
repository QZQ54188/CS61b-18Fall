import static org.junit.Assert.*;

import java.util.ArrayDeque;

import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> stuArrDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solArrDeque = new ArrayDequeSolution<>();
        String operation = "";

        while (true) {
            double rng = StdRandom.uniform();
            Integer temp = (int) (10 * rng);

            if (rng < 0.25) {
                stuArrDeque.addFirst(temp);
                solArrDeque.addFirst(temp);
                operation += "addFirst(" + temp + ")" + "\n";
                assertEquals(operation, solArrDeque.get(0), stuArrDeque.get(0));
            } else if (0.25 <= rng && rng < 0.5) {
                stuArrDeque.addLast(temp);
                solArrDeque.addLast(temp);
                operation += "addLast(" + temp + ")" + "\n";
                assertEquals(operation, stuArrDeque.get(stuArrDeque.size() - 1),
                        solArrDeque.get(solArrDeque.size() - 1));
            } else if (0.5 <= rng && 0.75 > rng && solArrDeque.size() != 0) {
                operation += "removeFirst()" + "\n";
                assertEquals(operation, solArrDeque.removeFirst(), solArrDeque.removeFirst());
            } else if (0.75 <= rng && solArrDeque.size() != 0) {
                operation += "removeLast()" + "\n";
                assertEquals(operation, stuArrDeque.removeLast(), solArrDeque.removeLast());
            }
        }

    }
}
