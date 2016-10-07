package net.engio.common;

import net.engio.mbassy.bus.MBassador;

/**
 * Adapter interface to plug in different event bus systems
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

        private MBassador delegate = new MBassador();

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

}
