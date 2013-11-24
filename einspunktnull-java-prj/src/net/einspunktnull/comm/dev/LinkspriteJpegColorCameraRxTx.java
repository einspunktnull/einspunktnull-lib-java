package net.einspunktnull.comm.dev;

import gnu.io.SerialPortEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.einspunktnull.io.Sysout;

public class LinkspriteJpegColorCameraRxTx extends RxTxSerialCommDevice
{

	public static final int IMAGE_SIZE_160x120 = 2;
	public static final int IMAGE_SIZE_320x240 = 1;
	public static final int IMAGE_SIZE_640x480 = 0;

	private final byte[] CMD_RESET_CAMERA =
	{ 0x56, 0x00, 0x26, 0x00 };
	private final byte[] CMD_TAKE_PICTURE =
	{ 0x56, 0x00, 0x36, 0x01, 0x00 };
	private final byte[] CMD_GET_SIZE =
	{ 0x56, 0x00, 0x34, 0x01, 0x00 };
	private final byte[] CMD_STOP_TAKING_PICS =
	{ 0x56, 0x00, 0x36, 0x01, 0x03 };
	private final byte[] CMD_READ_JPEG_FILE_CONTENT =
	{ 0x56, 0x00, 0x32, 0x0C, 0x00, 0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	private final byte[][] CMD_IMAGE_SIZE =
	{
	{ 0x56, 0x00, 0x54, 0x01, 0x00 },
	{ 0x56, 0x00, 0x54, 0x01, 0x11 },
	{ 0x56, 0x00, 0x54, 0x01, 0x22 } };

	public LinkspriteJpegColorCameraRxTx(String name, ICommListener listener, int baudrate, String portname, int timeout)
	{
		super(name, listener, baudrate, portname, timeout);
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent)
	{
		// override for prevent default handling
	}

	public void reset() throws CommDeviceException
	{
		Sysout.println("LinkspriteJpegColorCameraRxTx.reset()");
		String response = sendCommand(CMD_RESET_CAMERA, 4);
		printAsHex(response);
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public byte[] getPicture(int picSize) throws CommDeviceException
	{

		Sysout.println("LinkspriteJpegColorCameraRxTx.getPicture()");

		setImageSize(picSize);

		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		takePicture();

		int size = getSize();
		Sysout.println(size);

		byte[] bytes = readJpegFileContent(size);

		return bytes;
	}

	private void setImageSize(int picSize) throws CommDeviceException
	{
		Sysout.println("LinkspriteJpegColorCameraRxTx.setImageSize()", picSize);
		sendCommand(CMD_IMAGE_SIZE[picSize], 5);
	}

	private byte[] readJpegFileContent(int size) throws CommDeviceException
	{
		int count = 0;
		int address = 0;
		boolean eof = false;
		// StringBuilder fullSb = new StringBuilder();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] response = new byte[32];

		while (address < size)
		{

			count = readData(response, address);

			// Sysout.println("count:", count);

			for (int i = 0; i < count; i++)
			{

				byte byteAtI = response[i];
				if (i > 0)
				{
					byte byteAtI2 = response[i - 1];
					byte idx = (byte) ((int) 0xD9);
					byte idx2 = (byte) ((int) 0xFF);
					if ((byteAtI == idx) && (byteAtI2 == idx2)) eof = true;

				}

				baos.write(byteAtI);
				// fullSb.append((char) response[i]);
				if (eof)
				{
					break;
				}
			}
			address += count;

			float percent = (float) address / (float) size * 100;

			if (eof) break;
		}
		stopPictures();

		return baos.toByteArray();

	}

	private void stopPictures() throws CommDeviceException
	{
		sendCommand(CMD_STOP_TAKING_PICS, 5);
	}

	private int readData(byte[] response, int address) throws CommDeviceException
	{

		int count = 0;
		int readSize = 32;
		clearInput();

		CMD_READ_JPEG_FILE_CONTENT[8] = (byte) (address >> 8);
		CMD_READ_JPEG_FILE_CONTENT[9] = (byte) address;
		CMD_READ_JPEG_FILE_CONTENT[10] = (byte) 0x00;
		CMD_READ_JPEG_FILE_CONTENT[11] = (byte) 0x00;
		CMD_READ_JPEG_FILE_CONTENT[12] = (byte) (readSize >> 8);
		CMD_READ_JPEG_FILE_CONTENT[13] = (byte) readSize;
		CMD_READ_JPEG_FILE_CONTENT[14] = (byte) 0x00;
		CMD_READ_JPEG_FILE_CONTENT[15] = (byte) 0x0A;

		// Sysout.println("address", address);

		// printAsHex(CMD_READ_JPEG_FILE_CONTENT);

		// Kein plan warum, aber delay muss sein, da sonst zu schnell, was zu
		// unvollständigen daten führt.
		try
		{
			Thread.sleep(5);

			outputStream.write(CMD_READ_JPEG_FILE_CONTENT);

			// Read the response header.
			for (int i = 0; i < 5; i++)
			{
				while (inputStream.available() < 1)
					;
				inputStream.read();
			}

			// Now read the actual data and add it to the response string.
			count = 0;
			while (count < readSize)
			{
				while (inputStream.available() < 1)
					;
				int bite = inputStream.read();

				response[count] = (byte) bite;

				count++;
			}
		}
		catch (InterruptedException e)
		{
			throw new CommDeviceException("InterruptedException", e);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

		return count;
	}

	private int getSize() throws CommDeviceException
	{
		Sysout.println("LinkspriteJpegColorCameraRxTx.getSize()");
		String response = sendCommand(CMD_GET_SIZE, 9);
		printAsHex(response);

		int size = 0;
		int len = response.length();
		byte[] bytes = response.getBytes();

		int pimmel = (int) (bytes[len - 2] & 0xFF);
		int plus = pimmel * 256;
		Sysout.println("plus", pimmel);
		size += plus;
		size += (int) bytes[len - 1] & 0x00FF;
		return size;
	}

	private String takePicture() throws CommDeviceException
	{
		Sysout.println("LinkspriteJpegColorCameraRxTx.takePicture");
		String response = sendCommand(CMD_TAKE_PICTURE, 5);
		printAsHex(response);
		return response;
	}

	private void printAsHex(String response)
	{
		byte[] bytes = response.getBytes();
		printAsHex(bytes);
	}

	private void printAsHex(byte[] bytes)
	{
		int len = bytes.length;
		for (int i = 0; i < len; i++)
		{
			byte bite = bytes[i];
			String hex = String.format("%02X", bite);
			if (i > 0) Sysout.print(" ");
			Sysout.print(hex);
		}
		Sysout.println("");
	}

	private void clearInput() throws CommDeviceException
	{

		try
		{
			while (inputStream.available() > 0)
				inputStream.read();
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
	}

	private String sendCommand(byte[] command, int responseLength) throws CommDeviceException
	{
		StringBuilder sb = new StringBuilder();
		clearInput();
		try
		{
			outputStream.write(command);
			for (int i = 0; i < responseLength; i++)
			{
				while (inputStream.available() < 1)
					;
				sb.append((char) inputStream.read());
			}
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
		return sb.toString();
	}

}
