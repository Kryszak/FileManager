package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import com.kryszak.model.FileEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class FileTableViewController implements Observer {

    private final ObservableList<FileEntry> data =
            FXCollections.observableArrayList();

    @FXML
    private TableView<FileEntry> fileView;

    @FXML
    private TableColumn<FileEntry, String> fileNameColumn;

    @FXML
    private TableColumn<FileEntry, Long> fileSizeColumn;

    @FXML
    private TableColumn<FileEntry, Long> createdOnColumn;

    @FXML
    private void initialize() {
        LanguageManager.getInstance().addObserver(this);

        fileView.getColumns().forEach((column) -> column.prefWidthProperty().bind(fileView.widthProperty().multiply(0.33)));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));

        //TODO prowizorka - uładnić
        File file = new File(System.getProperty("user.home"));
        Arrays.stream(file.listFiles()).filter((item) -> item != null && !item.isHidden()).forEach((entry) -> {
            FileEntry item = new FileEntry();
            item.setCreatedOn(entry.lastModified());
            item.setFile(entry);
            item.setFileName(entry.getName());
            item.setFileSize(entry.length());
            data.add(item);
        });

        fileView.setItems(data);

        fileView.getSortOrder().setAll(fileNameColumn);

    }

    @Override
    public void update(Observable o, Object arg) {
        fileNameColumn.setText(translate("fileName"));
        fileSizeColumn.setText(translate("fileSize"));
        createdOnColumn.setText(translate("createdOn"));

    }
}
