package dev.comfast.experimental.events;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static lombok.AccessLevel.PACKAGE;

/**
 * Manager for events. Please use {@link EventsApi#get(String)} to create instances of this class.
 * @param <EventContext> Class which will be passed in every event.
 */
@ApiStatus.Experimental
@RequiredArgsConstructor(access = PACKAGE)
class EventsManager<EventContext> {
    private final Map<String, EventListener<EventContext>> listeners = new HashMap<>();

    public void addListener(String listenerName, EventListener<EventContext> listener) {
        if(listenerName.isEmpty()) throw new RuntimeException("Listener name should be not empty.");
        if(listener == null) throw new RuntimeException("Listener should not be null.");

        listeners.put(listenerName, listener);
    }

    public void removeListener(String name) {
        listeners.remove(name);
    }

    /** @see #action(BeforeEvent, Supplier) */
    public void action(BeforeEvent<EventContext> event, Runnable actionFunc) {
        action(event, () -> {actionFunc.run(); return "done";});
    }

    /**
     * Calls all listeners before and after action.
     * @param beforeEvent new instance of BeforeEvent. Created it just before action call.
     * @param actionFunc action to call
     * @param <T> type of action result
     * @return action result
     */
    public <T> T action(BeforeEvent<EventContext> beforeEvent, Supplier<T> actionFunc) {
        AfterEvent<EventContext> afterEvent = null;
        for(var l : listeners.values()) l.before(beforeEvent);
        try {
            var result = actionFunc.get();
            afterEvent = beforeEvent.passed(result);
            return result;
        } catch(Throwable e) {
            afterEvent = beforeEvent.failed(e);
            throw e;
        } finally {
            for(var l : listeners.values()) l.after(afterEvent);
        }
    }
}
