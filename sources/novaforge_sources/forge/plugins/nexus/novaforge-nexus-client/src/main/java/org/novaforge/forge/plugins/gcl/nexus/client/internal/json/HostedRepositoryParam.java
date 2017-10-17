package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import org.sonatype.nexus.repository.storage.WritePolicy;

public class HostedRepositoryParam {

	private final static String DEFAULT_BLOBSTORE_NAME = "default";
	private final static WritePolicy DEFAULT_WRITE_POLICY = WritePolicy.ALLOW;
	// The name of the new Repository
	protected String name;
	// The BlobStore the Repository should use
	protected String blobStoreName = DEFAULT_BLOBSTORE_NAME;
	// Whether or not the Repository should enforce strict content types
	protected boolean strictContentTypeValidation = true;
	// The {@link WritePolicy} for the Repository
	protected WritePolicy writePolicy = DEFAULT_WRITE_POLICY;

	public HostedRepositoryParam() {
		super();
	}
	
	public HostedRepositoryParam(String name) {
		super();
		this.name = name;

	}
	
	public HostedRepositoryParam(String name, String blobStoreName, boolean strictContentTypeValidation,
			WritePolicy writePolicy) {
		super();
		this.name = name;
		this.blobStoreName = blobStoreName;
		this.strictContentTypeValidation = strictContentTypeValidation;
		this.writePolicy = writePolicy;
	}

	public String getName() {
		return name;
	}

	public String getBlobStoreName() {
		return blobStoreName;
	}

	public boolean isStrictContentTypeValidation() {
		return strictContentTypeValidation;
	}

	public WritePolicy getWritePolicy() {
		return writePolicy;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBlobStoreName(String blobStoreName) {
		this.blobStoreName = blobStoreName;
	}

	public void setStrictContentTypeValidation(boolean strictContentTypeValidation) {
		this.strictContentTypeValidation = strictContentTypeValidation;
	}

	public void setWritePolicy(WritePolicy writePolicy) {
		this.writePolicy = writePolicy;
	}

	@Override
	public String toString() {
		return "HostedRepositoryParam [name=" + name + ", blobStoreName=" + blobStoreName
				+ ", strictContentTypeValidation=" + strictContentTypeValidation + ", writePolicy=" + writePolicy + "]";
	}
	
	
}
