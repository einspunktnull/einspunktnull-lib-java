package net.einspunktnull.cmd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SystemCommandeur
{

	private Process process;
	private DataOutputStream outStream;
	private BufferedReader buffInReader;
	private BufferedReader buffErrorReader;

	public SystemCommandeur(String initialCommand) throws IOException
	{
		// process = Runtime.getRuntime().exec("su");
		process = Runtime.getRuntime().exec(initialCommand);
		outStream = new DataOutputStream(process.getOutputStream());

		InputStream inStream = process.getInputStream();
		InputStreamReader inStreamReader = new InputStreamReader(inStream);
		buffInReader = new BufferedReader(inStreamReader);

		InputStream errorStream = process.getErrorStream();
		InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
		buffErrorReader = new BufferedReader(errorStreamReader);
	}

	public void execute(ArrayList<String> commands)
	{

	}

	public ArrayList<String> execute(String command) throws IOException, InterruptedException
	{
		ArrayList<String> lines = new ArrayList<String>();
		
		outStream.writeBytes(command + "\n");
		outStream.flush();
		outStream.writeBytes("exit\n");
		outStream.flush();
		process.waitFor();

		String line = null;
		while ((line = buffInReader.readLine()) != null)
		{
			lines.add(line);
		}
		
		
		return lines;
	}

}
