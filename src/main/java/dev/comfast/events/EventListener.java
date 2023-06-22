package dev.comfast.events;
import dev.comfast.events.model.BeforeEvent;
import dev.comfast.events.model.AfterEvent;

/**
 * Event listener interface.
 * @param <T> Type of context object that will be passed to listener.
 */
public interface EventListener<T> {
    /**
     * Called before action. example implementation <pre>{@code
     * void before(BeforeEvent<T> event) {
     *    if(e.actionName.equals("click")) {
     *        e.context.doSomething();
     *        log.info("clicking on {}", e.actionParams[0]);
     *    }
     * }
     * }</pre>
     * @param event wraps all event data
     */
    default void before(BeforeEvent<T> event) {}

    /**
     * Called after action. example implementation <pre>{@code
     * class MyListener implements EventListener<T> {
     *   void after(AfterEvent<T> e) {
     *     if(e.isFailed()) {
     *         log.info("action '{}' failed: {}", e.actionName, e.error);
     *         // action 'click' failed: NoSuchElementException
     *     } else {
     *         log.info("got result: '{}' in {}", e.result, e.time);
     *         // got result: 'hello' in 43ms
     *     }
     *   }
     * }
     * }</pre>
     * @param event wraps all event data, including result and time
     */
    default void after(AfterEvent<T> event) {}
}
