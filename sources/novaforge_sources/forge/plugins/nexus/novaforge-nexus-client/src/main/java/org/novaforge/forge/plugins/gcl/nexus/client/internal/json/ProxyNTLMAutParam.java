package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class ProxyNTLMAutParam extends ProxyBasicAutParam {

	private String ntlmHost;
	private String domain;

	public ProxyNTLMAutParam(String host, int port, String username, String password, String ntlmHost,
			String domain) {
		super(host, port, username, password);
		this.ntlmHost = ntlmHost;
		this.domain = domain;
	}

	public String getNtlmHost() {
		return ntlmHost;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return "ProxyNTLMAutParam [host=" + host + ", port=" + port + ", username=" + username + ", password="
				+ "******" + ", ntlmHost=" + ntlmHost + ", domain=" + domain + "]";
	}

}
