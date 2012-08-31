package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;


/**
 * <code>TextureManager</code> provides static methods for building a
 * <code>Texture</code> object. Typically, the information supplied is the
 * filename and the texture properties.
 *
 * @author Mark Powell
 * @author Joshua Slack - cleaned, commented, added ability to read 16bit true color and color-mapped TGAs.
 * @version $Id$
 */
public final class TGADecoder {

	// 0 - no image data in file
	public static final int TYPE_NO_IMAGE = 0;

	// 1 - uncompressed, color-mapped image
	public static final int TYPE_COLORMAPPED = 1;

	// 2 - uncompressed, true-color image
	public static final int TYPE_TRUECOLOR = 2;

	// 3 - uncompressed, black and white image
	public static final int TYPE_BLACKANDWHITE = 3;

	// 9 - run-length encoded, color-mapped image
	public static final int TYPE_COLORMAPPED_RLE = 9;

	// 10 - run-length encoded, true-color image
	public static final int TYPE_TRUECOLOR_RLE = 10;

	// 11 - run-length encoded, black and white image
	public static final int TYPE_BLACKANDWHITE_RLE = 11;


	// private to enforce use of static methods.
	private TGADecoder() {
	}

	/**
	 * <code>loadImage</code> is a manual image loader which is entirely
	 * independent of AWT. OUT: RGB888 or RGBA8888 jme.image.Image object
	 *
	 * @param fis
	 *            InputStream of an uncompressed 24b RGB or 32b RGBA TGA
	 * @return <code>Image</code> object that contains the
	 *         image, either as a RGB888 or RGBA8888
	 */
	public static BufferedImage loadImage(InputStream fis)
			throws IOException {
		return loadImage(fis, false);
	}

	/**
	 * @param fis
	 *            InputStream of an uncompressed 24b RGB or 32b RGBA TGA
	 * @param flip
	 *            Flip the image
	 * @return <code>Image</code> object that contains the
	 *         image, either as a RGB888 or RGBA8888
	 * @throws java.io.IOException
	 */
	public static BufferedImage loadImage(InputStream fis, boolean flip)
			throws IOException {
		return loadImage(fis, flip, false);
	}

