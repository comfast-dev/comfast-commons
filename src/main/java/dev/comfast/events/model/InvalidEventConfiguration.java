package dev.comfast.events.model;
public class InvalidEventConfiguration extends RuntimeException{
    public InvalidEventConfiguration(String message) {
        super(message);
    }
}
