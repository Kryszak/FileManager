package com.kryszak.operations;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static java.io.File.separator;

public class MoveOperation extends FileOperation {


    public MoveOperation(File sourceFile, File destinationDir) throws IOException {
        super(sourceFile, destinationDir);
        dialog.registerOperation(this, "moveOperation");
        dialog.show();
    }

    @Override
    protected Void call() throws Exception {

        files.forEach(file -> {
            if (!this.isCancelled()) {
                String path = file.getAbsolutePath().replace(sourceFile.getAbsolutePath() + separator, EMPTY);
                try {
                    FileUtils.moveFileToDirectory(file, new File(destinationDir.getAbsolutePath() + separator + sourceFile.getName() + separator + path.replace(file.getName(), EMPTY)), true);
                    currentFileNumber++;
                    updateProgress(currentFileNumber, totalFiles);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        });
        if (!this.isCancelled()) {
            FileUtils.deleteQuietly(sourceFile);
        }

        return null;
    }
}
