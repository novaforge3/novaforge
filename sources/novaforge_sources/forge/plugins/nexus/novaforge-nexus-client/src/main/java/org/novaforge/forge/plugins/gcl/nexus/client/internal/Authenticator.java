package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.io.BaseEncoding;

public class Authenticator implements javax.ws.rs.client.ClientRequestFilter {


	/**
	 * Logger component
	 */
	private static final Log LOGGER = LogFactory.getLog(Authenticator.class);
	
    private String user;
    private String password;

    public Authenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add(
            HttpHeaders.AUTHORIZATION, getBasicAuthentication());
    }

    private String getBasicAuthentication() {
        String userAndPassword = this.user + ":" + this.password;
        byte[] userAndPasswordBytes = null;
        try{
        	userAndPasswordBytes = userAndPassword.getBytes("UTF-8");
        } catch ( UnsupportedEncodingException uee)
        {
        	//password not logged for security purpose
        	LOGGER.error("error occurred when encoding user and password, user="+user, uee);
        }
        return "Basic " + BaseEncoding.base64().encode(userAndPasswordBytes);
    }


}
