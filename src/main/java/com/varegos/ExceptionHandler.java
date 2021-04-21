package com.varegos;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.varegos.dto.ExceptionResponse;
import com.varegos.exceptions.HttpException;

public class ExceptionHandler implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		HttpException exception = (HttpException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
		Integer targetStatus = exception.targetStatus;
		
		if (targetStatus != null) {
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, targetStatus);
		} else {
			exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
		}
		
		exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "application/json");
		exchange.getOut().setBody(new ExceptionResponse(exception.getMessage(), exception.code));
	}
	
}
