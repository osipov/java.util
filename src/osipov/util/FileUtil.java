package osipov.util;

import static osipov.util.StringUtil.removeNonAlphaNumericCharacters;
import static osipov.util.ZipUtil.unzip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
	
	/**
	 * command objects for the following method: @see #resolveUrlAsFile(URL)
	 */
	private static Map urlHandlers = new HashMap();
	static {
		urlHandlers.put("file", new FileHandler());
		urlHandlers.put("http", new HttpHandler());
		urlHandlers.put("jar", new JarFileHandler());
		//for some reason WAS 5.x uses wsjar prefix
		urlHandlers.put("wsjar", new GenericURLHandler());
		urlHandlers.put("bundleresource", new GenericURLHandler());
	}

	private interface Handler {
		/**
		 * Given a URL the handler will attempt to resolve the URL to a platform specific 
		 * absolute path pointing to a file on the local file system. In the case when the URL
		 * points to a file, a simple conversion is performed. In the case when the URL
		 * points to a file via HTTP or a to an archived file in a JAR, the handler will
		 * attempt to download/extract the file to the system wide temporary directory
		 * (as specified by java.io.tmpdir system property), with a unique identifier. If 
		 * the file referenced by the URL already exists on the file system, no attempts
		 * are made to overwrite the file by the contents of the file referenced by the URL.  
		 * @param url
		 * @return String platform specific absolute path to a file on the local file system.
		 * The content of the file is defined by the content of the file referenced by the URL
		 */
		public String resolveUrlAsFile(URL url);
	}

	private static class FileHandler implements Handler {
		public String resolveUrlAsFile(URL url) {
			String file = null;
			try {
				file = new File(URLDecoder.decode(url.getFile())).getCanonicalPath();
			} catch (IOException e) {
				file = null;
			}
			return file;
		}
	}
	private static class HttpHandler implements Handler {
		/**
		 * Implements the protocol specified by
		 * @see Util.Handler#resolveUrlAsFile(java.net.URL)
		 * This implementation will construct a unique
		 * directory for the file referenced by the URL
		 * as follows:
		 * ${java.io.tmpdir}
		 * /${path to jar file containing minus any non-alphanumeric characters}
		 * /${directory path into the file}
		 * /${HTTP last modified stamp}
		 * /${filename}
		 */
		public String resolveUrlAsFile(URL url) {
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection)url.openConnection();
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					
					File tmpFileDir = new File(System.getProperty("java.io.tmpdir") +File.separator + "osipov" + File.separator
										+ StringUtil.removeNonAlphaNumericCharacters(url.getProtocol() + url.getHost() + url.getPort())
										+ File.separator 
										+ url.getPath()
										+ File.separator
										+ Long.toString(conn.getLastModified()));
					
					if (!tmpFileDir.exists() && !tmpFileDir.mkdirs()) return null;
					 					
					final File tmpFile = new File(tmpFileDir + File.separator + StringUtil.getRightOf(url.getFile(), "/"));
					if (!tmpFile.exists()) {
						IOUtil.write(conn.getInputStream(), new FileOutputStream(tmpFile));

						//commented out the next line because it doesn't work
						//the file never gets released by the jvm
						//tmpFile.deleteOnExit();
					}
					return tmpFile.getCanonicalPath();
				} 
			}catch(IOException e) {
				return null;
			} finally {
				if (conn != null) conn.disconnect();
			}
			return null;
		}
	}
	
	private static class JarFileHandler implements Handler {
		/**
		 * Implements the protocol specified by
		 * @see Util.Handler#resolveUrlAsFile(java.net.URL)
		 * for the URLs pointing to a JAR file that have 
		 * a format similar to the following: jar:file:/D:/workspace/shared/library.jar!/plugin/jniac.dll
		 * This implementation will construct a unique
		 * directory for the file referenced by the URL
		 * as follows:
		 * ${java.io.tmpdir}
		 * /${path to jar file containing minus any non-alphanumeric characters}
		 * /${directory path into the file}
		 * /${CRC32 checksum of the file}
		 * /${filename}
		 */
		public String resolveUrlAsFile(URL url) {
			try {
				String jarFile = url.getFile();
				String archivedFilePath = jarFile.substring(jarFile.indexOf("!") + 1, jarFile.lastIndexOf("/"));		
				JarURLConnection c = (JarURLConnection)url.openConnection();
				File tmpFileDir = new File(System.getProperty("java.io.tmpdir") +File.separator + "osipov" + File.separator
									+ StringUtil.removeNonAlphaNumericCharacters(jarFile.substring(0, jarFile.indexOf("!")))
									+ File.separator
									+ archivedFilePath
									+ File.separator
									+ Long.toString(c.getJarEntry().getCrc()));
									
				if (!tmpFileDir.exists() && !tmpFileDir.mkdirs()) return null;
				
				File tmpFile = new File(tmpFileDir + File.separator + jarFile.substring(jarFile.lastIndexOf("/") + 1));
				if (!tmpFile.exists()) {
					IOUtil.write(c.getInputStream(), new FileOutputStream(tmpFile));
					
					//commented out the next line because it doesn't work
					//the file never gets released by the jvm
//					tmpFile.deleteOnExit();
				}
				return tmpFile.getCanonicalPath();					
			} catch (IOException e) {
				return null;
			} 
		}
	}

	private static class GenericURLHandler implements Handler {
		/**
		 * Implements the protocol specified by
		 * @see Util.Handler#resolveUrlAsFile(java.net.URL)
		 * for arbitrary URLs. This implementation will construct a unique
		 * directory for the URL reference as follows:
		 * ${java.io.tmpdir}
		 * /${hashcode of the path to file minus any non-alphanumeric characters}
		 * /${lastmodified}
		 * /${filename starting from the last / character}
		 */
		public String resolveUrlAsFile(URL url) {
			try {
				String file = url.getFile();
				
				URLConnection c = url.openConnection();
				File tmpFileDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "nlu" 
									+ File.separator	
									+ new String(StringUtil.removeNonAlphaNumericCharacters(file)).hashCode()
									+ File.separator
									+ Long.toString(c.getLastModified()));
				if (!tmpFileDir.exists() && !tmpFileDir.mkdirs()) return null;
				
				String filename = file.substring(file.lastIndexOf("/") + 1);
				File tmpFile = new File(tmpFileDir + File.separator + filename);
				if (!tmpFile.exists()) {
					IOUtil.write(c.getInputStream(), new FileOutputStream(tmpFile));
					
					//commented out the next line because it doesn't work
					//the file never gets released by the jvm
//					tmpFile.deleteOnExit();
				}
				return tmpFile.getCanonicalPath();					
			} catch (IOException e) {
				return null;
			} 
		}
	}
	
	/**
	 * @see #resolveUrlAsFile(URL)
	 * @param urlString
	 * @return null if the input is null, zero length, or is a malformed URL address
	 */
	
	public static String resolveUrlAsFile(String urlString) {
		if (urlString == null || urlString.length() == 0) 
			return null;

		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			return null;
		}
		return resolveUrlAsFile(url);
	}

	/**
	 * Determines the appropriate handler for the provided URL and 
	 * invokes a corresponding handler. For details @see Handler#resolveUrlAsFile(URL)
	 * @param url
	 * @return null if the input is null
	 */
	public static String resolveUrlAsFile(URL url) {
		if (url == null) return null;
		Handler h = (Handler)urlHandlers.get(url.getProtocol());
		if(h == null)
			h = new GenericURLHandler();
		return h.resolveUrlAsFile(url);
	}

	
    /** Returns a file handle to the requested resource. 
     * Looks for the file in the local filesystem using the following paths:
     * 		URI if a protocol is specified
     * 		./URI if a protocol is not specified 
     * 	    path/URI if a protocol is specified
     *		./path/URI if a protocol is not specified
     * 		
     * @param path The list of paths to search
     * @param URI The URI of the resource. Supported protocols: http://, 
     * file://, jar://
     * @param unpack If the file has a .zip extension, unpack it
     * 
     * @return A reference to a File object containing the downloaded 
     * file in the local filesystem, or a file object pointing to an
     * unpacked directory.
     */
    public static File resolveAsFile(String[] paths, String URI, boolean unpack) {
        try {
            URI = URI.replace('\\', '/');
            File localFile = null;

            // Find the file
            // If the URI has no protocol
            if (URI.indexOf("://") < 0) {
                boolean isAbsolute = (URI.charAt(0) == '/' || URI.indexOf(":/") >= 0);
                URI = isAbsolute ? URI : "/" + URI;

                localFile = new File(isAbsolute ? URI : (new File(".").getAbsolutePath() + URI)).getCanonicalFile();
                // If the file was not found, start searching the paths
                for (int i = 0; !localFile.exists() && paths != null && i < paths.length; i++) {
                    paths[i] = paths[i].replace('\\', '/');

                    // If the path has no protocol
                    if (paths[i].indexOf("://") < 0)
                        localFile = new File(new File(paths[i]).getAbsolutePath() + URI).getCanonicalFile();
                    else
                        localFile = new File(resolveUrlAsFile(paths[i] + URI));
                }
            } else {
                localFile = new File(resolveUrlAsFile(URI));
            }

            String filePath = localFile.getAbsolutePath();

            // If the path is a ZIP file, unpack the zip file and return
            // the path less the .zip extension
            if (unpack && localFile.getName().indexOf(".zip") >= 0 && localFile.isFile()) {

                File tmpdir = getTmpDir(localFile);
                filePath = tmpdir.getAbsolutePath();
                filePath = filePath.substring(0, filePath.length() - 4);
                tmpdir = new File(filePath);
                if (!tmpdir.exists()) {
                    tmpdir.mkdirs();
                    unzip(localFile.getAbsolutePath(), filePath);
                }
                localFile = tmpdir;
            }

            return (new File(filePath));

        } catch (Exception e) {
            throw new RuntimeException(e.getClass().getName()+":"+URI.toString());
        }
    }
        
        /** Returns the name of a directory that this file can be unzipped to
         * 
         * @param file The file to unzip
         * @return The temp directory
         */
        private static File getTmpDir(File file)
        {

            return(new File(System.getProperty("java.io.tmpdir") + File.separator + "osipov" 
    			+ File.separator	
    			+ removeNonAlphaNumericCharacters(file.getAbsolutePath())
    			+ File.separator
    			+ Long.toString(file.lastModified())
            	+ File.separator
            	+ file.getName()));
        }
}
