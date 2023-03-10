package ru.netology.cloudstorage.exception;

public class NewFileNameUnknownException extends RuntimeException {

    public NewFileNameUnknownException(String msg) {
        super(msg);
    }
}