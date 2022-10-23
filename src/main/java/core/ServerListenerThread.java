package core;


import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerListenerThread extends Thread {

    private int port;
    private ServerSocket serverSocket;


    public ServerListenerThread(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
    }
    /*
        Makes our server listen to specific port
    */
    @Override
    public void run() {
        try
        {
            // keep accepting connection while server is not closed and has connected to the node
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                // accepting and getting the connection
                Socket socket = serverSocket.accept();
                System.out.println("Connected: " + socket.getInetAddress());
                System.out.println("http://localhost:" + port);
                // To read input from the client
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                HttpRequest request = HttpRequest.parse(input);
                // establishing connection without connection queue for better server performance and sending the request to server
                ConnWorkerThread connectionWorkerThread = new ConnWorkerThread(socket, request);
                connectionWorkerThread.start();
            }
        }
        // closing socket connection
        catch (IOException e)
        {
            System.out.println("Error while setting socket: ");
            e.printStackTrace();

        } finally {
            // handling the socket connection closing
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) { }
            }
        }
    }
}
