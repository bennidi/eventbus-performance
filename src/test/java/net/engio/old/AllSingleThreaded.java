package net.engio.old;

import net.engio.old.mixed.SingleThreaded;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SingleThreaded.class,
        net.engio.old.pubincsub.SingleThreaded.class,
        net.engio.old.pubnosub.SingleThreaded.class,
        net.engio.old.subunsub.SingleThreaded.class})
public class AllSingleThreaded {



}
