package com.ngt.jopenmetaverse.shared.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

	
	public static void writeBytes(File f, byte[] bytes) throws IOException
	{
		FileOutputStream fo = new FileOutputStream(f);
		writeBytes(fo, bytes);
		closeStream(fo);
	}
	
	public static byte[] readBytes(File f) throws FileNotFoundException, IOException
	{
		FileInputStream fis = new FileInputStream(f);
		byte[] bytes = readBytes(fis);
		closeStream(fis);
		return bytes;
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
	
//	//TODO Just a testing function need to move it from here
//	public static void saveJpgImage(String baketype, String filepath, int w, int h, int[] pixels) throws NotSupportedException, NotImplementedException, Exception
//	{
//		BufferedImage img = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
//		final int[] a = ( (DataBufferInt) img.getRaster().getDataBuffer() ).getData();
//		System.arraycopy(pixels, 0, a, 0, pixels.length);
//		
////		int k =0;
////		for(int j =0; j< h; j++)
////		{
////			for(int i =0; i< w; i++)
////			{
////				k = j*w + i;
////
////				img.setRGB(i, j, pixels[k]);
////
//////				img.setRGB(i, j,   Color.RED.getRGB());
////
//////				if(img.getRGB(i, j) != pixels[k])
//////					System.out.println(String.format("Color Mismatch %d %d", img.getRGB(i, j), pixels[k]));
//////				System.out.println(String.format("BakeType %s Pixels @ <%d %d> <%d, %d, %d, %d>", baketype, i, j,
//////							(pixels[k]>> 24) & 0xff, 
//////							(pixels[k]>> 16) & 0xff, (pixels[k]>> 8) & 0xff, (pixels[k]) & 0xff));
////				
////			}
////		}
////		
////		FileOutputStream is = new FileOutputStream(filepath+ ".png");
//		
////		FileImageOutputStream fios = new FileImageOutputStream(new File(filepath)); 
////		
//////		ByteArrayOutputStream bos = new ByteArrayOutputStream();
////		Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
////		
////		ImageWriter writer = (ImageWriter)iter.next();
////		// instantiate an ImageWriteParam object with default compression options
////		
////		ImageWriteParam iwp = writer.getDefaultWriteParam();
////		
////		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
////		iwp.setCompressionQuality(1);   // an integer between 0 and 1
////		// 1 specifies minimum compression and maximum quality
////		
////		writer.setOutput(fios);
////		IIOImage image = new IIOImage(img, null, null);
////		writer.write(null, image, iwp);
////		writer.dispose();
//		
////		ImageIO.write( img, "png",  is );		
////		is.flush();
////		closeStream(is);
////		
////		img = ImageIO.read(new File(filepath+ ".png"));
////		
////		ImageIO.write(img, "jpg", new File(filepath+ ".jpg"));
//		
//		FileOutputStream is = new FileOutputStream(filepath + ".jp2");
//		 OpenJPEGImpl oji = new OpenJPEGImpl();
//		oji.setBitmapFactory(new BitmapFactoryImpl());
//		
//		 is.write(oji.EncodeFromImage(new BitmapBufferedImageImpl(img), false));
//			is.flush();
//			closeStream(is);		 
//	}
}
