package dev.comfast.experimental.events;
import dev.comfast.experimental.events.model.AfterEvent;
import dev.comfast.experimental.events.model.Event;
import dev.comfast.experimental.events.model.FailedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventInfoTest {
    final String MY_CONTEXT = "HELLO";
    final String MY_ACTION = "click";
    final String MY_RESULT = "WORLD";

    @Test void createEvent() {
        var event = new Event<>(MY_CONTEXT, MY_ACTION, "lol");
        assertAll(
            () -> assertEquals(MY_CONTEXT, event.context),
            () -> assertEquals(MY_ACTION, event.actionName),
            () -> assertEquals("lol", event.actionParams[0])
        );
    }

    @Test void passEvent() {
        var beforeEvent = new Event<>(MY_CONTEXT, MY_ACTION);
        var event = beforeEvent.passed(MY_RESULT);

        assertAll(
            () -> assertEquals(AfterEvent.class, event.getClass()),
            () -> assertEquals(MY_RESULT, event.result),
            () -> assertTrue(event.time.getNanos() > 0, "duration > 0"),
            () -> assertEquals(event.time.getNanos(), event.time.getNanos())
        );
    }

    @Test void failEvent() {
        var beforeEvent = new Event<>(MY_CONTEXT, MY_ACTION);
        var event = beforeEvent.failed(new RuntimeException("oh no"));
        assertAll(
            () -> assertEquals(FailedEvent.class, event.getClass()),
            () -> assertTrue(event.time.getNanos() > 0, "duration > 0"),
            () -> assertEquals("oh no", event.error.getMessage())
        );
    }
}