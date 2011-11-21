package osipov.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Math utilities, see function javadocs for details 
 * 
 * @author @osipov
 *
 */
public class MathUtil {
	public static final BigInteger ZERO = new BigInteger("0", 10);
	public static final BigInteger ONE = new BigInteger("1", 10);
	public static final BigInteger TWO = new BigInteger("2", 10);	
	private static final Random rnd = new Random(System.currentTimeMillis());
	
	public static BigInteger nextBigInteger(BigInteger n) {
		return randomBigInteger(rnd, n);
	}
	
	/**
	 * Computes a random BigInteger that is less than the upper bound BigInteger provided to the method.
	 * Since BigInteger doesn't provide a way to generate a random BigInteger in a given range,
	 * the method tries to generate a random BigInteger with the same number of bits as the BigInteger provided as an argument. 
	 * The method samples these random BigIntegers continuously until one is found that is less than the upper bound BigInteger.
	 * @param rnd Random number generator
	 * @param n Upper bound BigInteger
	 * @return BigInteger that is less than the upper bound BigInteger
	 */
	private static BigInteger randomBigInteger(Random rnd, BigInteger n) {
		BigInteger i = null;
		do {
			i = new BigInteger(n.bitLength(), rnd);
		} while (i.compareTo(n) != -1);
		return i;
	}
	
	/**
	 * Built in test case for the binomial coefficient function.
	 */
	private static void testBinomialCoefficient() {
		if (!binomialCoefficient(new BigInteger("64", 10), new BigInteger("32", 10)).equals(new BigInteger("1832624140942590534", 10)))
			throw new IllegalStateException();
	}
	
	/**
	 * Computes the binomial coefficient (number of unique ways to choose k elements from a set of n elements) over BigIntegers.
	 * @param n number of elements in the set
	 * @param k number of elements to choose from the set
	 * @return number of ways to choose k elements from n elements in the set
	 */
	public static BigInteger binomialCoefficient(BigInteger n, BigInteger k) {
		BigInteger result = ONE;
	    if (k.compareTo(n) > 0) return ZERO;
	    for (BigInteger i = ONE; i.compareTo(k) < 1; i = i.add(ONE)) {
	      result = result.multiply(n); //result *= n--;
	      n = n.subtract(ONE);
	      result = result.divide(i); //result /= i;
	    }
	    return result;
	}

	/**
	 * Computes the binomial coefficient (number of unique ways to choose k elements from a set of n elements).
	 * @param n number of elements in the set
	 * @param k number of elements to choose from the set
	 * @return number of ways to choose k elements from n elements in the set
	 */
	public static long binomialCoefficient(long n, long k) {
	    long result = 1;
	    if (k > n) return 0;
	    for (long i = 1; i <= k; i++) {
	      result *= n--;
	      result /= i;
	    }
	    return result;
	}

