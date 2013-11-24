package net.einspunktnull.file;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import net.einspunktnull.util.RegEx;

public class FileUtil
{

	/**
	 * @return the declared if exist, otherwise file and directories will be
	 *         created first.
	 */
	public static File getFileReference(String path)
	{
		path = path.replaceAll(RegEx.BACKSLASH, "/");
		String[] pathAsArray = path.split("/");

		if (pathAsArray.length < 1) return null;

		String fileDirectoryName = "";
		if (pathAsArray.length >= 2)
		{
			for (int i = 0; i < pathAsArray.length - 1; i++)
			{
				fileDirectoryName += pathAsArray[i] + "/";
			}

			File fileDirectory = new File(fileDirectoryName);
			if (!fileDirectory.exists())
			{
				if (!fileDirectory.mkdirs()) return null;
			}
		}
		File file = new File(path);
		if (!file.exists())
		{
			System.out.println("file doesn't exist");
		}
		return file;
	}

	public static void copyFile(File src, File dst) throws IOException
	{
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0)
		{
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static void copyDirectory(File srcDir, File dstDir) throws IOException
	{
		if (srcDir.isDirectory())
		{
			if (!dstDir.exists())
			{
				dstDir.mkdir();
			}

			String[] children = srcDir.list();
			for (int i = 0; i < children.length; i++)
			{
				copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
			}
		}
		else
		{
			copyFile(srcDir, dstDir);
		}
	}

	public static boolean deleteDirectory(File path)
	{
		if (path.exists())
		{
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]);
				}
				else
				{
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static void clearDirectory(File path)
	{
		if (path.exists())
		{
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]);
				}
				else
				{
					files[i].delete();
				}
			}
		}
	}

	public static String file2String(String filePath) throws java.io.IOException
	{
		return file2String(new File(filePath));
	}

	public static String file2String(File file) throws java.io.IOException
	{

		byte[] buffer = new byte[(int) file.length()];
		BufferedInputStream f = null;
		try
		{
			f = new BufferedInputStream(new FileInputStream(file.getPath()));
			f.read(buffer);
		}
		finally
		{
			if (f != null) try
			{
				f.close();
			}
			catch (IOException ignored)
			{
			}
		}
		return new String(buffer);
	}

	public static void string2File(String filePath, String string) throws FileNotFoundException, IOException
	{
		string2File(new File(filePath), string);
	}

	public static void string2File(File file, String string) throws FileNotFoundException, IOException
	{
		if (file == null) { throw new IllegalArgumentException("File should not be null."); }
		if (!file.exists())
		{
			file.createNewFile();
		}
		if (!file.isFile()) { throw new IllegalArgumentException("Should not be a directory: " + file); }
		if (!file.canWrite()) { throw new IllegalArgumentException("File cannot be written: " + file); }

		// use buffering
		Writer output = new BufferedWriter(new FileWriter(file));
		try
		{
			// FileWriter always assumes default encoding is OK!
			output.write(string);
		}
		finally
		{
			output.close();
		}
	}

	public static void createDirIfNotExist(String path)
	{
		createDirIfNotExist(path, true);
	}

	public static void createDirIfNotExist(File file)
	{
		createDirIfNotExist(file, true);
	}

	public static void createDirIfNotExist(String path, boolean recursive)
	{
		File file = new File(path);
		createDirIfNotExist(file, recursive);
	}

	public static void createDirIfNotExist(File file, boolean recursive)
	{
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	public static String getPathWithSeparator(File file)
	{
		return getPathWithSeparator(file.getPath());
	}

	public static String getPathWithSeparator(String path)
	{
		return path.endsWith(File.separator) ? path : path + File.separator;
	}

}
