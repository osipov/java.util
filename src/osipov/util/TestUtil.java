package osipov.util;

public class TestUtil {
	public static void assertTrue(boolean condition, Throwable t) throws Throwable {
		if (!condition) throw t;
	}
}
