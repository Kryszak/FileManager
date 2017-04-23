package com.kryszak.operations;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DeleteOperation extends FileOperation {

    public DeleteOperation(File sourceFile, File destinationDir) throws IOException {
        super(sourceFile, destinationDir);
        dialog.registerOperation(this, "deleteOperation");
        dialog.show();
    }

    @Override
    protected Void call() throws Exception {
        files.forEach(file -> {
            if (!this.isCancelled()) {
                FileUtils.deleteQuietly(file);
                currentFileNumber++;
                updateProgress(currentFileNumber, totalFiles);
            }
        });
        if (!this.isCancelled()) {
            FileUtils.deleteDirectory(sourceFile);
        }

        return null;
    }
}
