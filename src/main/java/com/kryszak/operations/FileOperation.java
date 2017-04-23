package com.kryszak.operations;

import com.kryszak.model.ProgressDialog;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

abstract class FileOperation extends Task<Void> {

    static final Logger LOGGER = Logger.getLogger(FileOperation.class.getName());

    static final String EMPTY = "";

    ProgressDialog dialog;

    int totalFiles;

    int currentFileNumber = 0;

    final File sourceFile;

    final File destinationDir;

    Collection<File> files;

    FileOperation(File sourceFile, File destinationDir) throws IOException {
        dialog = new ProgressDialog();
        this.setOnCancelled(event -> dialog.hide());
        this.setOnSucceeded(event -> dialog.hide());
        this.setOnFailed(event -> dialog.hide());

        this.sourceFile = sourceFile;
        this.destinationDir = destinationDir;

        if (sourceFile.isDirectory()) {
            files = FileUtils.listFiles(sourceFile, null, true);
        } else {
            files = new ArrayList<>();
            files.add(sourceFile);
        }
        totalFiles = files.size();

    }

}
