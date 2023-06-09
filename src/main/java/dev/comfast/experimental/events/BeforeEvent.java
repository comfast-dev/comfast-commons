package dev.comfast.experimental.events;
import dev.comfast.util.time.Stopwatch;
import lombok.Getter;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Event that is called before action.
 * Create new instance while You call the action.
 * @param <EventContext>
 */
@Getter
public class BeforeEvent<EventContext> {
    public final EventContext context;
    public final String actionName;
    public final Object[] actionParams;
    private final Stopwatch stopwatch = new Stopwatch();

    public BeforeEvent(EventContext context, String actionName, Object... actionParams) {
        this.context = context;
        this.actionName = actionName;
        this.actionParams = actionParams;
    }

    /**
     * Creates PASSED AfterEvent with given result.
     */
    public AfterEvent<EventContext> passed(Object result) {
        return new AfterEvent<>(context, actionName, actionParams,
            stopwatch.time(), result, null);
    }

    /**
     * Creates FAILED AfterEvent with given error.
     */
    public AfterEvent<EventContext> failed(Throwable error) {
        return new AfterEvent<>(context, actionName, actionParams,
            stopwatch.time(), null, error);
    }

    @Override public String toString() {
        return String.format("%s(%s) IN PROGRESS... %s",
            actionName,
            Stream.of(actionParams).map(Object::toString).collect(joining(", ")),
            stopwatch.time()
        );
    }
}
