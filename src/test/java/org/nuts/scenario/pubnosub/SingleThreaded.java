package org.nuts.scenario.pubnosub;

import com.adamtaft.eb.EventBusService;
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
* Time: 10:30 PM
* To change this template use File | Settings | File Templates.
*/
public class SingleThreaded  extends PerformanceTest {

    final int loopCount = 2000;

    final int numberOfBeans = 2000;

    @Test
    public void testMBassadorSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "MBassador")
                        .setProperty("Scenario", "PubNoSubST"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.MbassadorAdapter(), loopCount, numberOfBeans));

    }

    @Test
    public void testEventbusSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "EventBus")
                        .setProperty("Scenario", "PubNoSubST"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.EventBusAdapter(), loopCount,numberOfBeans));


    }

    @Test
    public void testSimpleBusSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "SimpleBus")
                        .setProperty("Scenario", "PubNoSubST"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.SimpleBusAdapter(), loopCount, numberOfBeans));

        calculateSimpleBusTimeToAdd();
    }

    @Test
    public void testGuavaSingleThreaded() {
        ConcurrentExecutor executor = new ConcurrentExecutor(statistics,
                new ExecutionContext()
                        .setProperty("Bus", "Guava")
                        .setProperty("Scenario", "PubNoSubST"));
        executor.runConcurrent(
                TestScenarios.PublishNoSubclasses(new IEventBus.GuavaBusAdapter(), loopCount,numberOfBeans));

    }

}
