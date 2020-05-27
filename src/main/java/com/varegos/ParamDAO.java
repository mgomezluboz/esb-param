package com.varegos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.varegos.dto.Parameter;

public class ParamDAO {

	@Autowired
	private ParamRepository repo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Logger log = LoggerFactory.getLogger(ParamDAO.class);
	
//	@Cacheable(value = "parametros", key="#routeId")
	public List<Parameter> getParams(Exchange exchange) {
		
		String routeId = (String)exchange.getIn().getHeader("routeId");
		List<Parameter> result = repo.findByRouteId(routeId);
		
		exchange.getOut().setBody(result);
		
		return result;
	}
	
	public void setExchangeParams(Exchange exchange) {
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
}
