package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.io.InputStream;

import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmap;
import com.ngt.jopenmetaverse.shared.sim.imaging.ITGAImageReader;
import com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga.TGADecoder;
import com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga.TGADecoder2;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class TGAImageReaderImpl implements ITGAImageReader{
	public IBitmap read(InputStream stream) {
		try {			
//			BitmapBufferedImageImpl img2 = new BitmapBufferedImageImpl(TGADecoder.loadImage(stream));
			BitmapBufferedImageImpl img2 = new BitmapBufferedImageImpl(TGADecoder2.loadImage(stream));
			return img2;
			} catch (Exception e) {
				JLogger.warn(Utils.getExceptionStackTraceAsString(e));
				return null;
			}
	}	
}
