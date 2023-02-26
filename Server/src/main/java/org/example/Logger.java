package org.example;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Logger implements Runnable {
    Thread thread;
    private static Logger logger = null;
    private File log;
    private PrintWriter printWriter;
    private ArrayBlockingQueue<String> logMessages = new ArrayBlockingQueue<>(10);

    private Logger() {
        this.log = new File("server.log");

        try {
            FileWriter fileWriter = new FileWriter(this.log, true);
            this.printWriter = new PrintWriter(fileWriter);
            this.thread = new Thread(this);
        } catch (IOException e) {
            throw new RuntimeException("Запись в файл невозможна");
        }
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return new Logger();
    }

    public void writeLog(String string) {
        printWriter.println(string);
        printWriter.flush();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = logMessages.take();
                writeLog(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addLogMessage(String message) {
        try {
            logMessages.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayBlockingQueue getLogMessages() {
        return logMessages;
    }

    public Thread getThread() {
        return thread;
    }
}
