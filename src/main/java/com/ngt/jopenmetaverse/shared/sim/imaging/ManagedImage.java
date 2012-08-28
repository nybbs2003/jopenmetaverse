package com.ngt.jopenmetaverse.shared.sim.imaging;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ngt.jopenmetaverse.shared.exception.NotImplementedException;
import com.ngt.jopenmetaverse.shared.exception.NotSupportedException;

public class ManagedImage {

	//	  [Flags]
	public enum ImageChannels
	{
		Gray (1),
		Color (2),
		Alpha (4),
		Bump (8);
		private int index;
		ImageChannels(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,ImageChannels> lookup  
		= new HashMap<Integer,ImageChannels>();

		static {
			for(ImageChannels s : EnumSet.allOf(ImageChannels.class))
				lookup.put(s.getIndex(), s);
		}

		public static EnumSet<ImageChannels> get(Integer index)
		{
			EnumSet<ImageChannels> enumsSet = EnumSet.allOf(ImageChannels.class);
			for(Entry<Integer,ImageChannels> entry: lookup.entrySet())
			{
				if((entry.getKey().intValue() | index) != index)
				{
					enumsSet.remove(entry.getValue());
				}
			}
			return enumsSet;
		}

		public static int getIndex(EnumSet<ImageChannels> enumSet)
		{
			int ret = 0;
			for(ImageChannels s: enumSet)
			{
				ret |= s.getIndex();
			}
			return ret;
		}
		
		public static boolean equals(EnumSet<ImageChannels> s, EnumSet<ImageChannels> p)
		{
			return ImageChannels.getIndex(s) == ImageChannels.getIndex(p);
		}

		public static int and(EnumSet<ImageChannels> s, EnumSet<ImageChannels> p)
		{
			return ImageChannels.getIndex(s) & ImageChannels.getIndex(p);
		}

		public static int and(EnumSet<ImageChannels> s, int i)
		{
			return ImageChannels.getIndex(s) & i;
		}
		
		public static int xor(EnumSet<ImageChannels> s, EnumSet<ImageChannels> p)
		{
			return ImageChannels.getIndex(s) ^ ImageChannels.getIndex(p);
		}

		public static int or(EnumSet<ImageChannels> s, EnumSet<ImageChannels> p)
		{
			return ImageChannels.getIndex(s) | ImageChannels.getIndex(p);
		}

		public static int or(EnumSet<ImageChannels> s, ImageChannels p)
		{
			return ImageChannels.getIndex(s) | p.getIndex();
		}
		
		public static int and(EnumSet<ImageChannels> s, ImageChannels p)
		{
			return (ImageChannels.getIndex(s) & p.getIndex());
		}
		
	};

	public enum ImageResizeAlgorithm
	{
		NearestNeighbor
	}

	/// <summary>
	/// Image width
	/// </summary>
	public int Width;

	/// <summary>
	/// Image height
	/// </summary>
	public int Height;

	/// <summary>
	/// Image channel flags
	/// </summary>
	public EnumSet<ImageChannels> Channels;

	/// <summary>
	/// Red channel data
	/// </summary>
	public byte[] Red;

	/// <summary>
	/// Green channel data
	/// </summary>
	public byte[] Green;

	/// <summary>
	/// Blue channel data
	/// </summary>
	public byte[] Blue;

	/// <summary>
	/// Alpha channel data
	/// </summary>
	public byte[] Alpha;

	/// <summary>
	/// Bump channel data
	/// </summary>
	public byte[] Bump;

