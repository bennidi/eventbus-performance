package net.engio.common.listeners;

import com.adamtaft.eb.EventHandler;
import com.google.common.eventbus.Subscribe;
import net.engio.common.events.TestEvent;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import org.bushe.swing.event.annotation.EventSubscriber;


@Listener(references = References.Strong)
public class TestEventListener {

    // every event of type TestEvent or any subtype will be delivered
    // to this listener
    @Handler()
    @Subscribe
    @EventHandler
    @EventSubscriber
    public void handleTestEvent(TestEvent event) {
        event.countHandled();
    }

}
