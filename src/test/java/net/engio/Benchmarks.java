package net.engio;

import net.engio.common.listeners.AllEventsListener;
import net.engio.common.listeners.ListenerFactory;
import net.engio.common.listeners.SubTestEventListener;
import net.engio.common.listeners.TestEventListener;
import net.engio.pips.lab.Benchmark;

/**
* Todo: Add javadoc
*
* @author bennidi
*         Date: 6/23/14
*/
public class Benchmarks {


    public static final String Listeners = "Listener factory";
    public static final String RoundsPerPublisher = "Number of batches to process with each publisher";
    public static final String EventsPerPublisher = "Batch size per publisher";
    public static final String Publishers = "Number of publisher threads";
    public static final String Subscribers = "Number of Subscriber threads";
    public static final String Unsubscribers = "Number of Unsubscriber threads";
    public static final String SubscriberDelay = "Delay after subscribing a single batch of listeners";
    public static final String UnsubscriberDelay = "Delay after unsubscribing a single batch of listeners";

    public static Benchmark ReadWriteVeryHighConcurrency(){
        Benchmark benchmark = new Benchmark("ReadWriteVeryHighConcurrency")
                .setProperty(Listeners, new ListenerFactory()
                        .create(1000,
                                AllEventsListener.class,
                                TestEventListener.class,
                                SubTestEventListener.class))
                .setProperty(EventsPerPublisher, 1000)
                .setProperty(Publishers, 60)
                .setProperty(RoundsPerPublisher, 10)
                .setProperty(Subscribers, 10)
                .setProperty(SubscriberDelay, 30L)
                .setProperty(Unsubscribers, 10)
                .setProperty(UnsubscriberDelay, 120L);
        return benchmark;
    }

    public static Benchmark ReadWriteHighConcurrency(){
        Benchmark benchmark = new Benchmark("ReadWriteHighConcurrency")
                .setProperty(Listeners, new ListenerFactory()
                        .create(1000,
                                AllEventsListener.class,
                                TestEventListener.class,
                                SubTestEventListener.class))
                .setProperty(EventsPerPublisher, 1000)
                .setProperty(Publishers, 30)
                .setProperty(RoundsPerPublisher, 10)
                .setProperty(Subscribers, 3)
                .setProperty(SubscriberDelay, 30L)
                .setProperty(Unsubscribers, 3)
                .setProperty(UnsubscriberDelay, 120L);
        return benchmark;
    }

    public static Benchmark ReadWriteLowConcurrency(){
        Benchmark benchmark = new Benchmark("ReadWriteLowConcurrency")
                .setProperty(Listeners, new ListenerFactory()
                        .create(1000,
                                AllEventsListener.class,
                                TestEventListener.class,
                                SubTestEventListener.class))
                .setProperty(EventsPerPublisher, 1000)
                .setProperty(Publishers, 10)
                .setProperty(RoundsPerPublisher, 20)
                .setProperty(Subscribers, 1)
                .setProperty(SubscriberDelay, 30L)
                .setProperty(Unsubscribers, 1)
                .setProperty(UnsubscriberDelay, 120L);
        return benchmark;
    }

    public static Benchmark ReadOnlyHighConcurrency(){
        Benchmark benchmark = new Benchmark("ReadOnlyHighConcurrency")
                .setProperty(Listeners, new ListenerFactory()
                        .create(1000,
                                AllEventsListener.class,
                                TestEventListener.class,
                                SubTestEventListener.class))
                .setProperty(EventsPerPublisher, 1000)
                .setProperty(Publishers, 30)
                .setProperty(RoundsPerPublisher, 10)
                .setProperty(Subscribers, 0)
                .setProperty(SubscriberDelay, 0L)
                .setProperty(Unsubscribers, 0)
                .setProperty(UnsubscriberDelay, 0L);
        return benchmark;
    }
}
