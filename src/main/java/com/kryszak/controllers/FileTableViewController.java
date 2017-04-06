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
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import static com.kryszak.language.StringUtilities.translate;

public class FileTableViewController implements Observer {

    private static final double TABLE_WIDTH_PERCENT = 0.33;

    private static final String FILE_NAME = "fileName";

    private static final String CREATED_ON = "createdOn";

    private static final String FILE_SIZE = "fileSize";

    private static final String USER_HOME = "user.home";

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

        fileView.getColumns().forEach((column) -> column.prefWidthProperty().bind(fileView.widthProperty().multiply(TABLE_WIDTH_PERCENT)));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>(FILE_NAME));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>(FILE_SIZE));
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>(CREATED_ON));

        //TODO prowizorka - uładnić
        fillView(new File(System.getProperty(USER_HOME)));

        fileView.getSortOrder().setAll(Collections.singletonList(fileNameColumn));
    }

    private void fillView(File directory) {
        data.clear();
        Arrays.stream(directory.listFiles()).filter((item) -> !item.isHidden()).forEach((entry) -> data.add(
                new FileEntry()
                        .file(entry)
                        .fileName(entry.getName())
                        .fileSize(entry.isDirectory() ? 0 : entry.length())
                        .createdOn(entry.lastModified())));
        fileView.setItems(data);
    }

    @Override
    public void update(Observable o, Object arg) {
        fileNameColumn.setText(translate(FILE_NAME));
        fileSizeColumn.setText(translate(FILE_SIZE));
        createdOnColumn.setText(translate(CREATED_ON));
    }
}
