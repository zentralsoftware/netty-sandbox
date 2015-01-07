package com.zentral.netty.websocket.pingpong;

public class ErrorCommand extends Command<String>{

	private static final long serialVersionUID = -1932807566976623685L;

	public ErrorCommand()
	{
		setName(CommandEnum.ERROR);
	}
}
