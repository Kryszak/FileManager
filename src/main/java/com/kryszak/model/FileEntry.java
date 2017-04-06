package com.kryszak.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class FileEntry {

    private File file;

    private StringProperty fileName;

    private LongProperty fileSize;

    private LongProperty createdOn;

    public FileEntry() {
        fileName = new SimpleStringProperty();
        fileSize = new SimpleLongProperty();
        createdOn = new SimpleLongProperty();
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getFileName() {
        return fileName.get();
    }
    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }
    public long getFileSize() {
        return fileSize.get();
    }
    public void setFileSize(long fileSize) {
        this.fileSize.set(fileSize);
    }
    public long getCreatedOn() {
        return createdOn.get();
    }
    public void setCreatedOn(long createdOn) {
        this.createdOn.set(createdOn);
    }
}
