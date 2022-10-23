import core.ServerListenerThread;

import java.io.*;

public class WebServer {
    public static void main(String[] args) {
        // Port number for http request
        final int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        // The maximum queue length for incoming connection
        try {
            ServerListenerThread webserverListenerThread = new ServerListenerThread(port);
            webserverListenerThread.start();
            System.out.println("Server statrted at http://localhost:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}