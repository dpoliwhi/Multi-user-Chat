package edu.school21.sockets;

import edu.school21.sockets.server.MultiServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SocketServer implements CommandLineRunner {
    MultiServer server;

    public SocketServer(MultiServer server) {
        this.server = server;
    }

    public static void main(String[] args) {
        SpringApplication.run(SocketServer.class, args);
    }

    private static int parseArgs(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--port=")) {
            System.err.println("Run program with 1 arguments (--port=????)");
            System.exit(-1);
        }
        return Integer.parseInt(args[0].substring("--port=".length()));
    }

    @Override
    public void run(String... args) throws Exception {
        int port = parseArgs(args);
        server.run(port);
    }
}