	/// <summary>
	/// Create a new blank image
	/// </summary>
	/// <param name="width">width</param>
	/// <param name="height">height</param>
	/// <param name="channels">channel flags</param>
	public ManagedImage(int width, int height, EnumSet<ImageChannels> channels)
	{
		Width = width;
		Height = height;
		Channels = channels;

		int n = width * height;

		if ((ImageChannels.getIndex(channels) & ImageChannels.Gray.getIndex()) != 0)
		{
			Red = new byte[n];
		}
		else if ((ImageChannels.getIndex(channels) & ImageChannels.Color.getIndex()) != 0)
		{
			Red = new byte[n];
			Green = new byte[n];
			Blue = new byte[n];
		}

		if ((ImageChannels.getIndex(channels) & ImageChannels.Alpha.getIndex()) != 0)
			Alpha = new byte[n];

		if ((ImageChannels.getIndex(channels) & ImageChannels.Bump.getIndex()) != 0)
			Bump = new byte[n];
	}

//	#if !NO_UNSAFE
	/// <summary>
	/// 
	/// </summary>
	/// <param name="bitmap"></param>
	public ManagedImage(IBitmap bitmap) throws NotSupportedException, NotImplementedException
	{
		Width = bitmap.getWidth();
		Height = bitmap.getHeight();

		int pixelCount = Width * Height;

		if (bitmap.hasPixelFormat(PixelFormat.Format32bppArgb) || bitmap.hasPixelFormat(PixelFormat.Custom))
		{
			Channels = ImageChannels.get(ImageChannels.Alpha.getIndex() | ImageChannels.Color.getIndex());
			Red = new byte[pixelCount];
			Green = new byte[pixelCount];
			Blue = new byte[pixelCount];
			Alpha = new byte[pixelCount];

			int i =0;
			for(int x = 0; x < Width; x++)
				for(int y = 0; y< Height; y++)
				{
					i = x*Height + y;
					int pixel = bitmap.getRGB(x, y);
					 	Alpha[i] = (byte) ((pixel >> 24) & 0xff);
				        Red[i] = (byte) ((pixel >> 16) & 0xff);
				        Green[i] = (byte) ((pixel >> 8) & 0xff);
				        Blue[i] = (byte) ((pixel >> 0) & 0xff);
				}
			
			
//			System.Drawing.Imaging.BitmapData bd = bitmap.LockBits(new System.Drawing.Rectangle(0, 0, Width, Height),
//					System.Drawing.Imaging.ImageLockMode.ReadOnly, System.Drawing.Imaging.PixelFormat.Format32bppArgb);
//
//			unsafe
//			{
//				byte* pixel = (byte*)bd.Scan0;
//
//				for (int i = 0; i < pixelCount; i++)
//				{
//					// GDI+ gives us BGRA and we need to turn that in to RGBA
//					Blue[i] = *(pixel++);
//					Green[i] = *(pixel++);
//					Red[i] = *(pixel++);
//					Alpha[i] = *(pixel++);
//				}
//			}
//
//			bitmap.UnlockBits(bd);
		}
		else if (bitmap.hasPixelFormat(PixelFormat.Format16bppGrayScale) || bitmap.hasPixelFormat(PixelFormat.Format8bppGrayScale))
		{
			Channels = ImageChannels.get(ImageChannels.Gray.getIndex());
			Red = new byte[pixelCount];
			
			int i =0;
			for(int x = 0; x < Width; x++)
				for(int y = 0; y< Height; y++)
				{
					i = x*Height + y;
					int pixel = bitmap.getRGB(x, y);
				        Red[i] = (byte) ((pixel >> 0) & 0xff);
				}			
		}
		else if (bitmap.hasPixelFormat(PixelFormat.Format24bppRgb))
		{
			Channels = ImageChannels.get(ImageChannels.Color.getIndex());
			Red = new byte[pixelCount];
			Green = new byte[pixelCount];
			Blue = new byte[pixelCount];

			int i =0;
			for(int x = 0; x < Width; x++)
				for(int y = 0; y< Height; y++)
				{
					i = x*Height + y;
					int pixel = bitmap.getRGB(x, y);
//					 	Alpha[i] = (byte) ((pixel >> 24) & 0xff);
				        Red[i] = (byte) ((pixel >> 16) & 0xff);
				        Green[i] = (byte) ((pixel >> 8) & 0xff);
				        Blue[i] = (byte) ((pixel >> 0) & 0xff);
				}
			
//			System.Drawing.Imaging.BitmapData bd = bitmap.LockBits(new System.Drawing.Rectangle(0, 0, Width, Height),
//					System.Drawing.Imaging.ImageLockMode.ReadOnly, System.Drawing.Imaging.PixelFormat.Format24bppRgb);
//
//			unsafe
//			{
//				byte* pixel = (byte*)bd.Scan0;
//
//				for (int i = 0; i < pixelCount; i++)
//				{
//					// GDI+ gives us BGR and we need to turn that in to RGB
//					Blue[i] = *(pixel++);
//					Green[i] = *(pixel++);
//					Red[i] = *(pixel++);
//				}
//			}
//
//			bitmap.UnlockBits(bd);
		}
		else if (bitmap.hasPixelFormat(PixelFormat.Format32bppRgb))
		{
			Channels = ImageChannels.get(ImageChannels.Color.getIndex());
			Red = new byte[pixelCount];
			Green = new byte[pixelCount];
			Blue = new byte[pixelCount];

			int i =0;
			for(int x = 0; x < Width; x++)
				for(int y = 0; y< Height; y++)
				{
					i = x*Height + y;
					int pixel = bitmap.getRGB(x, y);
//					 	Alpha[i] = (byte) ((pixel >> 24) & 0xff);
				        Red[i] = (byte) ((pixel >> 16) & 0xff);
				        Green[i] = (byte) ((pixel >> 8) & 0xff);
				        Blue[i] = (byte) ((pixel >> 0) & 0xff);
				}
			
//			System.Drawing.Imaging.BitmapData bd = bitmap.LockBits(new System.Drawing.Rectangle(0, 0, Width, Height),
//					System.Drawing.Imaging.ImageLockMode.ReadOnly, System.Drawing.Imaging.PixelFormat.Format32bppRgb);
//
//			unsafe
//			{
//				byte* pixel = (byte*)bd.Scan0;
//
//				for (int i = 0; i < pixelCount; i++)
//				{
//					// GDI+ gives us BGR and we need to turn that in to RGB
//					Blue[i] = *(pixel++);
//					Green[i] = *(pixel++);
//					Red[i] = *(pixel++);
//					pixel++;	// Skip over the empty byte where the Alpha info would normally be
//				}
//			}
//
//			bitmap.UnlockBits(bd);
		}
		else
		{
			throw new NotSupportedException("Unrecognized pixel format: " + bitmap.getPixelFormatAsString());
		}
	}
//	#endif

