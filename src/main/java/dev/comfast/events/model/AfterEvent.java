package dev.comfast.events.model;
import dev.comfast.util.time.StopwatchTime;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public class AfterEvent<T> extends Event<T> {
    public final StopwatchTime time;
    public final Object result;
    public final Throwable error;

    /**
     * Creates new AfterEvent with error included.
     */
    public AfterEvent(T eventContext , String actionName, Object[] actionParams, StopwatchTime eventTime, Throwable error) {
        super(eventContext, actionName, actionParams);
        this.time = eventTime;
        this.result = null;
        this.error = error;
    }

    /**
     * Creates new AfterEvent with normal result.
     */
    public AfterEvent(T eventContext , String actionName, Object[] actionParams, StopwatchTime eventTime, Object result) {
        super(eventContext, actionName, actionParams);
        this.time = eventTime;
        this.result = result;
        this.error = null;
    }

    public boolean isFailed() {
        return error != null;
    }

    @Override public String toString() {
        return super.toString() + format(" (%s)", time);
    }
}
