package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientService implements Runnable{
    private DataInputStream in;
    private DataOutputStream out;
private Server server;
    @Override
    public void run() {
        String message;
        while (true){
            try {
                message=in.readUTF();
                if(getCitate(message)){
                    ApiCitateCreator apiCitateCreator=new ApiCitateCreator();
                    out.writeUTF("@citate "+ apiCitateCreator.getCitate());
                }
            } catch (IOException e) {
                server.getLogger().addLogMessage(String.valueOf(new Date()));
                throw new RuntimeException("Cоединение с клиентом потеряно");
            }
        }

    }
    public ClientService(Socket socket,Server server){
this.server=server;
        server.getLogger().addLogMessage(new Date() +"Клиент подключился "+socket.getInetAddress().getHostName());
        try {
            in=new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out=new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean login(String message){
        return message.startsWith("@login");
    }
    public boolean getCitate(String message){
        return message.startsWith("@getCitate");
    }
    public boolean register(String message){
        return message.startsWith("@register");
    }


}
