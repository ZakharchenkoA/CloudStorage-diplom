package ru.netology.cloudstorage.exception;

public class NewFileNameNotUniqueException extends RuntimeException {

    public NewFileNameNotUniqueException(String msg) {
        super(msg);
    }
}