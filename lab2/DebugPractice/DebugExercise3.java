/**
 * Created by jug on 1/22/18.
 */
public class DebugExercise3 {
    public static long countTurnips(In in) {
        long totalTurnips = 0;
        while (!in.isEmpty()) {
            String vendor = in.readString();
            String foodType = in.readString();
            double cost = in.readDouble();
            long numAvailable = in.readLong();// 发生溢出
            if (foodType.equals("turnip")) {
                totalTurnips = totalTurnips + numAvailable;
            }
            in.readLine();
        }
        return totalTurnips;
    }

    public static void main(String[] args) {
        In in = new In("foods.csv");
        System.out.println(countTurnips(in));
    }
}
