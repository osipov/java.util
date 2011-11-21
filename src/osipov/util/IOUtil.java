package osipov.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class IOUtil {
	public static final int CACHE_SIZE = 8192;
	
	public static boolean write(Reader reader, Writer writer) {
		return write(reader, writer, new char[CACHE_SIZE], true);
	}

	public static boolean write(Reader reader, Writer writer, boolean close) {
		return write(reader, writer, new char[CACHE_SIZE], close);
	}
	
	public static boolean write(Reader reader, Writer writer, char[] buf, boolean close) {
		if (reader == null || writer == null || buf == null || buf.length == 0) return false;
		try {
			int len = -1;
			while ((len = reader.read(buf)) > -1) 
			   writer.write(buf, 0, len);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (close) {
				try {reader.close();} catch (IOException ex) {}
				try {writer.close();} catch (IOException ex) {}
			}
		}
	}

	public static boolean write(InputStream in, OutputStream out) {
		return write(in, out, new byte[CACHE_SIZE], true);
	}
	
	public static boolean write(InputStream in, OutputStream out, boolean close) {
		return write(in, out, new byte[CACHE_SIZE], close);
	}

	public static boolean write(InputStream in, OutputStream out, byte[] buf, boolean close) {
		if (in == null || out == null || buf == null || buf.length == 0) return false;
		try {
			int len = -1;
			while ((len = in.read(buf)) > -1) 
			   out.write(buf, 0, len);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (close) {
				try {in.close();} catch (IOException ex) {}
				try {out.close();} catch (IOException ex) {}
			}
		}
	}
    /** 
     * Copy character data from one stream to another.  If out is null, just
     * consume the input, like /dev/null.
     *
     * 
     * @param in Input Reader object.
     * @param out Output Reader object.
     * @return false if a problem occured with writing or closing the streams
     */
    // Copies data from reader in to writer out.  Closes both streams when done.
    public static boolean copy(Reader in, Writer out)
    {
        try {
    	char buf[] = new char[CACHE_SIZE];
	int size;
	while ((size = in.read(buf)) != -1)
	    if (out != null)
	        out.write(buf, 0, size);
        }
        catch (IOException e) { return false; }
	try {
	    in.close();
            if (out != null)
                out.close();
	} catch (IOException e) { return false; }
	return true;
    }
	

    /**
     * Copy binary data from one stream to another.  If out is null, just
     * consume the input, like /dev/null.
     * 
     * @param in Input stream.
     * @param out Output stream.
     * @return false if a problem occured with writing or closing the streams.
     */
    public static boolean copy(InputStream in, OutputStream out)
    {
        try {
            byte buf[] = new byte[CACHE_SIZE];
            int size;
            while ((size = in.read(buf)) != -1)
                if (out != null)
                    out.write(buf, 0, size);
        }
        catch (IOException e) { return false; }
        try {
            in.close();
            if (out != null)
                out.close();
        }
        catch (IOException e) { return false; }
        return true;
    }

    
    // Create a buffered file writer
    // TODO: align this class with lio.IO
    public static BufferedWriter writer(final String filename)
        throws IOException
    { return new BufferedWriter(new FileWriter(filename)); }
    
    // Create a writer to a particular character set
    // TODO: align this class with lio.IO
    public static BufferedWriter writer(final String filename, final String charset)
        throws IOException
    {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), charset));
    }

    public static BufferedReader reader(final String filename) throws FileNotFoundException
    {
        return new BufferedReader(new FileReader(filename));
    }
    
    public static BufferedReader reader(final String filename, final String charset)
        throws UnsupportedEncodingException, FileNotFoundException
    {
        return new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));
    }
}
