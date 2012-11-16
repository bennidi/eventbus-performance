package org.nuts.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benni
 * Date: 11/16/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionContext {

    private Map<String, Object> properties = new HashMap<String, Object>();

      public ExecutionContext setProperty(String key, Object value){
            properties.put(key, value);
          return this;

      }


    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(properties.get("Bus"));
        b.append(":");
        b.append(properties.get("Scenario"));
        return b.toString();
    }


}
