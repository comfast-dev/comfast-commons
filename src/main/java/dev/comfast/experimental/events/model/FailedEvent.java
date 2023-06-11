package dev.comfast.experimental.events.model;
import dev.comfast.util.time.StopwatchTime;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public class FailedEvent<T> extends Event<T> {
    public final StopwatchTime time;
    public final Throwable error;

    public FailedEvent(T eventContext, String actionName, Object[] actionParams, StopwatchTime time, Throwable error) {
        super(eventContext, actionName, actionParams);
        this.time = time;
        this.error = error;
    }

    @Override public String toString() {
        return super.toString() + format(" (%s)", time);
    }
}
