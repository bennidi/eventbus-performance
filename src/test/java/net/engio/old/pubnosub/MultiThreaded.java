package net.engio.old.pubnosub;

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

    // loopCount = number of events
    final int loopCount = 100;

    final int numberOfThreads = 20;

    // numberOfBeans * 3 = number of listeners
    final int numberOfBeans = 2000;

    @Test
    public void testEventbusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "PubNoSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.EventBusAdapter(), loopCount, numberOfBeans), numberOfThreads);

    }

    @Test
    public void testSimpleBusMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "SimpleBus")
                        .setProperty("Scenario", "PubNoSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.SimpleBusAdapter(), loopCount, numberOfBeans), numberOfThreads);
        calculateSimpleBusTimeToAdd();
    }


    @Test
    public void testMBassadorMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "MBassador")
                        .setProperty("Scenario", "PubNoSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.MbassadorAdapter(), loopCount, numberOfBeans), numberOfThreads);

    }

    @Test
    public void testGuavaMultiThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "Guava")
                        .setProperty("Scenario", "PubNoSubMT"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.GuavaBusAdapter(), loopCount, numberOfBeans), numberOfThreads);

    }


}
