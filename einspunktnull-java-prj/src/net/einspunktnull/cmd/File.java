package net.einspunktnull.cmd;

import java.io.IOException;
import java.util.ArrayList;

public class File
{

	private String path;

	public File(String path)
	{
		this.path = path;
	}

	public ArrayList<File> listFiles() throws IOException, InterruptedException
	{
		ArrayList<File> filez = new ArrayList<File>();
		SystemCommandeur commander = new SystemCommandeur("su");
		ArrayList<String> results = commander.execute("ls " + path);

		for (String line : results)
		{
			filez.add(new File(path + "/" + line));
		}
		return filez;

	}

	public String getFileName()
	{
		String[] ret = path.split("/");
		return ret[ret.length - 1];
	}

	public String getPath()
	{
		return path;
	}

	public String getStringContent()
	{
		return path;
	}

	public String getContent() throws IOException, InterruptedException
	{
		String cmd = "cat " + path;
		SystemCommandeur commander = new SystemCommandeur("su");
		ArrayList<String> fileCont = commander.execute(cmd);
		String content = "";
		for (String line : fileCont)
		{
			content += line;
		}
		return content;
	}
}
