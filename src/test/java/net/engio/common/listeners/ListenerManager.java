package net.engio.common.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author bennidi
 *         Date: 6/19/14
 */
public class ListenerManager {

    private ListenerWrapper[] listeners;

    private Random rand = new Random();

    private AtomicLong subscribed = new AtomicLong(0);

    public ListenerManager(ListenerFactory factory){
        super();
        List<Object> listeners = factory.getAll();
        this.listeners = new ListenerWrapper[listeners.size()];
        for(int i =0; i < listeners.size(); i++){
            this.listeners[i] = new ListenerWrapper(listeners.get(i));
        }
    }

    public Long getSubscribedListeners() {
        return subscribed.get();
    }

    private int nextIndex(){
        return rand.nextInt(listeners.length);
    }

    public ListenerWrapper getUnsubscribed(){
        ListenerWrapper result;
        while((result = getRandom()).isSubscribed());
        subscribed.incrementAndGet();
        return result;
    }

    private ListenerWrapper getRandom(){
        return listeners[nextIndex()];
    }

    public ListenerWrapper getSubscribed(){
        ListenerWrapper result;
        while(!(result = getRandom()).isSubscribed());
        return result;
    }

    public List<ListenerWrapper> getSubscribed(int count){
         List<ListenerWrapper> subscribed = new ArrayList<ListenerWrapper>(count);
        while(subscribed.size() < count){
            subscribed.add(getSubscribed());
        }
        return subscribed;
    }

    public List<ListenerWrapper> getUnsubscribed(int count){
        List<ListenerWrapper> unsubscribed = new ArrayList<ListenerWrapper>(count);
        while(unsubscribed.size() < count){
            unsubscribed.add(getUnsubscribed());
        }
        return unsubscribed;
    }


    public class ListenerWrapper{

        private Object listener;

        private AtomicBoolean isSubscribed = new AtomicBoolean(false);

        private ListenerWrapper(Object listener) {
            this.listener = listener;
        }

        public void subscribe(){
            isSubscribed.set(true);
            subscribed.incrementAndGet();
        }

        public void unsubscribe(){
            isSubscribed.set(false);
            subscribed.decrementAndGet();
        }

        private boolean isSubscribed(){
            return isSubscribed.get();
        }

        public Object getListener(){
            return listener;
        }


    }
}
