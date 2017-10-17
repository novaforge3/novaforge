package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class NexusObjectMapperProvider implements ContextResolver<ObjectMapper> {
 
    final ObjectMapper defaultObjectMapper;
 
    public NexusObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }
 
//    @Override
    public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
    
    }
 
    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(SerializationFeature.INDENT_OUTPUT, true);
        result.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
 
        return result;
    }
}
