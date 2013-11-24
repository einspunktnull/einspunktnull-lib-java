package net.einspunktnull.file;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearch
{

	private Matcher _matcher;
	private Pattern _pattern;
	private boolean _includeDirectories;
	private ArrayList<File> _matches = new ArrayList<File>();
	private ArrayList<File> _clashes = new ArrayList<File>();

	public void search(File file, String regex, boolean inclDirectories)
	{
		_includeDirectories = inclDirectories;
		_pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		searchRecursive(file);
	}

	public static ArrayList<File> getAllFiles(File file)
	{
		ArrayList<File> list = new ArrayList<File>();

		getFilesRecursive(file, list);

		return list;
	}

	private static void getFilesRecursive(File file, ArrayList<File> list)
	{
		list.add(file);
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			
			for (File file2 : files)
			{
				getFilesRecursive(file2, list);
			}
		}
	}

	private void searchRecursive(File file)
	{
		if (file.isDirectory())
		{
			if (_includeDirectories)
			{
				applyRegex(file);
			}

			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				searchRecursive(files[i]);
			}
		}
		else if (file.isFile())
		{
			applyRegex(file);
		}
	}

	private void applyRegex(File file)
	{
		_matcher = _pattern.matcher(file.getName());
		if (_matcher.matches()) _matches.add(file);
		else _clashes.add(file);
	}

	public ArrayList<File> getMatches()
	{
		return _matches;
	}

	public ArrayList<File> getClashes()
	{
		return _clashes;
	}
}
