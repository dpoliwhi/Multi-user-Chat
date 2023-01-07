package edu.school21.sockets.application;

import edu.school21.sockets.client.Client;

import java.io.IOException;

public class Main {
    private static final String IP = "127.0.0.1";

    public static void main(String[] args) {
        int port = parseArgs(args);
        try {
            Client client = new Client(IP, port);
            client.start();
        } catch (RuntimeException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int parseArgs(String[] args) {
        if (args.length != 1 && !args[0].startsWith("--server-port=")) {
            System.err.println("Run program with 1 arguments (--server-port=????)");
            System.exit(-1);
        }
        return Integer.parseInt(args[0].substring("--server-port=".length()));
    }
}
