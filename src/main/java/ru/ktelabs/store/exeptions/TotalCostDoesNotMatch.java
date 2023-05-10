package ru.ktelabs.store.exeptions;

public class TotalCostDoesNotMatch extends Exception {
    public TotalCostDoesNotMatch(String message) {
        super(message);
    }
}
