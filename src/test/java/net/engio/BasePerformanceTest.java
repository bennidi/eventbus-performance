package net.engio;

import net.engio.common.IEventBus;
import net.engio.common.events.Event;
import net.engio.common.events.SubEvent;
import net.engio.common.listeners.ListenerFactory;
import net.engio.common.listeners.ListenerManager;
import net.engio.pips.data.DataCollector;
import net.engio.pips.data.aggregator.Average;
import net.engio.pips.data.aggregator.SlidingAggregator;
import net.engio.pips.data.filter.IDataFilter;
import net.engio.pips.data.utils.ExecutionTimer;
import net.engio.pips.data.utils.TimeBasedAggregator;
import net.engio.pips.lab.Benchmark;
import net.engio.pips.lab.ExecutionContext;
import net.engio.pips.lab.Laboratory;
import net.engio.pips.lab.workload.*;
import net.engio.pips.reports.CSVFileExporter;
import net.engio.pips.reports.ChartGenerator;
import net.engio.pips.reports.SeriesGroup;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This base test defines all performance test that can be run for each event bus implementation.
 * For each implementation there will be a corresponding subclass.
 *
 */
public abstract class BasePerformanceTest {

    // change this to your local system settings (this is the directory of the github repository root)
    private static final String ProjectDir = "/ext/dev/workspace/eventbus-performance/";


    // for each event bus implementation there will be a corresponding wrapper and subclass of this test
    abstract IEventBus getBus();


    @Test
    public void ReadWriteVeryHighConcurrency() throws Exception {
        runBenchmark(Benchmarks.ReadWriteVeryHighConcurrency());
    }

    @Test
    public void ReadWriteHighConcurrency() throws Exception {
          runBenchmark(Benchmarks.ReadWriteHighConcurrency());
    }

    @Test
    public void ReadWriteLowConcurrency() throws Exception {
        runBenchmark(Benchmarks.ReadWriteLowConcurrency());
    }

    @Test
    public void HighReadConcurrency() throws Exception {
        runBenchmark(Benchmarks.ReadOnlyHighConcurrency());
    }

