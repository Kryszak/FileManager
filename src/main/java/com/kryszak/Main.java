package com.kryszak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private FXMLLoader loader;

    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("FileManager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
