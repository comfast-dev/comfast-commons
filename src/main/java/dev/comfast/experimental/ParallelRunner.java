package dev.comfast.experimental;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;

import static java.lang.String.format;

public class ParallelRunner {
    interface ThrowingRunnable { void run() throws Exception; }

    final ThrowingRunnable[] actions;

    public ParallelRunner(ThrowingRunnable... actions) {this.actions = actions;}

    public void run() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        for(int i = 0; i < actions.length; i++) {
            new WorkerWithCountDownLatch( latch,"action #" + i, actions[i]).start();
        }
        Thread.sleep(10); //time for threads to start

        System.out.println("RELEASE threads");
        latch.countDown();
        Thread.sleep(100); // max time for threads todo wait till each Thread end
    }

    static class WorkerWithCountDownLatch extends Thread {
        private final CountDownLatch latch;
        private final ThrowingRunnable action;

        public WorkerWithCountDownLatch(CountDownLatch latch, String name, ThrowingRunnable action) {
            this.latch = latch;
            this.action = action;
            setName(name);
        }

        @Override public void run() {
            try {
                System.out.printf("[ %s ] created, blocked by the latch...\n", getName());
                latch.await();
                System.out.printf("[ %s ] starts at: %s\n", getName(), Instant.now());
                action.run();
            } catch(Exception e) {
                String msg = format("Exception at '%s'. %s: '%s'", getName(), e.getClass().getSimpleName(), e.getMessage());
                System.err.println(msg);
            }
        }
    }
}
