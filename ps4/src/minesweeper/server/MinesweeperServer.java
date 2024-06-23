/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Stream;

import minesweeper.Board;

/**
 * Multiplayer Minesweeper server.
 */
public class MinesweeperServer {

    // System thread safety argument
    //   TODO Problem 5

    /** Default server port. */
    private static final int DEFAULT_PORT = 4444;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    /** Default square board size. */
    private static final int DEFAULT_SIZE = 10;

    /** Socket for receiving incoming connections. */
    private final ServerSocket serverSocket;
    /** True if the server should *not* disconnect a client after a BOOM message. */
    private final boolean debug;

    private final Board board;

    private int currentPlayer = 0;

    // TODO: Abstraction function, rep invariant, rep exposure

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * @param debug debug mode flag
     * @param sizeX the number of columns of the board
     * @param sizeY the number of rows of the board
     * @throws IOException if an error occurs opening the server socket
     */
    public MinesweeperServer(int port, boolean debug, int sizeX, int sizeY, Optional<File> file) throws IOException {
        serverSocket = new ServerSocket(port);
        this.debug = debug;

        if(file.isPresent()) {
            // read from a board file
            board = readBoardFromFile(file.get());
        }else{
            assert sizeX > 0 && sizeY > 0: "sizeX and sizeY must be positive";
            // A cell contains a bomb with prob 25%
            board = new Board(sizeY, sizeX, (r, c) -> Math.random() < .25 ? Board.BOMB : Board.NOTBOMB);
        }
    }

