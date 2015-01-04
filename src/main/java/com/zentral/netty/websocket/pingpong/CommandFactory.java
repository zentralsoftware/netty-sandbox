package com.zentral.netty.websocket.pingpong;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandFactory {

	public static HeartBeatCommand getInstance(String received) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		HeartBeatCommand obj = objectMapper.readValue(received, new TypeReference<HeartBeatCommand>() {});
		return obj;
	}
}
