package com.kryszak.model;

import com.kryszak.controllers.ProgressDialogController;
import com.kryszak.language.LanguageManager;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.kryszak.language.StringUtilities.translate;

public class ProgressDialog {

    private static final double MIN_HEIGHT = 150;

    private static final double MIN_WIDTH = 400;

    private ProgressDialogController controller;

    private Stage stage;

    private static final String PROGRESS_DIALOG_FXML = "/fxml/ProgressDialog.fxml";

    public ProgressDialog() throws IOException {
        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(PROGRESS_DIALOG_FXML));
        loader.setResources(LanguageManager.getInstance().getResources());

        Parent root = loader.load();

        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);

        controller = loader.getController();

        stage.setResizable(false);
        stage.setScene(new Scene(root));

    }

    public void registerOperation(Task<Void> task, String operationNameKey) {
        stage.setTitle(translate(operationNameKey));
        stage.setOnCloseRequest(event -> task.cancel(true));
        controller.registerOperation(task);
    }

    public void show() {
        this.stage.show();
    }

    public void hide() {
        this.stage.close();
    }

}
