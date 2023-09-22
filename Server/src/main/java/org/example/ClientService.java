package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientService implements Runnable {
    private final int LIMIT = 2;
    private int counter;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;

    @Override
    public void run() {

        String message;
        while (true) {
            try {
                message = in.readUTF();

                if (message.startsWith("@regLogin")) {
                    String[] logPass = message.split(" ");
                    String login = logPass[1];
                    String pass = logPass[2];
                    server.getUsers().put(login,pass);

                }


                if (message.startsWith("@checkLogin")) {
                    String[] logPass = message.split(" ");
                    String login = logPass[1];
                    String pass = logPass[2];
                    if (server.getUsers().containsKey(login) & server.getUsers().get(login).equals(pass)) {
                        out.writeUTF("@login_ok");

                    } else {
                        out.writeUTF("@login_bad");
                    }
                }


                if (logMessage(message)) {
                    System.out.println(message);
                    writeLogMessage(" Цитата " + message.substring(12) + " получена клиентом с IP  ");
                    counter++;
                    if (counter > LIMIT) {
                        writeLogMessage(" превышено количество бесплатных цитат клиента ");
                        break;
                    }
                }
                if (getCitate(message)) {
                    ApiCitateCreator apiCitateCreator = new ApiCitateCreator();
                    out.writeUTF("@citate " + apiCitateCreator.getCitate());
                }
            } catch (IOException e) {
                writeLogMessage(" потеряно соединение с клиентом ");
                throw new RuntimeException("Cоединение с клиентом потеряно");
            }

        }
        try {
            out.writeUTF("@break");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(1000);
        } catch (
                InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public ClientService(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        server.getLogger().addLogMessage(dataOfClient(" подключился клиент "));
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String message) {
        return message.startsWith("@login");
    }

    public boolean getCitate(String message) {
        return message.startsWith("@getCitate");
    }

    public boolean logMessage(String message) {
        return message.startsWith("@logMessage");
    }

    public boolean register(String message) {
        return message.startsWith("@register");
    }

    public String dataOfClient(String message) {
        return new Date() + message + " ip " + socket.getInetAddress().getHostAddress();
    }

    public void writeLogMessage(String message) {
        server.getLogger().addLogMessage(dataOfClient(message));
    }

    public void disconnect() {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
