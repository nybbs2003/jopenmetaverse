package com.ngt.jopenmetaverse.shared.sim.imaging;

import java.io.InputStream;


public interface ITGAImageReader {
	public IBitmap read(InputStream stream); 
}
