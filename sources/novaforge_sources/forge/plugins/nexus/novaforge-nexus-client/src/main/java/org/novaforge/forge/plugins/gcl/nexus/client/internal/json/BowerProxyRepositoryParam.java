package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class BowerProxyRepositoryParam extends ProxyRepositoryParam {

	
	private boolean rewritePackageUrls = false;
	
	public BowerProxyRepositoryParam(String name, String remoteUrl) {
		super(name, remoteUrl);

	}
	
	public BowerProxyRepositoryParam(String name, String remoteUrl, String blobStoreName,
			boolean strictContentTypeValidation, boolean rewritePackageUrls) {
		super(name, remoteUrl, blobStoreName, strictContentTypeValidation);
		this.rewritePackageUrls = rewritePackageUrls;
	}

	public boolean isRewritePackageUrls() {
		return rewritePackageUrls;
	}

	@Override
	public String toString() {
		return "BowerProxyRepositoryParam [name=" + name + ", remoteUrl=" + remoteUrl + ", blobStoreName="
				+ blobStoreName + ", strictContentTypeValidation=" + strictContentTypeValidation
				+ ", rewritePackageUrls=" + rewritePackageUrls + "]";
	}



}
