package net.engio;

import net.engio.common.IEventBus;

/**
* Benchmark of org.bushe...Eventbus
*
* @author bennidi
*         Date: 6/23/14
*/
public class EventbusPerformanceTest extends BasePerformanceTest {

    IEventBus getBus() {
        return new IEventBus.EventBusAdapter();
    }
}
