package dev.comfast.events.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Base for all Events.
 * @param <T> Event context type
 */
@Getter
@RequiredArgsConstructor
public abstract class Event<T> {
    public final T context;
    public final String actionName;
    public final Object[] actionParams;

    @Override public String toString() {
        return String.format("%s %s(%s)",
            getClass().getSimpleName(),
            actionName,
            Stream.of(actionParams).map(Object::toString).collect(joining(", "))
        );
    }
}
