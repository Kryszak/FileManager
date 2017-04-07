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
        this.fileName.set(file.getName());
        this.fileSize.set(file.isDirectory() ? 0 : file.length());
        this.createdOn.set(file.lastModified());
    }
    public String getFileName() {
        return fileName.get();
    }
    public long getFileSize() {
        return fileSize.get();
    }

    public String getCreatedOn() {
        return DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT,
                DateFormat.DEFAULT,
                LanguageManager.getInstance().getSelectedLocale()
        ).format(new Date(createdOn.get()));
    }

    public FileEntry file(File file) {
        this.file = file;
        this.fileName.set(file.getName());
        this.fileSize.set(file.isDirectory() ? 0 : file.length());
        this.createdOn.set(file.lastModified());
        return this;
    }

    public FileEntry fileName(String fileName) {
        this.fileName.set(fileName);
        return this;
    }
}
