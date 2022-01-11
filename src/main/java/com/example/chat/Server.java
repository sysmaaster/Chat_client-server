package com.example.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

@SuppressWarnings("ALL")
public class Server {
    public static LinkedList<Servers> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(3345)) {
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Servers(socket));
                    System.out.println("new User");
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}

class Servers extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yy hh:mm:s");

    public Servers(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            try {
                while (true) {
                    word = in.readLine();
                    for (Servers vr : Server.serverList) {
                    vr.send(word);
                        System.out.println(word);

                    if (word.equals("exit")) {
                        this.downService();
                        System.out.println(22);
                        break;
                    }System.out.println(word);
                    }
                }
            } catch (NullPointerException ignored) {    }
            } catch (IOException e) {
                this.downService();
            }

    }

    private void send(String msg) {
        try {
            out.write(formatDate.format(new Date()) + "   " + msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (Servers vr : Server.serverList) {
                    if (vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
