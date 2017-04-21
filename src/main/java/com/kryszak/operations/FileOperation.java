package com.kryszak.operations;

import com.kryszak.controllers.FileTableViewController;
import javafx.concurrent.Task;

import java.util.logging.Logger;

abstract class FileOperation extends Task<Void> {

    static final Logger LOGGER = Logger.getLogger(FileTableViewController.class.getName());

    static final String EMPTY = "";

    int totalFiles;

    int currentFileNumber = 0;

}
