package org.nuts.base;

import com.adamtaft.eb.EventHandler;
import com.google.common.eventbus.Subscribe;
import org.bushe.swing.event.annotation.EventSubscriber;
import net.engio.mbassy.listener.Handler;

/**
* Simple handler for SubTestEvent
*/
public class SubTestEventListener {

    // every event of type TestEvent or any subtype will be delivered
    // to this listener
    @Handler
    @Subscribe
    @EventHandler
    @EventSubscriber
    public void handleTestEvent(SubTestEvent event) {
        event.countHandled();
    }


}
