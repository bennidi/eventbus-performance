package net.engio.old.subunsub;

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
public class MultiThreaded  extends PerformanceTest {

    // loopCount * 3 = subscriptions per thread
    final int loopCount = 100;

    final int numberOfThreads = 20;

    // NOT WORKING: EventBus produces out of memory exceptions
    @Test
    public void testEventbusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "SubUnsubMT"));
        executor.runConcurrent(
                TestScenarios.SubscribeUnsubscribe(new IEventBus.EventBusAdapter(), loopCount), numberOfThreads);

    }

    @Test
    public void testSimpleBusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "SimpleBus")
                        .setProperty("Scenario", "SubUnsubMT"));
        executor.runConcurrent(
                TestScenarios.SubscribeUnsubscribe(new IEventBus.SimpleBusAdapter(), loopCount), numberOfThreads);

    }


    @Test
    public void testMBassadorMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "MBassador")
                        .setProperty("Scenario", "SubUnsubMT"));
        executor.runConcurrent(
                TestScenarios.SubscribeUnsubscribe(new IEventBus.MbassadorAdapter(), loopCount), numberOfThreads);

    }

    @Test
    public void testGuavaMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "Guava")
                        .setProperty("Scenario", "SubUnsubMT"));
        executor.runConcurrent(
                TestScenarios.SubscribeUnsubscribe(new IEventBus.GuavaBusAdapter(), loopCount), numberOfThreads);

    }


}