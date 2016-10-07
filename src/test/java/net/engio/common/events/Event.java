package net.engio.common.events;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple event that can keep track of how many times it was handled by a certain listener
*/
public class Event {

    private AtomicInteger counter = new AtomicInteger();

    public void countHandled(){
        counter.incrementAndGet();
    }

    public int getCount(){
        return counter.get();
    }

}