	/// <summary>
	/// Convert the channels in the image. Channels are created or destroyed as required.
	/// </summary>
	/// <param name="channels">new channel flags</param>
	public void ConvertChannels(EnumSet<ImageChannels> channels)
	{
		if (Channels == channels)
			return;

		int n = Width * Height;
		EnumSet<ImageChannels> add = ImageChannels.get(ImageChannels.and(channels, ImageChannels.xor(Channels,  channels)));
		EnumSet<ImageChannels> del = ImageChannels.get(ImageChannels.and(Channels, ImageChannels.xor(Channels,  channels)));

		if ((ImageChannels.getIndex(add) & ImageChannels.Color.getIndex()) != 0)
		{
			Red = new byte[n];
			Green = new byte[n];
			Blue = new byte[n];
		}
		else if ((ImageChannels.and(del , ImageChannels.Color)) != 0)
		{
			Red = null;
			Green = null;
			Blue = null;
		}

		if ((ImageChannels.and(del , ImageChannels.Alpha)) != 0)
		{
			Alpha = new byte[n];
			FillArray(Alpha, (byte)255);
		}
		else if ((ImageChannels.and(del , ImageChannels.Alpha) != 0))
			Alpha = null;

		if ((ImageChannels.and(del , ImageChannels.Bump) != 0))
			Bump = new byte[n];
		else if ((ImageChannels.and(del , ImageChannels.Bump) != 0))
			Bump = null;

		Channels = channels;
	}

	/// <summary>
	/// Resize or stretch the image using nearest neighbor (ugly) resampling
	/// </summary>
	/// <param name="width">new width</param>
	/// <param name="height">new height</param>
	public void ResizeNearestNeighbor(int width, int height)
	{
		if (width == Width && height == Height)
			return;

		byte[]
				red = null, 
				green = null, 
				blue = null, 
				alpha = null, 
				bump = null;
		int n = width * height;
		int di = 0, si;

		if (Red != null) red = new byte[n];
		if (Green != null) green = new byte[n];
		if (Blue != null) blue = new byte[n];
		if (Alpha != null) alpha = new byte[n];
		if (Bump != null) bump = new byte[n];

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				si = (y * Height / height) * Width + (x * Width / width);
				if (Red != null) red[di] = Red[si];
				if (Green != null) green[di] = Green[si];
				if (Blue != null) blue[di] = Blue[si];
				if (Alpha != null) alpha[di] = Alpha[si];
				if (Bump != null) bump[di] = Bump[si];
				di++;
			}
		}

