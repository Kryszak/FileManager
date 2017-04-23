package com.kryszak.controllers;

import com.kryszak.language.LanguageManager;
import com.kryszak.model.FileEntry;
import com.kryszak.operations.CopyOperation;
import com.kryszak.operations.DeleteOperation;
import com.kryszak.operations.FileClipboard;
import com.kryszak.operations.MoveOperation;
import javafx.application.Platform;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.kryszak.language.StringUtilities.translate;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

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

    private static final String CONFIRM = "confirm";

    private static final String DELETE_DIALOG_CONTENT = "deleteDialogContent";

    private static final String CANCEL_OPTION = "cancelOption";

    private ObservableList<FileEntry> data =
            FXCollections.observableArrayList();

    private File currentDirectory;

    private WatchService service;

    private WatchKey watchKey;

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
    private void initialize() throws IOException {
        currentDirectory = new File(System.getProperty(USER_HOME));

        LanguageManager.getInstance().addObserver(this);

        setTableRowListeners();

        fileView.getColumns().forEach(column -> column.prefWidthProperty().bind(fileView.widthProperty().multiply(TABLE_WIDTH_PERCENT)));

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>(FILE_NAME));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>(FILE_SIZE));
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>(CREATED_ON));

        service = FileSystems.getDefault().newWatchService();

        changeCurrentDirectory(this.currentDirectory);

        setupDirectoryWatcherThread();

    }

    private void fillView() {
        data.clear();
        Arrays.stream(currentDirectory.listFiles()).filter(entry -> !entry.isHidden()).forEach(entry -> data.add(
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
                        try {
                            changeCurrentDirectory(rowData.getFile());
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                }
            });

            row.setOnDragDetected(event -> {
                FileEntry rowData = row.getItem();
                if (rowData != null) {
                    Dragboard dragboard = row.startDragAndDrop(TransferMode.ANY);
                    dragboard.setDragView(row.snapshot(null, null));
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.put(FILE_ENTRY_DATA_TYPE, rowData);
                    dragboard.setContent(clipboardContent);
                }
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
                    File destinationDir;
                    if (row.getItem() == null) {
                        destinationDir = currentDirectory;
                    } else {
                        destinationDir = row.getItem().getFile();
                    }

                    MoveOperation moveOperation = null;
                    try {
                        moveOperation = new MoveOperation(file, destinationDir);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }

                    new Thread(moveOperation).start();

                    event.setDropCompleted(true);
                    event.consume();
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

            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText(translate(CONFIRM));
            dialog.setTitle(translate(CONFIRM));
            dialog.setContentText(String.format(translate(DELETE_DIALOG_CONTENT), rowData.getFile().getName()));
            Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancel.setText(translate(CANCEL_OPTION));
            final Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                DeleteOperation deleteOperation = new DeleteOperation(rowData.getFile(), null);

                new Thread(deleteOperation).start();
            }
        } else if (event.getCode().equals(KeyCode.C) && event.isControlDown()) {
            FileClipboard.storeFileEntry(rowData);
        } else if (event.getCode().equals(KeyCode.V) && event.isControlDown()) {
            FileEntry storedEntry = FileClipboard.getStoredFileEntry();

            CopyOperation copyOperation = new CopyOperation(storedEntry.getFile(), currentDirectory);

            new Thread(copyOperation).start();

        }
    }

    private void changeCurrentDirectory(File file) throws IOException {
        currentDirectory = file;
        pathLabel.setText(file.getAbsolutePath());
        registerDirectory(currentDirectory);
        fillView();
    }

    private void registerDirectory(File directory) throws IOException {
        if (watchKey != null) {
            watchKey.cancel();
        }
        watchKey = directory.toPath().register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    private void setupDirectoryWatcherThread() {
        Thread watcherThread = new Thread(() -> {
            while (true) {
                WatchKey key = null;
                try {
                    key = service.take();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    Thread.currentThread().interrupt();
                }

                if (key != null) {
                    key.pollEvents().forEach(watchEvent -> Platform.runLater(this::fillView));
                }

                key.reset();
            }
        });

        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        fileNameColumn.setText(translate(FILE_NAME));
        fileSizeColumn.setText(translate(FILE_SIZE));
        createdOnColumn.setText(translate(CREATED_ON));
        fillView();
    }
}
