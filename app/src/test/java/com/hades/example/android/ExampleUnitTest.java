package com.hades.example.android;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test_addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String s = "uat";
        System.out.println(s.toUpperCase());
    }
}