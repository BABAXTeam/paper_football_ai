package org.babax.somegame;

import org.babax.somegame.models.Point;
import org.babax.somegame.models.Team;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.babax.somegame.Util.getInt;

public class Client {
    private final String address;
    private final int port;
    private final BufferedReader in;
    private final BufferedWriter out;
    private Socket socket;
    private Engine engine = new Engine();
    private static Scanner keyboard = new Scanner(System.in);

    public final static String START_GAME = "startGame";
    public final static String CONFIRM_REGIST = "confirmRegistration";
    public final static String MY_NEXT_MOVE = "move";
    public final static String YOU_MOVE = "nextMove";
    public final static String PLAYER_MOVED = "playerMoved";
    public final static String CONFIRM_START_GAME = "confirmStartGame";
    public final static String MOVE_OK = "moveOK";
    public final static String GAME_OVER = "gameOver";
    public final static String FAILED_REGISTRATION = "failedRegistration";
    public final static String ILLEGAL_MOVE = "illegalMove";
    public final static String REGIST = "registerTeam";
    public static final String CONFIRM_GAME_OVER = "gameOverConfirm";
    public static final String SHUTDOWN = "shutdown";

    static String teamName = "BABAX";


    public Client(final String address, final int port) throws IOException {
        this.address = address;
        this.port = port;
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private String readMsg() throws IOException {
        return in.readLine();
    }

    public void play() {
        boolean gameOn = true;
        try {
            sendMsg(REGIST + " " + getTeamName());
            while (gameOn) {
                final String nextMessage = readMsg();
                gameOn = handleGameStatus(nextMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTeamName() {
        System.out.print("Enter your Team Name:");
        return teamName;
    }

    protected boolean handleGameStatus(final String nextMessage)
            throws IOException {
        System.out.println("get message " + nextMessage);

        final String[] args = nextMessage.split(" ");
        List<String> params = Stream.of(args)
                .skip(1)
                .collect(toList());

        switch (args[0]) {
            case START_GAME:
                System.out.println("Start the game:" + new Date());
                engine = new Engine();
                engine.init(params);
                sendMsg(CONFIRM_START_GAME);
                break;
            case YOU_MOVE:
                sendMsg(MY_NEXT_MOVE + " " + getNextMove());
                break;
            case PLAYER_MOVED:
                setArgument(args);
                break;
            case MOVE_OK:
                System.out.println("Good to know that I moved OK");
                break;
            case ILLEGAL_MOVE:
                System.out.println("Ups...");
                break;
            case CONFIRM_START_GAME:
                System.out.println("Game started");
                break;
            case CONFIRM_REGIST:
                System.out.println("Team registered");
                break;
            case FAILED_REGISTRATION:
                System.out.println("Now what???");
                break;
            case GAME_OVER:
                System.out.println("GAME OVER...");
                if ("0".equals(args[1])) {
                    System.out.println("You lost!");
                } else if ("1".equals(args[1])) {
                    System.out.println("You win!");
                }
                sendMsg(CONFIRM_GAME_OVER);
                break;
            case SHUTDOWN:
                System.out.println("Shut down...");
                return false;
            default:
                System.out.println("Unknown command ");
        }
        return true;
    }

    private void setArgument(String[] args) {
        if(args == null || args.length < 6){
            System.out.println("ILLEGAL HADNLE MOVE: " + Arrays.toString(args));
            System.out.println("Maybe we win? ^_^");
            return;
        }
        Point move = new Point(getInt(args[1]), getInt(args[2]));
        Point keeper1 = new Point(getInt(args[3]), getInt(args[4]));
        Point keeper2 = new Point(getInt(args[5]), getInt(args[6]));
        engine.handleEnemyMove(move, keeper1, keeper2);
        System.out.println("Player moved to " + args[1] + " " + args[2]);
    }

    private String getNextMove() {
        System.out.print("Enter your next move as x y");
        Point point = engine.findNextMove();
        final int x = point.x;
        final int y = point.y;

        Point keeper1 , keeper2;
        if(engine.getTeam() == Team.FIRST) {
            keeper1 = engine.keeper1_1;
            keeper2 = engine.keeper1_2;
        } else {
            keeper1 = engine.keeper2_1;
            keeper2 = engine.keeper2_2;
        }
        return "" + x + " " + y + " " + keeper1.x + " " + keeper1.y + " " + keeper2.x + " " + keeper2.y;
    }

    protected void sendMsg(final String message) throws IOException {
        System.out.println("send " + message);
        out.write(message);
        out.write("\r\n");
        out.flush();
    }


    public static void main(final String... args)
            throws IOException, InterruptedException {

        Runnable r = () -> {
            try {
                if(args.length > 2 && args[2] != null)
                    teamName = args[2];
                Client client = new Client(args[0], Integer.valueOf(args[1]));
                client.play();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        new Thread(r).start();
    }
}
