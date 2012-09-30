package com.ngt.jopenmetaverse.shared.sim.imaging;

import com.ngt.jopenmetaverse.shared.sim.imaging.ManagedImage.ImageChannels;

public abstract class AbstractOpenJPEGDecoder implements IOpenJPEG
{
	public DecodedTgaImage DecodeToTgaImage(byte[] imageData) throws Exception
	{
	ManagedImage mi = OpenJPEGFactory.getIntance().DecodeToImage(imageData);
      if (mi == null)
    	  return null;

      DecodedTgaImage cachedImage = new DecodedTgaImage();
      
      cachedImage.hasAlpha = false;
      cachedImage.fullAlpha = false;
      cachedImage.isMask = false;
		if ((ImageChannels.and(mi.Channels,  ManagedImage.ImageChannels.Alpha)) != 0)
      {
			cachedImage.fullAlpha = true;
			cachedImage.isMask = true;

          // Do we really have alpha, is it all full alpha, or is it a mask
          for (int i = 0; i < mi.Alpha.length; i++)
          {
              if (mi.Alpha[i] < 255)
              {
            	  cachedImage.hasAlpha = true;
              }
              if (mi.Alpha[i] != 0)
              {
            	  cachedImage.fullAlpha = false;
              }
              if (mi.Alpha[i] != 0 && mi.Alpha[i] != 255)
              {
            	  cachedImage.isMask = false;
              }
          }

          if (!cachedImage.hasAlpha)
          {
				mi.ConvertChannels(ImageChannels.get(ImageChannels.and(mi.Channels, ~ManagedImage.ImageChannels.Alpha.getIndex())));
//              mi.ConvertChannels(mi.Channels & ~ManagedImage.ImageChannels.Alpha);
          }
          
          cachedImage.data = mi.ExportTGA();
          
      }
        return cachedImage;
	}
	
}
