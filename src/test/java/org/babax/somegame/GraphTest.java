package org.babax.somegame;

import org.babax.somegame.models.Field;
import org.babax.somegame.models.Team;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dos65 on 21.11.15.
 */
public class GraphTest {

    @Test
    public void testSimpleGraph() throws Exception {
        Field f = Fixture.dummyField();
        Graph g = new Graph(f);
        assertEquals(g.key2Vertex.size(), f.length * f.width);
    }

    @Test
    public void testSetGoalsSimpe() {
        Field f = Fixture.mini5x10Field();
        Graph g = new Graph(f);
        g.initGoal(Team.FIRST);
        assertEquals(1, g.goals.size());
        assertEquals(2, g.goals.iterator().next().getX());
    }

    @Test
    public void testSetGoals() {
        Field f = Fixture.dummyField();
        Graph g = new Graph(f);
        g.initGoal(Team.FIRST);
        assertEquals(6, g.goals.size());
    }
}