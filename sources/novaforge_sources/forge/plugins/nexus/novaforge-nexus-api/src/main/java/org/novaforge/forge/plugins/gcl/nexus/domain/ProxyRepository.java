package org.novaforge.forge.plugins.gcl.nexus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyRepository extends Repository {

	public final static String TYPE = "proxy";
			
	private String remoteUrl;
	
	public ProxyRepository() {
		super();
	}

	public ProxyRepository(RepositoryFormat format, String name) {
		super(format, name);
	}

	public ProxyRepository(String format, String name, String url) {
		super(format, name, url);

	}

	public ProxyRepository(String remoteUrl) {
		super();
		this.remoteUrl = remoteUrl;
	}

	@JsonCreator
	public ProxyRepository(@JsonProperty("format")String format, @JsonProperty("name")String name,@JsonProperty("url")String url, @JsonProperty("versionPolicy")String versionPolicy, @JsonProperty("remoteUrl")String remoteUrl) {
		super(format, name, url, versionPolicy);
		this.remoteUrl = remoteUrl;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	
	@Override
	public String getType() {
	
		return TYPE;
	}

	@Override
	public String toString() {
		
		String strVersionPolicy = "null";
		String strRemoteUrl = "null";
		
		if(versionPolicy != null){
			
			strVersionPolicy = this.versionPolicy.toString();
		}
		
		if(strRemoteUrl != null){
			
			strRemoteUrl = this.remoteUrl;
		}
		
		return "ProxyRepository [format=" + format + ", name=" + name + ", url=" + url + ", versionPolicy=" + strVersionPolicy + ", remoteUrl=" + strRemoteUrl
				+ "]";
	}
}
