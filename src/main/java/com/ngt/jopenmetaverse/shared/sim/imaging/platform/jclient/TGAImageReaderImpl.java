package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.FileCacheImageInputStream;
import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmap;
import com.ngt.jopenmetaverse.shared.sim.imaging.ITGAImageReader;
import com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga.TGAImageReader;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class TGAImageReaderImpl implements ITGAImageReader{

	static
	{
		IIORegistry registry = IIORegistry.getDefaultInstance();
		registry.registerServiceProvider(new  com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga.TGAImageReaderSpi());
	}
	public IBitmap read(InputStream stream) {
		try {
			//TODO need to specify the system tmp directory
			FileCacheImageInputStream fileImageInputStream = new FileCacheImageInputStream(stream, null); 
			TGAImageReader reader = (TGAImageReader)ImageIO.getImageReadersByFormatName("tga").next();
			
			reader.setInput(fileImageInputStream);
			BufferedImage img = reader.read(0);
			//TODO need to verify the image type loaded, probably Alpha values are not loaded
			BitmapBufferedImageImpl img2 = new BitmapBufferedImageImpl(img);
			return img2;
			} catch (IOException e) {
				JLogger.warn(Utils.getExceptionStackTraceAsString(e));
				return null;
			}
	}	
}
