package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class ProxyParam {

	protected String host;
	protected int port;

	public ProxyParam(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "ProxyParam [host=" + host + ", port=" + port + "]";
	}

}
