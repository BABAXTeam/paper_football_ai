package org.babax.somegame.models;

import org.babax.somegame.Fixture;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {

    @Test
    public void testIsBorder() throws Exception {
        Field f = Fixture.getField(5, 4);
        assertTrue(f.isBorder(0,0));
        assertTrue(f.isBorder(0,1));
        assertFalse(f.isBorder(1,1));
        assertTrue(f.isBorder(1,0));
    }

    @Test
    public void testIsBorderSimple() throws Exception {
        Field f = Fixture.getField(1, 1);
        assertTrue(f.isBorder(0,0));
        assertTrue(f.isBorder(0,1));
        assertTrue(f.isBorder(1,1));
        assertTrue(f.isBorder(1,0));
    }

    @Test
    public void testIsBorder1() throws Exception {

    }
}