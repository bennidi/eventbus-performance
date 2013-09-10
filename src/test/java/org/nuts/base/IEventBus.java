package org.nuts.base;

import com.adamtaft.eb.EventBusService;
import com.mycila.event.Dispatcher;
import com.mycila.event.Dispatchers;
import com.mycila.event.Topic;
import com.mycila.event.Topics;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import net.engio.mbassy.bus.BusConfiguration;
import net.engio.mbassy.bus.MBassador;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 11/9/12
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IEventBus {

    public void publish(Object event);

    public void subscribe(Object listener);

    public void unsubscribe(Object listener);



    public class GuavaBusAdapter implements IEventBus {

        private com.google.common.eventbus.EventBus delegate = new com.google.common.eventbus.EventBus();

        @Override
        public void publish(Object event) {
           delegate.post(event);
        }

        @Override
        public void subscribe(Object listener) {
           delegate.register(listener);
        }

        @Override
        public void unsubscribe(Object listener) {
            delegate.unregister(listener);
        }
    }

    public class MbassadorAdapter implements IEventBus {

        private MBassador delegate = new MBassador(BusConfiguration.Default());

        @Override
        public void publish(Object event) {
            delegate.publish(event);
        }

        @Override
        public void subscribe(Object listener) {
            delegate.subscribe(listener);
        }

        @Override
        public void unsubscribe(Object listener) {
            delegate.unsubscribe(listener);
        }
    }


    public class SimpleBusAdapter implements IEventBus {

        @Override
        public void publish(Object event) {
            EventBusService.publish(event);
        }

        @Override
        public void subscribe(Object listener) {
            EventBusService.subscribe(listener);
        }

        @Override
        public void unsubscribe(Object listener) {
            EventBusService.unsubscribe(listener);
        }
    }


    public class EventBusAdapter implements IEventBus {

        @Override
        public void publish(Object event) {
            org.bushe.swing.event.EventBus.publish(event);
        }

        @Override
        public void subscribe(Object listener) {
            AnnotationProcessor.process(listener);
        }

        @Override
        public void unsubscribe(Object listener) {
            AnnotationProcessor.unprocess(listener);
        }
    }

    public class MycilaAdapter implements IEventBus {

        private Dispatcher bus = Dispatchers.synchronousSafe();

        @Override
        public void publish(Object event) {
            bus.publish(Topic.topic("any"), event);
        }

        @Override
        public void subscribe(Object listener){
            bus.subscribe(Topics.any(),SubTestEvent.class, AllEventsListener.Mycila.subTestEventSubscriber());
            bus.subscribe(Topics.any(),TestEvent.class, AllEventsListener.Mycila.testEventSubscriber());
        }

        @Override
        public void unsubscribe(Object listener) {

        }
    }

}
