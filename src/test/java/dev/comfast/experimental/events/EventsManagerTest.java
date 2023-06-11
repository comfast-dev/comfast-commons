package dev.comfast.experimental.events;
import dev.comfast.experimental.events.model.AfterEvent;
import dev.comfast.experimental.events.model.Event;
import dev.comfast.experimental.events.model.FailedEvent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventsManagerTest {
    public static final String EXCEPTION_MESSAGE = "oh no!";
    EventsManager<EventsManagerTest> events;
    List<String> testEventOut = new ArrayList<>();
    List<Event<?>> afterEvents = new ArrayList<>();

    @BeforeEach
    void init() {
        events = new EventsManager<>();
        testEventOut.clear();
    }

    @Test void noListeners() {
        events.action(new Event<>(this, "let's do it"), this::doSomething);
    }

    @SneakyThrows
    private void doSomething() {
        Thread.sleep(1);
    }

    @Test void listen() {
        events.addListener("myListener", new EventListener<>() {
            @Override public void before(Event<EventsManagerTest> event) {
                testEventOut.add("myListener-before log");
            }

            @Override public void after(AfterEvent<EventsManagerTest> event) {
                testEventOut.add("myListener-after log");
                testEventOut.add("status: PASSED");
            }

            @Override public void failed(FailedEvent<EventsManagerTest> event) {
                testEventOut.add("myListener-failed log");
            }
        });

        events.action(new Event<>(this, "let's do it"), this::doSomething);
        assertIterableEquals(testEventOut, List.of(
            "myListener-before log",
            "myListener-after log",
            "status: PASSED"
        ));
    }

    @Test void failLog() {
        events.addListener("myListener", new EventListener<>() {
            @Override public void after(AfterEvent<EventsManagerTest> event) {
                afterEvents.add(event);
            }
            @Override public void failed(FailedEvent<EventsManagerTest> event) {
                afterEvents.add(event);
            }
        });

        events.action(new Event<>(this, "ok"), this::doSomething);
        var catchedError = assertThrows(RuntimeException.class, () -> {
            events.action(new Event<>(this, "fail here"), this::fail);
        });

        var eventError = ((FailedEvent<?>)afterEvents.get(1)).error;
        assertEquals(catchedError, eventError);
        assertEquals(EXCEPTION_MESSAGE, catchedError.getMessage());
        assertEquals("ok", afterEvents.get(0).actionName);
        assertEquals("fail here", afterEvents.get(1).actionName);
        assertEquals(FailedEvent.class, afterEvents.get(1).getClass());
    }

    private void fail() {
        throw new RuntimeException(EXCEPTION_MESSAGE);
    }
}