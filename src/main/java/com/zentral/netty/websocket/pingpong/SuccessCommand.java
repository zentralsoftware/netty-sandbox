package com.zentral.netty.websocket.pingpong;

public class SuccessCommand  extends Command<String> {

	private static final long serialVersionUID = -4062741576892116602L;

	public SuccessCommand()
	{
		setName(CommandEnum.SUCCESS);
	}
}
