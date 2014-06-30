package net.engio;

import net.engio.common.IEventBus;

/**
* Benchmark of Guava's event bus
*
* @author bennidi
*         Date: 6/23/14
*/
public class GuavaPerformanceTest extends BasePerformanceTest {

    IEventBus getBus() {
        return new IEventBus.GuavaBusAdapter();
    }
}
