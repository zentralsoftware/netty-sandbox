package com.zentral.netty.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zentral.netty.websocket.pingpong.Command;
import com.zentral.netty.websocket.pingpong.CommandEnum;
import com.zentral.netty.websocket.pingpong.CommandFactory;
import com.zentral.netty.websocket.pingpong.HeartBeatCommand;

public class CommandFactoryTest {

	@Test
	public void heartBeat() throws JsonProcessingException, IOException
	{
		String json = "{\"name\":\""+CommandEnum.HEARTBEAT+"\",\"headers\":{\"source\":\"source1\",\"destination\":\"destination1\"},\"body\":\"HALO\"}";
		Command<String> cmd = CommandFactory.getInstance(json);
		System.err.println(cmd.getClass().getName());
		Assert.assertEquals(true, cmd instanceof HeartBeatCommand);
	}
}
