package dev.comfast.rgx;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.comfast.rgx.RgxApi.rgx;
import static org.junit.jupiter.api.Assertions.*;

class RgxTest {
    private static final String text1 = "abc def acc";

    @Test
    public void simpleRegex() {
        String result = rgx(".*def").match(text1).get();
        assertEquals(result, "abc def");
    }

    @Test
    public void allMatch() {
        List<String> result = rgx("a.c").matchAllAsString(text1);
        assertEquals(result.get(0), "abc");
        assertEquals(result.get(1), "acc");

        RgxMatch match = rgx("(.)b").match("ab cb db");
    }

}