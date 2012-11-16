package org.nuts.scenario.pubnosub;

import org.junit.Test;
import org.nuts.IEventBus;
import org.nuts.TestScenarios;
import org.nuts.base.ConcurrentExecutor;
import org.nuts.base.ExecutionContext;
import org.nuts.base.PerformanceTest;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 11/9/12
* Time: 10:16 PM
* To change this template use File | Settings | File Templates.
*/
public class MultiThreaded  extends PerformanceTest {

    final int loopCount = 100;

    final int numberOfThreads = 20;

    final int numberOfBeans = 6000;

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
