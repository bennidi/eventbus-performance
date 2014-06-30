package net.engio.common.listeners;

import com.adamtaft.eb.EventHandler;
import com.google.common.eventbus.Subscribe;
import com.mycila.event.Event;
import com.mycila.event.Subscriber;
import net.engio.common.events.SubTestEvent;
import net.engio.common.events.TestEvent;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import org.bushe.swing.event.annotation.EventSubscriber;

/**
* Created with IntelliJ IDEA.
* User: benni
* Date: 11/3/12
* Time: 5:25 PM
* To change this template use File | Settings | File Templates.
*/
@Listener(references = References.Strong)
public class AllEventsListener {

    // every event of type TestEvent or any subtype will be delivered
    // to this listener
    @Handler()
    @Subscribe
    @EventHandler
    @EventSubscriber
    public void handleTestEvent(TestEvent event) {
        event.countHandled();
    }

    @Handler
    @Subscribe
    @EventHandler
    @EventSubscriber
    public void handleSubTestEvent(SubTestEvent event) {
        event.countHandled();
    }



    public static class Mycila{

        public static Subscriber<TestEvent> testEventSubscriber(){
            return new Subscriber<TestEvent>() {
                @Override
                public void onEvent(Event<TestEvent> testEventEvent) throws Exception {
                    testEventEvent.getSource().countHandled();
                }
            };
        }

        public static Subscriber<SubTestEvent> subTestEventSubscriber(){
            return new Subscriber<SubTestEvent>() {
                @Override
                public void onEvent(Event<SubTestEvent> testEventEvent) throws Exception {
                    testEventEvent.getSource().countHandled();
                }
            };
        }


        // analogous to

        @com.mycila.event.annotation.Subscribe(topics = "all", eventType = TestEvent.class)
        public void handleTestEvent(Event<TestEvent> event) {
            event.getSource().countHandled();
        }

        @com.mycila.event.annotation.Subscribe(topics = "all", eventType = SubTestEvent.class)
        public void handleSubTestEvent(Event<SubTestEvent> event) {
            event.getSource().countHandled();
        }

    }


}
