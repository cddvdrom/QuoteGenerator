package org.example;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Server {
private final int LIMIT=3;
private     Logger logger;
    private ArrayList<ClientService> clients;

    private int port;

    public Server(int port) {
        this.logger = Logger.getLogger();
        this.logger.getThread().start();
        this.port = port;
        clients = new ArrayList<>();

        logger.addLogMessage(new Date() + " сервер стартовал");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Socket socket;
        while (true) {
            try {
                socket = serverSocket.accept();
                if(clients.size()<3) {
                    clients.add(new ClientService(socket, this));
                    new Thread(clients.get(clients.size() - 1), String.format("Клиент %s", clients.size() - 1)).start();
                }
                else {socket.close();
                    System.out.println("Cоединение невозможно");
                    logger.addLogMessage("Подключение нового пользователя невозможно.Достигута макс нагрузка");}


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Logger getLogger() {
        return logger;
    }
}