	/**
	 * <code>loadImage</code> is a manual image loader which is entirely
	 * independent of AWT. OUT: RGB888 or RGBA8888 jme.image.Image object
	 *
	 * @return <code>Image</code> object that contains the
	 *         image, either as a RGB888 or RGBA8888
	 * @param flip
	 *            Flip the image vertically
	 * @param exp32
	 *            Add a dummy Alpha channel to 24b RGB image.
	 * @param fis
	 *            InputStream of an uncompressed 24b RGB or 32b RGBA TGA
	 * @throws java.io.IOException
	 */
	public static BufferedImage loadImage(InputStream fis, boolean flip,
			boolean exp32) throws IOException {
		boolean flipH = false;
		// open a stream to the file
		BufferedInputStream bis = new BufferedInputStream(fis, 8192);
		
		DataInputStream dis = new DataInputStream(bis);
		
		boolean createAlpha=false;


		// ---------- Start Reading the TGA header ---------- //
		// length of the image id (1 byte)
		int idLength = dis.readUnsignedByte();

		// Type of color map (if any) included with the image
		// 0 - no color map data is included
		// 1 - a color map is included
		int colorMapType = dis.readUnsignedByte();

		// Type of image being read:
		int imageType = dis.readUnsignedByte();

		// Read Color Map Specification (5 bytes)
		// Index of first color map entry (if we want to use it, uncomment and remove extra read.)
		//        short cMapStart = flipEndian(dis.readShort());
		dis.readShort();
		// number of entries in the color map
		short cMapLength = flipEndian(dis.readShort());
		// number of bits per color map entry
		int cMapDepth = dis.readUnsignedByte();

		// Read Image Specification (10 bytes)
		// horizontal coordinate of lower left corner of image. (if we want to use it, uncomment and remove extra read.)
		//        int xOffset = flipEndian(dis.readShort());
		dis.readShort();
		// vertical coordinate of lower left corner of image. (if we want to use it, uncomment and remove extra read.)
		//        int yOffset = flipEndian(dis.readShort());
		dis.readShort();
		// width of image - in pixels
		int width = flipEndian(dis.readShort());
		// height of image - in pixels
		int height = flipEndian(dis.readShort());
		// bits per pixel in image.
		int pixelDepth = dis.readUnsignedByte();
		int imageDescriptor = dis.readUnsignedByte();
		if ((imageDescriptor & 32) != 0) // bit 5 : if 1, flip top/bottom ordering
			flip = !flip;
		//TODO need to handle flipH
		if ((imageDescriptor & 16) != 0) // bit 4 : if 1, flip left/right ordering
			flipH = !flipH;

		// ---------- Done Reading the TGA header ---------- //

		// Skip image ID
		if (idLength > 0)
			bis.skip(idLength);

		ColorMapEntry[] cMapEntries = null;
		if (colorMapType != 0) {
			// read the color map.
			int bytesInColorMap = (cMapDepth * cMapLength) >> 3;
			int bitsPerColor = Math.min(cMapDepth/3 , 8);

			byte[] cMapData = new byte[bytesInColorMap];
			bis.read(cMapData);

			// Only go to the trouble of constructing the color map
			// table if this is declared a color mapped image.
			if (imageType == TYPE_COLORMAPPED || imageType == TYPE_COLORMAPPED_RLE) {
				cMapEntries = new ColorMapEntry[cMapLength];
				int alphaSize = cMapDepth - (3*bitsPerColor);
				float scalar = (float) (255f/(Math.pow(2, bitsPerColor)-1));
				float alphaScalar = (float) (255f/(Math.pow(2, alphaSize)-1));
				for (int i = 0; i < cMapLength; i++) {
					ColorMapEntry entry = new ColorMapEntry();
					int offset = cMapDepth * i;
					entry.red = (byte)(int)(getBitsAsByte(cMapData, offset, bitsPerColor) * scalar);
					entry.green = (byte)(int)(getBitsAsByte(cMapData, offset+bitsPerColor, bitsPerColor) * scalar);
					entry.blue = (byte)(int)(getBitsAsByte(cMapData, offset+(2*bitsPerColor), bitsPerColor) * scalar);
					if (alphaSize <= 0)
						entry.alpha = (byte)255;
					else
						entry.alpha = (byte)(int)(getBitsAsByte(cMapData, offset+(3*bitsPerColor), alphaSize) * alphaScalar);

					cMapEntries[i] = entry;
				}
			}
		}


		// Allocate image data array
		byte[] rawData = null;
		int dl;
		if ((pixelDepth == 32) || (exp32)) {
			rawData = new byte[width * height * 4];
			dl = 4;
			createAlpha = true;
		} else if(pixelDepth == 8) {
			rawData = new byte[width * height];
			dl = 1;
		}
		else{
			rawData = new byte[width * height * 3];
			dl = 3;
		}
		int rawDataIndex = 0;


		if (imageType == TYPE_TRUECOLOR) {
			byte red = 0;
			byte green = 0;
			byte blue = 0;
			byte alpha = 0;

			// Faster than doing a 16-or-24-or-32 check on each individual pixel,
			// just make a seperate loop for each.
			if (pixelDepth == 16) {
				byte[] data = new byte[2];
				float scalar = 255f/31f;
				for (int i = 0; i <= (height - 1); i++) {
					if (!flip)
						rawDataIndex = (height - 1 - i) * width * dl;
					for (int j = 0; j < width; j++) {
						data[1] = dis.readByte();
						data[0] = dis.readByte();
						rawData[rawDataIndex++] = (byte)(int)(getBitsAsByte(data, 1, 5) * scalar);
						rawData[rawDataIndex++] = (byte)(int)(getBitsAsByte(data, 6, 5) * scalar);
						rawData[rawDataIndex++] = (byte)(int)(getBitsAsByte(data, 11, 5) * scalar);
						if (dl == 4) {
							// create an alpha channel
							alpha = getBitsAsByte(data, 0, 1);
							if (alpha == 1) alpha = (byte)255;
							rawData[rawDataIndex++] = alpha;
						}
					}
				}
			} else if (pixelDepth == 24)
				for (int i = 0; i <= (height - 1); i++) {
					if (!flip)
						rawDataIndex = (height - 1 - i) * width * dl;
					for (int j = 0; j < width; j++) {
						blue = dis.readByte();
						green = dis.readByte();
						red = dis.readByte();
						rawData[rawDataIndex++] = red;
						rawData[rawDataIndex++] = green;
						rawData[rawDataIndex++] = blue;
						if (dl == 4) {
							// create an alpha channel
							rawData[rawDataIndex++] = (byte) 255;
						}

					}
				}
			else if (pixelDepth == 32)
				for (int i = 0; i <= (height - 1); i++) {
					if (!flip)
						rawDataIndex = (height - 1 - i) * width * dl;
					for (int j = 0; j < width; j++) {
						blue = dis.readByte();
						green = dis.readByte();
						red = dis.readByte();
						alpha = dis.readByte();
						rawData[rawDataIndex++] = red;
						rawData[rawDataIndex++] = green;
						rawData[rawDataIndex++] = blue;
						rawData[rawDataIndex++] = alpha;
					}
				}
			else throw new IOException("Unsupported TGA true color depth: "+pixelDepth);


		} else if( imageType == TYPE_TRUECOLOR_RLE ){
			byte red = 0;
			byte green = 0;
			byte blue = 0;
			byte alpha = 0;
			// Faster than doing a 16-or-24-or-32 check on each individual pixel,
			// just make a seperate loop for each.
			if( pixelDepth == 32 ){
				for( int i = 0; i <= ( height - 1 ); ++i ){
					if( !flip ){
						rawDataIndex = ( height - 1 - i ) * width * dl;
					}

					for( int j = 0; j < width; ++j ){
						// Get the number of pixels the next chunk covers (either packed or unpacked)
						int count = dis.readByte();
						if( ( count & 0x80 ) != 0 ){
							// Its an RLE packed block - use the following 1 pixel for the next <count> pixels
							count &= 0x07f;
							j += count;
							blue = dis.readByte();
							green = dis.readByte();
							red = dis.readByte();
							alpha = dis.readByte();
							while( count-- >= 0 ){
								rawData[rawDataIndex++] = red;
								rawData[rawDataIndex++] = green;
								rawData[rawDataIndex++] = blue;
								rawData[rawDataIndex++] = alpha;
							}
						} else{
							// Its not RLE packed, but the next <count> pixels are raw.
							j += count;
							while( count-- >= 0 ){
								blue = dis.readByte();
								green = dis.readByte();
								red = dis.readByte();
								alpha = dis.readByte();
								rawData[rawDataIndex++] = red;
								rawData[rawDataIndex++] = green;
								rawData[rawDataIndex++] = blue;
								rawData[rawDataIndex++] = alpha;
							}
						}
					}
				}
			} else if( pixelDepth == 24 ){
				for( int i = 0; i <= ( height - 1 ); i++ ){
					if( !flip ){
						rawDataIndex = ( height - 1 - i ) * width * dl;
					}
					for( int j = 0; j < width; ++j ){
						// Get the number of pixels the next chunk covers (either packed or unpacked)
						int count = dis.readByte();
						if( ( count & 0x80 ) != 0 ){
							// Its an RLE packed block - use the following 1 pixel for the next <count> pixels
							count &= 0x07f;
							j += count;
							blue = dis.readByte();
							green = dis.readByte();
							red = dis.readByte();
							while( count-- >= 0 ){
								rawData[rawDataIndex++] = red;
								rawData[rawDataIndex++] = green;
								rawData[rawDataIndex++] = blue;
								if( createAlpha ){
									rawData[rawDataIndex++] = (byte) 255;
								}
							}
						} else{
							// Its not RLE packed, but the next <count> pixels are raw.
							j += count;
							while( count-- >= 0 ){
								blue = dis.readByte();
								green = dis.readByte();
								red = dis.readByte();
								rawData[rawDataIndex++] = red;
								rawData[rawDataIndex++] = green;
								rawData[rawDataIndex++] = blue;
								if( createAlpha ){
									rawData[rawDataIndex++] = (byte) 255;
								}
							}
						}
					}
				}
			} else if( pixelDepth == 16 ){
				byte[] data = new byte[ 2 ];
				float scalar = 255f / 31f;
				for( int i = 0; i <= ( height - 1 ); i++ ){
					if( !flip ){
						rawDataIndex = ( height - 1 - i ) * width * dl;
					}
					for( int j = 0; j < width; j++ ){
						// Get the number of pixels the next chunk covers (either packed or unpacked)
						int count = dis.readByte();
						if( ( count & 0x80 ) != 0 ){
							// Its an RLE packed block - use the following 1 pixel for the next <count> pixels
							count &= 0x07f;
							j += count;
							data[1] = dis.readByte();
							data[0] = dis.readByte();
							blue = (byte) (int) ( getBitsAsByte( data, 1, 5 ) * scalar );
							green = (byte) (int) ( getBitsAsByte( data, 6, 5 ) * scalar );
							red = (byte) (int) ( getBitsAsByte( data, 11, 5 ) * scalar );
							while( count-- >= 0 ){
								rawData[rawDataIndex++] = red;
								rawData[rawDataIndex++] = green;
								rawData[rawDataIndex++] = blue;
								if( createAlpha ){
									rawData[rawDataIndex++] = (byte) 255;
								}
							}
						} else{
							// Its not RLE packed, but the next <count> pixels are raw.
							j += count;
							while( count-- >= 0 ){
								data[1] = dis.readByte();
								data[0] = dis.readByte();
								blue = (byte) (int) ( getBitsAsByte( data, 1, 5 ) * scalar );
								green = (byte) (int) ( getBitsAsByte( data, 6, 5 ) * scalar );
								red = (byte) (int) ( getBitsAsByte( data, 11, 5 ) * scalar );
								rawData[rawDataIndex++] = red;
								rawData[rawDataIndex++] = green;
								rawData[rawDataIndex++] = blue;
								if( createAlpha ){
									rawData[rawDataIndex++] = (byte) 255;
								}
							}
						}
					}
				}
			} else{
				throw new IOException( "Unsupported TGA true color depth: " + pixelDepth );
			}

		} else if( imageType == TYPE_COLORMAPPED ){
			int bytesPerIndex = pixelDepth / 8;

			if (bytesPerIndex == 1) {
				for (int i = 0; i <= (height - 1); i++) {
					if (!flip)
						rawDataIndex = (height - 1 - i) * width * dl;
					for (int j = 0; j < width; j++) {
						int index = dis.readUnsignedByte();
						if (index >= cMapEntries.length || index < 0)
							throw new IOException("TGA: Invalid color map entry referenced: "+index);
						ColorMapEntry entry = cMapEntries[index];
						rawData[rawDataIndex++] = entry.red;
						rawData[rawDataIndex++] = entry.green;
						rawData[rawDataIndex++] = entry.blue;
						if (dl == 4) {
							rawData[rawDataIndex++] = entry.alpha;
						}

					}
				}
			} else if (bytesPerIndex == 2) {
				for (int i = 0; i <= (height - 1); i++) {
					if (!flip)
						rawDataIndex = (height - 1 - i) * width * dl;
					for (int j = 0; j < width; j++) {
						int index = flipEndian(dis.readShort());
						if (index >= cMapEntries.length || index < 0)
							throw new IOException("TGA: Invalid color map entry referenced: "+index);
						ColorMapEntry entry = cMapEntries[index];
						rawData[rawDataIndex++] = entry.red;
						rawData[rawDataIndex++] = entry.green;
						rawData[rawDataIndex++] = entry.blue;
						if (dl == 4) {
							rawData[rawDataIndex++] = entry.alpha;
						}
					}
				}
			} else {
				throw new IOException("TGA: unknown colormap indexing size used: "+bytesPerIndex);
			}
		}
		else  if (imageType == TYPE_BLACKANDWHITE) {
			byte red = 0;
			byte green = 0;
			byte blue = 0;
			byte alpha = 0;

			byte[] data = new byte[1];
			float scalar = 255f/31f;
			for (int i = 0; i <= (height - 1); i++) {
				if (!flip)
					rawDataIndex = (height - 1 - i) * width * dl;
				for (int j = 0; j < width; j++) {
					data[0] = dis.readByte();
					//TODO handle properly
					rawData[rawDataIndex++] = data[0];
				}
			}
		}
		else  if (imageType == TYPE_BLACKANDWHITE_RLE) {
			byte red = 0;
			byte green = 0;
			byte blue = 0;
			byte alpha = 0;

			for( int i = 0; i <= ( height - 1 ); ++i ){
				if( !flip ){
					rawDataIndex = ( height - 1 - i ) * width * dl;
				}

				for( int j = 0; j < width; ++j ){
					// Get the number of pixels the next chunk covers (either packed or unpacked)
					int count = dis.readByte();
					if( ( count & 0x80 ) != 0 ){
						// Its an RLE packed block - use the following 1 pixel for the next <count> pixels
						count &= 0x07f;
						j += count;
						blue = green = red = alpha = dis.readByte();
						while( count-- >= 0 ){
							rawData[rawDataIndex++] = red;
							//                                 rawData[rawDataIndex++] = green;
							//                                 rawData[rawDataIndex++] = blue;
							//                                 rawData[rawDataIndex++] = alpha;
						}
					} else{
						// Its not RLE packed, but the next <count> pixels are raw.
						j += count;
						while( count-- >= 0 ){
							blue = green = red = alpha = dis.readByte();
							//                                 green = dis.readByte();
							//                                 red = dis.readByte();
							//                                 alpha = dis.readByte();
							rawData[rawDataIndex++] = red;
							//                                 rawData[rawDataIndex++] = green;
							//                                 rawData[rawDataIndex++] = blue;
							//                                 rawData[rawDataIndex++] = alpha;
						}
					}
				}
			}
		}
		else
		{
			throw new IOException("TGA: Monochrome and Whiteblack not supported image type = " + imageType);
		}


		fis.close();

		BufferedImage  bufferedImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		System.out.println(String.format("PixelDepth %d", pixelDepth));

		if(imageType == TYPE_BLACKANDWHITE_RLE || imageType == TYPE_BLACKANDWHITE)
		{
			bufferedImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for(int j = 0; j < height; j++)
				for(int i = 0; i < width; i++) {
					int  index = ( (j)* width + i) * (pixelDepth/8);

//					int value = ((rawData[index + 0] & 0xFF) << 24)  | ((rawData[index + 0] & 0xFF) << 16)|
//							((rawData[index + 0] & 0xFF) <<  8)|
//							(rawData[index + 0] & 0xFF) ;	

					int value = ((rawData[index + 0] & 0xFF) << 24) ;
					
					bufferedImage.setRGB(i, j,value);
				}
		}
		else
		{
			if(dl ==4)
				bufferedImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			else
				bufferedImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for(int j = 0; j < height; j++)
				for(int i = 0; i < width; i++) {
					int index = ( (j)* width + i) * (pixelDepth/8);
					int value =  (rawData[index + 0] & 0xFF) << 16|
							(rawData[index + 1] & 0xFF) <<  8|
							(rawData[index + 2] & 0xFF) ;

					if(dl ==4 )
						value |= ((rawData[index + 3] & 0xFF) << 24);
					
					bufferedImage.setRGB(i, j,value);

				}
		}
		return bufferedImage;
	}

