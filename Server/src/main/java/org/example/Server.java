package org.example;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private HashMap<String, String> users;
    private final int LIMIT = 3;
    private Logger logger;
    private ArrayList<ClientService> clients;

    private int port;

    public Server(int port) {
        this.users = new HashMap<>() {{
            put("Den", "12345");
            put("Max", "54321");
            put("1", "1");
        }};

        this.logger = Logger.getLogger();
        this.logger.getThread().start();
        this.port = port;
        this.clients = new ArrayList<>();

        logger.addLogMessage(new Date() + " сервер стартовал");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port, LIMIT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Socket socket;
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.clients.add(new ClientService(socket, this));
            new Thread(this.clients.get(this.clients.size() - 1), String.format("Клиент %s", this.clients.size() - 1)).start();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public ArrayList<ClientService> getClients() {
        return clients;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }
}
