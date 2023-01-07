package edu.school21.sockets.client;

import edu.school21.sockets.json.JSONConverter;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

public class ServerWriter extends Thread {
    private final Scanner scanner = new Scanner(System.in);
    private final PrintWriter writer;
    Scanner reader;
    Socket socket;

    public ServerWriter(PrintWriter writer, Scanner reader, Socket socket) {
        this.writer = writer;
        this.reader = reader;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = scanner.nextLine();
                writer.println(Objects.requireNonNull(JSONConverter.makeJSONObject(message, LocalDateTime.now())).toJSONString());
                if ("exit".equals(message)) break;
            }
            Client.close(writer, reader, socket, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
