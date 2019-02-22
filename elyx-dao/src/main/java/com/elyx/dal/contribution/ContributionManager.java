package com.elyx.dal.contribution;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elyx.model.contribution.Contribution;

@Repository("contributionManager")
@Transactional(propagation = Propagation.REQUIRED)
public class ContributionManager {

	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Contribution contribute(Contribution contribution){
		return entityManager.merge(contribution);
	}
	
	public List<Contribution> getContributionsByCaseId(long caseId){
		List<Contribution> results = entityManager
	            .createNamedQuery("Contribution.findByCaseId", Contribution.class)
	            .setParameter("id", caseId).getResultList();
		return results;
	}
	
	public List<Contribution> getContributionsByUserIdAndCaseId(long userId, long caseId){
		List<Contribution> results = entityManager
	            .createNamedQuery("Contribution.findByUserIdAndCaseId", Contribution.class)
	            .setParameter("userId", userId).setParameter("caseId", caseId).getResultList();
		return results;
	}
	
	public List<Contribution> getContributionsByUserId(long userId){
		List<Contribution> results = entityManager
	            .createNamedQuery("Contribution.findByUserId", Contribution.class)
	            .setParameter("id", userId).getResultList();
		return results;
	}
	
	public Contribution getContributionDetail(long contributionId) {
		List<Contribution> results = entityManager
	            .createNamedQuery("Contribution.findDetail", Contribution.class)
	            .setParameter("id", contributionId).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}

	public void deleteContribution(long contributionId) {
		Contribution contribution = entityManager.find(Contribution.class, contributionId);
		entityManager.remove(contribution);
	}
}
