package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmap;
import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmapFactory;

public class BitmapFactoryImpl implements IBitmapFactory{

	public IBitmap getNewIntance(int w, int h, int[] pixels) {
		return new BitmapBufferedImageImpl(w, h, pixels);
	}
	
}
