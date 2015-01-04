package com.zentral.netty.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zentral.netty.websocket.pingpong.HeartBeatCommand;

public class CommandTest {

	@Test
	public void heartBeatDataBinding() throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		String json = "{\"name\":\"HeartBeat\",\"headers\":{\"source\":\"source1\",\"destination\":\"destination1\"},\"body\":\"HALO\"}";
		HeartBeatCommand hb = new HeartBeatCommand();
		hb.setName("HeartBeat");
		hb.getHeaders().put("source", "source1");
		hb.getHeaders().put("destination", "destination1");
		hb.setBody("HALO");
		String out = mapper.writeValueAsString(hb);
		Assert.assertEquals(json, out);
	}
	
	@Test
	public void heartBeatStreamParse() throws JsonParseException, IOException 
	{
		String json = "{\"name\":\"HeartBeat\",\"headers\":{\"source\":\"source1\",\"destination\":\"destination1\"},\"body\":\"HALO\"}";
		JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);
        
        while (!parser.isClosed()) {
        	JsonToken token = parser.nextToken();
        	if (token == null)
                break;
        	if (JsonToken.FIELD_NAME.equals(token) && "name".equals(parser.getCurrentName()))
        	{
        		token = parser.nextToken();
        		System.err.println(parser.getText());
        	}
        }
        	
        
	}
}
