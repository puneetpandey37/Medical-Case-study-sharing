package com.elyx.bo.tenant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elyx.common.exceptions.ElyxUserDatExistsException;
import com.elyx.common.security.ValidateUserAccess;
import com.elyx.common.util.DateTimeUtil;
import com.elyx.dal.tenant.TenantManager;
import com.elyx.model.user.Tenant;

@Component
public class TenantService {

	TenantManager tenantManager;
	private ValidateUserAccess validateUserAccess;

	public ValidateUserAccess getValidateUserAccess() {
		return validateUserAccess;
	}
    @Autowired
	public void setValidateUserAccess(ValidateUserAccess validateUserAccess) {
		this.validateUserAccess = validateUserAccess;
	}
	@Autowired
	public void setTenantManager(TenantManager tenantManager) {
		this.tenantManager = tenantManager;
	}

	public List<Tenant> getTenants() {
		return tenantManager.getTenants();
	}

	public Tenant createTenant(Tenant tenant,String clientId, String clientSecret) throws ElyxUserDatExistsException {
		validateUserAccess.validateClientSecret(clientId, clientSecret);
		tenant.setDateCreated(DateTimeUtil.getCurrentDateTime());
		tenant.setDateUpdated(DateTimeUtil.getCurrentDateTime());
		return tenantManager.createTenant(tenant);
	}
	
	public Tenant updateUser(Tenant tenant) {
		return tenantManager.createTenant(tenant);
	}
}
