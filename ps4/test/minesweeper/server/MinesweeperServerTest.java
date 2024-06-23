/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import org.junit.Test;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Tests for MinesweeperServer.
 *
 * This class runs the MinesweeperServerTest tests against MinesweeperServer, as
 * well as tests for that particular implementation.
 *
 */
public class MinesweeperServerTest {
    
    // TODO
    private static final String LOCALHOST = "127.0.0.1";
    private static final int PORT = 4000 + new Random().nextInt(1 << 15);

    private static final int MAX_CONNECTION_ATTEMPTS = 10;
    private static final int MAX_CLIENTS = 5; // Max number of clients, for tests only
    private static final String BOARDS_PKG = "autograder/boards/";

//    /**
//     * Start a MinesweeperServer.
//     * @return thread running the server
//     * @throws IOException if the board file cannot be found
//     */
//    private static Thread startMinesweeperServer() throws IOException {
//        final String[] args = new String[] {
//                "--port", Integer.toString(PORT)
//        };
//        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
//        serverThread.start();
//        return serverThread;
//    }

    /**
     * Connect to a MinesweeperServer and return the connected socket.
     * @param server abort connection attempts if the server thread dies
     * @return socket connected to the server
     * @throws IOException if the connection fails
     */
    private static Socket connectToMinesweeperServer(Thread server) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                Socket socket = new Socket(LOCALHOST, PORT);
                return socket;
            } catch (ConnectException ce) {
                if ( ! server.isAlive()) {
                    throw new IOException("Server thread not running");
                }
                if (++attempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new IOException("Exceeded max connection attempts", ce);
                }
            }
        }
    }

    /**
     * Start a MinesweeperServer in debug mode with a board file from BOARDS_PKG.
     * @param boardFile board to load
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startMinesweeperServer(Optional<String> boardFile) throws IOException {
        final String[] args;
        if(boardFile.isPresent()){
            final URL boardURL = ClassLoader.getSystemClassLoader().getResource(BOARDS_PKG + boardFile.get());
            if (boardURL == null) {
                throw new IOException("Failed to locate resource " + boardFile);
            }
            final String boardPath;
            try {
                boardPath = new File(boardURL.toURI()).getAbsolutePath();
            } catch (URISyntaxException urise) {
                throw new IOException("Invalid URL " + boardURL, urise);
            }
            args = new String[] {
                    "--debug",
                    "--port", Integer.toString(PORT),
                    "--file", boardPath
            };
        }else{
            args = new String[] {
                    "--port", Integer.toString(PORT)
            };
        }
        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
        serverThread.start();
        return serverThread;
    }

    // This test covers that the server could handle multiple clients
    @Test
    public void testMinesweeperServerHandleMultipleClients() throws IOException {
        Set<Socket> clients = new HashSet<>();
        Thread serverThread = startMinesweeperServer(Optional.ofNullable(null));
        try{
            while(true){
                Socket socket = connectToMinesweeperServer(serverThread);
                clients.add(socket);
                if (clients.size() > MAX_CLIENTS)
                    throw new IOException("Too many client connections");
            }
        }catch(IOException e){
            // Close all the clients
            for(Socket client : clients){
                client.close();
            }
            assertEquals("Too many client connections", e.getMessage());
            assertEquals(String.format("expect %d clients", MAX_CLIENTS + 1), MAX_CLIENTS + 1, clients.size());
        }
    }

    // This test covers that the server could read board file correctly
    @Test
    public void testReadBoardFromFile () throws IOException, InterruptedException {
        Thread thread = startMinesweeperServer(Optional.of("board_file_5"));
        Socket socket = connectToMinesweeperServer(thread);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 3 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look"); // debug mode is on
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("1 1          ", in.readLine());
        assertEquals("- 1          ", in.readLine());

        out.println("bye");
        // If we ignore the last readLine(), after out.println("bye")
        // the main thread exits, causing the program to terminate.
        // Therefore, thread-0 would not hit the finally block (because the
        // program terminates).

        // Also, if the socket is closed by the client (e.g., closed by our test),
        // since the server thread continues to run, it tries to
        assertNull(in.readLine());
    }
}
