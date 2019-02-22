package com.elyx.controller.signon;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elyx.bo.tenant.TenantService;
import com.elyx.bo.user.UserService;
import com.elyx.common.exceptions.ElyxUserDatExistsException;
import com.elyx.model.user.Tenant;
import com.elyx.model.user.User;

@Controller
@RequestMapping("/signon")
public class ElyxSingOnController {

	UserService userService;
	TenantService tenantService;

	@Autowired
	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<User> getUsers() {
		return userService.getUsers(null);
	}

	@RequestMapping(value = "/tenant", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Tenant createTenant(@RequestBody Tenant tenant,
			@RequestHeader(value = "Client-Id") String clientId,
			@RequestHeader(value = "Client-Secret") String clientSecret)
			throws ElyxUserDatExistsException {
		return tenantService.createTenant(tenant, clientId, clientSecret);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public User createUser(@RequestBody User user/*,
			@RequestHeader(value = "Client-Id") String clientId,
			@RequestHeader(value = "Client-Secret") String clientSecret*/)
			throws ElyxUserDatExistsException, IOException {
		return userService.createUser(user/*, clientId, clientSecret*/);
	}
}
