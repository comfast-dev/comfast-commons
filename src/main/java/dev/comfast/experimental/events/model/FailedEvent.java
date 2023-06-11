package dev.comfast.experimental.events.model;
import dev.comfast.util.time.StopwatchTime;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public class FailedEvent<EventContext> extends Event<EventContext> {
    public final StopwatchTime time;
    public final Throwable error;

    public FailedEvent(Event<EventContext> parent, Throwable error) {
        super(parent.context, parent.actionName, parent.actionParams, parent.stopwatch);
        time = parent.stopwatch.time();
        this.error = error;
    }

    @Override public String toString() {
        return super.toString() + format(" (%s)", time);
    }
}
