package ru.ktelabs.store.exeptions;

public class ClientNotFoundException extends Exception{
    public ClientNotFoundException(String message){
        super(message);
    }
}
