package com.example.chat;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    private Socket socket;
    private static String msg;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String login = "sasa";

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button ButtonSend;
    @FXML
    private TextArea ChatHistory;
    @FXML
    private TextField Text_field;


    static {        msg = "";    }
    public static void setMsg(String _msg) {        msg = _msg;    }
    public static String getMsg() {        return msg;    }

    // отправить і записать в історію
    public final void sender () {
        try {
            if (Text_field.getText().isEmpty())
            return;
            msg = Text_field.getText();
            Text_field.clear();
            if (msg.equals("1")) {
                out.write("exit");
                out.flush();
                return;
              //  downService();
            }else {
            out.write(login + ": " + msg + "\n");}
            out.flush();
        } catch (IOException e) {
            setChatHistory("err send");
            downService();
        }
    }

    // написать чтото в чат клиента
    public final void setChatHistory(String _msg) {
        ChatHistory.setText(ChatHistory.getText() + ">> " + _msg + "\r\n");
    }

    @FXML
    void initialize() {

        try {
            this.socket = new Socket("localhost", 3345);
            setChatHistory("Socket connect");
        } catch (IOException e) {
            setChatHistory("Socket Error");
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                new ReadMsg().start();
            setChatHistory("Listen server");
        } catch (IOException e) {
            setChatHistory("failed");
            System.err.println(" failed");
            //   Clients.this.downService();
        }
        ButtonSend.setOnAction(event -> {   sender();     });
    }
        // Function Exit
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                System.exit(0);
            }System.exit(0);
        } catch (IOException ignored) {}
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) {
                        downService();
                        break;
                    }
                    setChatHistory(str);
                }
            } catch (IOException e) {
                downService();
            }
        }
    }




}
