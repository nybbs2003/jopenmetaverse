package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.FileCacheImageOutputStream;

import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmap;
import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmapFactory;
import com.ngt.jopenmetaverse.shared.sim.imaging.IOpenJPEG;
import com.ngt.jopenmetaverse.shared.sim.imaging.ManagedImage;
import com.sun.media.imageio.plugins.jpeg2000.J2KImageWriteParam;

public class OpenJPEGImpl implements IOpenJPEG {

	protected IBitmapFactory bitmapFactory;
	
	public IBitmapFactory getBitmapFactory() {
		return bitmapFactory;
	}

	public void setBitmapFactory(IBitmapFactory bitmapFactory) {
		this.bitmapFactory = bitmapFactory;
	}

	public byte[] Encode(ManagedImage image, boolean lossless) throws Exception {
		IBitmap bitmap = bitmapFactory.getNewIntance(image.Width, image.Height, image.ExportPixels());
		return EncodeFromImage(bitmap, lossless);
	}

	public byte[] Encode(ManagedImage image) throws Exception {
		return Encode(image, false);
	}

	public IBitmap DecodeToIBitMap(byte[] encoded) throws Exception{
		ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
		ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("jpeg2000").next();
		FileCacheImageInputStream fileImageOutputStream = new FileCacheImageInputStream(bais, null); 
		reader.setInput(fileImageOutputStream);
		BufferedImage img = reader.read(0);
		bais.close();
//		
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ImageIO.write( img, "jpg",  bos );
//		bos.flush();
//		BufferedImage bimage = ImageIO.read( new ByteArrayInputStream(bos.toByteArray()));
//		bos.close();
		return new BitmapBufferedImageImpl(img);
	}
	
	public DecodeToImageResult DecodeToImage2(byte[] encoded) throws Exception {
		IBitmap img2 = DecodeToIBitMap(encoded);
		return new DecodeToImageResult(new ManagedImage(img2), img2);
	}

	public ManagedImage DecodeToImage(byte[] encoded) throws Exception {
		return DecodeToImage2(encoded).getManagedImage();
	}

	public byte[] EncodeFromImage(IBitmap bitmap, boolean lossless) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg2000").next();
		J2KImageWriteParam iwp = (J2KImageWriteParam)writer.getDefaultWriteParam();
		
		iwp.setLossless(lossless);
////		//list of all types, only one seems to exist
//		String[] ct = iwp.getCompressionTypes();
//		for(int i=0;i < ct.length; i++)
//			System.out.println("compression type : "+ct[0]);
//		
//		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//		iwp.setCompressionType(ct[0]);
//		iwp.setCompressionQuality(0.01f);
////		iwp.setEncodingRate(0.01);
////		jwp.setFilter(jwp.FILTER_97);
		
		FileCacheImageOutputStream fileImageOutputStream = new FileCacheImageOutputStream(baos, null); 
		
		writer.setOutput(fileImageOutputStream);
		writer.write(null, new IIOImage(((BitmapBufferedImageImpl)bitmap).getImage(), null, null), iwp);
		fileImageOutputStream.flush();
//		ImageIO.write((BitmapBufferedImageImpl)bitmap, "jpg2000", baos);
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}

}
