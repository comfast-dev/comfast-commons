package dev.comfast.experimental.events;
import dev.comfast.experimental.events.model.AfterEvent;
import dev.comfast.experimental.events.model.BeforeEvent;
import dev.comfast.experimental.events.model.FailedEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static lombok.AccessLevel.PACKAGE;

/**
 * Manager for events. Please use {@link EventsApi#get(String, Class)} to create/get instances of this class.
 *
 * @param <T> Class which will be passed in every event.
 */
@ApiStatus.Experimental
@RequiredArgsConstructor(access = PACKAGE)
public class EventsManager<T> {
    private final Map<String, EventListener<T>> eventListeners = new HashMap<>();

    /**
     * Add listener to this manager.
     * @param listenerKey Unique key to access / remove listener
     * @param listener Listener instance
     */
    public void addListener(String listenerKey, EventListener<T> listener) {
        if(listenerKey.isEmpty()) throw new InvalidEventConfiguration("Listener name should be not empty.");
        if(listener == null) throw new InvalidEventConfiguration("Listener should not be null.");

        eventListeners.put(listenerKey, listener);
    }

    /**
     * Remove listener by its key
     */
    public void removeListener(String name) {
        eventListeners.remove(name);
    }

    /**
     * Notify all event listeners about failed event.
     * <pre>{@code
     * var beforeEvent = new BeforeEvent<>(null, "someAction");
     * manager.notifyBefore(beforeEvent);
     * // do someAction()
     * }</pre>
     */
    public void notifyBefore(BeforeEvent<T> e) {
        eventListeners.values().forEach(l -> l.before(e));
    }

    /**
     * Notify all event listeners about failed event.
     * <pre>{@code
     * var beforeEvent = new BeforeEvent<>(null, "someAction");
     * manager.notifyBefore(beforeEvent);
     * var someResult = someAction();
     * manager.notifyAfter(beforeEvent.passed(someResult));
     * }</pre>
     */
    public void notifyAfter(AfterEvent<T> e) {
        eventListeners.values().forEach(l -> l.after(e));
    }

    /**
     * Notify all event listeners about failed event.
     * <pre>{@code
     * var beforeEvent = new BeforeEvent<>(null, "someAction");
     * manager.notifyBefore(beforeEvent);
     * try { something failed }
     * catch(Exception e) {
     *    manager.notifyFailed(beforeEvent.failed(e));
     *    throw e;
     * }
     * }</pre>
     */
    public void notifyFailed(FailedEvent<T> e) {
        eventListeners.values().forEach(l -> l.failed(e));
    }

    /**
     * Embed action between events: "before", "after" and "failed".
     * @see #action(BeforeEvent, Supplier)
     */
    public void action(BeforeEvent<T> event, Runnable actionFunc) {
        action(event, () -> {actionFunc.run(); return "done";});
    }

    /**
     * Embed action between events: "before", "after" and "failed".
     * <p>Capture action result to be available in AfterEvent</p>
     *
     * @param event new instance of BeforeEvent. Created it just before action call.
     * @param actionFunc action to call
     * @param <R> type of action result
     * @return action result
     */
    public <R> R action(BeforeEvent<T> event, Supplier<R> actionFunc) {
        R result;
        notifyBefore(event);
        try {
            result = actionFunc.get();
        } catch(Exception e) {
            notifyFailed(event.failed(e));
            throw e;
        }
        notifyAfter(event.passed(result));
        return result;
    }
}
