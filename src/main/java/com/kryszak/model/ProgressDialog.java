package com.kryszak.model;

import com.kryszak.controllers.ProgressDialogController;
import com.kryszak.language.LanguageManager;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProgressDialog {

    private ProgressDialogController controller;

    private Stage stage;

    private static final String PROGRESS_DIALOG_FXML = "/fxml/ProgressDialog.fxml";

    public ProgressDialog() throws IOException {
        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(PROGRESS_DIALOG_FXML));
        loader.setResources(LanguageManager.getInstance().getResources());

        Parent root = loader.load();

        controller = loader.getController();

        //TODO title of dialog
        stage.setTitle("%CHANGE ME%");
        stage.setScene(new Scene(root));

    }

    public void registerOperation(Task<Void> task) {
        controller.registerOperation(task);
    }

    public void show() {
        this.stage.show();
    }

    public void hide() {
        this.stage.close();
    }

}
