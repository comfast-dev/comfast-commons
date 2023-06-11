package dev.comfast.experimental.events.model;
import dev.comfast.util.time.Stopwatch;
import lombok.Getter;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Event that is called before action.
 * Create new instance while You call an action.
 * @param <EventContext>
 */
@Getter
public class Event<EventContext> {
    public final EventContext context;
    public final String actionName;
    public final Object[] actionParams;
    protected final Stopwatch stopwatch = new Stopwatch();

    public Event(EventContext context, String actionName, Object... actionParams) {
        this.context = context;
        this.actionName = actionName;
        this.actionParams = actionParams;
    }

    /**
     * Creates AfterEvent with given result.
     */
    public AfterEvent<EventContext> passed(Object result) {
        return new AfterEvent<>(this, result);
    }

    /**
     * Creates FailedEvent with given error.
     */
    public FailedEvent<EventContext> failed(Throwable error) {
        return new FailedEvent<>(this, error);
    }

    @Override public String toString() {
        return String.format("%s %s(%s)",
            getClass().getSimpleName(),
            actionName,
            Stream.of(actionParams).map(Object::toString).collect(joining(", "))
        );
    }
}
