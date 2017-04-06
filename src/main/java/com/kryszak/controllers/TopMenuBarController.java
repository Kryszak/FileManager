package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class TopMenuBarController implements Observer {

    @FXML
    private MenuItem closeMenuOption;

    @FXML
    private MenuItem englishOption;

    @FXML
    private MenuItem polishOption;

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu optionMenu;

    @FXML
    private Menu selectLanguageSubMenu;

    @FXML
    private void initialize() {
        LanguageManager.getInstance().addObserver(this);

        closeMenuOption.setOnAction(event -> System.exit(0));
        englishOption.setOnAction(event -> LanguageManager.getInstance().setSelectedLocale(LanguageManager.EN));
        polishOption.setOnAction(event -> LanguageManager.getInstance().setSelectedLocale(LanguageManager.PL));
    }

    @Override
    public void update(Observable o, Object arg) {
        closeMenuOption.setText(translate("closeOption"));
        englishOption.setText(translate("englishOption"));
        polishOption.setText(translate("polishOption"));
        fileMenu.setText(translate("fileMenu"));
        optionMenu.setText(translate("optionsMenu"));
        selectLanguageSubMenu.setText(translate("selectLanguageOption"));
    }
}
