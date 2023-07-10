package dev.comfast.events;
import dev.comfast.events.model.AfterEvent;
import dev.comfast.events.model.BeforeEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventInfoTest {
    final String MY_CONTEXT = "HELLO";
    final String MY_ACTION = "click";
    final String MY_RESULT = "WORLD";

    @Test void createEvent() {
        var event = new BeforeEvent<>(MY_CONTEXT, MY_ACTION, "lol");

        assertEquals(MY_CONTEXT, event.context);
        assertEquals(MY_ACTION, event.actionName);
        assertEquals("lol", event.actionParams[0]);
    }

    @Test void passEvent() {
        var beforeEvent = new BeforeEvent<>(MY_CONTEXT, MY_ACTION);
        var event = beforeEvent.passed(MY_RESULT);

        assertEquals(AfterEvent.class, event.getClass());
        assertEquals(MY_RESULT, event.result);
        assertTrue(event.time.getNanos() > 0, "duration > 0");
    }

    @Test void failEvent() {
        var beforeEvent = new BeforeEvent<>(MY_CONTEXT, MY_ACTION);
        var event = beforeEvent.failed(new RuntimeException("oh no"));

        assertEquals(AfterEvent.class, event.getClass());
        assertTrue(event.time.getNanos() > 0, "duration > 0");
        assertThat(event.error).hasMessage("oh no");
    }
}