    private class Point{
        final int r, c;
        Point(int r, int c){ this.r = r; this.c = c; }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof Point)){ return false; }
            Point p = (Point)o;
            return p.r == r && p.c == c;
        }

        @Override
        public int hashCode(){
            return Objects.hash(r, c);
        }
    }

    /**
     * Generate a new board from a given board file (a text file)
     * @param file the board file to be read, which should have format below:
     *      rowNum colNum
     *      ----------> (colNum)
     *      | c c c c c
     *      | c c c c c
     *      | c c c c c c=0(NOTBOMB), 1(BOMB)
     *      v
     *      (rowNum)
     * @return a board generated from file
     */
    private Board readBoardFromFile(File file) throws IOException {
        Set<Point> bombPoint = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        int[] sizes = extractIntegersFromString(br.readLine());
        int rowNum = sizes[0];
        int colNum = sizes[1];
        String line = null;
        for(int i = 0; i < rowNum; i++){
            line = br.readLine();
            int[] cellIsBomb = extractIntegersFromString(line);
            for(int j = 0; j < colNum; j++)
                if(cellIsBomb[j] == 1) // Cell i,j contains a bomb
                    bombPoint.add(new Point(i, j));
        }

        return new Board(rowNum, colNum, (r, c) -> bombPoint.contains(new Point(r, c)));
    }

    /**
     * Extract integers from a string of a single line
     * @param line the string representing a line
     * @return the integer array extracted from line
     */
    private int[] extractIntegersFromString(String line){
        return Stream.of(line.split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            Socket socket = serverSocket.accept();
            currentPlayer++;

            // handle the client
            new Thread(
                    () -> {
                        try {
                            handleConnection(socket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            // SOLVED: finally block does not execute
                            // Adding handler in test to avoid the main thread
                            // terminates.
                            try{
                                socket.close();
                                currentPlayer--;
                            }catch (IOException e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
            ).start();
        }
    }

    /**
     * Handle client connections. Returns when client disconnects.
     * 
     * @param socket socket where the client is connected
     * @throws IOException if the connection encounters an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Print hello message once the server accept a connection
        out.println(String.format("Welcome to Minesweeper. Board: %d columns by %d rows. " +
                "Players: %d including you. Type 'help' for help.", board.getCol(), board.getRow(), currentPlayer));

        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                String output = handleRequest(line);
                if (output != null) {
                    // TODO: Consider improving spec of handleRequest to avoid use of null
                    out.println(output);
                    if(output.equals("BOOM!") && !debug)
                        break;
                }else{
                    // Currently, only BYE message returns null;
                    // and upon receiving BYE, the server disconnects without any return.
                    // Therefore, we close the socket upon receiving null.
                    break;
                }
            }
        } finally {
            // SOLVED: finally block does not execute
            // Adding handler in test to avoid the main thread
            // terminates.
            out.close();
            in.close();
        }
    }

    /**
     * Handler for client input, performing requested operations and returning an output message.
     * 
     * @param input message from client
     * @return message to client, or null if none
     */
    private String handleRequest(String input) {
        String regex = "(look)|(help)|(bye)|"
                     + "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
        if ( ! input.matches(regex)) {
            // invalid input
            // TODO Problem 5
            return "Invalid command";
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) {
            // 'look' request
            // TODO Problem 5
            return board.toString();
        } else if (tokens[0].equals("help")) {
            // 'help' request
            // TODO Problem 5
            return  "MESSAGE ::= ( LOOK | DIG | FLAG | DEFLAG | HELP_REQ | BYE ) NEWLINE\n" +
                    "LOOK ::= \"look\"\n" +
                    "DIG ::= \"dig\" SPACE X SPACE Y\n" +
                    "FLAG ::= \"flag\" SPACE X SPACE Y\n" +
                    "DEFLAG ::= \"deflag\" SPACE X SPACE Y\n" +
                    "HELP_REQ ::= \"help\"\n" +
                    "BYE ::= \"bye\"\n" +
                    "NEWLINE ::= \"\\n\" | \"\\r\" \"\\n\"?\n" +
                    "X ::= INT\n" +
                    "Y ::= INT\n" +
                    "SPACE ::= \" \"\n" +
                    "INT ::= \"-\"? [0-9]+";
        } else if (tokens[0].equals("bye")) {
            // 'bye' request
            // TODO Problem 5
            return null;
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                // 'dig r y' request
                // TODO Problem 5
                // Note that in board data structure,
                // we use (row, col) rep, where row=y, col=x here.
                boolean isBomb = board.dig(y, x);
                if (isBomb)
                    return "BOOM!";
                else
                    return board.toString();
            } else if (tokens[0].equals("flag")) {
                // 'flag r y' request
                // TODO Problem 5
                board.flag(y, x);
                return board.toString();
            } else if (tokens[0].equals("deflag")) {
                // 'deflag r y' request
                // TODO Problem 5
                board.deflag(y, x);
                return board.toString();
            }
        }
        // TODO: Should never get here, make sure to return in each of the cases above
        throw new UnsupportedOperationException();
    }

    /**
     * Start a MinesweeperServer using the given arguments.
     * 
     * <br> Usage:
     *      MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]
     * 
     * <br> The --debug argument means the server should run in debug mode. The server should disconnect a
     *      client after a BOOM message if and only if the --debug flag was NOT given.
     *      Using --no-debug is the same as using no flag at all.
     * <br> E.g. "MinesweeperServer --debug" starts the server in debug mode.
     * 
     * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * <br> E.g. "MinesweeperServer --port 1234" starts the server listening on port 1234.
     * 
     * <br> SIZE_X and SIZE_Y are optional positive integer arguments, specifying that a random board of size
     *      SIZE_X*SIZE_Y should be generated.
     * <br> E.g. "MinesweeperServer --size 42,58" starts the server initialized with a random board of size
     *      42*58.
     * 
     * <br> FILE is an optional argument specifying a file pathname where a board has been stored. If this
     *      argument is given, the stored board should be loaded as the starting board.
     * <br> E.g. "MinesweeperServer --file boardfile.txt" starts the server initialized with the board stored
     *      in boardfile.txt.
     * 
     * <br> The board file format, for use with the "--file" option, is specified by the following grammar:
     * <pre>
     *   FILE ::= BOARD LINE+
     *   BOARD ::= X SPACE Y NEWLINE
     *   LINE ::= (VAL SPACE)* VAL NEWLINE
     *   VAL ::= 0 | 1
     *   X ::= INT
     *   Y ::= INT
     *   SPACE ::= " "
     *   NEWLINE ::= "\n" | "\r" "\n"?
     *   INT ::= [0-9]+
     * </pre>
     * 
     * <br> If neither --file nor --size is given, generate a random board of size 10x10.
     * 
     * <br> Note that --file and --size may not be specified simultaneously.
     * 
     * @param args arguments as described
     */
    public static void main(String[] args) {
        // Command-line argument parsing is provided. Do not change this method.
        boolean debug = false;
        int port = DEFAULT_PORT;
        int sizeX = DEFAULT_SIZE;
        int sizeY = DEFAULT_SIZE;
        Optional<File> file = Optional.empty();

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--debug")) {
                        debug = true;
                    } else if (flag.equals("--no-debug")) {
                        debug = false;
                    } else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        }
                    } else if (flag.equals("--size")) {
                        String[] sizes = arguments.remove().split(",");
                        sizeX = Integer.parseInt(sizes[0]);
                        sizeY = Integer.parseInt(sizes[1]);
                        file = Optional.empty();
                    } else if (flag.equals("--file")) {
                        sizeX = -1;
                        sizeY = -1;
                        file = Optional.of(new File(arguments.remove()));
                        if ( ! file.get().isFile()) {
                            throw new IllegalArgumentException("file not found: \"" + file.get() + "\"");
                        }
                    } else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]");
            return;
        }

        try {
            runMinesweeperServer(debug, file, sizeX, sizeY, port);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Start a MinesweeperServer running on the specified port, with either a random new board or a
     * board loaded from a file.
     * 
     * @param debug The server will disconnect a client after a BOOM message if and only if debug is false.
     * @param file If file.isPresent(), start with a board loaded from the specified file,
     *             according to the input file format defined in the documentation for main(..).
     * @param sizeX If (!file.isPresent()), start with a random board with width sizeX
     *              (and require sizeX > 0).
     * @param sizeY If (!file.isPresent()), start with a random board with height sizeY
     *              (and require sizeY > 0).
     * @param port The network port on which the server should listen, requires 0 <= port <= 65535.
     * @throws IOException if a network error occurs
     */
    public static void runMinesweeperServer(boolean debug, Optional<File> file, int sizeX, int sizeY, int port) throws IOException {
        
        // TODO: Continue implementation here in problem 4
        
        MinesweeperServer server = new MinesweeperServer(port, debug, sizeX, sizeY, file);
        server.serve();
    }
}
