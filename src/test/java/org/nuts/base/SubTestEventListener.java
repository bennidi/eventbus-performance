package org.nuts.base;

import com.adamtaft.eb.EventHandler;
import com.google.common.eventbus.Subscribe;
import net.engio.mbassy.dispatch.HandlerInvocation;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import net.engio.mbassy.subscription.SubscriptionContext;
import org.bushe.swing.event.annotation.EventSubscriber;
import net.engio.mbassy.listener.Handler;

/**
* Simple handler for SubTestEvent
*/
@Listener(references = References.Strong)
public class SubTestEventListener {

    // every event of type TestEvent or any subtype will be delivered
    // to this listener
    @Handler()
    @Subscribe
    @EventHandler
    @EventSubscriber
    public void handleTestEvent(SubTestEvent event) {
        event.countHandled();
    }

    public static class HandleSubTestEvent extends HandlerInvocation<SubTestEventListener, SubTestEvent>{


        public HandleSubTestEvent(SubscriptionContext context) {
            super(context);
        }

        @Override
        public void invoke(SubTestEventListener listener, SubTestEvent message) {
            listener.handleTestEvent(message);
        }
    }
}
