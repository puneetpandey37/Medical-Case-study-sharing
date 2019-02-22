package com.elyx.controller.tenant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.tenant.TenantService;
import com.elyx.model.user.Tenant;

@Controller
@RequestMapping("/tenants")
public class ElyxTenantController {

	TenantService tenantService;
	@Autowired
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Tenant> getTenants() {
		return tenantService.getTenants();
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Tenant updateTenant(@RequestBody Tenant tenant) {
		return tenantService.updateUser(tenant);
	}
	
}
