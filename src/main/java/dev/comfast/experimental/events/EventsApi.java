package dev.comfast.experimental.events;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Api for events. Start here
 */
@ApiStatus.Experimental
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsApi {
    /**
     * Store all Event managers globally.
     */
    private static final Map<String, EventsManager<?>> eventManagers = new HashMap<>();

    /**
     * Creates/gets EventsManager with given name.
     * @param eventManagerName Unique name for EventsManager.
     * @return EventsManager
     * @param <T> Class type which will be passed in every event.
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <T> EventsManager<T> get(String eventManagerName, Class<T> clazz) {
        //noinspection unchecked
        return (EventsManager<T>) eventManagers.computeIfAbsent(eventManagerName, name -> new EventsManager<>());
    }
}
