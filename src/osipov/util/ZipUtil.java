package osipov.util;

import static osipov.util.IOUtil.copy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {
	
	/** Unzips the zip file to the destination directory
	 * 
	 * @param file The file to unzip
	 * @param destination The directory to unzip to
	 */
	public static void unzip(String file, String destination) {
		String base = destination.replace('\\', '/') + "/";
		try {
			(new File(base)).mkdirs();
			ZipFile zipFile = new ZipFile(file);
			Enumeration entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					(new File(base + entry.getName())).mkdirs();
					continue;
				}

				new File((new File(base+entry.getName())).getParent()).mkdirs();
				copy(zipFile.getInputStream(entry),	new BufferedOutputStream(new FileOutputStream(base+entry.getName())));
			}

			zipFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

	}
}
