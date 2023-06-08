package dev.comfast.util.waiter;
public class WaitTimeout extends RuntimeException {
    private final WaiterStats stats;

    public WaitTimeout(WaiterStats stats, String message) {
        super(message, stats.recentError());
        this.stats = stats;
    }

    public String getStats() {
        return stats.getStats();
    }
}
