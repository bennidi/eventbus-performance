eventbus-performance
====================

This is a collection of benchmarks for event-bus libraries that implement the publish/subscribe pattern.
Each benchmark is defined in terms of an abstraction over the actual event-bus implementations that are being benchmarks. For each benchmark run, a graphical and textual report is generated. 

Currently the only candidates are Guava EventBus and MBassador. This is because including the other available libraries (see earlier versions of this repository) would have required a more complex EventBus abstraction in the tests. In previous versions they had been included but their performance was so poor in comparison to Guava and MBassador that continued support of their interface seemed not worth the effort. The results of the benchmarks of the most recent versions is illustrated below. Older versions can be found in the projects `result` folder. For a discussion of the results, see the end of this README.

> Disclosure: I am the author of MBassador. The motivation to design these benchmarks was to test the performance wins that MBassdor has over other libraries. 




# Benchmarks

Each benchmark consists of a number of workloads. Each workload defines a runnable task that will be executed concurrently. The event handlers written for this benchmark simply count the received events using an atomic counter. The consume either Event or SubEvent (a subclass of Event, used to test polymorphic handlers).

Depending on the scenario, different numbers of parallel tasks are executed to test different degrees of concurrency. Results of different task executions are collected during benchmark execution and a line graph as well as a textual report is generated after completion. The graphical representation of execution times allows for an in-depth comparison of Guava and MBassador

## Workload: Initializer
creates the initial amount of event listeners and subscribes them to the bus. Publishes ~half a million events to warm up the JVM.
## Workload: Publisher
publishes two 1000 instances of Event and 1000 of SubEvent to the bus. Each publisher is run multiple times according to workload configuration. This equals in **2 mio. handler invocations** for Event and 4 mio. handler invocations for SubEvent **per round**.
## Workload: Subscriber
continuously subscribes new listeners from a predefined set of listeners to the bus
## Workload: Unsubscriber
continuously unsubscribes new listeners from the same predefined set of listeners to the bus. Listeners that have formerly been subscribed by "Subscriber" are available to the "Unsubscriber" threads for subsequent unsubcription.

## Read Write High Concurrency

+ 3 workloads (36 threads)
  + Publisher: Parallel tasks:30, start=after Initializer,run 10 times
  + Subscriber: Parallel tasks=3, start=after Initializer,ends=with Publisher
  + Unsubscriber->Parallel tasks:3, start=after Initializer,ends=with Publisher

 > Visualization of execution times for Mbassador 1.3.0
![Chart of execution times for mbassador](/results/ReadWriteHighConcurrency/mbassador-1.3.0/chart.jpg?raw=true , "mbassador")
 > Visualization of execution times for Guava 19.0
![Chart of execution times for Guava](/results/ReadWriteHighConcurrency/guava-19.0/chart.jpg?raw=true, "guava")

| Event Bus | Publish 1000 Event | Publish 1000 SubEvent | Subscribe 200 listeners | Unsubscribe 200 listeners |
| ------------- |:-------------:|:-----:|:-----:|:-----:|
| Mbassador 1.3.0 | ~650 ms | ~1300 ms  | ~1 ms  | ~1 ms |
| Guava     19.0 | ~1400 ms | ~4000 ms | ~20 ms | ~70 ms |


## Read Write Low Concurrency

+ 3 workloads (36 threads)
  + Publisher: Parallel tasks:10, start=after workload Initializer,run 20 times
  + Subscriber: Parallel tasks:1, start=after workload Initializer,end=with Publisher
  + Unsubscriber: Parallel tasks:1, start=after workload Initializer,end=with Publisher

 > Visualization of execution times for Mbassador 1.3.0
![Chart of execution times for mbassador](/results/ReadWriteLowConcurrency/mbassador-1.3.0/chart.jpg?raw=true , "mbassador")
 > Visualization of execution times for Guava 19.0
![Chart of execution times for Guava](/results/ReadWriteLowConcurrency/guava-19.0/chart.jpg?raw=true, "guava")

| Event Bus | Publication TestEvent | Publication SubTestEvent | Subscription | Unsubscription |
| ------------- |:-------------:|:-----:|:-----:|:-----:|
| Mbassador 1.3.0 | ~220 ms | ~450 ms  | ~.5 ms  | ~.5 ms |
| Guava     19.0 | ~800 ms | ~2200 ms | ~7 ms | ~49 ms |


## Read Only High Concurrency

+ 3 workloads (36 threads)
  + Publisher: Parallel tasks:30, start=after Initializer,run 10 times
  + Subscriber: Parallel tasks=3, start=after Initializer,ends=with Publisher
  + Unsubscriber->Parallel tasks:3, start=after Initializer,ends=with Publisher

 > Visualization of execution times for Mbassador 1.3.0
![Chart of execution times for mbassador](/results/ReadOnlyHighConcurrency/mbassador-1.3.0/chart.jpg?raw=true , "mbassador")
 > Visualization of execution times for Guava 19.0
![Chart of execution times for Guava](/results/ReadOnlyHighConcurrency/guava-19.0/chart.jpg?raw=true, "guava")

| Event Bus | Publication TestEvent | Publication SubTestEvent | Subscription | Unsubscription |
| ------------- |:-------------:|:-----:|:-----:|:-----:|
| Mbassador 1.3.0 | ~175 ms | ~350 ms  | n.a.  | n.a. |
| Guava     19.0 | ~350 ms | ~1100 ms | n.a. | n.a. |


##Discussion

Both event bus implementations show quite consistent results in all scenarios. Execution times exhibit quite some variance distributed equally around a stable average - both for read (publish) and write(subscribe/unsubscribe) operations. This is expectable considering that the thread scheduling has a significant influence on results.


 The throughput of both libraries is as follows.
 Without writes Guava handles ~5.700 invocations per ms for Event and ~3.600 invocations per ms for SubEvent. With higher write concurrency this drops to ~1.400 invocations per ms for Event and ~1.000 invocations per ms for SubEvent.
 
MBassadors numbers are significantly better. Without write it handles ~11.500 invocations per ms for Event and ~11.500 invocations per ms for SubEvent. With higher write concurrency this drops to ~3.000 invocations per ms for Event and ~3.000 invocations per ms for SubEvent.

 Both event bus suffer from slowdown incurred by concurrent write access. Guava experiences this slowdown even in scenarios with only one concurrent writer, whereas MBassador shows a real slowdown only when multiple writers come into play.

Guava also shows a slowdown in polymorphic handler invocation that is disproportionate to the increase number of matching handlers. There are always twice as many handlers for SubEvent as for Event but execution times for SubEvent is consistently 3 times higher. In contrast, te ratio for MBassador is consistently 1:2

For Guava, unsubscription (removal of listeners) is considerably slower (~5 times) than subscription (addition of listeners). Possibly this is a leaking characteristic of the underlying data structure (optimized for insertion instead of removal). MBassador shows no difference between insertion or removal.





