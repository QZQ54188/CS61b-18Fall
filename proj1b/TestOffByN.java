import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    @Test
    public void testOffByN() {
        OffByN offBy5 = new OffByN(5);
        assertTrue(offBy5.equalChars('0', '5'));
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertTrue(offBy5.equalChars('c', 'h'));
        assertFalse(offBy5.equalChars('0', '0'));
        assertFalse(offBy5.equalChars('r', 'a'));
        assertFalse(offBy5.equalChars('l', 'k'));
        assertFalse(offBy5.equalChars('d', '4'));
    }
}
