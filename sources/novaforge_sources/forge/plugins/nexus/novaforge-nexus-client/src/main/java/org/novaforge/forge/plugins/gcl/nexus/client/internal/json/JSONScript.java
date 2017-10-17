package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

public class JSONScript {

	private String name;
	private String content;
	private String type;

	public JSONScript() {
		super();
	}
	
	public JSONScript(String name, String content, String type) {
		super();
		this.name = name;
		this.content = content;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setType(String type) {
		this.type = type;
	}

}
