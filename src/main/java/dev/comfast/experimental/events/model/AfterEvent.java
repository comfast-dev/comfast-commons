package dev.comfast.experimental.events.model;
import dev.comfast.util.time.StopwatchTime;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public class AfterEvent<T> extends Event<T> {
    public final StopwatchTime time;
    public final Object result;

    public AfterEvent(T eventContext , String actionName, Object[] actionParams, StopwatchTime time, Object result) {
        super(eventContext, actionName, actionParams);
        this.time = time;
        this.result = result;
    }

    @Override public String toString() {
        return super.toString() + format(" (%s)", time);
    }
}
