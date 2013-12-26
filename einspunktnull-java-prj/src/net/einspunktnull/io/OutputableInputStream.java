package net.einspunktnull.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class OutputableInputStream extends InputStream
{

	private PipedOutputStream pipedOutputStream;
	private PipedInputStream pipedInputStream;

	public OutputableInputStream() throws IOException
	{
		pipedOutputStream = new PipedOutputStream();
		pipedInputStream = new PipedInputStream(pipedOutputStream);
	}

	@Override
	public int read() throws IOException
	{
		return pipedInputStream.read();
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		return pipedInputStream.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return pipedInputStream.read(b, off, len);
	}

	@Override
	public int available() throws IOException
	{
		return pipedInputStream.available();
	}

	public void write(char c) throws IOException
	{
		pipedOutputStream.write(c);
	}

	public void write(int c) throws IOException
	{
		pipedOutputStream.write(c);
	}

	public void write(byte b) throws IOException
	{
		pipedOutputStream.write(b);
	}

	public void write(byte[] b) throws IOException
	{
		pipedOutputStream.write(b);
	}

}
