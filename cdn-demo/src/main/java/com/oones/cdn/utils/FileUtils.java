/**
 * 
 */
package com.oones.cdn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletContext;

import org.springframework.http.MediaType;

/**
 * @author Son Truong
 *
 */
public class FileUtils {
	
	public static String getMimeType(String url) {
		String type = null; 
		try {
			type = URLConnection.guessContentTypeFromName(url);
		} catch (Exception e) {
		}

		if (null == type) {
			try {
				Path path = new File(url).toPath();
				type = Files.probeContentType(path);
			} catch (Exception e) {
			}
		}
		return type;
	}
	
	public static String getMimeType(ServletContext servletContext, String url) {
		String type = null; 
        try {
        	type = servletContext.getMimeType(url);
            MediaType.parseMediaType(type);
            return type;
        } catch (Exception e) {
        }

		return getMimeType(url);
	}
	
	public static String getMimeType(File file) throws MalformedURLException, IOException {
		URLConnection connection = file.toURL().openConnection();
	    String mimeType = connection.getContentType();
	    return mimeType;
	}

	public static void copyFile(String cdnRoot, String path, String output) {
		FileInputStream fis = null;
        FileOutputStream fos = null;
        try {

            fis = new FileInputStream(cdnRoot + "/" + path);
            fos = new FileOutputStream(cdnRoot + "/" + output);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Problems when copying file.");
        } finally {
            if (path != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                  System.out.println(e);
                }
            }
            if (output != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
	}
}