    void runBenchmark(final Benchmark benchmark) throws Exception {
        final IEventBus bus = getBus();
        benchmark.setProperty("Eventbus", bus.getName());
        final ListenerManager listenerManager = new ListenerManager(benchmark.<ListenerFactory>getProperty(Benchmarks.Listeners));
        final int batchSize = benchmark.getProperty(Benchmarks.EventsPerPublisher);
        final int publishersCnt = benchmark.getProperty(Benchmarks.Publishers);
        final int subscribersCnt = benchmark.getProperty(Benchmarks.Subscribers);
        final int unsubscribersCnt = benchmark.getProperty(Benchmarks.Unsubscribers);

        Workload initializer = new Workload("Initializer")
                .setParallelTasks(1)
                .starts().immediately()
                .duration().repetitions(1)
                .setITaskFactory(new ITaskFactory() {
                    @Override
                    public ITask create(ExecutionContext context) {
                        return new ITask() {
                            @Override
                            public void run(ExecutionContext context) throws Exception {
                                List<ListenerManager.ListenerWrapper> unsubscribed = listenerManager.getUnsubscribed(batchSize);
                                for (ListenerManager.ListenerWrapper wrapper : unsubscribed) {
                                    bus.subscribe(wrapper.getListener());
                                    wrapper.subscribe();
                                }
                                for (int round=0; round < 200000;round++){
                                    bus.publish(new Event());
                                    bus.publish(new SubEvent());
                                }
                            }
                        };
                    }
                });


        Workload publisher = new Workload("Publisher")
                .setParallelTasks(publishersCnt)
                .starts().after(initializer)
                .duration().repetitions((Integer) benchmark.getProperty(Benchmarks.RoundsPerPublisher))
                .handle(ExecutionEvent.WorkloadCompletion, new ExecutionHandler() {
                    @Override
                    public void handle(ExecutionContext context) {
                        while (bus.hasPending())
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                })
                .setITaskFactory(new ITaskFactory() {
                    @Override
                    public ITask create(ExecutionContext context) {
                        DataCollector testEvent = new DataCollector("publish:testevent");
                        final DataCollector handlersTestEvent = new DataCollector("handlers:testevent");
                        benchmark.addCollector(handlersTestEvent);
                        benchmark.addCollector(testEvent);
                        final ExecutionTimer timeTestEvent = new ExecutionTimer(testEvent);

                        DataCollector publishSubEvent = new DataCollector("publish:subtestevent");
                        final DataCollector handlerSubEvent = new DataCollector("handlers:subtestevent");
                        benchmark.addCollector(publishSubEvent);
                        benchmark.addCollector(handlerSubEvent);
                        final ExecutionTimer timeSubTestEvent = new ExecutionTimer(publishSubEvent);

                        return new ITask() {
                            @Override
                            public void run(ExecutionContext context) throws Exception {
                                timeTestEvent.begin();
                                Event event = null;
                                for (int i = 0; i < batchSize; i++) {
                                    event = new Event();
                                    bus.publish(event);
                                }
                                timeTestEvent.end();
                                handlersTestEvent.receive(event.getCount()); // store handler invocations

                                timeSubTestEvent.begin();
                                SubEvent subEvent = null;
                                for (int i = 0; i < batchSize; i++) {
                                    subEvent = new SubEvent();
                                    bus.publish(subEvent);
                                }
                                timeSubTestEvent.end();
                                handlerSubEvent.receive(subEvent.getCount()); // store handler invocations
                            }
                        };
                    }
                });


        Workload subscriber = new Workload("Subscriber")
                .setParallelTasks(subscribersCnt)
                .starts().after(initializer)
                .duration().depends(publisher)
                .setDelay((Long) benchmark.getProperty(Benchmarks.SubscriberDelay))
                .setITaskFactory(new ITaskFactory() {
                    @Override
                    public ITask create(ExecutionContext context) {
                        final ExecutionTimer timer = new ExecutionTimer(benchmark.addCollector(new DataCollector<Long>("subscribe")));
                        return new ITask() {
                            @Override
                            public void run(ExecutionContext context) throws Exception {
                                List<ListenerManager.ListenerWrapper> unsubscribed = listenerManager.getUnsubscribed(batchSize / 5);
                                timer.begin();
                                for (ListenerManager.ListenerWrapper wrapper : unsubscribed) {
                                    bus.subscribe(wrapper.getListener());
                                    wrapper.subscribe();
                                }
                                timer.end();
                            }
                        };
                    }
                });

        Workload unsubscriber = new Workload("Unsubscriber")
                .setParallelTasks(unsubscribersCnt)
                .starts().after(initializer)
                .duration().depends(publisher)
                .setDelay((Long) benchmark.getProperty(Benchmarks.UnsubscriberDelay))
                .setITaskFactory(new ITaskFactory() {
                    @Override
                    public ITask create(ExecutionContext context) {
                        final ExecutionTimer timer = new ExecutionTimer(benchmark.addCollector(new DataCollector<Long>("unsubscribe")));
                        return new ITask() {
                            @Override
                            public void run(ExecutionContext context) throws Exception {
                                List<ListenerManager.ListenerWrapper> subscribed = listenerManager.getSubscribed(batchSize / 5);
                                timer.begin();
                                for (ListenerManager.ListenerWrapper wrapper : subscribed) {
                                    bus.unsubscribe(wrapper.getListener());
                                    wrapper.unsubscribe();
                                }
                                timer.end();

                            }
                        };
                    }
                });
        benchmark.addWorkload(initializer, publisher, subscriber, unsubscriber)
                .setBasePath(ProjectDir + "results");



        new Laboratory().run(benchmark);

        // collapse collectors into a a single collector (values with same timestamps are aggregated into averages)
        DataCollector publicationTestEventAvg = new DataCollector("Publish 1000 x Event");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("publish:testevent"))
                .fold(new Average())
                .feed(publicationTestEventAvg);
        DataCollector publicationSubTestEventAvg = new DataCollector("Publish 1000 x SubEvent");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("publish:subtestevent"))
                .fold(new Average())
                .feed(publicationSubTestEventAvg);
        DataCollector subscriptionAvg = new DataCollector("Subscribe ~200 listeners");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("subscribe"))
                .fold(new Average())
                .feed(new SlidingAggregator(new IDataFilter.ItemCountBased(20), new Average()).add(subscriptionAvg));
        DataCollector unsubscriptionAvg = new DataCollector("Unsubscribe ~200 listeners");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("unsubscribe"))
                .fold(new Average())
                .feed(new SlidingAggregator(new IDataFilter.ItemCountBased(20), new Average()).add(unsubscriptionAvg));
        DataCollector handlersAvg = new DataCollector("Handlers for Event");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("handlers:testevent"))
                .fold(new Average())
                .feed(handlersAvg);
        DataCollector handlersSubTestEventAvg = new DataCollector("Handlers for SubEvent");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("handlers:subtestevent"))
                .fold(new Average())
                .feed(handlersSubTestEventAvg);

        int threadCount = publishersCnt + subscribersCnt + unsubscribersCnt;
        String title = benchmark.getTitle() + " = " + threadCount + " threads (" +
                publishersCnt + " publishers, " +
                subscribersCnt + " subscribers, " +
                unsubscribersCnt + " unsubscribers)";

        ChartGenerator chartgen = new ChartGenerator()
                .setTitle(title)
                .setXAxisLabel("time")
                .setPixelPerDatapoint(5)
                .draw(new SeriesGroup("Publication (ms)")
                                .addCollector(publicationTestEventAvg)
                                .addCollector(publicationSubTestEventAvg))
                .draw(new SeriesGroup("Registered handlers")
                        .setYAxisOrientation(SeriesGroup.Orientation.Right)
                        .addCollector(handlersAvg)
                        .addCollector(handlersSubTestEventAvg));
            if(subscribersCnt > 0){
                chartgen.draw(new SeriesGroup("Subscriptions (ms)")
                        .addCollector(unsubscriptionAvg)
                        .addCollector(subscriptionAvg));
            }

        benchmark.generateReports(new CSVFileExporter(), chartgen);

    }


}
