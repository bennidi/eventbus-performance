package net.engio;

import net.engio.common.IEventBus;

/**
* Benchmark of Simple Bus
*
* @author bennidi
*         Date: 6/23/14
*/
public class SimpleBusPerformanceTest extends BasePerformanceTest {

    IEventBus getBus() {
        return new IEventBus.SimpleBusAdapter();
    }
}
