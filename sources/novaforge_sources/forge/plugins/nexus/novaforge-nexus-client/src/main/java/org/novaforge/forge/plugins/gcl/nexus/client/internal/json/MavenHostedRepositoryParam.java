package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import org.sonatype.nexus.repository.maven.LayoutPolicy;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.repository.storage.WritePolicy;

public class MavenHostedRepositoryParam extends HostedRepositoryParam {

	private final static VersionPolicy DEFAULT_VERSION_POLICY = VersionPolicy.RELEASE;
	private final static LayoutPolicy DEFAULT_LAYOUT_POLICY = LayoutPolicy.STRICT;
	
	// The {@link VersionPolicy} for the Repository
	private VersionPolicy versionPolicy;
	// The {@link LayoutPolicy} for the Repository
	private LayoutPolicy layoutPolicy;

	public MavenHostedRepositoryParam(String name) {
		super(name);
		this.versionPolicy = DEFAULT_VERSION_POLICY;
		this.layoutPolicy = DEFAULT_LAYOUT_POLICY;
	}
	
	public MavenHostedRepositoryParam(String name, String blobStoreName, boolean strictContentTypeValidation,
			WritePolicy writePolicy, VersionPolicy versionPolicy, LayoutPolicy layoutPolicy) {
		super(name, blobStoreName, strictContentTypeValidation, writePolicy);
		this.versionPolicy = versionPolicy;
		this.layoutPolicy = layoutPolicy;
	}

	public VersionPolicy getVersionPolicy() {
		return versionPolicy;
	}

	public LayoutPolicy getLayoutPolicy() {
		return layoutPolicy;
	}

	public void setVersionPolicy(VersionPolicy versionPolicy) {
		this.versionPolicy = versionPolicy;
	}

	public void setLayoutPolicy(LayoutPolicy layoutPolicy) {
		this.layoutPolicy = layoutPolicy;
	}

	@Override
	public String toString() {
		return "MavenHostedRepositoryParam [name=" + name + ", blobStoreName=" + blobStoreName
				+ ", strictContentTypeValidation=" + strictContentTypeValidation + ", writePolicy=" + writePolicy
				+ ", versionPolicy=" + versionPolicy + ", layoutPolicy=" + layoutPolicy + "]";
	}

}
