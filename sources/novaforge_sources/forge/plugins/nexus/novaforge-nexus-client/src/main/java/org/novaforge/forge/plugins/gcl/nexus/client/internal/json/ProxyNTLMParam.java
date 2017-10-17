package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class ProxyNTLMParam extends ProxyParam {

	private String ntlmHost;
	private String domain;

	public ProxyNTLMParam(String host, int port, String ntlmHost, String domain) {
		super(host, port);
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
		return "ProxyNTLMParam [host=" + host + ", port=" + port + ", ntlmHost=" + ntlmHost + ", domain=" + domain
				+ "]";
	}
}
