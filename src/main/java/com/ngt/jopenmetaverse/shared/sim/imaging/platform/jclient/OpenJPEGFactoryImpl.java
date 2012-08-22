package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import com.ngt.jopenmetaverse.shared.sim.imaging.IOpenJPEG;
import com.ngt.jopenmetaverse.shared.sim.imaging.IOpenJPEGFactory;

public class OpenJPEGFactoryImpl implements IOpenJPEGFactory{
	private final static IOpenJPEG iOpenJPEG;
	static{
		iOpenJPEG= new OpenJPEGImpl();
		((OpenJPEGImpl)iOpenJPEG).setBitmapFactory(new BitmapFactoryImpl());
	}
	
	public IOpenJPEG getNewIntance()
	{
		return iOpenJPEG;
	}
}
