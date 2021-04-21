package com.varegos;

import java.util.List;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.varegos.dto.Parameter;
import com.varegos.exceptions.HttpException;

public class ParamDAO {

	@Autowired
	private ParamRepository repo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Logger log = LoggerFactory.getLogger(ParamDAO.class);
	
	public List<Parameter> getParams(Exchange exchange) throws HttpException {
		
		String routeId = (String)exchange.getIn().getHeader("routeId");
		List<Parameter> result = repo.findByRouteId(routeId);
		
		if (result.isEmpty()) {
			throw new HttpException("Parametro no encontrado", 404);
		}
		
		exchange.getOut().setHeader("ParamCount", result.size());
		exchange.getOut().setBody(result);
		
		return result;
	}
	
	public void processParam(Exchange exchange) {
		Parameter p = (Parameter)exchange.getIn().getBody();
		
		exchange.getOut().setBody(p);
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		
		if (p.getEncrypted() == 1) {
			exchange.getOut().setHeader("Decrypt", "1");
			exchange.getOut().setHeader("DecryptValue", p.getValor());
		}
	}
	
	public void setExchangeParams(Exchange exchange) throws HttpException {
		List<Parameter> paramList = getParams(exchange);
		
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		
		for(Parameter param : paramList) {
			log.info("Parametro: " + param.getNombre() + " - " + param.getValor());
			exchange.getOut().setHeader(param.getNombre(), param.getValor());
		}
	}
	
	public void clearCache() {
		Cache paramCache = entityManager.getEntityManagerFactory().getCache();
		if (paramCache != null) {
			paramCache.evictAll();
		}
	}
	
	public void processDecryptedParam(Exchange exchange) {
		Parameter finalBody = (Parameter)exchange.getIn().getHeader("originalBody");
		String decryptedValue = new String((byte[])exchange.getIn().getBody());
		finalBody.setValor(decryptedValue);
		
		exchange.getOut().setBody(finalBody);
	}
}
