package net.engio.common.listeners;

import com.google.common.eventbus.Subscribe;
import net.engio.common.events.Event;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;


@Listener(references = References.Strong)
public class TestEventListener {

    // every event of type Event or any subtype will be delivered
    // to this listener
    @Handler()
    @Subscribe
    public void handleTestEvent(Event event) {
        event.countHandled();
    }

}
