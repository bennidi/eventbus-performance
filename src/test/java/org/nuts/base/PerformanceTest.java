package org.nuts.base;

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

}
