package org.nuts.scenario;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.nuts.scenario.mixed.SingleThreaded;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        org.nuts.scenario.mixed.SingleThreaded.class,
        org.nuts.scenario.pubincsub.SingleThreaded.class,
        org.nuts.scenario.pubnosub.SingleThreaded.class,
        org.nuts.scenario.subunsub.SingleThreaded.class})
public class AllSingleThreaded {



}
