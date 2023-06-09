package dev.comfast.experimental.events;
/**
 * Event listener interface.
 * @param <EventContext>
 */
public interface EventListener<EventContext> {
    /**
     * Called before event.
     * @param event wraps all event data
     */
    default void before(BeforeEvent<EventContext> event) {}

    /**
     * Called after event.
     * @param event wraps all event data, including result/error and time
     */
    default void after(AfterEvent<EventContext> event) {}
}
