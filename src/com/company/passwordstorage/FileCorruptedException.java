package com.company.passwordstorage;

public class FileCorruptedException extends Exception {
    public FileCorruptedException() {
        super("Corrupted file");
    }
}
