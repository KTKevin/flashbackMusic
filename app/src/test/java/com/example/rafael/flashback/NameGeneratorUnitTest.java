package com.example.rafael.flashback;

import android.util.Log;

import com.example.rafael.flashback.utils.NameGenerator;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by null on 3/16/18.
 */

public class NameGeneratorUnitTest {
    NameGenerator gen = new NameGenerator();
    @Test
    public void testNameGen()
    {
        assertEquals("beaver0", gen.proxy(""));
        String a = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        String b = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        assertEquals(gen.proxy(a), gen.proxy(b));
        assertEquals(gen.proxy("a"),"manatee0");





    }

}
