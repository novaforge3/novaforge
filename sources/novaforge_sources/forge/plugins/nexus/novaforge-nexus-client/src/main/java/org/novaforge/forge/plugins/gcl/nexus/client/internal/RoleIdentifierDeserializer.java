package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.IOException;

import org.sonatype.nexus.security.role.RoleIdentifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class RoleIdentifierDeserializer extends StdDeserializer<RoleIdentifier> {

	public RoleIdentifierDeserializer(Class<?> vc) {
		super(vc);
	}

	public RoleIdentifierDeserializer(JavaType valueType) {
		super(valueType);
	}

//	public RoleIdentifierDeserializer(StdDeserializer<?> src) {
//		super(src);
//	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6082573926987358504L;

	@Override
	public RoleIdentifier deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

		String source = node.get("source").asText();
		String roleId = node.get("roleId").asText();

		return new RoleIdentifier(source, roleId);
	}

}
