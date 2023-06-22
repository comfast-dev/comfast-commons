package dev.comfast.events;
import dev.comfast.events.model.InvalidEventConfiguration;
import dev.comfast.events.model.AfterEvent;

import java.util.function.Consumer;

/**
 * Allows to add/remove subscriber listeners.
 * @param <T> any class that will be passed to events
 */
public interface SubscriberManager<T> {
    /**
     * Add new EventListener for given key.
     * @param listenerKey identifies given Listener
     * @param listener contain action to do before and after the event
     * @throws InvalidEventConfiguration If key is already taken or is null or empty
     */
    void subscribe(String listenerKey, EventListener<T> listener);

    /**
     * Removes Listener identified by its key
     * @param listenerKey identifies given Listener
     */
    void unsubscribe(String listenerKey);

    /**
     * Utility method that creates listener listening to only one specific action.
     * <p>It is called after given action. Can be defined inline e.g.</p>
     * e.g.<pre>{@code
     * myManager.on("click", e -> { if(e.isFailed()) e.context.takeScreenshot(); });
     * myManager.on("getText", e -> log.info("got text: '{}'", e.result));
     * }</pre>
     *
     * @param actionName only after this action listener will be called
     * @param listenerHandler Action to do after call
     *
     * @return listener key
     */
    default String on(String actionName, Consumer<AfterEvent<T>> listenerHandler) {
        String listenerKey = "on_" + actionName;
        subscribe(listenerKey, new EventListener<>() {
            @Override public void after(AfterEvent<T> event) {
                if(actionName.equals(event.actionName)) listenerHandler.accept(event);
            }
        });
        return listenerKey;
    }
}
