package com.ngt.jopenmetaverse.shared.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static String getFileNameWithoutExtension(String fileName)
	{
		 int mid= fileName.lastIndexOf(".");
		 return mid >= 0 ? fileName.substring(0,mid) : "";
	}
	

	public static String getExtension(String fileName)
	{
		 int mid= fileName.lastIndexOf(".");
		 return mid >= 0 ? fileName.substring(mid,fileName.length()) : "";
	}
	
	public static File[] getFileList(String dirname, boolean topDirectoryOnly)
	{
		return getFileList(dirname, ".+", topDirectoryOnly);
	}
	
	public static File[] getFileList(String dirname, String regex, boolean topDirectoryOnly)
	{
		//TODO need to implement topDirectoryOnly
		JLogger.debug("Try to traverse the directory" + dirname);
		List<File> files = new ArrayList<File>(); 
		File file = new File(dirname); 

		if(file.isDirectory())
		{
			System.out.println("Directory is  " + dirname);
			String str[] = file.list();
			for( int i = 0; i < str.length; i++)
			{
				if(str[i].matches(regex))
				{
					File f=new File(dirname + "/" + str[i]);
					if(f.isDirectory()){
						System.out.println(str[i] + " is a directory");
					}
					else
					{
						files.add(f);
						System.out.println(str[i] + " is a file");
					}
				}
			}
		}
		return files.toArray(new File[0]);
	}

	public static byte[] readBytes(File f) throws FileNotFoundException, IOException
	{
		return readBytes(new FileInputStream(f));
	}
	
	public static byte[] readBytes(InputStream input) throws IOException
	{
		int totalBytesRead = 0;
		int bytesRead = 0;
		final int maxByteRead = 10000;
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(2048);
		byte[] bytes = new byte[maxByteRead];

		while(bytesRead >= 0){
			bytesRead = input.read(bytes, 0, maxByteRead); 
			if (bytesRead > 0){
				totalBytesRead = totalBytesRead + bytesRead;
				byteBuffer.write(bytes, 0, bytesRead);
			}
		}
		return byteBuffer.toByteArray();
	}

	public static void writeBytes(OutputStream output, byte[] data) throws IOException
	{	
		output.write(data);	
	}   
	
	static public void deleteDirectory(File path, boolean topDirectoryOnly) {
	    if( path.exists() && path.isDirectory()) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory() && !topDirectoryOnly) {
	           deleteDirectory(files[i], topDirectoryOnly);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	      path.delete();
	    }
	  }
	
	static public String combineFilePath(String path1, String path2)
	{
		File f = new File(new File(path1), path2);
		return f.getPath();
	}
	
	static public void copyFile(String src, String dst, boolean overwrite) throws Exception
	{
		File sfile = new File(src);
		File dfile = new File(dst);
		if(!dfile.exists() || overwrite)
			org.apache.commons.io.FileUtils.copyFile(sfile, dfile);
	}
	
	/*
	 * Silently try to close the stream with raising exception
	 */
	public static void closeStream(InputStream is)
	{
		try {
			is.close();
		} catch (IOException e) {
			JLogger.warn("Error in closing the input stream " + Utils.getExceptionStackTraceAsString(e));
		}
	}

	/*
	 * Silently try to close the stream with raising exception
	 */
	public static void closeStream(OutputStream is)
	{
		try {
			is.close();
		} catch (IOException e) {
			JLogger.warn("Error in closing the output stream " + Utils.getExceptionStackTraceAsString(e));
		}
	}
}
