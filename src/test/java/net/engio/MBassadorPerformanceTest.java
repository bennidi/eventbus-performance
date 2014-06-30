package net.engio;

import net.engio.common.IEventBus;

/**
* Benchmark of MBassador
*
* @author bennidi
*         Date: 6/23/14
*/
public class MBassadorPerformanceTest extends BasePerformanceTest {

    IEventBus getBus() {
        return new IEventBus.MbassadorAdapter();
    }
}
