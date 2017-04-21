package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class ProgressDialogController implements Observer {

    @FXML
    Button cancelButton;

    @FXML
    ProgressBar progressBar;

    @FXML
    private void initialize() {
        LanguageManager.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        cancelButton.setText(translate("cancelOption"));
    }

}
