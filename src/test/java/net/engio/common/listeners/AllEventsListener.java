package net.engio.common.listeners;

import com.google.common.eventbus.Subscribe;
import net.engio.common.events.SubEvent;
import net.engio.common.events.Event;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 11/3/12
* Time: 5:25 PM
* To change this template use File | Settings | File Templates.
*/
@Listener(references = References.Strong)
public class AllEventsListener {

    // every event of type Event or any subtype will be delivered
    // to this listener
    @Handler()
    @Subscribe
    public void handleTestEvent(Event event) {
        event.countHandled();
    }

    @Handler
    @Subscribe
    public void handleSubTestEvent(SubEvent event) {
        event.countHandled();
    }


}
