package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class DockerProxyRepositoryParam extends ProxyRepositoryParam {

	// Use 'REGISTRY' to use the proxy url for the index as well. Use 'HUB' to use the index from DockerHub.
	// Use 'CUSTOM' in conjunction with the 'indexUrl' param to specify a custom index location
	private String indexType;
	// The url of a 'CUSTOM' index; only used in conjunction with the 'indexType' parameter
	private String indexUrl;
	// The http port to accept traffic for this Repository on (optional)
	private Integer httpPort;
	// The https port to accept traffic for this Repository on (optional)
	private Integer httpsPort;
	// Whether or not this Repository supports Docker V1 format
	private boolean v1Enabled = true;
	
	
	public DockerProxyRepositoryParam(String name, String remoteUrl, String indexType, String indexUrl, Integer httpPort, Integer httpsPort) {
		super(name, remoteUrl);
		this.indexType = indexType;
		this.indexUrl = indexUrl;
		this.httpPort = httpPort;
		this.httpsPort = httpsPort;
	}
	
	public DockerProxyRepositoryParam(String name, String remoteUrl, String indexType, 
			String indexUrl, Integer httpPort, Integer httpsPort, boolean v1Enabled) {
		super(name, remoteUrl);
		this.indexType = indexType;
		this.indexUrl = indexUrl;
		this.httpPort = httpPort;
		this.httpsPort = httpsPort;
		this.v1Enabled = v1Enabled;
	}
	public DockerProxyRepositoryParam(String name, String remoteUrl, String blobStoreName,
			boolean strictContentTypeValidation, String indexType, String indexUrl, Integer httpPort, Integer httpsPort,
			boolean v1Enabled) {
		super(name, remoteUrl, blobStoreName, strictContentTypeValidation);
		this.indexType = indexType;
		this.indexUrl = indexUrl;
		this.httpPort = httpPort;
		this.httpsPort = httpsPort;
		this.v1Enabled = v1Enabled;
	}
	
	public String getIndexType() {
		return indexType;
	}
	public String getIndexUrl() {
		return indexUrl;
	}
	public Integer getHttpPort() {
		return httpPort;
	}
	public Integer getHttpsPort() {
		return httpsPort;
	}
	public boolean isV1Enabled() {
		return v1Enabled;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public void setHttpPort(Integer httpPort) {
		this.httpPort = httpPort;
	}

	public void setHttpsPort(Integer httpsPort) {
		this.httpsPort = httpsPort;
	}

	public void setV1Enabled(boolean v1Enabled) {
		this.v1Enabled = v1Enabled;
	}

	@Override
	public String toString() {
		return "DockerProxyRepositoryParam [name=" + name + ", remoteUrl=" + remoteUrl + ", blobStoreName="
				+ blobStoreName + ", strictContentTypeValidation=" + strictContentTypeValidation + ", indexType="
				+ indexType + ", indexUrl=" + indexUrl + ", httpPort=" + httpPort + ", httpsPort=" + httpsPort
				+ ", v1Enabled=" + v1Enabled + "]";
	}

}
