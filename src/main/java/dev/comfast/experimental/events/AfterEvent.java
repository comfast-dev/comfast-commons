package dev.comfast.experimental.events;
import dev.comfast.util.time.StopwatchTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AfterEvent<EventContext> {
    public final EventContext context;
    public final String actionName;
    public final Object[] actionParams;
    public final StopwatchTime time;
    public final Object result;
    public final Throwable error;

    @Override public String toString() {
        return String.format("%s(%s)%s %s%s",
            actionName,
            Stream.of(actionParams).map(Object::toString).collect(joining(", ")),
            result != null ? "=>" + result : "",
            getStatus().toString(), time != null ? " (" + time + ")" : ""
        );
    }

    public EventStatus getStatus() {
        return error == null ? EventStatus.PASSED : EventStatus.FAILED;
    }
}
