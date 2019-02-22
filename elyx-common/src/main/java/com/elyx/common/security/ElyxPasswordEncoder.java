package com.elyx.common.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Puneet Pandey
 *
 */
public class ElyxPasswordEncoder {

	public String getEncryptedPassword(String password) {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encryptedPass = bCryptPasswordEncoder.encode(password);
		return encryptedPass;
	}
}
