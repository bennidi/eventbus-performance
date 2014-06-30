package net.engio.common;

import com.adamtaft.eb.EventBusService;
import com.mycila.event.Dispatcher;
import com.mycila.event.Dispatchers;
import com.mycila.event.Topic;
import com.mycila.event.Topics;
import net.engio.common.events.SubTestEvent;
import net.engio.common.events.TestEvent;
import net.engio.common.listeners.AllEventsListener;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import org.bushe.swing.event.annotation.AnnotationProcessor;

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

    public boolean unsubscribe(Object listener);

    public boolean hasPending();

    public String getName();



    public class GuavaBusAdapter implements IEventBus {

        private com.google.common.eventbus.EventBus delegate = new com.google.common.eventbus.EventBus();

        @Override
        public void subscribe(Object listener) {
            delegate.register(listener);
        }

        @Override
        public void publish(Object event) {
           delegate.post(event);
        }

        @Override
        public boolean unsubscribe(Object listener) {
            try{
                delegate.unregister(listener);
                return true;
            }catch (Exception e){
                return false;
            }
        }

        @Override
        public boolean hasPending() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getName() {
            return "Guava Event Bus";
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
        public boolean unsubscribe(Object listener) {
           return delegate.unsubscribe(listener);
        }

        @Override
        public boolean hasPending() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getName() {
            return "Mbassador";
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
        public boolean unsubscribe(Object listener) {
            EventBusService.unsubscribe(listener);
            return true; // TODO
        }

        @Override
        public boolean hasPending() {
            return EventBusService.hasPendingEvents();
        }

        @Override
        public String getName() {
            return "Simplebus";
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
        public boolean unsubscribe(Object listener) {
            AnnotationProcessor.unprocess(listener);
            return true; //TODO
        }

        @Override
        public boolean hasPending() {
           return false;
        }

        @Override
        public String getName() {
            return "Eventbus";
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
        public boolean unsubscribe(Object listener) {
            return false;
        }

        @Override
        public boolean hasPending() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getName() {
            return "Mycila";
        }
    }

}
