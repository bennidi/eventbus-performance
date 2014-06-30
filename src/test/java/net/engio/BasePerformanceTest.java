package net.engio;

import net.engio.common.IEventBus;
import net.engio.common.events.SubTestEvent;
import net.engio.common.events.TestEvent;
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
    private static final String ProjectDir = "/home/bennidi/Development/workspaces/mbassador/eventbus-performance/";


    // for each event bus implementation there will be a corresponding wrapper and subclass of this test
    abstract IEventBus getBus();


    @Test
    public void benchmarkScenario1() throws Exception {
          runBenchmark(Benchmarks.ReadWriteHighConcurrency());
    }

    @Test
    public void benchmarkScenario2() throws Exception {
        runBenchmark(Benchmarks.ReadWriteLowConcurrency());
    }

    @Test
    public void benchmarkScenario3() throws Exception {
        runBenchmark(Benchmarks.HighReadConcurrency());
    }

    void runBenchmark(final Benchmark benchmark) throws Exception {
        final IEventBus bus = getBus();
        benchmark.setProperty("Eventbus", bus.getName());
        final ListenerManager listenerManager = new ListenerManager(benchmark.<ListenerFactory>getProperty(Benchmarks.Listeners));
        final int batchSize = benchmark.getProperty(Benchmarks.BatchSize);

        Workload publisher = new Workload("Publisher")
                .setParallelTasks((Integer) benchmark.getProperty(Benchmarks.Publishers))
                .starts().after(2, TimeUnit.SECONDS)
                .duration().repetitions((Integer) benchmark.getProperty(Benchmarks.BatchesPerPublisher))
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
                                TestEvent event = null;
                                for (int i = 0; i < batchSize; i++) {
                                    event = new TestEvent();
                                    bus.publish(event);
                                }
                                timeTestEvent.end();
                                handlersTestEvent.receive(event.getCount()); // store handler invocations

                                timeSubTestEvent.begin();
                                SubTestEvent subEvent = null;
                                for (int i = 0; i < batchSize; i++) {
                                    subEvent = new SubTestEvent();
                                    bus.publish(subEvent);
                                }
                                timeSubTestEvent.end();
                                handlerSubEvent.receive(subEvent.getCount()); // store handler invocations
                            }
                        };
                    }
                });


        Workload subscriber = new Workload("Subscriber")
                .setParallelTasks((Integer) benchmark.getProperty(Benchmarks.Subscribers))
                .starts().immediately()
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
                .setParallelTasks((Integer) benchmark.getProperty(Benchmarks.Unsubscribers))
                .starts().after(5, TimeUnit.SECONDS)
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
        benchmark.addWorkload(publisher, subscriber, unsubscriber)
                .setBasePath(ProjectDir + "results");



        new Laboratory().run(benchmark);

        // collapse collectors into a a single collector (values with same timestamps are aggregated into averages)
        DataCollector publicationTestEventAvg = new DataCollector("Publication TestEvent(AVG)");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("publish:testevent"))
                .fold(new Average())
                .feed(publicationTestEventAvg);
        DataCollector publicationSubTestEventAvg = new DataCollector("Publication SubTestEvent(AVG)");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("publish:subtestevent"))
                .fold(new Average())
                .feed(publicationSubTestEventAvg);
        DataCollector subscriptionAvg = new DataCollector("Subscription(AVG)");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("subscribe"))
                .fold(new Average())
                .feed(new SlidingAggregator(new IDataFilter.ItemCountBased(20), new Average()).add(subscriptionAvg));
        DataCollector unsubscriptionAvg = new DataCollector("Unsubscription(AVG)");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("unsubscribe"))
                .fold(new Average())
                .feed(new SlidingAggregator(new IDataFilter.ItemCountBased(20), new Average()).add(unsubscriptionAvg));
        DataCollector handlersAvg = new DataCollector("Handlers TestEvent(AVG)");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("handlers:testevent"))
                .fold(new Average())
                .feed(handlersAvg);
        DataCollector handlersSubTestEventAvg = new DataCollector("Handlers SubTestEvent (AVG)");
        new TimeBasedAggregator()
                .consume(benchmark.getCollectors("handlers:subtestevent"))
                .fold(new Average())
                .feed(handlersSubTestEventAvg);


        benchmark.generateReports(new CSVFileExporter(), new ChartGenerator()
                .setTitle("Execution times")
                .setXAxisLabel("time")
                .setPixelPerDatapoint(5)
                .draw(new SeriesGroup("Execution time of publications (ms)")
                        .addCollector(publicationTestEventAvg)
                                //.addCollectors(benchmark.getCollectorManager().getCollectors("publish:testevent", 3))
                        .addCollector(publicationSubTestEventAvg)
                        //.addCollectors(benchmark.getCollectorManager().getCollectors("publish:subtestevent", 3))
                )
                .draw(new SeriesGroup("Execution time of subscriptions (ms)")
                        .addCollector(unsubscriptionAvg)
                        .addCollector(subscriptionAvg))
                .draw(new SeriesGroup("Number of handlers invoked")
                        .setYAxisOrientation(SeriesGroup.Orientation.Right)
                        .addCollector(handlersAvg)
                        .addCollector(handlersSubTestEventAvg)));

    }


}
