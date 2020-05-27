package com.varegos;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.varegos.dto.ExceptionResponse;

public class ExceptionHandler implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
		Integer targetStatus = exchange.getProperty("targetStatus", Integer.class);
		
		if (targetStatus != null) {
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, targetStatus);
		} else {
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
		}
		
		exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
		exchange.getOut().setBody(new ExceptionResponse(exception.getMessage(), 101));
	}
	
}
