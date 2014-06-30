package net.engio.common.events;

import java.util.concurrent.atomic.AtomicInteger;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 11/3/12
* Time: 5:24 PM
* To change this template use File | Settings | File Templates.
*/
public class TestEvent {

    private AtomicInteger counter = new AtomicInteger();

    public void countHandled(){
        counter.incrementAndGet();
    }

    public int getCount(){
        return counter.get();
    }

}
