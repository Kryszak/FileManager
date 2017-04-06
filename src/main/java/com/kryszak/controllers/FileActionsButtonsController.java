package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import javafx.fxml.FXML;

import java.util.Observable;
import java.util.Observer;

public class FileActionsButtonsController implements Observer {

    @FXML
    private void initialize() {
        LanguageManager.getInstance().addObserver(this);

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
