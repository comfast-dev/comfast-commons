package dev.comfast.util.waiter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
public final class WaiterConfig {
    public final long timeoutMs;
    public final long poolingMinMs;
    public final long poolingMaxMs;
    public final long poolingDivider;
    public final String description;
    public final boolean includeCauseInErrorMessage;
}
