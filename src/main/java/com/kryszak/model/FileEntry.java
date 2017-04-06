package com.kryszak.model;

import com.kryszak.language.LanguageManager;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

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
    public String getCreatedOn() {
        return DateFormat.getDateInstance(DateFormat.DEFAULT, LanguageManager.getInstance().getSelectedLocale()).format(new Date(createdOn.get()));
    }
    public void setCreatedOn(long createdOn) {
        this.createdOn.set(createdOn);
    }

    public FileEntry file(File file){
        this.file = file;
        return this;
    }

    public FileEntry fileName(String fileName){
        this.fileName.set(fileName);
        return this;
    }

    public FileEntry fileSize(long fileSize){
        this.fileSize.set(fileSize);
        return this;
    }

    public FileEntry createdOn(long createdOn){
        this.createdOn.set(createdOn);
        return this;
    }
}
