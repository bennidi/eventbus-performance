package org.nuts.base;

import com.adamtaft.eb.EventBusService;
import org.junit.AfterClass;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 11/16/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
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
