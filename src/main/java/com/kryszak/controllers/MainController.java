package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import com.kryszak.model.FileEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;


public class MainController implements Observer {

    /**
     * TopBar menus
     */
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

    /**
     * File explorer windows - left view
     */
    @FXML
    private TableView<FileEntry> leftFileView;

    @FXML
    private TableColumn<FileEntry, String> fileNameColumnLeft;

    @FXML
    private TableColumn<FileEntry, Long> fileSizeColumnLeft;

    @FXML
    private TableColumn<FileEntry, Long> createdOnColumnLeft;

    /**
     * File explorer windows - right view
     */
    @FXML
    private TableView<FileEntry> rightFileView;

    @FXML
    private TableColumn<FileEntry, String> fileNameColumnRight;

    @FXML
    private TableColumn<FileEntry, Long> fileSizeColumnRight;

    @FXML
    private TableColumn<FileEntry, Long> createdOnColumnRight;

    private final ObservableList<FileEntry> data =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() throws IOException {
        closeMenuOption.setOnAction(event -> System.exit(0));
        englishOption.setOnAction(event -> LanguageManager.getInstance().setSelectedLocale(LanguageManager.EN));
        polishOption.setOnAction(event -> LanguageManager.getInstance().setSelectedLocale(LanguageManager.PL));

        leftFileView.getColumns().forEach((column) -> column.prefWidthProperty().bind(leftFileView.widthProperty().multiply(0.33)));
        rightFileView.getColumns().forEach((column) -> column.prefWidthProperty().bind(leftFileView.widthProperty().multiply(0.33)));

        fileNameColumnLeft.setCellValueFactory(new PropertyValueFactory<FileEntry, String>("fileName"));
        fileSizeColumnLeft.setCellValueFactory(new PropertyValueFactory<FileEntry, Long>("fileSize"));
        createdOnColumnLeft.setCellValueFactory(new PropertyValueFactory<FileEntry, Long>("createdOn"));

        fileNameColumnRight.setCellValueFactory(new PropertyValueFactory<FileEntry, String>("fileName"));
        fileSizeColumnRight.setCellValueFactory(new PropertyValueFactory<FileEntry, Long>("fileSize"));
        createdOnColumnRight.setCellValueFactory(new PropertyValueFactory<FileEntry, Long>("createdOn"));

        File file = new File(System.getProperty("user.home"));
        Arrays.stream(file.listFiles()).filter((item) -> item != null && !item.isHidden()).forEach((entry) -> {
            FileEntry item = new FileEntry();
            item.setCreatedOn(entry.lastModified());
            item.setFile(entry);
            item.setFileName(entry.getName());
            item.setFileSize(entry.length());
            data.add(item);
        });

        leftFileView.setItems(data);
        rightFileView.setItems(data);
    }

    @Override
    public void update(Observable o, Object arg) {
        closeMenuOption.setText(translate("closeOption"));
        englishOption.setText(translate("englishOption"));
        polishOption.setText(translate("polishOption"));
        fileMenu.setText(translate("fileMenu"));
        optionMenu.setText(translate("optionsMenu"));
        selectLanguageSubMenu.setText(translate("selectLanguageOption"));
        fileNameColumnLeft.setText(translate("fileName"));
        fileNameColumnRight.setText(translate("fileName"));
        fileSizeColumnLeft.setText(translate("fileSize"));
        fileSizeColumnRight.setText(translate("fileSize"));
        createdOnColumnLeft.setText(translate("createdOn"));
        createdOnColumnRight.setText(translate("createdOn"));
    }
}
