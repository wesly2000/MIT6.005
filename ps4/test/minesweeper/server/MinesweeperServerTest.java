/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    /**
     * Start a MinesweeperServer.
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startMinesweeperServer() throws IOException {
        final String[] args = new String[] {
                "--port", Integer.toString(PORT)
        };
        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
        serverThread.start();
        return serverThread;
    }

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

    // This test covers that the server could handle multiple clients
    @Test
    public void testMinesweeperServerHandleMultipleClients() throws IOException {
        Set<Socket> clients = new HashSet<>();
        Thread serverThread = startMinesweeperServer();
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
}
