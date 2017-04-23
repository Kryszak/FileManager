package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class MainController implements Observer {


    @FXML
    private void initialize() throws IOException {
        LanguageManager.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        //No text to update on main component
    }
}
