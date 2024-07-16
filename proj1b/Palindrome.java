public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> output = new LinkedListDeque<>();
        for (char letter : word.toCharArray()) {
            output.addLast(letter);
        }
        return output;
    }

    public boolean isPalindrome(String word) {

        return isPalindromeHelper(word, 0, word.length() - 1);
    }

    private boolean isPalindromeHelper(String word, int first, int last) {

        if (last <= first) {
            return true;
        } else if (word.charAt(first) != word.charAt(last)) {
            return false;
        }
        return isPalindromeHelper(word, first + 1, last - 1);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        return isPalindromeHelper(word, cc, 0, word.length() - 1);
    }

    private boolean isPalindromeHelper(String word, CharacterComparator cc, int first, int last) {
        if (last <= first) {
            return true;
        } else if (!cc.equalChars(word.charAt(first), word.charAt(last))) {
            return false;
        }
        return isPalindromeHelper(word, cc, first + 1, last - 1);
    }
}
