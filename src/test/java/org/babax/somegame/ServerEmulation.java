package org.babax.somegame;

import org.babax.somegame.models.Point;

import java.util.Arrays;

/**
 * Created by dos65 on 21.11.15.
 */
public class ServerEmulation {

    Engine player1;
    Engine player2;

    private void initEngine(Engine engine, String params) {
        engine.init(Arrays.asList(params.split(" ")));
    }

    public void run(String params) {
        player1 = new Engine();
        initEngine(player1, params);

        player2 = new Engine();
        initEngine(player2, params);

        int middle = player1.getField().length /2;

        while (true) {
            Point p = player1.getPosition();
            do {
                Point next = player1.findNextMove();
                System.out.println("player 1 move to:" + p);
                if(next.equals(p))
                    System.out.println("WTF");
                p = next;
            } while(p.y != middle);
            if(p == Point.NONE) {
                System.out.println("SASIA 1");
                return;
            }
            player2.setPosition(p);
            p = player2.getPosition();
            do {
                p = player2.findNextMove();
                System.out.println("player 2 move to:" + p);
            } while(p.y != middle);
            if(p == Point.NONE) {
                System.out.println("SASIA 2");
                return;
            }
            player1.setPosition(p);
        }
    }

}
