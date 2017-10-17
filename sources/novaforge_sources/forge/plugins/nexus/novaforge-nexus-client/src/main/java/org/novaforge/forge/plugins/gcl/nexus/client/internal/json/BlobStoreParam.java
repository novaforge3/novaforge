package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class BlobStoreParam {

	// the name for the new BlobStore
	private String name;

	// the path where the BlobStore should store data
	private String path;

	public BlobStoreParam(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "BlobStoreParam [name=" + name + ", path=" + path + "]";
	}
}
