package com.kryszak.operations;

import com.kryszak.model.FileEntry;

public class FileClipboard {

    private static FileEntry storedFileEntry;

    private FileClipboard() {
        // blank
    }

    public static void storeFileEntry(FileEntry entry) {
        storedFileEntry = entry;
    }

    public static FileEntry getStoredFileEntry() {
        return storedFileEntry;
    }
}
