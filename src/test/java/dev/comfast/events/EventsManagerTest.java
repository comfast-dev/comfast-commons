package dev.comfast.events;
import dev.comfast.events.model.AfterEvent;
import dev.comfast.events.model.BeforeEvent;
import dev.comfast.events.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventsManagerTest {
    public static final String EXCEPTION_MESSAGE = "oh no!";
    EventsManager<EventsManagerTest> eventManager;

    List<String> testEventsOut = new ArrayList<>();

    @BeforeEach
    void init() {
        eventManager = new EventsManager<>();
        testEventsOut.clear();
    }

    @Test void noListeners() {
        //when
        eventManager.action(new BeforeEvent<>(this, "someAction"), this::doSomething);

        //then
        assertThat(testEventsOut).isEmpty();
    }

    @Test void removeListener() {
        //given
        var listenerKey = eventManager.on("click", e -> testEventsOut.add("on-click captured"));
        eventManager.action(new BeforeEvent<>(this, "click"), this::doSomething);
        eventManager.action(new BeforeEvent<>(this, "click"), this::doSomething);

        //when
        eventManager.unsubscribe(listenerKey);
        //third action should not be captured by listener
        eventManager.action(new BeforeEvent<>(this, "click"), this::doSomething);

        //then
        assertThat(testEventsOut).containsExactly(
            "on-click captured",
            "on-click captured"
        );
    }

    @Test void listenMultipleSubscribers() {
        //given
        eventManager.on("click", e -> testEventsOut.add("on-click captured"));
        eventManager.subscribe("myListener", new EventListener<>() {
            @Override public void before(BeforeEvent<EventsManagerTest> event) {
                testEventsOut.add("myListener-before: " + event.getActionName());
            }

            @Override public void after(AfterEvent<EventsManagerTest> event) {
                testEventsOut.add("myListener-after:" + event.getActionName());
                testEventsOut.add("status: " + (event.isFailed() ? "FAILED" : "PASSED"));
            }
        });

        //when
        eventManager.action(new BeforeEvent<>(this, "someAction"), this::doSomething);
        eventManager.action(new BeforeEvent<>(this, "click"), this::doSomething);

        //then
        assertThat(testEventsOut).containsExactly(
            "myListener-before: someAction",
            "myListener-after:someAction",
            "status: PASSED",
            "myListener-before: click",
            "myListener-after:click",
            "status: PASSED",
            "on-click captured"
        );
    }

    @Test void subscribeOneAction() {
        //given
        eventManager.on("click", e -> testEventsOut.add("click captured"));

        //when
        eventManager.action(new BeforeEvent<>(this, "click"), this::doSomething);
        eventManager.action(new BeforeEvent<>(this, "otherAction"), this::doSomething);

        //then
        assertThat(testEventsOut).containsExactly("click captured");
    }

    @Test void useShortApi() {
        //given
        eventManager.on("click", e -> testEventsOut.add("click captured"));

        //when
        var something = eventManager.action("getSomething", this::getSomething);
        eventManager.action("click", this::doSomething);

        //then
        assertThat(testEventsOut).containsExactly("click captured");
        assertThat(something).isEqualTo("something");
    }

    @Test void failLog() {
        List<Event<?>> afterEvents = new ArrayList<>();

        //given
        eventManager.subscribe("myListener", new EventListener<>() {
            @Override public void after(AfterEvent<EventsManagerTest> event) {
                afterEvents.add(event);
            }
        });

        //when
        eventManager.action(new BeforeEvent<>(this, "ok"), this::doSomething);
        var event = new BeforeEvent<>(this, "fail here");

        //when-then
        var catchedError = assertThrows(RuntimeException.class, () ->
            eventManager.action(event, this::fail));

        //then
        var eventError = ((AfterEvent<?>)afterEvents.get(1)).error;
        assertEquals(catchedError, eventError);
        assertEquals(EXCEPTION_MESSAGE, catchedError.getMessage());
        assertEquals("ok", afterEvents.get(0).actionName);
        assertEquals("fail here", afterEvents.get(1).actionName);
        assertEquals(AfterEvent.class, afterEvents.get(1).getClass());
    }

    private void fail() {
        throw new RuntimeException(EXCEPTION_MESSAGE);
    }

    private void doSomething() {
        //something
    }

    private String getSomething() {
        return "hello";
    }
}