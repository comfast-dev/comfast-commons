package dev.comfast.experimental.events;
import dev.comfast.experimental.events.model.AfterEvent;
import dev.comfast.experimental.events.model.Event;
import dev.comfast.experimental.events.model.FailedEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static lombok.AccessLevel.PACKAGE;

/**
 * Manager for events. Please use {@link EventsApi#get(String, Class)} to create instances of this class.
 * @param <EventContext> Class which will be passed in every event.
 */
@ApiStatus.Experimental
@RequiredArgsConstructor(access = PACKAGE)
public class EventsManager<EventContext> {
    private final Map<String, EventListener<EventContext>> listeners = new HashMap<>();

    public void addListener(String listenerName, EventListener<EventContext> listener) {
        if(listenerName.isEmpty()) throw new RuntimeException("Listener name should be not empty.");
        if(listener == null) throw new RuntimeException("Listener should not be null.");

        listeners.put(listenerName, listener);
    }

    public void removeListener(String name) {
        listeners.remove(name);
    }

    /**
     * Manually call given event, that will notify all listeners. e.g. <pre>{@code
     * var event = new BeforeEvent(null, "myAction");
     * myManager.callEvent(event); //call before event
     * //do some stuff, that is our action
     * myManager.callEvent(event.passed("OK")); //call After event
     * }</pre>
     * @param event BeforeEvent created just before action
     */
    public void callEvent(Event<EventContext> event) {
        for(var listener : listeners.values()) listener.before(event);
    }

    /**
     * @see EventsManager#callEvent(Event)
     */
    public void callEvent(AfterEvent<EventContext> event) {
        for(var listener : listeners.values()) listener.after(event);
    }

    /**
     * @see EventsManager#callEvent(Event)
     */
    public void callEvent(FailedEvent<EventContext> event) {
        for(var listener : listeners.values()) listener.failed(event);
    }

    /** @see #action(Event, Supplier) */
    public void action(Event<EventContext> event, Runnable actionFunc) {
        action(event, () -> {actionFunc.run(); return "done";});
    }

    /**
     * Calls all listeners before, run the action and call after action events.
     * @param event new instance of BeforeEvent. Created it just before action call.
     * @param actionFunc action to call
     * @param <T> type of action result
     * @return action result
     */
    public <T> T action(Event<EventContext> event, Supplier<T> actionFunc) {
        callEvent(event);
        try {
            var result = actionFunc.get();
            callEvent(event.passed(result));
            return result;
        } catch(Throwable e) {
            callEvent(event.failed(e));
            throw e;
        }
    }
}