		Width = width;
		Height = height;
		Red = red;
		Green = green;
		Blue = blue;
		Alpha = alpha;
		Bump = bump;
	}

	/// <summary>
	/// Create a byte array containing 32-bit RGBA data with a bottom-left
	/// origin, suitable for feeding directly into OpenGL
	/// </summary>
	/// <returns>A byte array containing raw texture data</returns>
	public byte[] ExportRaw()
	{
		byte[] raw = new byte[Width * Height * 4];

		if ((ImageChannels.and(Channels, ImageChannels.Alpha)) != 0)
		{
			if ((ImageChannels.and(Channels, ImageChannels.Color)) != 0)
			{
				// RGBA
				for (int h = 0; h < Height; h++)
				{
					for (int w = 0; w < Width; w++)
					{
						int pos = (Height - 1 - h) * Width + w;
						int srcPos = h * Width + w;

						raw[pos * 4 + 0] = Red[srcPos];
						raw[pos * 4 + 1] = Green[srcPos];
						raw[pos * 4 + 2] = Blue[srcPos];
						raw[pos * 4 + 3] = Alpha[srcPos];
					}
				}
			}
			else
			{
				// Alpha only
				for (int h = 0; h < Height; h++)
				{
					for (int w = 0; w < Width; w++)
					{
						int pos = (Height - 1 - h) * Width + w;
						int srcPos = h * Width + w;

						raw[pos * 4 + 0] = Alpha[srcPos];
						raw[pos * 4 + 1] = Alpha[srcPos];
						raw[pos * 4 + 2] = Alpha[srcPos];
						raw[pos * 4 + 3] = (byte)0xFF;
					}
				}
			}
		}
		else
		{
			// RGB
			for (int h = 0; h < Height; h++)
			{
				for (int w = 0; w < Width; w++)
				{
					int pos = (Height - 1 - h) * Width + w;
					int srcPos = h * Width + w;

					raw[pos * 4 + 0] = Red[srcPos];
					raw[pos * 4 + 1] = Green[srcPos];
					raw[pos * 4 + 2] = Blue[srcPos];
					//TODO need to verify
//					raw[pos * 4 + 3] = Byte.MAX_VALUE;
					raw[pos * 4 + 3] = (byte)0xFF;

				}
			}
		}

		return raw;
	}
	
	/// <summary>
	/// Create a byte array containing 32-bit RGBA data with a bottom-left
	/// origin, suitable for feeding directly into OpenGL
	/// </summary>
	/// <returns>A byte array containing raw texture data</returns>
	public int[] ExportPixels()
	{
		int[] raw = new int[Width * Height];
		int index = 0;
		
		if ((ImageChannels.and(Channels, ImageChannels.Alpha)) != 0)
		{
			if ((ImageChannels.and(Channels, ImageChannels.Color)) != 0)
			{
				// RGBA
				for (int h = 0; h < Height; h++)
				{
					for (int w = 0; w < Width; w++)
					{
						int srcPos = h * Width + w;

						raw[index++] = Alpha[srcPos] << 24 | Red[srcPos] << 16 | Green[srcPos] << 8| Blue[srcPos];

					}
				}
			}
			else
			{
				// Alpha only
				for (int h = 0; h < Height; h++)
				{
					for (int w = 0; w < Width; w++)
					{
//						int pos = (Height - 1 - h) * Width + w;
						int srcPos = h * Width + w;

//						raw[pos * 4 + 0] = Alpha[srcPos];
//						raw[pos * 4 + 1] = Alpha[srcPos];
//						raw[pos * 4 + 2] = Alpha[srcPos];
//						raw[pos * 4 + 3] = (byte)0xFF;
						raw[index++] = (byte)0xFF << 24| Alpha[srcPos] << 16 | Alpha[srcPos] << 8| Alpha[srcPos];

					}
				}
			}
		}
		else
		{
			// RGB
			for (int h = 0; h < Height; h++)
			{
				for (int w = 0; w < Width; w++)
				{
					int pos = (Height - 1 - h) * Width + w;
					int srcPos = h * Width + w;

//					raw[pos * 4 + 0] = Red[srcPos];
//					raw[pos * 4 + 1] = Green[srcPos];
//					raw[pos * 4 + 2] = Blue[srcPos];
//					//TODO need to verify
////					raw[pos * 4 + 3] = Byte.MAX_VALUE;
//					raw[pos * 4 + 3] = (byte)0xFF;
					raw[index++] = (byte)0xFF << 24| Red[srcPos] << 16 | Green[srcPos] << 8| Blue[srcPos];

				}
			}
		}

		return raw;
	}
	
	public byte[] ExportTGA()
	{
		byte[] tga = new byte[Width * Height * ((ImageChannels.and(Channels, ImageChannels.Alpha)) == 0 ? 3 : 4) + 32];
		int di = 0;
		tga[di++] = 0; // idlength
		tga[di++] = 0; // colormaptype = 0: no colormap
		tga[di++] = 2; // image type = 2: uncompressed RGB
		tga[di++] = 0; // color map spec is five zeroes for no color map
		tga[di++] = 0; // color map spec is five zeroes for no color map
		tga[di++] = 0; // color map spec is five zeroes for no color map
		tga[di++] = 0; // color map spec is five zeroes for no color map
		tga[di++] = 0; // color map spec is five zeroes for no color map
		tga[di++] = 0; // x origin = two bytes
		tga[di++] = 0; // x origin = two bytes
		tga[di++] = 0; // y origin = two bytes
		tga[di++] = 0; // y origin = two bytes
		tga[di++] = (byte)(Width & 0xFF); // width - low byte
		tga[di++] = (byte)(Width >> 8); // width - hi byte
		tga[di++] = (byte)(Height & 0xFF); // height - low byte
		tga[di++] = (byte)(Height >> 8); // height - hi byte
		tga[di++] = (byte)((ImageChannels.and(Channels,  ImageChannels.Alpha)) == 0 ? 24 : 32); // 24/32 bits per pixel
		tga[di++] = (byte)((ImageChannels.and(Channels,  ImageChannels.Alpha)) == 0 ? 32 : 40); // image descriptor byte

		int n = Width * Height;

		if ((ImageChannels.and(Channels,  ImageChannels.Alpha)) != 0)
		{
			if ((ImageChannels.and(Channels,  ImageChannels.Color)) != 0)
			{
				// RGBA
				for (int i = 0; i < n; i++)
				{
					tga[di++] = Blue[i];
					tga[di++] = Green[i];
					tga[di++] = Red[i];
					tga[di++] = Alpha[i];
				}
			}
			else
			{
				// Alpha only
				for (int i = 0; i < n; i++)
				{
					tga[di++] = Alpha[i];
					tga[di++] = Alpha[i];
					tga[di++] = Alpha[i];
//					tga[di++] = Byte.MaxValue;
					tga[di++] = (byte)0xff;
				}
			}
		}
		else
		{
			// RGB
			for (int i = 0; i < n; i++)
			{
				tga[di++] = Blue[i];
				tga[di++] = Green[i];
				tga[di++] = Red[i];
			}
		}

		return tga;
	}

	private static void FillArray(byte[] array, byte value)
	{
		if (array != null)
		{
			for (int i = 0; i < array.length; i++)
				array[i] = value;
		}
	}

	public void Clear()
	{
		FillArray(Red, (byte)0);
		FillArray(Green, (byte)0);
		FillArray(Blue, (byte)0);
		FillArray(Alpha, (byte)0);
		FillArray(Bump, (byte)0);
	}

	public ManagedImage Clone()
	{
		ManagedImage image = new ManagedImage(Width, Height, Channels);
		if (Red != null) image.Red = (byte[])Arrays.copyOf(Red, Red.length);
		if (Green != null) image.Green = (byte[])Arrays.copyOf(Green, Green.length);
		if (Blue != null) image.Blue = (byte[])Arrays.copyOf(Blue, Blue.length);
		if (Alpha != null) image.Alpha = (byte[])Arrays.copyOf(Alpha, Alpha.length);
		if (Bump != null) image.Bump = (byte[])Arrays.copyOf(Bump, Bump.length);
		return image;
	}
}
