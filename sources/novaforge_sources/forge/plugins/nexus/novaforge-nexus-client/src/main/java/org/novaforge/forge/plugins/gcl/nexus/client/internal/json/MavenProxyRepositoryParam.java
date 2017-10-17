package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import org.sonatype.nexus.repository.maven.LayoutPolicy;
import org.sonatype.nexus.repository.maven.VersionPolicy;

public class MavenProxyRepositoryParam extends ProxyRepositoryParam {

	private final static VersionPolicy DEFAULT_VERSION_POLICY = VersionPolicy.RELEASE;
	private final static LayoutPolicy DEFAULT_LAYOUT_POLICY = LayoutPolicy.STRICT;

	// The {@link VersionPolicy} for the Repository
	private VersionPolicy versionPolicy;
	// The {@link LayoutPolicy} for the Repository
	private LayoutPolicy layoutPolicy;
	
	public MavenProxyRepositoryParam(String name, String remoteUrl) {
		super(name, remoteUrl);
		this.versionPolicy = DEFAULT_VERSION_POLICY;
		this.layoutPolicy = DEFAULT_LAYOUT_POLICY;
	}
	
	public MavenProxyRepositoryParam(String name, String remoteUrl, String blobStoreName,
			boolean strictContentTypeValidation, VersionPolicy versionPolicy, LayoutPolicy layoutPolicy) {
		super(name, remoteUrl, blobStoreName, strictContentTypeValidation);
		this.versionPolicy = versionPolicy;
		this.layoutPolicy = layoutPolicy;
	}
	
	public VersionPolicy getVersionPolicy() {
		return versionPolicy;
	}
	public LayoutPolicy getLayoutPolicy() {
		return layoutPolicy;
	}

	@Override
	public String toString() {
		return "MavenProxyRepositoryParam [name=" + name + ", remoteUrl=" + remoteUrl + ", blobStoreName="
				+ blobStoreName + ", strictContentTypeValidation=" + strictContentTypeValidation + ", versionPolicy="
				+ versionPolicy + ", layoutPolicy=" + layoutPolicy + "]";
	}

}
