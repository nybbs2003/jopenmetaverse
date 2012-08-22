package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormatImpl;

/**
 * <p>The image metadata format for a TGA image type.  At this time there are
 * no elements in the format (i.e. {@link javax.imageio.metadata.IIOMetadataFormat#canNodeAppear(java.lang.String, javax.imageio.ImageTypeSpecifier)}
 * always returns <code>false</code>).</p>
 * 
 * @author Rob Grzywinski <a href="mailto:rgrzywinski@realityinteractive.com">rgrzywinski@realityinteractive.com</a>
 * @version $Id: TGAImageMetadataFormat.java,v 1.1 2005/04/12 11:23:53 ornedan Exp $
 * @since 1.0
 */
// NOTE:  this is currently unused
public class TGAImageMetadataFormat extends IIOMetadataFormatImpl
{
    /**
     * <p>The singleton instance of this {@linkjavax.imageio.metadata.IIOMetadataFormat}.
     * It is created lazily.</p> 
     */
    private static TGAImageMetadataFormat instance;

    // =========================================================================
    /**
     * <p>A private constructor to enforce the singleton pattern.</p>
     */
    private TGAImageMetadataFormat()
    {
        // set the name of the root document node.  The child elements may
        // repeat
        super(TGAImageReaderSpi.NATIVE_IMAGE_METADATA_FORMAT_NAME,
              CHILD_POLICY_REPEAT);
        
        // TODO:  add the full metadata
    }

    /**
     * <p>Retrieves the singleton instance of <code>TGAMetadataformat</code>.
     * The instance is created lazily.</p>
     * 
     * @return the singleton instnace 
     */
    public static synchronized TGAImageMetadataFormat getInstance()
    {
        // if the instance doesn't already exist then create it
        if(instance == null)
        {
            instance = new TGAImageMetadataFormat();
        } /* else -- there is a singleton instance */

        return instance;
    }

    // =========================================================================
    /**
     * @see javax.imageio.metadata.IIOMetadataFormat#canNodeAppear(java.lang.String, javax.imageio.ImageTypeSpecifier)
     */
    public boolean canNodeAppear(final String elementName,
                                 final ImageTypeSpecifier imageType)
    {
        // NOTE:  since there are no elements, none are allowed
        return false;
    }
}
