package net.engio.old.pubincsub;

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
public class MultiThreaded extends PerformanceTest{

    final int loopCount = 500;

    final int numberOfThreads = 20;

    final int numberOfBeans = 3000;

    // NOT WORKING: EventBus produces out of memory exceptions
    //@Test
    public void testEventbusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "PubSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishWithSubclasses(new IEventBus.EventBusAdapter(), loopCount, numberOfBeans), numberOfThreads);

    }

    @Test
    public void testSimpleBusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "PubSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishWithSubclasses(new IEventBus.SimpleBusAdapter(), loopCount, numberOfBeans), numberOfThreads);
        calculateSimpleBusTimeToAdd();
    }


    @Test
    public void testMBassadorMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "PubSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishWithSubclasses(new IEventBus.MbassadorAdapter(), loopCount, numberOfBeans), numberOfThreads);

    }

    @Test
    public void testGuavaMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "PubSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishWithSubclasses(new IEventBus.GuavaBusAdapter(), loopCount, numberOfBeans), numberOfThreads);

    }


}
