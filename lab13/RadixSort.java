import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1
     * length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        // 计算最长位数
        int len = 0;
        for (String s : asciis) {
            len = len > s.length() ? len : s.length();
        }
        String[] res = Arrays.copyOf(asciis, asciis.length);
        for (int i = len - 1; i >= 0; i--) {
            sortHelperLSD(res, i);
        }
        return res;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * 
     * @param asciis Input array of Strings
     * @param index  The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        int R = 256;
        int[] counts = new int[R + 1];
        for (String item : asciis) {
            int c = charAtOrMinChar(index, item);
            counts[c]++;
        }

        int[] starts = new int[R + 1];
        int pos = 0;
        for (int i = 0; i < R + 1; i++) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            String item = asciis[i];
            int c = charAtOrMinChar(index, item);
            int place = starts[c];
            sorted[place] = item;
            starts[c]++;
        }

        System.arraycopy(sorted, 0, asciis, 0, asciis.length);
    }

    // 获取对应字符串位数的字符
    private static int charAtOrMinChar(int index, String s) {
        if (index >= 0 && index < s.length()) {
            return s.charAt(index) + 1;
        } else {
            return 0;
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the
     * sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String
     *               at start)
     * @param end    int for where to end sorting in this method (does not include
     *               String at end)
     * @param index  the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] asciis = new String[] { "56", "112", "94", "4", "9", "82", "394", "80" };
        String[] res = RadixSort.sort(asciis);
        for (String s : res) {
            System.out.print(s + " ");
        }

        System.out.println();

        String[] asciis2 = new String[] { "  ", "      ", "    ", " " };
        String[] res2 = RadixSort.sort(asciis2);
        for (String s : res2) {
            System.out.print(s + ",");
        }
    }
}
