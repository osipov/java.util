package osipov.util;

import static osipov.util.TestUtil.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author @osipov
 */
public class WordUtil {
	public static final String[] EOA = new String[]{};
	
	public static final String EMPTY = "";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	
	
	private static void testGetWord() throws Throwable {
		assertTrue(getWord("a", 0, ",", null).equals("a"), new RuntimeException(new WordUtil().getClass().getName() + ".getWord()"));
		assertTrue(getWord("a", 1, ",", null) == null,new RuntimeException(new WordUtil().getClass().getName() + ".getWord()"));
		assertTrue(getWord("a,b", 0, ",", null).equals("a"),new RuntimeException(new WordUtil().getClass().getName() + ".getWord()"));
		assertTrue(getWord("a,b", 1, ",", null).equals("b"),new RuntimeException(new WordUtil().getClass().getName() + ".getWord()"));
		assertTrue(getWord("a,b", 2, ",", null) == null,new RuntimeException(new WordUtil().getClass().getName() + ".getWord()"));
	}
	
	/**
	 * Calls @see #getWord(String, int, String, String) using
	 * a value of space for the word delimeter and a null for
	 * the value of the ifNoWord variable 
	 * @param s the string containing a word
	 * @param pos zero indexed position of the word in a string
	 * @return
	 */
	public static String getWord(String s, int pos) {
		return getWord(s, pos, SPACE, null);
	}

	/**
	 * Returns a word in a string 
	 * @param s the string containing a word
	 * @param pos zero indexed position of the word in a string
	 * @param sep word separator to use in the string, 
	 * typically a space " "
	 * @param ifNoWord string that should be returned 
	 * if the word does not exist in this string
	 * @return
	 */
	public static String getWord(final String s, final int pos, final String sep, final String ifNoWord) {
		int i = 0, j = 0, k = 0;
		while((j = s.indexOf(sep, j)) != -1 && i < pos) {
			k = ++j; i++;
		}
		if (j < 0)
			if (pos > i)
				return ifNoWord;
			else
				return s.substring(k, s.length());
		else
			return s.substring(k, j);
	}
    

	/**
	 * Splits a string into space separated words and
	 * adds each word to the collection provided by the user
	 * using Collection.add() method
	 * 
	 * @param s the string to be split into space separated values 
	 * @param delim the delimeter string that should be used when splitting the string into words
	 * @param c the collection that should hold the result
	 * @return
	 */
	public static Collection split(final Collection c, final String s) {
//		int i = 0, j = 0;
//		while ((j = s.indexOf(SPACE, i)) != -1) {
//			c.add(s.substring(i, j));
//			i = j + 1;
//		}
//		c.add(s.substring(i, s.length()));
//		return c;
        return split(c, s, SPACE);
	}

    /**
     * Splits a string into delimeter separated words and
     * adds each word to the collection provided by the user
     * using Collection.add() method
     * 
     * @param s the string to be split into space separated values 
     * @param delim the delimeter string that should be used when splitting the string into words
     * @param c the collection that should hold the result
     * @return
     */
    public static Collection split(final Collection c, final String s, final String delim) {
        return split(c, s, delim, Integer.MAX_VALUE);
    }

    
    /**
     * Splits a string into delimeter separated words and
     * adds each word to the collection provided by the user
     * using Collection.add() method
     * 
     * @param s the string to be split into space separated values 
     * @param delim the delimeter string that should be used when splitting the string into words
     * @param c the collection that should hold the result
     * @return
     */
    public static Collection split(final Collection c, final String s, final int nth) {
        return split(c, s, SPACE, nth);
    }
    
    /**
	 * Splits a string into delimeter separated words and
	 * adds each word to the collection provided by the user
	 * using Collection.add() method
	 * 
	 * @param s the string to be split into space separated values 
	 * @param delim the delimeter string that should be used when splitting the string into words
	 * @param c the collection that should hold the result
     * @param nth indicates that the string should be split up to and including the nth occurence of the delimiter, starting from 1
	 * @return
	 */
	public static Collection split(final Collection c, final String s, final String delim, final int nth) {
        c.clear();
		final int length = delim.length();
		int i = 0, j = 0, k = 0;
		while ((j = s.indexOf(delim, i)) != -1 && k < nth) {
			c.add(s.substring(i, j));
			i = j + length; k++;
		}
		c.add(s.substring(i, s.length()));
		return c;
	}
    

    
	private static void testSplit() throws SecurityException, RuntimeException, NoSuchMethodException {
		//assertTrue(Arrays.deepEquals(new String[]{"a", "b", "c"}, split("a,b,c", ",", new String[3])), new RuntimeException(new WordUtil().getClass().getMethod("split", new Class[]{String.class, String.class, String[].class}).toString()));
	}
	
