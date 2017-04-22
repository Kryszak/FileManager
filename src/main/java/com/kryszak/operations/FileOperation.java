package com.kryszak.operations;

import com.kryszak.model.ProgressDialog;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.logging.Logger;

abstract class FileOperation extends Task<Void> {

    static final Logger LOGGER = Logger.getLogger(FileOperation.class.getName());

    static final String EMPTY = "";

    ProgressDialog dialog;

    int totalFiles;

    int currentFileNumber = 0;

    FileOperation() throws IOException {
        dialog = new ProgressDialog();
        this.setOnCancelled(event -> dialog.hide());
        this.setOnSucceeded(event -> dialog.hide());
        this.setOnFailed(event -> dialog.hide());
    }

}
