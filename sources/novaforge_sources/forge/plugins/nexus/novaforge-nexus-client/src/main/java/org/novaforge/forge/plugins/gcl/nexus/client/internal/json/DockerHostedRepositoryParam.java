package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import org.sonatype.nexus.repository.storage.WritePolicy;

public class DockerHostedRepositoryParam extends HostedRepositoryParam {


	// The http port to accept traffic for this Repository on (optional)
	private Integer httpPort;
	// The https port to accept traffic for this Repository on (optional)
	private Integer httpsPort;
	// Whether or not this Repository supports Docker V1 format
	private boolean v1Enabled = true;
	
	public DockerHostedRepositoryParam(String name, Integer httpPort, Integer httpsPort) {
		super(name);
		this.httpPort = httpPort;
		this.httpsPort = httpsPort;
	}
	
	public DockerHostedRepositoryParam(String name, String blobStoreName, boolean strictContentTypeValidation,
			WritePolicy writePolicy, Integer httpPort, Integer httpsPort, boolean v1Enabled) {
		super(name, blobStoreName, strictContentTypeValidation, writePolicy);
		this.httpPort = httpPort;
		this.httpsPort = httpsPort;
		this.v1Enabled = v1Enabled;
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
		return "DockerHostedRepositoryParam [name=" + name + ", blobStoreName=" + blobStoreName
				+ ", strictContentTypeValidation=" + strictContentTypeValidation + ", writePolicy=" + writePolicy
				+ ", httpPort=" + httpPort + ", httpsPort=" + httpsPort + ", v1Enabled=" + v1Enabled + "]";
	}
}
