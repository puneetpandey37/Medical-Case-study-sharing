package com.elyx.common.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.kernel.impl.nioneo.store.InvalidRecordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.elyx.common.exceptions.ElyxBadCredentialsException;
import com.elyx.common.exceptions.ElyxOTPNotFoundException;
import com.elyx.common.exceptions.ElyxUserNotExistsException;
import com.elyx.common.util.RandomNumberUtil;
import com.elyx.common.util.SmsGatewayUtil;
import com.elyx.dal.user.UserManager;
import com.elyx.model.user.User;

/**
 * @author Puneet Pandey
 *
 */
public class ElyxAuthenticationProvider implements AuthenticationProvider {
	
	private UserManager userManager;
	private SmsGatewayUtil smsGatewayUtil;
	@Autowired
	public void setSmsGatewayUtil(SmsGatewayUtil smsGatewayUtil) {
		this.smsGatewayUtil = smsGatewayUtil;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		ElyxPasswordAuthenticationToken userPassAuthToken = null;
		
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		String userName = (String) authentication.getPrincipal();
		User user = userManager.getUserByLogin(userName);
		
		if(user != null) {
			String passwordInput = (String) authentication.getCredentials();
			Integer otp = user.getOtp();
			String otpPassword = String.valueOf(otp);
			
			if(passwordInput == null) {
				int otpNumber = RandomNumberUtil.getRandomNumber();
				userManager.setUserOTP(userName, otpNumber);
				try {
					smsGatewayUtil.sendMessage(userName, otpNumber, "otp");
				} catch (IOException e) {
					throw new ElyxOTPNotFoundException("Send another OTP request");
				}
				throw new ElyxOTPNotFoundException("OTP not provided! Please check your inbox now!");
			} else {
				if (passwordInput.equals(otpPassword)) {
					userPassAuthToken = new ElyxPasswordAuthenticationToken(authentication.getPrincipal(),
							authentication.getCredentials(), grantedAuthorities);
					userManager.setUserStatus(userName, 1);
				} else {
					throw new ElyxBadCredentialsException("Bad User Credentials.");
				}
			}
		} else {
			throw new ElyxUserNotExistsException("User Does Not Exist");
		}
		return userPassAuthToken;
	}

	public boolean supports(Class<? extends Object> authentication) {
		return true;
	}

}
