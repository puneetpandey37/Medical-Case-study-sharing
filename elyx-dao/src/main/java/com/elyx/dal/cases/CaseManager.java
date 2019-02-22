package com.elyx.dal.cases;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elyx.model.cases.Case;
import com.elyx.model.cases.CaseSharing;

@Repository("caseManager")
@Transactional(propagation = Propagation.REQUIRED)
public class CaseManager {

	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Case getCase(Long id) {
		List<Case> results = entityManager
				.createNamedQuery("Case.findByCaseId", Case.class)
				.setParameter("id", id).getResultList();
		return results.isEmpty() ? null : results.get(0);
	}

	public List<Case> getCasesSharedWithTenant(long id, long userId) {

		TypedQuery<Case> query = entityManager.createNamedQuery(
				"Case.findByTenantId", Case.class);
		query.setParameter("id", id);
		query.setParameter("userId", userId);
		List<Case> results = query.getResultList();
		// List<Case> results = entityManager
		// .createNamedQuery("Case.findAll", Case.class)
		// .setParameter("id", id).getResultList();
		return results;
	}

	/*public List<Case> getCasesByUserId(long id, Integer start, Integer maxResult, String orderBy, String order) {
		List<Case> results = entityManager
				.createNamedQuery("Case.findByUserId", Case.class)
				.setParameter("id", id)
				.setParameter("orderBy", orderBy)
				.setParameter("order", order)
				.setFirstResult(start)
				.setMaxResults(maxResult)
				.getResultList();
		return results;
	}*/
	
	
	/*public List<Case> getCasesByUserId(long id, Integer start,
			Integer maxResult, String orderBy, String order) {
		
		String queryString = "SELECT c FROM Case c where c.user.id = :id";// order by c."+orderBy+ " "+ order;
		TypedQuery<Case> query = entityManager.createQuery(queryString, Case.class)
				.setParameter("id", id)
				.setFirstResult(start)
				.setMaxResults(maxResult);
		return query.getResultList();
	}*/
	
	
	/*public List<Case> getCasesByUserId(long id) {
		List<Case> results = entityManager
				.createNamedQuery("Case.findByUserId", Case.class)
				.setParameter("id", id)
				.getResultList();
		return results;
	}*/
	
	
	public List<Case> getCasesByUserId(String queryInput) {
		TypedQuery<Case> query = entityManager.createQuery(queryInput,
				Case.class);
		List<Case> results = query.getResultList();
		return results;
	}
	
	public Case createCase(Case medCase) {
		return entityManager.merge(medCase);
	}

	public void shareCase(CaseSharing caseShare) {
		entityManager.merge(caseShare);
	}

	public List<Case> getCasesSharedWithMe(long userId) {
		List<Case> results = entityManager
				.createNamedQuery("CaseSharing.findCasesSharedToMe", Case.class)
				.setParameter("id", userId).getResultList();
		return results;
	}

	public Long getMaxCaseShareId() {
		return (Long) entityManager.createQuery(
				"SELECT max(cs.id) FROM CaseSharing cs", Long.class)
				.getSingleResult();
	}

	public CaseSharing getShareCaseByCaseAndUserId(long userId, long caseId) {
		List<CaseSharing> results = entityManager
				.createNamedQuery("CaseSharing.findCasesSharedByCaseAndUserId",
						CaseSharing.class).setParameter("userId", userId)
				.setParameter("caseId", caseId).getResultList();
		return results.isEmpty() ? null : results.get(0);
	}

}
