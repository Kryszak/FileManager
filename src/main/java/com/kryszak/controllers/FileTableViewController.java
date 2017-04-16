package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import com.kryszak.model.FileEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
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

    private static final DataFormat FILE_ENTRY_DATA_TYPE = new DataFormat("com.kryszak.model.FileEntry");

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

        setTableRowListeners();

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

    private void setTableRowListeners() {
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
                FileEntry rowData = row.getItem();
                Dragboard dragboard = row.startDragAndDrop(TransferMode.ANY);
                dragboard.setDragView(row.snapshot(null, null));
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.put(FILE_ENTRY_DATA_TYPE, rowData);
                dragboard.setContent(clipboardContent);
                event.consume();
            });

            row.setOnDragOver(event -> {
                if (event.getDragboard().hasContent(FILE_ENTRY_DATA_TYPE)) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            });

            row.setOnDragDropped(event -> {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasContent(FILE_ENTRY_DATA_TYPE)) {
                    FileEntry draggedFile = (FileEntry) dragboard.getContent(FILE_ENTRY_DATA_TYPE);
                    File file = draggedFile.getFile();
                    File destinationDir = row.getItem().getFile();
                    try {
                        //TODO progress bar
                        if (file.isDirectory()) {
                            FileUtils.moveDirectoryToDirectory(file, destinationDir, false);
                        } else {
                            FileUtils.moveFileToDirectory(file, destinationDir, false);
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }
                    event.setDropCompleted(true);
                    event.consume();
                    fillView();
                }
            });

            return row;
        });
    }

    @FXML
    private void onKeyPressed(KeyEvent event) throws IOException {
        FileEntry rowData = fileView.getSelectionModel().getSelectedItem();
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (rowData.getFile().isDirectory()) {
                changeCurrentDirectory(rowData.getFile());
            }
        } else if (event.getCode().equals(KeyCode.DELETE)) {
            //TODO progress bar
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText(translate("confirm"));
            dialog.setTitle(translate("confirm"));
            dialog.setContentText(String.format(translate("deleteDialogContent"), rowData.getFile().getName()));
            Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancel.setText(translate("cancelOption"));
            final Optional<ButtonType> result =  dialog.showAndWait();
            if(result.get().equals(ButtonType.OK)){
                if(rowData.getFile().isDirectory()){
                    FileUtils.deleteDirectory(rowData.getFile());
                } else {
                    FileUtils.deleteQuietly(rowData.getFile());
                }
                fillView();
            }
        } else if (event.getCode().equals(KeyCode.F2)) {
            //TODO handle file rename NICE TO HAVE
            LOGGER.log(Level.INFO, "you are renaming {0}", rowData.getFileName());
        } else if (event.getCode().equals(KeyCode.C) && event.isControlDown()) {
            //TODO copy file
            LOGGER.log(Level.INFO, "you are copying {0}", rowData.getFileName());
        } else if (event.getCode().equals(KeyCode.V) && event.isControlDown()) {
            //TODO paste copied file to source
            LOGGER.log(Level.INFO, "you are pasting {0}", event.getSource());
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
