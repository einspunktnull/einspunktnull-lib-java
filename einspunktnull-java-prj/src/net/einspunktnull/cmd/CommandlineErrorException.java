package net.einspunktnull.cmd;

public class CommandlineErrorException extends Exception
{

	String mistake;

	public CommandlineErrorException(String error)
	{
		super("CommandlineErrorException: " + error);
		mistake = error;
	}

	public String getError()
	{
		return mistake;
	}

}
