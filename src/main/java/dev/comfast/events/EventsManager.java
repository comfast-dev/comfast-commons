package dev.comfast.events;
import dev.comfast.events.model.AfterEvent;
import dev.comfast.events.model.BeforeEvent;
import dev.comfast.events.model.InvalidEventConfiguration;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static dev.comfast.util.Utils.isNullOrEmpty;
import static java.lang.String.format;
import static lombok.AccessLevel.PACKAGE;

/**
 * Manager for events. You are responsible for creating and managing instance of this class.
 * <p>e.g. It can be static final field or be managed by Dependency injection</p>
 *
 * Implements 2 interfaces: {@link SubscriberManager} for add/remove subscribers and {@link EventsNotifier} for notify subscribers.
 * <p>Example:</p> <pre>{@code
 *
 * class MyObservable {
 *     // here, keeps manager as static final field
 *     public static final EventsManager<AnyClass> myEvents = EventsManager<>();
 *
 *     public void click() {
 *         AnyClass context = null; // context is optional
 *
 *         myEvents.action(new BeforeEvent<>(context, "click"), () -> {
 *         // do something
 *         });
 *     }
 * }
 *
 *
 * /// CLIENT (OBSERVER) CODE
 * // Optionally typed as SubscriberManager reduces API to only subscriber methods.
 * SubscriberManager<AnyClass> myEvents = MyObservable.myEvents;
 *
 * //1st example, short one: listener implemented inline, as lambda
 * myEvents.on("click", event -> log.info(event)); // will be called after 'click' event
 *
 *
 * //2nd example, more flexible: listener implemented as class
 * myEvents.subscribe("myListener", new MyListener());
 *
 * //Where MyListener is:
 * class MyListener implements EventListener<AnyClass> {
 *    @Override public void before(BeforeEvent<AnyClass> event) { // will be called before every event
 *        log.info("attempt to run " event.actionName)
 *    }
 *
 *    @Override public void before(AfterEvent<AnyClass> event) { // will be called after every event
 *       log.info("action '{}' finished with result: '{}' in {}", event.actionName, event.result, event.time);
 *       //if context is set by the caller, it  is available here as well
 *       event.context.methodFromAnyClass();
 *   }
 * }
 * }</pre>
 * <p>See {@link EventsNotifier} - notify listeners (subscribers)</p>
 * <p>See {@link SubscriberManager} - add/remove listeners</p>
 * <p>See {@link EventListener} - listener actions</p>
 *
 * @param <T> Context class which will be passed in every event.
 */
@RequiredArgsConstructor(access = PACKAGE)
public class EventsManager<T> implements SubscriberManager<T>, EventsNotifier<T> {
    private final Map<String, EventListener<T>> eventListeners = new HashMap<>();

    /**
     * Add listener to this manager.
     * @param key Unique key to access / remove listener
     * @param listener Listener instance
     */
    public void subscribe(String key, EventListener<T> listener) {
        if(isNullOrEmpty(key)) throw new InvalidEventConfiguration("Listener name should be not null/empty.");
        if(eventListeners.get(key) != null) throw new InvalidEventConfiguration(format(
            "Listener with key: '%s' already added. Use another key, or call unsubscribe(\"%s\") before.",
            key, key));

        eventListeners.put(key, listener);
    }

    /**
     * Remove listener by its key
     */
    public void unsubscribe(String key) {
        eventListeners.remove(key);
    }

    /**
     * Notify all event listeners about failed event.
     * <pre>{@code
     * manager.notifyBefore(new BeforeEvent<>(null, "someAction"));
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
}
