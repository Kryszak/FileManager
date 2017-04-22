package com.kryszak.operations;

import com.kryszak.model.ProgressDialog;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.logging.Logger;

abstract class FileOperation extends Task<Void> {

    static final Logger LOGGER = Logger.getLogger(FileOperation.class.getName());

    static final String EMPTY = "";

    int totalFiles;

    int currentFileNumber = 0;

    FileOperation() throws IOException {
        ProgressDialog dialog = new ProgressDialog();
        dialog.registerOperation(this);
        dialog.show();
        this.setOnCancelled(event -> dialog.hide());
        this.setOnSucceeded(event -> dialog.hide());
        this.setOnFailed(event -> dialog.hide());
    }

}
