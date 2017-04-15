package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import com.kryszak.model.FileEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.kryszak.language.StringUtilities.translate;

public class FileTableViewController implements Observer {

    private static final Logger LOGGER = Logger.getLogger(FileTableViewController.class.getName());

    private static final double TABLE_WIDTH_PERCENT = 0.33;

    private static final String FILE_NAME = "fileName";

    private static final String CREATED_ON = "createdOn";

    private static final String FILE_SIZE = "fileSize";

    private static final String USER_HOME = "user.home";

    private static final int DOUBLE_CLICK = 2;

    private static final String PARENT_DIR_NAME = "..";

    private ObservableList<FileEntry> data =
            FXCollections.observableArrayList();

    private File currentDirectory;

    @FXML
    private Label pathLabel;

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
        currentDirectory = new File(System.getProperty(USER_HOME));

        LanguageManager.getInstance().addObserver(this);

        setRowDoubleClickListener();

        fileView.getColumns().forEach((column) -> column.prefWidthProperty().bind(fileView.widthProperty().multiply(TABLE_WIDTH_PERCENT)));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>(FILE_NAME));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>(FILE_SIZE));
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>(CREATED_ON));

        changeCurrentDirectory(this.currentDirectory);
    }

    private void fillView() {
        data.clear();
        Arrays.stream(currentDirectory.listFiles()).filter((entry) -> !entry.isHidden()).forEach((entry) -> data.add(
                new FileEntry().file(entry)));
        data.add(new FileEntry().file(currentDirectory.getParentFile()).fileName(PARENT_DIR_NAME));
        fileView.setItems(data);
        sortView();
    }


    private void sortView() {
        fileView.getSortOrder().setAll(Collections.singletonList(fileNameColumn));
    }

    private void setRowDoubleClickListener() {
        fileView.setRowFactory(rowFactory -> {
            TableRow<FileEntry> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == DOUBLE_CLICK && (!row.isEmpty())) {
                    FileEntry rowData = row.getItem();
                    if (rowData.getFile().isDirectory()) {
                        changeCurrentDirectory(rowData.getFile());
                    }
                }
            });

            row.setOnDragDetected(event -> {
                LOGGER.log(Level.INFO, "Started dragging {0}",  event.getSource());
            });

            row.setOnDragOver(event -> {
                LOGGER.log(Level.INFO, "Dragged over {0}", event.getSource());
            });

            row.setOnDragDropped(event -> {
                LOGGER.log(Level.INFO, "Drag dropped on {0}", event.getSource());
            });

            return row;
        });
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        FileEntry rowData = fileView.getSelectionModel().getSelectedItem();
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (rowData.getFile().isDirectory()) {
                changeCurrentDirectory(rowData.getFile());
            }
        } else if (event.getCode().equals(KeyCode.DELETE)) {
            //TODO handle deleting files/directories
            LOGGER.log(Level.INFO, "you are deleting {0}", rowData.getFileName());
        } else if (event.getCode().equals(KeyCode.F2)) {
            //TODO handle file rename
            LOGGER.log(Level.INFO, "you are renaming {0}", rowData.getFileName());
        }
    }

    private void changeCurrentDirectory(File file) {
        currentDirectory = file;
        pathLabel.setText(file.getAbsolutePath());
        fillView();
    }

    @Override
    public void update(Observable o, Object arg) {
        fileNameColumn.setText(translate(FILE_NAME));
        fileSizeColumn.setText(translate(FILE_SIZE));
        createdOnColumn.setText(translate(CREATED_ON));
        fillView();
    }
}
