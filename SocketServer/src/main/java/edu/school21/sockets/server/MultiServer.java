package edu.school21.sockets.server;

import edu.school21.sockets.service.MessageService;
import edu.school21.sockets.service.RoomsService;
import edu.school21.sockets.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class MultiServer {
    private static UsersService usersService;
    protected static RoomsService roomsService;
    private static MessageService messageService;
    private ServerSocket serverSocket;
    private static final List<ClientServer> clients = new ArrayList<>();
    protected static final List<String> rooms = new ArrayList<>();
    private static int numberOfClients = 1;

    @Autowired
    public MultiServer(UsersService usersService, RoomsService roomsService, MessageService messageService) {
        this.usersService = usersService;
        this.roomsService = roomsService;
        this.messageService = messageService;
    }

    public void run(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientServer client = new ClientServer(socket, usersService, messageService);
                clients.add(client);
                System.out.println("New client connected! Number of clients: " + numberOfClients++);
                client.start();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            stop();
        }
    }

    public static synchronized void sendMessageToAll(String username, String roomTitle, String message) {
        usersService.createMessage(username, roomTitle, message);
        clients.stream().filter(c -> roomTitle.equals(c.getRoomTitle())).forEach(c -> c.sendMsg(username + ": " + message));
    }

    public static synchronized void sendInfoMessage(String username, String roomTitle, String message) {
        clients.stream().filter(c -> roomTitle.equals(c.getRoomTitle())).forEach(c -> c.sendMsg(username + ": " + message));
    }

    public static void removeClient(ClientServer client) {
        System.out.println("The user " + client.getUsername() + " has left the chat");
        clients.remove(client);
        numberOfClients--;
    }

    private void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.exit(-1);
    }
}