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

public class ParamDAO {

	@Autowired
	private ParamRepository repo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Logger log = LoggerFactory.getLogger(ParamDAO.class);
	
	public List<Parameter> getParams(Exchange exchange) {
		
		String routeId = (String)exchange.getIn().getHeader("routeId");
		List<Parameter> result = repo.findByRouteId(routeId);
		
		exchange.getOut().setBody(result);
		
		for (Parameter p : result) {
			if (p.getEncrypted() == 1) {
				exchange.getOut().setHeader("Decrypt", "1");
				exchange.getOut().setHeader("DecryptValue", p.getValor());
				break;
			}
		}
		
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
	
	public void processDecryptedParam(Exchange exchange) {
		List<Parameter> finalBody = (List<Parameter>)exchange.getIn().getHeader("originalBody");
		String decryptedValue = new String((byte[])exchange.getIn().getBody());
		
		for (Parameter p : finalBody) {
			if (p.getEncrypted() == 1) {
				p.setValor(decryptedValue);
				break;
			}
		}
		
		exchange.getOut().setBody(finalBody);
	}
}
