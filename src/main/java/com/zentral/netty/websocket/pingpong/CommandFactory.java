package com.zentral.netty.websocket.pingpong;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandFactory {

	public static Command<String> getInstance(String received) throws JsonProcessingException, IOException 
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(received);
		JsonNode name = node.get("name");
		CommandEnum cmd = CommandEnum.valueOf(name.asText());
		Command<String> result = null;
		if (CommandEnum.HEARTBEAT.equals(cmd))
		{
			result = mapper.readValue(received, HeartBeatCommand.class);
		}
		return result;
	}
}
