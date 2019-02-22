package com.elyx.common.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.BaseClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import com.elyx.common.util.ElyxPropertiesUtil;

/**
 * @author Puneet Pandey
 *
 */
@Service
public class ElyxClientDetailsService implements ClientDetailsService {

	public ClientDetails loadClientByClientId(String clientId)
			throws OAuth2Exception {

		ElyxPropertiesUtil propertiesUtil = new ElyxPropertiesUtil();
		String clientSecret = propertiesUtil
				.getElyxProperty("elyx.security.secret");

		if (clientId.equals("32ewfeer33ewd32")) {

			List<String> authorizedGrantTypes = new ArrayList<String>();
			authorizedGrantTypes.add("password");
			authorizedGrantTypes.add("refresh_token");
			authorizedGrantTypes.add("client_credentials");

			BaseClientDetails clientDetails = new BaseClientDetails();
			clientDetails.setClientId(clientId);
			clientDetails.setClientSecret(clientSecret);
			clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
			return clientDetails;
		}

		else {
			throw new NoSuchClientException("No client with requested id: "
					+ clientId);
		}
	}

}
