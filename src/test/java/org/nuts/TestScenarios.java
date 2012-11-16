package org.nuts;

import org.nuts.base.*;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 11/9/12
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestScenarios {


    public static Runnable PublishNoSubclasses(final IEventBus bus, final int loopCount, final int numberOfBeans) {
        return new Runnable() {

            private LinkedList beans = new LinkedList();

            {

                for(int i = 0; i< numberOfBeans; i++){
                    AllEventsListener bean = new AllEventsListener();
                    SubTestEventListener bean2 = new SubTestEventListener();
                    TestEventListener bean3 = new TestEventListener();
                    beans.add(bean);
                    beans.add(bean2);
                    beans.add(bean3);
                    bus.subscribe(bean);
                    bus.subscribe(bean2);
                    bus.subscribe(bean3);
                }
            }


            @Override
            public void run() {
                for (int i = 0; i < loopCount; i++) {
                    bus.publish(new TestEvent());
                }
            }
        };
    }

    public static Runnable PublishWithSubclasses(final IEventBus bus, final int loopCount, final int numberOfBeans) {
        return new Runnable() {

            private LinkedList beans = new LinkedList();

            {

                for(int i = 0; i< numberOfBeans; i++){
                    AllEventsListener bean = new AllEventsListener();
                    SubTestEventListener bean2 = new SubTestEventListener();
                    TestEventListener bean3 = new TestEventListener();
                    beans.add(bean);
                    beans.add(bean2);
                    beans.add(bean3);
                    bus.subscribe(bean);
                    bus.subscribe(bean2);
                    bus.subscribe(bean3);
                }
            }

            @Override
            public void run() {
                for (int i = 0; i < loopCount; i++) {
                    bus.publish(new SubTestEvent());
                }
            }
        };
    }

    public static Runnable Mixed(final IEventBus bus, final int loopCount) {
        return new Runnable() {

            private CopyOnWriteArrayList beanLists = new CopyOnWriteArrayList();

            @Override
            public void run() {
                LinkedList beans = new LinkedList();
                beanLists.add(beans); // ensure beans list does not get garbage collected
                // as long as the runnable exists
                for (int i = 0; i < loopCount; i++) {
                    AllEventsListener bean = new AllEventsListener();
                    SubTestEventListener bean2 = new SubTestEventListener();
                    TestEventListener bean3 = new TestEventListener();
                    beans.add(bean);
                    beans.add(bean2);
                    beans.add(bean3);
                    bus.subscribe(bean);
                    bus.subscribe(bean2);
                    bus.subscribe(bean3);
                    bus.publish(new TestEvent());
                    bus.publish(new SubTestEvent());
                    if(i % 10 == 0){
                        bus.unsubscribe(bean);
                    }

                }
            }
        };
    }

    public static Runnable SubscribeUnsubscribe(final IEventBus bus, final int loopCount) {
        return new Runnable() {

            private CopyOnWriteArrayList beanLists = new CopyOnWriteArrayList();

            @Override
            public void run() {
                LinkedList beans = new LinkedList();
                beanLists.add(beans); // ensure beans list does not get garbage collected
                // as long as the runnable exists
                for (int i = 0; i < loopCount; i++) {
                    AllEventsListener bean = new AllEventsListener();
                    SubTestEventListener bean2 = new SubTestEventListener();
                    TestEventListener bean3 = new TestEventListener();
                    beans.add(bean);
                    beans.add(bean2);
                    beans.add(bean3);
                    bus.subscribe(bean);
                    bus.subscribe(bean2);
                    bus.subscribe(bean3);
                    if(i % 10 == 0){
                        bus.unsubscribe(bean);
                    }
                    if(i % 13 == 0){
                        bus.unsubscribe(bean2);
                    }
                    if(i % 17 == 0){
                        bus.unsubscribe(bean3);
                    }


                }
            }
        };
    }
}
