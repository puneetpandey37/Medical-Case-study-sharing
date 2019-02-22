package com.elyx.common.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author puneet
 *
 */
@Controller
public class ElyxUserLogout {
@SuppressWarnings("deprecation")
@RequestMapping(value="/logout")
   public String logoutPage (HttpServletRequest request, HttpServletResponse response) throws Exception {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null){    
         new SecurityContextLogoutHandler().logout(request, response, auth);
         new PersistentTokenBasedRememberMeServices().logout(request, response, auth);
      }
	return null;
   }
}