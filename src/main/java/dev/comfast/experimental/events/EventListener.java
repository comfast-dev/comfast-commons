package dev.comfast.experimental.events;
import dev.comfast.experimental.events.model.AfterEvent;
import dev.comfast.experimental.events.model.Event;
import dev.comfast.experimental.events.model.FailedEvent;

/**
 * Event listener interface.
 * @param <EventContext>
 */
public interface EventListener<EventContext> {
    /**
     * Called before event.
     * @param event wraps all event data
     */
    default void before(Event<EventContext> event) {}

    /**
     * Called after event.
     * @param event wraps all event data, including result and time
     */
    default void after(AfterEvent<EventContext> event) {}

    /**
     * Called after event throws exception.
     * @param event wraps all event data, including error and time
     */
    default void failed(FailedEvent<EventContext> event) {};
}