	private static byte getBitsAsByte(byte[] data, int offset, int length) {
		int offsetBytes = offset / 8;
		int indexBits = offset % 8;
		int rVal = 0;

		// start at data[offsetBytes]...  spill into next byte as needed.
		for (int i = length; --i >=0;) {
			byte b = data[offsetBytes];
			int test = indexBits == 7 ? 1 : 2 << (6-indexBits);
			if ((b & test) != 0) {
				if (i == 0)
					rVal++;
				else
					rVal += (2 << i-1);
			}
			indexBits++;
			if (indexBits == 8) {
				indexBits = 0;
				offsetBytes++;
			}
		}

		return (byte)rVal;
	}

	/**
	 * <code>flipEndian</code> is used to flip the endian bit of the header
	 * file.
	 *
	 * @param signedShort
	 *            the bit to flip.
	 * @return the flipped bit.
	 */
	private static short flipEndian(short signedShort) {
		int input = signedShort & 0xFFFF;
		return (short) (input << 8 | (input & 0xFF00) >>> 8);
	}

	static class ColorMapEntry {
		byte red, green, blue, alpha;

		@Override
		public String toString() {
			return "entry: "+red+","+green+","+blue+","+alpha;
		}
	}

	public static ByteBuffer createByteBuffer(int size) {
		ByteBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
		buf.clear();
		return buf;
	}
}