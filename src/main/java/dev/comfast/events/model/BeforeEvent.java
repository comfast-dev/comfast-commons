package dev.comfast.events.model;
import dev.comfast.util.time.Stopwatch;
import lombok.Getter;

/**
 * Event that is called before action.
 * @param <T> Event context
 */
@Getter
public class BeforeEvent<T> extends Event<T> {
    private final Stopwatch stopwatch;

    public BeforeEvent(T eventContext, String actionName, Object... actionParams) {
        super(eventContext, actionName, actionParams);
        stopwatch = new Stopwatch();
    }

    /**
     * Creates AfterEvent with given result.
     */
    public AfterEvent<T> passed(Object result) {
        return new AfterEvent<>(context, actionName, actionParams, stopwatch.time(), result);
    }

    /**
     * Creates FailedEvent with given error.
     */
    public AfterEvent<T> failed(Throwable error) {
        return new AfterEvent<>(context, actionName, actionParams, stopwatch.time(), error);
    }
}