	public static String[] split(final String[] array, final String s) {
		return split(array, s, SPACE, Integer.MAX_VALUE, EOA);
	}
    public static String[] split(final String[] array, final String s, final int nth) {
        return split(array, s, SPACE, nth, EOA);
    }
    public static String[] split(final String[] array, final String s, final String[] ifNull) {
		return split(array, s, SPACE, Integer.MAX_VALUE, ifNull);
	}
    public static String[] split(final String[] array, final String s, final String delim, final int nth) {
        return split(array, s, delim, nth, EOA);
    }
    
    /**
     * splits the specified string based on the specific delimeter up until the zero-indexed Nth instance of the delimited string
     * and places the results sequentially into the provided string array. if the specific string is null, the ifNull value is returned
     * @param array the array that will store the results splitting the string
     * @param s the string that should be broken up based on the delimeter
     * @param delim the delimeter to use for breaking up the string
     * @param nth zero-indexed position of the last delimited string element that should be included in the result
     * @param ifNull the value to return if the input array or the specific string is null
     * @return
     */
	public static String[] split(final String[] array, final String s, final String delim, final int nth, final String[] ifNull) {
		if (s == null || array == null) return ifNull;
		final int length = delim.length();
		int i = 0, j = 0, k = 0, l = 0;
		while ((j = s.indexOf(delim, i)) != -1 && l < nth) {
			array[k] = s.substring(i, j); k++; l++;
			i = j + length;
		}
		array[k] = s.substring(i, s.length());
		return array;
	}
	
//	public static Collection getTrigrams(final Collection c, String s, String delim) {
//		return getNgrams(c, s, delim, 3);
//	}
	public static String[] ngrams(String s) {
		ArrayList ngrams = (ArrayList)ngrams(new ArrayList(), s);
		return (String[])ngrams.toArray(new String[ngrams.size()]);
	}

	public static Collection ngrams(final Collection c, String s) {
		return ngrams(c, s, SPACE, 3);
	}

	public static Collection ngrams(final Collection c, String s, int n) {
		return ngrams(c, s, SPACE, n);
	}
	
	public static Collection ngrams(final Collection c, String s, String delim, int n) {
		StringBuffer ngram = new StringBuffer();
		List words = (List)split(new ArrayList(), s, delim, Integer.MAX_VALUE);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < words.size() - i; j++) {
				int k = 0;
				for (; k < i; k++) {
					ngram.append(words.get(j + k)); ngram.append(SPACE);
				}
				ngram.append(words.get(j + k));
				c.add(ngram.toString().intern());
				ngram.setLength(0);
			}
		}
		return c;
	}
	
	public static Collection ngrams2(final Collection c, String s, int n) {
		return ngrams2(c, s, SPACE, n);
	}
	
	public static Collection ngrams2(final Collection c, String s, String delim, int n) {
		StringBuffer ngram = new StringBuffer();
		for (int i = 0; i < n; i++) {
			String word = null;
			for (int j = 0; (word = getWord(s, j, SPACE, null)) != null; j++) {
			empty:{				
				ngram.append(word.intern());
				for(int k = j + 1; k < j + 1 + i; k++) {
					word = getWord(s, k, SPACE, null);
					if (word == null)  
						{break empty;} 
					else
						ngram.append(SPACE); ngram.append(word.intern());
				}
				c.add(ngram.toString().intern());
			}
				ngram.setLength(0);
			}
		}
		return c;
	}
    
	public static void main(String[] args) throws Throwable {
	}
	
	public static String join(final Collection c) {
		return join(c, SPACE);
	}
	public static String join(final Collection c, final String delim) {
		StringBuffer buf = new StringBuffer();
		for(Iterator i = c.iterator(); i.hasNext(); ) 
			buf.append(i.next() + delim);
		if (buf.length() < 1)
			return EMPTY;
		else 
			return buf.substring(0, buf.length() - 1);
	}
	
	/**
	 * @param s
	 * @param delim
	 * @return
	 */
	public static String join(Object[] s, final String delim) {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (; i < s.length - 1; i++)
			buf.append(s[i] + delim);
		return buf.append(s[i]).toString();
	}
	
	public static String join(String[] s) {
		return join(s, SPACE);
	}
	
//	public static String[] alias(String[] s, IndexedHashSet set) {
//		
//	}
}
