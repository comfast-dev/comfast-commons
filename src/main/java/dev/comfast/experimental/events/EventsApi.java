package dev.comfast.experimental.events;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Api for events. Start here
 */
@ApiStatus.Experimental
public class EventsApi {
    /**
     * Store all Event managers globally.
     */
    private static final Map<String, EventsManager<?>> eventManagers = new HashMap<>();

    /**
     * Creates/gets EventsManager with given name.
     * @param eventManagerName Unique name for EventsManager.
     * @return EventsManager
     * @param <EventContext> Class which will be passed in every event.
     */
    public static <EventContext> EventsManager<EventContext> get(String eventManagerName, Class<EventContext> contextClass) {
        //noinspection unchecked
        return (EventsManager<EventContext>) eventManagers.computeIfAbsent(eventManagerName, name -> new EventsManager<>());
    }
}
