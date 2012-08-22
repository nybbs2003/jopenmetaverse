package com.ngt.jopenmetaverse.shared.sim.imaging;

import com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.OpenJPEGFactoryImpl;

public class SuperFactory {
	public static IOpenJPEGFactory getIOpenJPEGFactoryInstance()
	{
		return new OpenJPEGFactoryImpl();
	}
}
