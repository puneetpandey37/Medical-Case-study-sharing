package com.elyx.dal.tenant;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elyx.model.user.Tenant;

@Repository("tenantManager")
@Transactional(propagation = Propagation.REQUIRED)
public class TenantManager {

	
	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<Tenant> getTenants() {
		List<Tenant> results = entityManager
	            .createNamedQuery("Tenant.findAll", Tenant.class).getResultList();
		return results;
	}
	
	public Tenant getTenantByName(String name) {
		List<Tenant> results = entityManager
	            .createNamedQuery("Tenant.findByName", Tenant.class)
	            .setParameter("name", name).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}
	
	@Transactional
	public Tenant createTenant(Tenant tenant) {
		return entityManager.merge(tenant);
	}
}
