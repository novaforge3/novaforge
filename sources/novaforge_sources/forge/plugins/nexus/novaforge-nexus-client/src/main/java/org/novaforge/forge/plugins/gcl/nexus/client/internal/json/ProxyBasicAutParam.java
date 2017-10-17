package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class ProxyBasicAutParam {

	protected String host;
	protected int port;
	protected String username;
	protected String password;

	public ProxyBasicAutParam(String host, int port, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "ProxyBasicAutParam [host=" + host + ", port=" + port + ", username=" + username + ", password="
				+ "********" + "]";
	}
}
