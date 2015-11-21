package org.babax.somegame;

import org.babax.somegame.models.Field;
import org.babax.somegame.models.Point;
import org.babax.somegame.models.Team;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by blvp on 21.11.15.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class EngineTest {

    @Test
    public void testInitWithoutTraps() throws Exception {
        Engine engine = new Engine();
        engine.init(Arrays.asList(
                "L1",
                "31", "61",
                "1", "1",
                "0"
        ));
        assertEquals(engine.getLevel(), "L1");
        Field dummyField = Fixture.dummyField();
        Field expectedField = engine.getField();
        assertEquals(expectedField.width, dummyField.width);
        assertEquals(expectedField.length, dummyField.length);
        assertEquals(expectedField.gate1.top.x, dummyField.gate1.top.x);
        assertEquals(expectedField.gate1.top.x, dummyField.gate1.top.x);
        assertEquals(expectedField.gate2.top.y, dummyField.gate2.top.y);
        assertEquals(expectedField.gate2.top.y, dummyField.gate2.top.y);
        assertNotNull(expectedField.traps);
        assertEquals(engine.getPosition().x, 1);
        assertEquals(engine.getPosition().y, 1);
    }

    @Test
    public void testEngineInitWithTraps() {

        Engine engine = new Engine();
        engine.init(Arrays.asList(
                "L1",
                "30", "60",
                "1", "1",
                "1",
                "3", "3"
        ));

        assertEquals(engine.getField().traps.size(), 1);
        Point actualTrapPoint = engine.getField().traps.iterator().next();
        assertEquals(3, actualTrapPoint.x);
        assertEquals(3, actualTrapPoint.y);
    }

    @Test
    public void testFindNextMove() throws Exception {
        Engine engine = new Engine();
        engine.init(Arrays.asList(
                "L1",
                "5", "4",
                "0", "0",
                "0"
        ));
        Point nextMove = engine.findNextMove();
        assertEquals(1, nextMove.x);
        assertEquals(1, nextMove.y);
    }

    @Test
    public void testEnemyMove() throws Exception {
        Engine engine = Fixture.dummyEngine();
        engine.handleEnemyMove(new Point(1, 2));
        assertEquals(1, engine.getPosition().x);
        assertEquals(2, engine.getPosition().y);
        assertEquals(Team.SECOND, engine.getTeam());
    }

    @Test
    public void testFindMove() {
        Engine engine = Fixture.dummyEngine();
        Point initial = new Point(0, 0);
        engine.setPosition(initial);
        Point p = engine.findNextMove();
        assertNotEquals(p, initial);
        assertEquals(p, engine.getPosition());
    }

    private Engine getFirstGameEngine() {
        Engine engine = new Engine();
        engine.init(
                Arrays.asList(
                        (
                                "1 31 61 15 30" +
                                        " 18 11 25 16 31 23 58 16 25" +
                                        " 6 49 28 42 12 46 15" +
                                        " 20 28 5 15 3 30 35" +
                                        " 26 48 2 45 25 32" +
                                        " 2 0 12 40 18 22 19 9"
                        ).split(" ")
                )
        );
        return engine;
    }

    @Ignore
    @Test
    public void testFirstGameVSSelf() throws Exception {
        ServerEmulation emulation = new ServerEmulation();
        emulation.run( "1 31 61 15 30" +
                " 18 11 25 16 31 23 58 16 25" +
                " 6 49 28 42 12 46 15" +
                " 20 28 5 15 3 30 35" +
                " 26 48 2 45 25 32" +
                " 2 0 12 40 18 22 19 9");
    }

    private String readResource(String name) {
        try {
            File file = Paths.get("/home/blvp/global/somegame/src/test/resources/game3").toFile();
            return Files.lines(file.toPath())
                    .reduce(String::concat).get().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGame3() throws InterruptedException {
        Engine engine = new Engine();
        long start1 = System.currentTimeMillis();
        engine.init(Arrays.asList(readResource("game3").split(" ")));
        System.out.println("init: "+ (System.currentTimeMillis() - start1));
        System.out.println(Util.getEdgesCount(engine.graph));
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            System.out.println(engine.findNextMove());
            System.out.println("End:" + (System.currentTimeMillis() - start) / 1000.0);
        }
    }

}