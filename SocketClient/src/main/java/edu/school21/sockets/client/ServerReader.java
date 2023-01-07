package edu.school21.sockets.client;

import edu.school21.sockets.json.JSONConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerReader extends Thread {
    private Scanner reader;
    private Socket socket;
    private PrintWriter writer;
    ServerWriter serverWriter;

    public ServerReader(Scanner reader, PrintWriter writer, Socket socket, ServerWriter serverWriter) {
        this.reader = reader;
        this.serverWriter = serverWriter;
        this.writer = writer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (reader.hasNextLine()) {
                String message = JSONConverter.parseToObject(reader.nextLine().trim()).getMessage();
                System.out.println(message);
            }
            Client.close(writer, reader, socket, -1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
