package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class ShortcutsLabelsController implements Observer {

    @FXML
    private Label copyLabel;

    @FXML
    private Label pasteLabel;

    @FXML
    private Label deleteLabel;

    @FXML
    private void initialize() {
        LanguageManager.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        copyLabel.setText(translate("copy"));
        pasteLabel.setText(translate("paste"));
        deleteLabel.setText(translate("delete"));
    }
}
