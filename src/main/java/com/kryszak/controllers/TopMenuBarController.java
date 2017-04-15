package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import com.kryszak.language.StringUtilities;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class TopMenuBarController implements Observer {

    private static final String CLOSE_OPTION = "closeOption";

    private static final String ENGLISH_OPTION = "englishOption";

    private static final String POLISH_OPTION = "polishOption";

    private static final String FILE_MENU = "fileMenu";

    private static final String OPTIONS_MENU = "optionsMenu";

    private static final String SELECT_LANGUAGE_OPTION = "selectLanguageOption";

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
        closeMenuOption.setText(translate(CLOSE_OPTION));
        englishOption.setText(translate(ENGLISH_OPTION));
        polishOption.setText(translate(POLISH_OPTION));
        fileMenu.setText(translate(FILE_MENU));
        optionMenu.setText(translate(OPTIONS_MENU));
        selectLanguageSubMenu.setText(translate(SELECT_LANGUAGE_OPTION));
    }
}
