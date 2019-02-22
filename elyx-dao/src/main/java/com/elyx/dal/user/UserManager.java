package com.elyx.dal.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elyx.model.user.User;

@Repository("userManager")
@Transactional(propagation = Propagation.REQUIRED)
public class UserManager {

	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<User> getUsers(long tenantId) {
		List<User> results = entityManager
	            .createNamedQuery("User.findAll", User.class).setParameter("id", tenantId).getResultList();
		return results;
	}
	
	public User getUserByUserName(String userName) {
		List<User> results = entityManager
	            .createNamedQuery("User.findByLogin", User.class)
	            .setParameter("login", userName).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}
	
	public String getUserPassword(String userName) {
		String sqlString = "select u.password from user u where u.login = ?";
		List resultList = entityManager
	            .createNativeQuery(sqlString)
	            .setParameter(1, userName).getResultList();
		List<String> results = resultList;
	      return resultList.isEmpty() ? null : results.get(0);
	}
	
	@Transactional
	public User createUser(User user) {
		return entityManager.merge(user);
	}

	public User getUserByMobileNo(String mobileNo) {
		List<User> results = entityManager
	            .createNamedQuery("User.findByPhone", User.class)
	            .setParameter("phone", mobileNo).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}
	
	public User getUserByLogin(String login) {
		List<User> results = entityManager
	            .createNamedQuery("User.findByLogin", User.class)
	            .setParameter("login", login).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}

	public User getUserByEmail(String email) {
		List<User> results = entityManager
	            .createNamedQuery("User.findByEmail", User.class)
	            .setParameter("email", email).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}
	
	public User getUserById(Long id) {
		List<User> results = entityManager
	            .createNamedQuery("User.findById", User.class)
	            .setParameter("id", id).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}

	public User getUserByOtp(String mobileNumber, int otp) {
		List<User> results = entityManager
	            .createNamedQuery("User.findByMobAndOtp", User.class)
	            .setParameter("phone", mobileNumber)
	            .setParameter("otp", otp).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}

	public void setUserOTP(String userName, int otp) {
		String sqlString = "update User u set u.otp = :otp where u.login = :login";
		entityManager
	            .createQuery(sqlString)
	            .setParameter("otp", otp).
	            setParameter("login", userName).executeUpdate();
	}
	
	public void setUserStatus(String userName, int status) {
		String sqlString = "update User u set u.status = :status where u.login = :login";
		entityManager
	            .createQuery(sqlString)
	            .setParameter("status", status).
	            setParameter("login", userName).executeUpdate();
	}

	public Integer getOtpForMobileNo(String userName) {
		List<Integer> results = entityManager
	            .createNamedQuery("User.findOtpForMobileNo", Integer.class)
	            .setParameter("login", userName).getResultList();
	      return results.isEmpty() ? null : results.get(0);
	}

	public List<User> searchUser(String searchCriteria) {
		List<User> results = entityManager
	            .createNamedQuery("User.userSearch", User.class).setParameter("input", searchCriteria).getResultList();
		return results;
	}

	public void updateNumberOfImages(int images, long userId) {
		String sqlString = "update User u set u.images = :images where u.id = :userId";
		entityManager
	            .createQuery(sqlString)
	            .setParameter("images", images)
	            .setParameter("userId", userId).executeUpdate();
	}
}
