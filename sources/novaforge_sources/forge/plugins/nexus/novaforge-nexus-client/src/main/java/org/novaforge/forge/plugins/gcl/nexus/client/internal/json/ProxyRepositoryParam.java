package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class ProxyRepositoryParam {

	protected final static String DEFAULT_BLOBSTORE_NAME = "default";
	
	// The name of the new Repository
	protected String name;
	// The url of the external proxy for this Repository
	protected String remoteUrl;
	// The BlobStore the Repository should use
	protected String blobStoreName = DEFAULT_BLOBSTORE_NAME;
	// Whether or not the Repository should enforce strict content types
	protected boolean strictContentTypeValidation = true;
	
	public ProxyRepositoryParam(String name, String remoteUrl) {
		super();
		this.name = name;
		this.remoteUrl = remoteUrl;

	}
	
	public ProxyRepositoryParam(String name, String remoteUrl, String blobStoreName, boolean strictContentTypeValidation) {
		super();
		this.name = name;
		this.remoteUrl = remoteUrl;
		this.blobStoreName = blobStoreName;
		this.strictContentTypeValidation = strictContentTypeValidation;
	}

	public String getName() {
		return name;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public String getBlobStoreName() {
		return blobStoreName;
	}

	public boolean isStrictContentTypeValidation() {
		return strictContentTypeValidation;
	}

	@Override
	public String toString() {
		return "ProxyRepositoryParam [name=" + name + ", remoteUrl=" + remoteUrl + ", blobStoreName=" + blobStoreName
				+ ", strictContentTypeValidation=" + strictContentTypeValidation + "]";
	}

}
