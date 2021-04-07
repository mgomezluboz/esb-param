package com.varegos;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class ListAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Object newBody = newExchange.getIn().getBody();
        ArrayList<Object> list = null;
        if (oldExchange == null) {
            list = new ArrayList<Object>();
            list.add(newBody);
            newExchange.getIn().setBody(list);
            
//            if (newExchange.getIn().getHeader("ParamCount", int.class) == 1) {
//            	newExchange.setProperty(Exchange.AGGREGATION_COMPLETE_CURRENT_GROUP, true);
//            } else {
//            	newExchange.getIn().setHeader("CurrentCount", 1);
//            }
            
            return newExchange;
        } else {
            list = oldExchange.getIn().getBody(ArrayList.class);
            list.add(newBody);
            
//            int currentParam = oldExchange.getIn().getHeader("CurrentCount", int.class)+1;
//            
//            if (oldExchange.getIn().getHeader("ParamCount", int.class) == currentParam) {
//            	oldExchange.setProperty(Exchange.AGGREGATION_COMPLETE_CURRENT_GROUP, true);
//            } else {
//            	oldExchange.getIn().setHeader("CurrentCount", currentParam);
//            }
            
            return oldExchange;
        }
	}

}
