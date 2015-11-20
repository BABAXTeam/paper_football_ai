package org.babax.somegame;

import org.babax.somegame.models.Field;

import java.util.Arrays;

/**
 * Created by blvp on 21.11.15.
 */
public class Fixture {

    public static Field dummyField(){
        Field field = new Field();
        field.init(30, 60);
        return field;
    }

    public static Engine dummyEngine() {
        Engine engine = new Engine();
        engine.init(Arrays.asList(
                "L1",
                "30", "60",
                "1", "1",
                "0"
        ));
        return engine;
    }
}
