package com.ngt.jopenmetaverse.shared.sim.imaging;


public interface IOpenJPEG {
	
	public static class DecodeToImageResult
	{
		ManagedImage managedImage;
		IBitmap image;
		
		public DecodeToImageResult(ManagedImage managedImage, IBitmap image) {
			super();
			this.managedImage = managedImage;
			this.image = image;
		}
		public ManagedImage getManagedImage() {
			return managedImage;
		}
		public void setManagedImage(ManagedImage managedImage) {
			this.managedImage = managedImage;
		}
		public IBitmap getImage() {
			return image;
		}
		public void setImage(IBitmap image) {
			this.image = image;
		}
	}
	
    /// <summary>
    /// Encode a <seealso cref="ManagedImage"/> object into a byte array
    /// </summary>
    /// <param name="image">The <seealso cref="ManagedImage"/> object to encode</param>
    /// <param name="lossless">true to enable lossless conversion, only useful for small images ie: sculptmaps</param>
    /// <returns>A byte array containing the encoded Image object</returns>
    public byte[] Encode(ManagedImage image, boolean lossless) throws Exception;
    
    /// <summary>
    /// Encode a <seealso cref="ManagedImage"/> object into a byte array
    /// </summary>
    /// <param name="image">The <seealso cref="ManagedImage"/> object to encode</param>
    /// <returns>a byte array of the encoded image</returns>
    public  byte[] Encode(ManagedImage image) throws Exception;

    /// <summary>
    /// Decode JPEG2000 data to an <seealso cref="System.Drawing.Image"/> and
    /// <seealso cref="ManagedImage"/>
    /// </summary>
    /// <param name="encoded">JPEG2000 encoded data</param>
    /// <param name="managedImage">ManagedImage object to decode to</param>
    /// <param name="image">Image object to decode to</param>
    /// <returns>DecodeToImageResult if the decode succeeds, otherwise null</returns>
    public  DecodeToImageResult DecodeToImage2(byte[] encoded) throws Exception;
    
    /// <summary>
    /// 
    /// </summary>
    /// <param name="encoded"></param>
    /// <param name="managedImage"></param>
    /// <returns>ManagedImage, if the decode succeeds, otherwise null</returns>
    public  ManagedImage DecodeToImage(byte[] encoded) throws Exception;
    
//    /// <summary>
//    /// 
//    /// </summary>
//    /// <param name="encoded"></param>
//    /// <param name="layerInfo"></param>
//    /// <param name="components"></param>
//    /// <returns></returns>
//    public  boolean DecodeLayerBoundaries(byte[] encoded, out J2KLayerInfo[] layerInfo, out int components);
//	
    /// <summary>
    /// Encode a <seealso cref="System.Drawing.Bitmap"/> object into a byte array
    /// </summary>
    /// <param name="bitmap">The source <seealso cref="System.Drawing.Bitmap"/> object to encode</param>
    /// <param name="lossless">true to enable lossless decoding</param>
    /// <returns>A byte array containing the source Bitmap object</returns>
    //unsafe
    public  byte[] EncodeFromImage(IBitmap bitmap, boolean lossless) throws Exception;

	public IBitmap DecodeToIBitMap(byte[] encoded) throws Exception;
}
