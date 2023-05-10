package ru.ktelabs.store.exeptions;

public class MoreThanOneArgumentException extends Exception {
    public MoreThanOneArgumentException(String message) {
        super(message);
    }
}