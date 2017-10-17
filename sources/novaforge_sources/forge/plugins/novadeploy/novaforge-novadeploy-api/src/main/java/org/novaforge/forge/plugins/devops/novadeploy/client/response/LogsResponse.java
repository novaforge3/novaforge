package org.novaforge.forge.plugins.devops.novadeploy.client.response;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonRootName(value = "response")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LogsResponse extends ServiceResponse implements Serializable
{
	@JsonProperty
	public String logID;
	@JsonProperty
	public String[] logs;

	@JsonIgnore
	public String[] getLogs()
	{
		return logs;
	}

	public void setLogs(List<String> logs)
	{
		this.logs = new String[logs.size()];
		logs.toArray(this.logs);
	}

	public void setLogID(String logID)
	{
		this.logID = logID;
	}

}
