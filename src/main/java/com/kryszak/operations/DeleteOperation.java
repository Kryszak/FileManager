package com.kryszak.operations;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

public class DeleteOperation extends FileOperation {

    private final File sourceFile;

    public DeleteOperation(File sourceFile) throws IOException {
        this.sourceFile = sourceFile;
    }

    @Override
    protected Void call() throws Exception {
        try {
            if (sourceFile.isDirectory()) {
                Collection<File> files = FileUtils.listFiles(sourceFile, null, true);
                totalFiles = files.size();
                files.forEach((file) -> {
                    if(!this.isCancelled()) {
                        FileUtils.deleteQuietly(file);
                        currentFileNumber++;
                        updateProgress(currentFileNumber, totalFiles);
                    }
                });
                if(!this.isCancelled()) {
                    FileUtils.deleteDirectory(sourceFile);
                }
            } else {
                totalFiles = 1;
                FileUtils.deleteQuietly(sourceFile);
                currentFileNumber++;
                updateProgress(currentFileNumber, totalFiles);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }
}
