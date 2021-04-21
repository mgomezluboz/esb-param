package com.varegos;

import java.util.List;

//import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.varegos.dto.Parameter;

@Repository
public interface ParamRepository extends JpaRepository<Parameter, String> {
	
//	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	public List<Parameter> findByRouteId(String routeId);
	
}
