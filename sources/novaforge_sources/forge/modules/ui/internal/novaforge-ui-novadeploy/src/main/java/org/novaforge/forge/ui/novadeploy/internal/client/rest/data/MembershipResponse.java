package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@SuppressWarnings("serial")
@JsonRootName(value="response") 
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class MembershipResponse extends ServiceResponse implements Serializable
{

	@JsonProperty
	private Membership membership;

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}
	

}