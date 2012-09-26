package com.ngt.jopenmetaverse.shared.sim.imaging;

import com.ngt.jopenmetaverse.shared.util.Utils;

public  class ImageUtils
{
	/// <summary>
	/// Performs bilinear interpolation between four values
	/// </summary>
	/// <param name="v00">First, or top left value</param>
	/// <param name="v01">Second, or top right value</param>
	/// <param name="v10">Third, or bottom left value</param>
	/// <param name="v11">Fourth, or bottom right value</param>
	/// <param name="xPercent">Interpolation value on the X axis, between 0.0 and 1.0</param>
	/// <param name="yPercent">Interpolation value on fht Y axis, between 0.0 and 1.0</param>
	/// <returns>The bilinearly interpolated result</returns>
	public static float Bilinear(float v00, float v01, float v10, float v11, float xPercent, float yPercent)
	{
		return Utils.lerp(Utils.lerp(v00, v01, xPercent), Utils.lerp(v10, v11, xPercent), yPercent);
	}
	
	public static IBitmap createImageWithSolidColor(int width, int height, int r, int g, int b, int a)
	{
		//FIXME need to do in an optimized way
		IBitmap bitmap = BitmapFactory.getIntance().getNewIntance(width, height, PixelFormat.Format64bppArgb);
		return bitmap.createImageWithSolidColor(width, height, r, g, b, a);
	}
	
}