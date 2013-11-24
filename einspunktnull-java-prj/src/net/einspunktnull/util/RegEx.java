package net.einspunktnull.util;

public class RegEx
{
	public static final String BACKSLASH = "\\\\";
	public static final String SLASH_OR_BACKSLASH = "\\\\|/";
	
	
	/* get all htmljavascriptscript-tags
	 * /<\s?script\b(?=[^<>]*\s+(type\s?=\s?"text/javascript"))(?=[^<>]*\s+(src\s?=\s?".+\.js"))[^<>]+>\s*<\s?/script\s?>/gi
	 */
	public static final String HTML_TAG_JAVASCRIPT_EMBED= "<\\s?script\\b(?=[^<>]*\\s+(type\\s?=\\s?\"text/javascript\"))(?=[^<>]*\\s+(src\\s?=\\s?\".+\\.js\"))[^<>]+>\\s*<\\s?/script\\s?>";

	public static final String HTML_TAG_ANCHOR_HREF = "(<\\s*a\\s+.*?href\\=\")([\\w\\d:_\\-\\.\\/\\\\]+)(\"[^<>]*>)(.*?)(<\\s*/\\s*a>)";
	
	public static final String MARKER_BEGIN_END =  "<!--###([A-Z_0-9]+)_BEGIN###(.*?)###(?:[A-Z_0-9]+)_END###-->";
	
	public static final String HTML_TAG_JAVASCRIPT = "<script type=\"text/javascript\"[^<>]*>([^<>]*)</script>";
	
}
