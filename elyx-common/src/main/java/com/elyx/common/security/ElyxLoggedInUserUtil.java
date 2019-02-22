package com.elyx.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.elyx.dal.tenant.TenantManager;
import com.elyx.dal.user.UserManager;
import com.elyx.model.user.User;

@Component
public class ElyxLoggedInUserUtil {

	private UserManager userManager;
	private TenantManager tenantManager;
	public TenantManager getTenantManager() {
		return tenantManager;
	}
	@Autowired
	public void setTenantManager(TenantManager tenantManager) {
		this.tenantManager = tenantManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public User getLoggedInUser() {
		String userName = getLoggedInUserName();
		User user = userManager.getUserByUserName(userName);
		return user;
	}
	
	public long getLoggedInUserTenantId() {
		String userName = getLoggedInUserName();
		User user = userManager.getUserByUserName(userName);
		long tenantId = user.getParentTenant().getId();
		return tenantId; 
	}
	
	public String getLoggedInUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = (String)authentication.getPrincipal();
		return userName;
	}
	
	public long getLoggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = (String)authentication.getPrincipal();
		User user = userManager.getUserByUserName(userName);
		return user.getId();
	}
}
