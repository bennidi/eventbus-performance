package net.engio.old.mixed;

import net.engio.common.IEventBus;
import net.engio.old.ConcurrentExecutor;
import net.engio.old.ExecutionContext;
import net.engio.old.PerformanceTest;
import net.engio.old.TestScenarios;
import org.junit.Test;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 11/9/12
* Time: 10:16 PM
* To change this template use File | Settings | File Templates.
*/
public class MultiThreaded extends PerformanceTest {

    // loopCount * 3 = subscriptions per thread
    // loopCount * 2 = events per thread
    final int loopCount = 500;

    final int numberOfThreads = 20;

    @Test
    public void testEventbusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "MixedMT"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.EventBusAdapter(), loopCount), numberOfThreads);

    }

    @Test
    public void testSimpleBusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "SimpleBus")
                        .setProperty("Scenario", "MixedMT"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.SimpleBusAdapter(), loopCount), numberOfThreads);
        calculateSimpleBusTimeToAdd();
    }

    @Test
    public void testMycilaMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "Mycila")
                        .setProperty("Scenario", "MixedMT"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.MycilaAdapter(), loopCount), numberOfThreads);
    }


    @Test
    public void testMBassadorMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "MBassador")
                        .setProperty("Scenario", "MixedMT"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.MbassadorAdapter(), loopCount), numberOfThreads);

    }

    @Test
    public void testGuavaMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "Guava")
                        .setProperty("Scenario", "MixedMT"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.GuavaBusAdapter(), loopCount), numberOfThreads);

    }


}
