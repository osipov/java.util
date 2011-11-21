package osipov.util;

public class StringUtil {
    public static final int countOccurencesOf(char ch, String s, int fromIndex, int toIndex) {
        int count = 0;
        while ( (fromIndex = s.indexOf(ch, fromIndex)) > -1 && fromIndex < toIndex) {
            count++;
            fromIndex++;
        }
        return count;
    }
    
    /**
     * Returns the string index of the Nth occurence of a character within the indicies of the specified string
     * provided or a -1 when the Nth occurence does not exist within the indicies.
     * @param ch a character
     * @param s a string
     * @param fromIndex starting index, inclusive
     * @param toIndex ending index, exclusive
     * @param nth index of the occurence of the specified character, any index below 1 is ignored
     * @return the string index of the Nth occurence of the specified character in a string
     */
    public static final int indexOfOccurence(char ch, String s, int fromIndex, int toIndex, int nth) {
        int i = 0;
        for(; i < nth && fromIndex > -1 && fromIndex < toIndex; i++, fromIndex++)
            fromIndex = s.indexOf(ch, fromIndex);
        return fromIndex < toIndex ? fromIndex : -1;
    }
	
	public static String escape(String buff, String[] from, String[] to)
	{
		for(int i=0;i<from.length;i++)
			buff = replace(buff,from[i],to[i]);
		return(buff);
	}
	public static boolean isLetter(char[] c) {
		for (int i = 0; i < c.length; i++) 
			if (!Character.isLetter(c[i])) return false;
		return true;
	}
	
	public static boolean isLetter(String s) {
		for (int i = 0; i < s.length(); i++) 
			if (!Character.isLetter(s.charAt(i))) return false;
		return true;			
	}
	public static String removeWhitespaceCharacters(String s) {
		StringBuffer buf = new StringBuffer(s);
		for (int i = 0; i < buf.length(); i++) {
			if (Character.isWhitespace(buf.charAt(i)))
				buf.deleteCharAt(i);
		}
		return buf.toString();
	}
	
	/** Replaces s1 with s2 in the string buf
	 * 
	 * @return A new string with the replacement made
	 * @param buf The string to operate on
	 * @param s1 The string to replace with string s2
	 * @param s2 The string to replace s1 with
	 */
	public static String replace(String buf, String s1, String s2) {
		int offset1;
		int offset2;
		StringBuffer result = new StringBuffer("");
		if (buf == null || s1 == null || s1.length() == 0 || s2 == null)
			return (buf);

		for (offset1 = 0;(offset2 = buf.indexOf(s1, offset1)) >= 0; offset1 = offset2 + s1.length())
			result.append(buf.substring(offset1, offset2) + s2);
		if (offset2 < 0)
			result.append(buf.substring(offset1, buf.length()));
		return (result.toString());
	}

	/**
	 * Returns the contents of the string contained to the left
	 * of the first occurence of the delimiter. If either of 
	 * the input arguments is null the result is null. If 
	 * the source string doesn't contain the delimeter, the
	 * result is the input string
	 * 
	 * @param str source string
	 * @param seg delimeter
	 * @return
	 */
	public static String getLeftOf(String str, String seg) {
		return getLeftOf(str, seg, str);
	}
	
	/**
	 * Returns the contents of the string contained to the left
	 * of the first occurence of the delimiter. If either of 
	 * the input arguments is null the result is null. If 
	 * the source string doesn't contain the delimeter, the
	 * result is the value of the ifNoSeg variable
	 * @param str source string
	 * @param seg delimeter
	 * @param ifNoSeg return code in the case when the delimeter
	 * hasn't been found in the string
	 * @return
	 */
	public static String getLeftOf(String str, String seg, String ifNoSeg) {
		if (str == null || seg == null) return null;
		if (str.indexOf(seg) == -1) return ifNoSeg;
		return str.substring(0, str.indexOf(seg));
	}

