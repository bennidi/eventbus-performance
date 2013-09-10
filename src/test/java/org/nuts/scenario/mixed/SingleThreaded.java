package org.nuts.scenario.mixed;

import org.junit.Test;
import org.nuts.base.IEventBus;
import org.nuts.TestScenarios;
import org.nuts.base.ConcurrentExecutor;
import org.nuts.base.ExecutionContext;
import org.nuts.base.PerformanceTest;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 11/9/12
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleThreaded extends PerformanceTest {

    // loopCount * 3 = subscriptions per thread
    // loopCount * 2 = events per thread
    final int loopCount = 20000;

    @Test
    public void testMBassadorSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "MBassador")
                        .setProperty("Scenario", "MixedST"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.MbassadorAdapter(), loopCount));

    }

    @Test
    public void testEventbusSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "MixedST"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.EventBusAdapter(), loopCount));


    }

    @Test
    public void testSimpleBusSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "SimpleBus")
                        .setProperty("Scenario", "MixedST"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.SimpleBusAdapter(), loopCount));
        calculateSimpleBusTimeToAdd();
    }

    @Test
    public void testGuavaSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "Guava")
                        .setProperty("Scenario", "MixedST"));
        executor.runConcurrent(
                TestScenarios.Mixed(new IEventBus.GuavaBusAdapter(), loopCount));

    }

}
