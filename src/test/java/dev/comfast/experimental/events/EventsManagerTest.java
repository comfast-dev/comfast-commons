package dev.comfast.experimental.events;
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
    List<AfterEvent<?>> afterEvents = new ArrayList<>();

    @BeforeEach
    void init() {
        events = new EventsManager<>();
        testEventOut.clear();
    }

    @Test void noListeners() {
        events.action(new BeforeEvent<>(this, "let's do it"),
            this::doSomething);
    }

    @SneakyThrows
    private void doSomething() {
        Thread.sleep(1);
    }

    @Test void listen() {
        events.addListener("myListener", new EventListener<>() {
            @Override public void before(BeforeEvent<EventsManagerTest> event) {
                testEventOut.add("myListener-before log");
            }

            @Override public void after(AfterEvent<EventsManagerTest> event) {
                testEventOut.add("myListener-after log");
                testEventOut.add("status: " + event.getStatus());
            }
        });

        events.action(new BeforeEvent<>(this, "let's do it"), this::doSomething);
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
        });

        events.action(new BeforeEvent<>(this, "ok"), this::doSomething);
        var catchedError = assertThrows(RuntimeException.class, () -> {
            events.action(new BeforeEvent<>(this, "fail here"), this::fail);
        });

        var eventError = afterEvents.get(1).getError();
        assertEquals(catchedError, eventError);
        assertEquals(EXCEPTION_MESSAGE, catchedError.getMessage());
        assertEquals("ok", afterEvents.get(0).getActionName());
        assertEquals("fail here", afterEvents.get(1).getActionName());
        assertEquals(EventStatus.FAILED, afterEvents.get(1).getStatus());
    }

    private void fail() {
        throw new RuntimeException(EXCEPTION_MESSAGE);
    }
}