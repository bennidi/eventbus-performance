package net.engio.old;

import com.adamtaft.eb.EventBusService;
import org.junit.AfterClass;


public class PerformanceTest{

    protected static ExecutionStatistics statistics = new ExecutionStatistics();

    @AfterClass
    public static void finish(){
        statistics.printExecutionTimes(System.out);
    }

    public void calculateSimpleBusTimeToAdd(){
        long start = System.currentTimeMillis();
        while(EventBusService.hasPendingEvents()){
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        long end = System.currentTimeMillis() - start;
        System.out.println("Time to add " + end);
    }

}
