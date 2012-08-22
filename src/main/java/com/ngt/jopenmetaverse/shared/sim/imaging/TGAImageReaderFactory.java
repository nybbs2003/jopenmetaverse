package com.ngt.jopenmetaverse.shared.sim.imaging;

import com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.TGAImageReaderImpl;

public class TGAImageReaderFactory {

//	static {
//		IIORegistry registry = IIORegistry.getDefaultInstance();
//		registry.registerServiceProvider(new  com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga.TGAImageReaderSpi());
//		
//	}
	
	public static ITGAImageReader getInstance()
	{
		return new TGAImageReaderImpl();
	}
}
