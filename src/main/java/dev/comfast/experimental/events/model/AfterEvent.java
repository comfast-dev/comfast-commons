package dev.comfast.experimental.events.model;
import dev.comfast.util.time.StopwatchTime;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public class AfterEvent<EventContext> extends Event<EventContext> {
    public final StopwatchTime time;
    public final Object result;

    public AfterEvent(Event<EventContext> parent, Object result) {
        super(parent.context, parent.actionName, parent.actionParams, parent.stopwatch);
        time = parent.stopwatch.time();
        this.result = result;
    }

    @Override public String toString() {
        return super.toString() + format(" (%s)", time);
    }
}
