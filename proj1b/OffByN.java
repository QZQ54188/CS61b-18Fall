public class OffByN implements CharacterComparator {
    private int distance;

    public OffByN(int n) {
        distance = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == distance;
    }
}
