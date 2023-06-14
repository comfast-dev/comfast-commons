package dev.comfast.util.waiter;
import lombok.Getter;

import static java.lang.System.currentTimeMillis;

/**
 * Handle all timing calculations for Waiter.
 */
public class WaiterState {
    private final WaiterConfig conf;
    private final long startedTimestamp;
    @Getter private int tries;
    private long loopStartTime;
    private long loopEndTime;

    public WaiterState(WaiterConfig config) {
        this.conf = config;
        tries = -1;
        startedTimestamp = currentTimeMillis();
    }

    /**
     * Start new timing loop.
     * @return return false if timeout exceed
     */
    public boolean nextTry() {
        var now = getTime();
        tries++;
        loopStartTime = now;
        loopEndTime = loopStartTime + calculatePoolingTime(loopStartTime);

        return tries == 0 //always allow first try
               || now < conf.timeoutMs;
    }

    /**
     * @return Current millisecond of wait, where start time = 0ms
     */
    long getTime() {
        return currentTimeMillis() - startedTimestamp;
    }

    /**
     * @return Current millisecond of current loop.
     */
    long getLoopTime() {
        return getTime() - loopStartTime;
    }

    long getTimeTillNextLoop() {
        return Math.max(0, loopEndTime - getTime());
    }

    /**
     * @param currentWaitTimeMs current wait time between [0, timeoutMs]
     * @return next pooling time in [ms]
     */
    long calculatePoolingTime(long currentWaitTimeMs) {
        var calculatedTime = currentWaitTimeMs / conf.poolingDivider;

        if(calculatedTime < conf.poolingMinMs) calculatedTime = conf.poolingMinMs;
        if(calculatedTime > conf.poolingMaxMs) calculatedTime = conf.poolingMaxMs;
        if(currentWaitTimeMs + calculatedTime > conf.timeoutMs) calculatedTime = conf.timeoutMs - currentWaitTimeMs;

        return calculatedTime;
    }
}
