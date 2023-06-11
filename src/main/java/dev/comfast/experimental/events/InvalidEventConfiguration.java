package dev.comfast.experimental.events;
public class InvalidEventConfiguration extends RuntimeException{
    public InvalidEventConfiguration(String message) {
        super(message);
    }
}
