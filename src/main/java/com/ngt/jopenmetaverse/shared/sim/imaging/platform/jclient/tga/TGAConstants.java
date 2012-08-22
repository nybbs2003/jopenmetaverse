package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga;


public interface TGAConstants
{
    // =========================================================================
    // image types
    /**
     * <p>An image type indicating no image data.</p>
     */
    int NO_IMAGE = 0;

    /**
     * <p>An image type indicating an uncompressed color mapped (indexed) image.</p>
     */
    int COLOR_MAP = 1;

    /**
     * <p>An image type indicating an uncompressed true-color image.</p>
     */
    int TRUE_COLOR = 2;

    /**
     * <p>An image type indicating a black and white (monochrome) image.</p>
     */
    int MONO = 3;

    /**
     * <p>An image type indicating an RLE (run-length encoded) color-mapped
     * (indexed) image.</p>
     */
    int RLE_COLOR_MAP = 9;

    /**
     * <p>An image type indicating an RLE (run-length encoded) true-color
     * image.</p>
     */
    int RLE_TRUE_COLOR = 10;

    /**
     * <p>An image type indicating an RLE (run-length encoded) black and white
     * (monochrome) image.</p>
     */
    int RLE_MONO = 11;

    // =========================================================================
    // Image descriptor bit
    /**
     * <p>The bit of the image descriptor field (5.5) indicating that the first
     * pixel should be at the left or the right.</p>
     */
    int LEFT_RIGHT_BIT = 0x10;

    /**
     * <p>The bit of the image descriptor field (5.5) indicating that the first
     * pixel should be at the bottom or the top.</p>
     */
    int BOTTOM_TOP_BIT = 0x20;
}
