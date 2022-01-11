package com.example.chat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class ChatApplication extends Application {
    @Override
    public void start(Stage primaryStorage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("chat-view.fxml")));
        primaryStorage.setTitle("Chat SysMaaster");
        primaryStorage.setResizable(false);
        primaryStorage.setScene(new Scene(root, 700, 400));
        primaryStorage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}