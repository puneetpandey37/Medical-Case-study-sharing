package com.elyx.common.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Puneet Pandey
 *
 */
public class ElyxPasswordAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 310L;
	private final Object principal;
	private Object credentials;
	private int loginId;

	public int getLoginId() {
		return loginId;
	}
	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}
	public ElyxPasswordAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}
	public ElyxPasswordAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}
	public Object getCredentials() {
		return this.credentials;
	}
	public Object getPrincipal() {
		return this.principal;
	}
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted");
		}

		super.setAuthenticated(false);
	}
	public void eraseCredentials() {
		super.eraseCredentials();
		this.credentials = null;
	}
}