	/**
	 * returns the contents of the string contained to the left
	 * of the nth occurence of the delimiter where n is greater than zero and
	 * is specified by the count parameter. If either of the input arguments is null
	 * the result is null. If the source string doesn't contain the delimeter
	 * or if there are less then index delimeters, the result is the value
	 * of the source string.
	 * @param str source string
	 * @param seg delimeter
	 * @param count number of the delimiters counted from the right before 
	 * returning the substring
	 * @param ifNoSeg return code in the case when the delimeter
	 * hasn't been found in the string
	 * @return
	 */
	public static String getLeftOf(String str, String seg, int count) {
		return getLeftOf(str, seg, count, str);
	}
	/**
	 * returns the contents of the string contained to the left
	 * of the nth occurence of the delimiter where n is greater than zero and
	 * is specified by the count parameter. If either of the input arguments is null
	 * the result is null. If the source string doesn't contain the delimeter
	 * or if there are less then index delimeters, the result is the value
	 * of the ifNoSeg variable.
	 * @param str source string
	 * @param seg delimeter
	 * @param count number of the delimiters counted from the right before 
	 * returning the substring
	 * @param ifNoSeg return code in the case when the delimeter
	 * hasn't been found in the string
	 * @return
	 */
	public static String getLeftOf(String str, String seg, int count, String ifNoSeg) {
		if (str == null || seg == null) return null;
		int lastIndex = str.length();
		for (int i = 0; i < count && lastIndex > 0; i++)
			lastIndex = str.lastIndexOf(seg, lastIndex - 1);
		if (lastIndex < 0)
			return ifNoSeg;
		else
			return str.substring(0, lastIndex);
	}
	
	/**
	 * Returns the contents of the string contained to the right
	 * of the last occurence of the delimiter searching from the end of the string. 
	 * If either of 
	 * the input arguments is null the result is null. If 
	 * the source string doesn't contain the delimeter, the
	 * result is an empty string
	 * @param str source string
	 * @param seg delimeter
	 * @return
	 */
	public static String getRightOf(String str, String seg) {
		return getRightOf(str, seg, "");
	}
	
	/**
	 * Returns the contents of the string contained to the right
	 * of the last occurence of the delimiter searching from the end
	 * of the string. If either of the input arguments is null the 
	 * result is null. If the source string doesn't contain the delimeter, the
	 * result is the value of the ifNoSeg parameter
	 * @param str source string
	 * @param seg delimeter
	 * @param ifNoSeg return value if the seg is not found in the string
	 * @return
	 */
	public static String getRightOf(String str, String seg, String ifNoSeg) {
		if (str == null || seg == null) return null;
		if (str.lastIndexOf(seg) == -1) return ifNoSeg;
		return str.substring(str.lastIndexOf(seg) + seg.length(), str.length());
	}
	
	/**
	 * Returns the number of occurrences of a a string segment
	 * in a given source string.  If either of the parameters
	 * is null the result is null.
	 * @param str
	 * @param seg
	 * @return
	 */
	public static int countOccurrencesOf(String str, String seg) {
		return countOccurrencesOf(str, seg, 0);
	}
	
	/**
	 * Returns the number of occurrences of a a string segment
	 * in a given source string.  If either of the parameters
	 * is null the result is null.
	 * @param str
	 * @param seg
	 * @return
	 */
	public static int countOccurrencesOf(String str, String seg, int fromIndex) {
		if (str == null || seg == null) return -1;
		int count = 0;
		int i = str.indexOf(seg, fromIndex);
		while (i > -1) {
			count++;
			i += seg.length();
			i = str.indexOf(seg, i);
		}
		return count;
	}
	
	
	public static int indexOfOccurrence(String str, String seg, int occurrence) {
		int index = -1;
		for (int i = 0; i < occurrence; i++)
			index = str.indexOf(seg, index) + seg.length();
		return index;
			 
	}
	
	/**
	 * In the given String, removes any characters that do not pass
	 * Character.isLetterOrDigit test
	 * @param s original string
	 * @return the original string without any non alpha number characters 
	 */
	public static String removeNonAlphaNumericCharacters(String s) {
		StringBuffer buf = new StringBuffer();
		for(int i = 0, j = 0; j < s.length(); j++) {
			while(j < s.length() && Character.isLetterOrDigit(s.charAt(j))) j++;
			buf.append(s.substring(i, j));	

			while(j < s.length() && !Character.isLetterOrDigit(s.charAt(j))) j++;
			i = j;
		}
		return buf.toString();
	}
}
