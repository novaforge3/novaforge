package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;


/**
 * JSON response  returned by the Nexus Rest request.
 * 
 * @author s241664
 *
 */
public class JSONResponse {

	// function name called by the request
	private String name;

	// result returned by the function called on the Nexus server:  object serialized by the toString method
	// returnValue.toString()
	private String result;
	
	private int status;

	public JSONResponse() {
		super();
	}

	public JSONResponse(String name, String result) {
		super();
		this.name = name;
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "JSONResponse [name=" + name + ", result=" + result + ", status=" + status + "]";
	}

}
