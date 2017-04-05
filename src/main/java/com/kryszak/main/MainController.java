package com.kryszak.main;

import com.kryszak.language.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class MainController implements Observer {

    @FXML
    Button plButton;

    @FXML
    Button enButton;

    @FXML
    Label testLabel;

    @FXML
    private void initialize() throws IOException {
        plButton.setOnAction(event -> LanguageManager.getInstance().setSelectedLocale(LanguageManager.PL));
        enButton.setOnAction(event -> LanguageManager.getInstance().setSelectedLocale(LanguageManager.EN));
    }

    @Override
    public void update(Observable o, Object arg) {
        testLabel.setText(translate("test"));
    }
}
