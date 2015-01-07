package com.zentral.netty.websocket.pingpong;

public class JoinCommand extends Command<String> {

	private static final long serialVersionUID = -1741456254713915656L;

	public JoinCommand()
	{
		setName(CommandEnum.JOIN);
	}
}
