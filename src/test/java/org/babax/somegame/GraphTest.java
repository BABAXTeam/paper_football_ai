package org.babax.somegame;

import org.babax.somegame.models.Field;
import org.babax.somegame.models.Point;
import org.babax.somegame.models.Team;
import org.babax.somegame.models.Vertex;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dos65 on 21.11.15.
 */
public class GraphTest {

    @Test
    public void testVertexCount() throws Exception {
        Field f = Fixture.getField(2, 2);
        Graph g = new Graph(f);
        assertEquals((f.length+1) * (f.width+1), g.key2Vertex.size());
    }

    @Test
    public void testEdges() throws Exception {
        Field f = Fixture.getField(2, 2);
        Graph g = new Graph(f);
        int count = 0;
        for(Vertex v : g.key2Vertex.values()) {
            count += v.getAccepted().size();
            v.getAccepted().forEach(System.out::println);
        }
        assertEquals(24, count);
    }

    @Test
    public void testFindSome() throws Exception {
        Field f = Fixture.getField(5, 4);
        Graph g = new Graph(f);
        System.out.println(f.gate2);
        boolean result = g.findMove(new Point(0, 0), f.gate2);
        assertTrue(result);
    }
}