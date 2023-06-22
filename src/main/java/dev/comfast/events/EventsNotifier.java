package dev.comfast.events;
import dev.comfast.events.model.BeforeEvent;
import dev.comfast.events.model.AfterEvent;

import java.util.function.Supplier;

public interface EventsNotifier<T> {

    /**
     * Notify all event listeners about failed event.
     * <pre>{@code
     * manager.notifyBefore(new BeforeEvent<>(null, "someAction"));
     * // do someAction()
     * }</pre>
     */
    void notifyBefore(BeforeEvent<T> e);

    /**
     * Notify all event listeners about ended event.
     * <pre>{@code
     * var beforeEvent = new BeforeEvent<>(null, "someAction");
     * manager.notifyBefore(beforeEvent);
     * var someResult = someAction();
     * manager.notifyAfter(beforeEvent.passed(someResult));
     * }</pre>
     */
    void notifyAfter(AfterEvent<T> e);
    /**
     * Shorthand for action with null context and no params
     */
    default void action(String name, Runnable actionFunc) {
        action(new BeforeEvent<>(null, name), actionFunc);
    }

    /**
     * Shorthand for action with null context and no params
     */
    default <R> R action(String name, Supplier<R> actionFunc) {
        return action(new BeforeEvent<>(null, name), actionFunc);
    }

    /**
     * Embed action between events: "before" and "after".
     * see {@link #action(BeforeEvent, Supplier)} for more details / examples.
     */
    default void action(BeforeEvent<T> event, Runnable actionFunc) {
        action(event, () -> {actionFunc.run(); return "done";});
    }

    /**
     * Embed action between events: "before" and "after".
     * <p>Capture action result/Exception to be available in AfterEvent</p>
     * e.g. <pre>{@code
     * // During this code all notifiers will be called:
     * //   notifyBefore(event)
     * //   <<run the action>>
     * //   notifyAfter(event)
     * // or
     * //   notifyAfter(eventWithError)
     *
     * String context = null; // context is optional, will be available in listener by call event.context...
     * String someParam = "abc"; //params are optional, only for additional information
     *
     * var actionResult = myManager.action(new BeforeEvent<>(context, "myAction", someParam), () -> {
     *     //do my action
     *     //can use someParam as well
     *     return "hello";
     * })
     *
     * print(actionResult); // -> hello
     * }</pre>
     *
     * @param event new instance of BeforeEvent. Create it just before action call.
     * @param actionFunc action to call
     * @param <R> type of action result
     * @return action result
     */
    default <R> R action(BeforeEvent<T> event, Supplier<R> actionFunc) {
        R result;
        notifyBefore(event);
        try {
            result = actionFunc.get();
        } catch(Exception e) {
            notifyAfter(event.failed(e));
            throw e;
        }
        notifyAfter(event.passed(result));
        return result;
    }
}
