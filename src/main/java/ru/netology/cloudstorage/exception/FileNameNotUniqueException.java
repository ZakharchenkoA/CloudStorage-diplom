package ru.netology.cloudstorage.exception;

public class FileNameNotUniqueException extends RuntimeException {

    public FileNameNotUniqueException(String msg) {
        super(msg);
    }
}