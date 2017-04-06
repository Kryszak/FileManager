package com.kryszak;

import com.kryszak.controllers.SingletonControllerFactory;
import com.kryszak.language.LanguageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private static final String APPLICATION_TITLE = "Conquer and Command";

    private FXMLLoader loader;

    public void start(Stage primaryStage) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
        loader.setResources(LanguageManager.getInstance().getResources());
        loader.setController(SingletonControllerFactory.getMainController());

        Parent root = loader.load();

        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
