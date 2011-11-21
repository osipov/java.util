package osipov.util;

/**
 * Utilities to streamline common parsing tasks
 * @author @osipov
 */
public class ParseUtil {

	/**
	 * Attempt to parse the input string as an integer and return the specified integer as the alternative if the parse fails.
	 * @param s string to parse
	 * @param i integer to return if the string could not be parsed
	 * @return integer based on parsing of the value in the input string or the specified integer if parse fails 
	 */
	public static final int parseIntOrElse(String s, int i) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return i; 
		}
	}
	/**
	 * Attempt to parse the input string as a long and return the specified long as the alternative if the parse fails.
	 * @param s string to parse
	 * @param i long to return if the string could not be parsed
	 * @return long based on parsing of the value in the input string or the specified long if parse fails 
	 */
	public static final long parseLongOrElse(String s, long l ) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return l;
		}
	}
	
	/**
	 * Attempt to parse the input string as an integer and return the specified integer as the alternative if the parse fails.
	 * @param s string to parse
	 * @param i integer to return if the string could not be parsed
	 * @return integer based on parsing of the value in the input string or the specified integer if parse fails 
	 */
	public static boolean parseBooleanOrElse(String s, boolean b) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Throwable t) {
			return b;
		}
	}

}
