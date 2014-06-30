package net.engio.old;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 11/16/12
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionStatistics {

    private Multimap<ExecutionContext, Long> executionTimes = ArrayListMultimap.create();

    public synchronized ExecutionStatistics store(ExecutionContext context, long executionTimeInMs){

        executionTimes.put(context, executionTimeInMs);

        return this;
    }


    public void printExecutionTimes(PrintStream writer){
       for(ExecutionContext context : executionTimes.keySet()){
           Long max = Collections.max(executionTimes.get(context));
           Long min = Collections.min(executionTimes.get(context));
           Double avg = getAverage(executionTimes.get(context));
           writer.print(context.toString());
           writer.print(":");
           writer.print(avg);
           writer.print(":");
           writer.print(min);
           writer.print(":");
           writer.println(max);
       }
    }


    public double getAverage(Collection<Long> values){
        if(values.isEmpty()) return Long.MIN_VALUE;
        long sum = 0;
        for(Long value : values){
           sum+=value;
        }
        return sum / values.size();
    }


}
