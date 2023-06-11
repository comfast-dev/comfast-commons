package dev.comfast.experimental.events;
import dev.comfast.experimental.events.model.AfterEvent;
import dev.comfast.experimental.events.model.BeforeEvent;
import dev.comfast.experimental.events.model.FailedEvent;

/**
 * Event listener interface.
 * @param <T>
 */
public interface EventListener<T> {
    /**
     * Called before event.
     * @param event wraps all event data
     */
    default void before(BeforeEvent<T> event) {}

    /**
     * Called after event.
     * @param event wraps all event data, including result and time
     */
    default void after(AfterEvent<T> event) {}

    /**
     * Called after event throws exception.
     * @param event wraps all event data, including error and time
     */
    default void failed(FailedEvent<T> event) {}
}
