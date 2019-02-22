package com.elyx.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elyx.common.exceptions.ElyxUserDatExistsException;
import com.elyx.common.util.ElyxPropertiesUtil;
import com.elyx.dal.user.UserManager;
import com.elyx.model.user.User;

@Component
public class ValidateUserAccess {

	private UserManager userManager;

	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void validateClientSecret(String clientIdInput,
			String clientSecretInput) throws ElyxUserDatExistsException {
		ElyxPropertiesUtil propertiesUtil = new ElyxPropertiesUtil();
		String clientId = propertiesUtil.getElyxProperty("elyx.security.id");
		String exceptionMessage = null;
		if (clientId.equals(clientIdInput)) {
			String clientSecret = propertiesUtil
					.getElyxProperty("elyx.security.secret");
			if (!clientSecret.equals(clientSecretInput)) {
				exceptionMessage = "Unauthorized Access";
				throw new ElyxUserDatExistsException(exceptionMessage);
			}
		} else {
			exceptionMessage = "Unauthorized Access";
			throw new ElyxUserDatExistsException(exceptionMessage);
		}
	}

	public void checkIfUserDataExists(User user)
			throws ElyxUserDatExistsException {

		String exceptionMessage = null;
		String userName = user.getLogin();
		String mobileNo = user.getPhone();
		String email = user.getEmail();
		User userByUsername = userManager.getUserByUserName(userName);
		User userByMobileNo = userManager.getUserByMobileNo(mobileNo);
		User userByEmail = null;
		if (email != null) {
			userByEmail = userManager.getUserByEmail(email);
			if (userByEmail != null) {
				exceptionMessage = "Email already registered";
				
				int status = userByEmail.getStatus();
				if(status == 0) {
					exceptionMessage = "User already registered, but authenticity is not veified.";
				}
			}
		}

		if (userByMobileNo != null)
			exceptionMessage = "Mobile No already registered";
		if (userByUsername != null)
			exceptionMessage = "User already registered";

		if (exceptionMessage != null) {
			throw new ElyxUserDatExistsException(exceptionMessage);
		}
	}
}
