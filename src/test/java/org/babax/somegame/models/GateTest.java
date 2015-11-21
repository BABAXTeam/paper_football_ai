package org.babax.somegame.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class GateTest {

    @Test
    public void testIsGoal() throws Exception {
        Gate gate = new Gate();
        gate.top = new Point(0, 0);
        gate.bottom = new Point(2, 0);
        assertTrue(gate.isGoal(1, 0));
        assertTrue(gate.isGoal(0, 0));
        assertTrue(gate.isGoal(2, 0));
        assertFalse(gate.isGoal(3, 1));
        assertFalse(gate.isGoal(0, 1));
    }
}