package org.nuts.base;

import com.adamtaft.eb.EventHandler;
import com.google.common.eventbus.Subscribe;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.mbassy.listener.Listener;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 11/3/12
* Time: 5:25 PM
* To change this template use File | Settings | File Templates.
*/
public class SubTestEventListener {

    // every event of type TestEvent or any subtype will be delivered
    // to this listener
    @Listener
    @Subscribe
    @EventHandler
    @EventSubscriber
    public void handleTestEvent(SubTestEvent event) {
        event.countHandled();
    }


}