	/**
	 * Computes the binomial coefficient (number of unique ways to choose k elements from a set of n elements) by converting arguments from type long to type BigInteger. 
	 * @param n number of elements in the set
	 * @param k number of elements to choose from the set
	 * @return number of ways to choose k elements from n elements in the set
	 */
	public static BigInteger binomialCoefficientAsBigInteger(long n, long k) {
		return binomialCoefficient(new BigInteger(Long.toString(n), 10), new BigInteger(Long.toString(k), 10));
	}
	
	
	/**
	 * Represents an array of an array of ints (an int matrix) as a matrix style formatted string
	 * @param a array of an array of ints to be formatted
	 * @return a String representing the input array as a matrix
	 */
	public static String toString(int[][] a) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				buf.append(a[i][j]); buf.append(" ");
			}
			buf.replace(buf.length() - 1, buf.length(), "\n");
		}
		return buf.substring(0, buf.length() - 1);
	}
	
	/**
	 * Performs a deep copy of an array of an array of ints
	 * @param s
	 * @return
	 */
	public static int[][] clone(int[][] s) {
		int[][] d = new int[s.length][0];
		for (int i = 0; i < s.length; i++) {
			d[i] = new int[s[i].length];
			for (int j = 0; j < s[i].length; j++)
				d[i][j] = s[i][j];
		}
		return d;
	}

	/**
	 * Randomly chooses from a uniform distribution of integers in the range [i, j)
	 * @param random random number generator
	 * @param i beginning of the range (inclusive)
	 * @param j ending of the range (exclusive)
	 * @return random integer between i and j
	 */
	public static final int sampleUniformDistribution(Random random, int i, int j) {
		if (i >= j) 
			throw new IllegalArgumentException();
		else
			return i + random.nextInt(j - i);
	}
	
	
	private static void testGetNthBitstring() {
		byte[] ba = null;
		ba = getBitSetByIndex(1, 1, 0);
		if (!Arrays.equals(ba, new byte[]{1})) throw new IllegalStateException();
		System.out.println("(1,1,0)=" + Arrays.toString(ba));

		ba = getBitSetByIndex(2, 1, 0);
		if (!Arrays.equals(ba, new byte[]{0, 1})) throw new IllegalStateException();
		System.out.println("(2,1,0)=" + Arrays.toString(ba));	

		ba = getBitSetByIndex(2, 1, 1);
		if (!Arrays.equals(ba, new byte[]{1, 0})) throw new IllegalStateException();
		System.out.println("(2,1,1)=" + Arrays.toString(ba));	
		
		ba = getBitSetByIndex(4, 2, 5);
		if (!Arrays.equals(ba, new byte[]{1, 1, 0, 0})) throw new IllegalStateException();
		System.out.println("(4,2,5)=" + Arrays.toString(ba));	
	}
	
	/**
	 * Consider all bit strings of specified length that have a specified number of 1 bits. 
	 * The exact number of such bit strings can be found using the binomial coefficient. 
	 * The function chooses a unique and consistent ordering of the bit strings and returns
	 * one of such bit strings based on the index specified by the caller. This is useful
	 * when randomly choosing one of the many possible bit strings with a pre-specified number of 1s.
	 * For example, consider all bit strings of length 3 have one 1: 
	 * 001
	 * 010
	 * 100
	 * The calling the function with parameters (3, 1, 2) will return the 2nd bit string (i.e. 010). 
	 * @param length number of bits to be in the returned bit string 
	 * @param numOnes number of 1 bits in the returned bit string
	 * @param index for the bit string to return, in the range from [0, C(length, numOnes)) where
	 * C(length, numOnes) is the binomial coefficient, length choose numOnes
	 * @return
	 */
	public static byte[] getBitSetByIndex(int length, int numOnes, int n) {
		return getBitSetByIndex(length, numOnes, new BigInteger(Integer.toString(n), 10));
	}
	/**
	 * Consider all bit strings of specified length that have a specified number of 1 bits. 
	 * The exact number of such bit strings can be found using the binomial coefficient. 
	 * The function chooses a unique and consistent ordering of the bit strings and returns
	 * one of such bit strings based on the index specified by the caller. This is useful
	 * when randomly choosing one of the many possible bit strings with a pre-specified number of 1s.
	 * For example, consider all bit strings of length 3 have one 1: 
	 * 001
	 * 010
	 * 100
	 * The calling the function with parameters (3, 1, 2) will return the 2nd bit string (i.e. 010). 
	 * @param length number of bits to be in the returned bit string 
	 * @param numOnes number of 1 bits in the returned bit string
	 * @param index for the bit string to return, in the range from [0, C(length, numOnes)) where
	 * C(length, numOnes) is the binomial coefficient, length choose numOnes
	 * @return
	 */
	public static byte[] getBitSetByIndex(int length, int numOnes, BigInteger n) {
		return getBitSetByIndex(length, numOnes, n, true);
	}
	/**
	 * Consider all bit strings of specified length that have a specified number of 1 bits. 
	 * The exact number of such bit strings can be found using the binomial coefficient. 
	 * The function chooses a unique and consistent ordering of the bit strings and returns
	 * one of such bit strings based on the index specified by the caller. This is useful
	 * when randomly choosing one of the many possible bit strings with a pre-specified number of 1s.
	 * For example, consider all bit strings of length 3 have one 1: 
	 * 001
	 * 010
	 * 100
	 * The calling the function with parameters (3, 1, 2) will return the 2nd bit string (i.e. 010). 
	 * @param length number of bits to be in the returned bit string 
	 * @param numOnes number of 1 bits in the returned bit string
	 * @param index for the bit string to return, in the range from [0, C(length, numOnes)) or [1, C(length, numOnes)] 
	 * depending on the value of the boolean parameter and where C(length, numOnes) is the binomial coefficient, length choose numOnes
	 * @param controls whether the index parameter starts with 0 or 1
	 * @return
	 */
	public static byte[] getBitSetByIndex(int length, int numOnes, BigInteger n, boolean zeroIndexed) {
		byte[] ba = new byte[length];
		return getNthBitstring(ba, length, numOnes, zeroIndexed ? n.add(ONE) : n);
	}
	
	/**
	 * Recursively constructs a bit string with a pre-specified number of 1 bits
	 * based on a unique and consistent ordering of the space of such possible strings.
	 * @param ba storage space for the bit string
	 * @param i length of the bit string
	 * @param j number of 1 bits in the string
	 * @param n a number in the range of [1, C(i, j)] specifying which of the C(i,j) bit strings to return 
	 * and where C(i, j) is the binomial coefficient, i choose j
	 * @return
	 */
	private static byte[] getNthBitstring(byte[]ba, int i, int j, BigInteger n) {
		if (i > Integer.MAX_VALUE || j > Integer.MAX_VALUE) throw new IllegalArgumentException();
		BigInteger l = binomialCoefficientAsBigInteger(i, j);
		if (n.compareTo(l) == 1) throw new ArrayIndexOutOfBoundsException();
		if (j == 0) 
			return ba;
		else
		if (i == j) {
			for (int z = 0; z < i; z++)
				ba[z] = 1;
			return ba;
		} else 
		if (i > j) {
			BigInteger k = binomialCoefficientAsBigInteger(i - 1, j - 1);
			if (n.compareTo(k) != 1) {
				ba[(int)i - 1] = 1;
				return getNthBitstring(ba, i - 1, j - 1, n);
			} else {
				ba[(int)i - 1] = 0;
				return getNthBitstring(ba, i - 1, j, n.subtract(k));
			}
		} else
			throw new ArrayIndexOutOfBoundsException();
	}
	
	public static void main(String[] args) {
		//testBinomialCoefficient();
		//testGetNthBitstring();
	}
}
