package com.kryszak;

import com.kryszak.language.LanguageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.kryszak.config.ApplicationConfiguration.getProperty;

public class Main extends Application {

    private static final String APPLICATION_TITLE = getProperty("application.title");

    private static final String MAIN_LAYOUT_FXML = "/fxml/MainLayout.fxml";

    private static final int MIN_HEIGHT = 600;

    private static final int MIN_WIDTH = 800;

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_LAYOUT_FXML));
        loader.setResources(LanguageManager.getInstance().getResources());

        Parent root = loader.load();

        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);

        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
