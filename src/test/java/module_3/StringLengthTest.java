package module_3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringLengthTest {
    @Test
    public void stringLengthTest() {

        String actual = "asdfgjhjl;';l123";

        assertTrue(actual.length() > 15, "The string length is less than 15 characters");
    }
}