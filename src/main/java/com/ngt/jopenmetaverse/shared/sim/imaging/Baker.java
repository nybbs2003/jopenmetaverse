package com.ngt.jopenmetaverse.shared.sim.imaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ngt.jopenmetaverse.shared.protocol.Helpers;
import com.ngt.jopenmetaverse.shared.sim.AppearanceManager;
import com.ngt.jopenmetaverse.shared.sim.AppearanceManager.AvatarTextureIndex;
import com.ngt.jopenmetaverse.shared.sim.AppearanceManager.BakeType;
import com.ngt.jopenmetaverse.shared.sim.Settings;
import com.ngt.jopenmetaverse.shared.sim.asset.AssetTexture;
import com.ngt.jopenmetaverse.shared.sim.imaging.ManagedImage.ImageChannels;
import com.ngt.jopenmetaverse.shared.sim.visual.VisualParams.VisualAlphaParam;
import com.ngt.jopenmetaverse.shared.types.Color4;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

/// <summary>
/// A set of textures that are layered on texture of each other and "baked"
/// in to a single texture, for avatar appearances
/// </summary>
public class Baker {
        //region Properties
        /// <summary>Final baked texture</summary>
        public AssetTexture getBakedTexture() { return bakedTexture; } 
        /// <summary>Component layers</summary>
        public List<AppearanceManager.TextureData> getTextures() { return textures; } 
        /// <summary>Width of the final baked image and scratchpad</summary>
        public int getBakeWidth() {return bakeWidth; } 
        /// <summary>Height of the final baked image and scratchpad</summary>
        public int getBakeHeight() {return bakeHeight; } 
        /// <summary>Bake type</summary>
        public BakeType getBakeType() {return bakeType; } 
        /// <summary>Is this one of the 3 skin bakes</summary>
        private boolean isSkin() {return bakeType == BakeType.Head || bakeType == BakeType.LowerBody || bakeType == BakeType.UpperBody; } 
        //endregion

        //region Private fields
        /// <summary>Final baked texture</summary>
        private AssetTexture bakedTexture;
        /// <summary>Component layers</summary>
        private List<AppearanceManager.TextureData> textures = new ArrayList<AppearanceManager.TextureData>();
        /// <summary>Width of the final baked image and scratchpad</summary>
        private int bakeWidth;
        /// <summary>Height of the final baked image and scratchpad</summary>
        private int bakeHeight;
        /// <summary>Bake type</summary>
        private BakeType bakeType;
        //endregion

        //region Constructor
        /// <summary>
        /// Default constructor
        /// </summary>
        /// <param name="bakeType">Bake type</param>
        public Baker(BakeType bakeType)
        {
            this.bakeType = bakeType;

            if (bakeType == BakeType.Eyes)
            {
                bakeWidth = 128;
                bakeHeight = 128;
            }
            else
            {
                bakeWidth = 512;
                bakeHeight = 512;
            }
        }
        //endregion

        //region Public methods
        /// <summary>
        /// Adds layer for baking
        /// </summary>
        /// <param name="tdata">TexturaData struct that contains texture and its params</param>
        public void AddTexture(AppearanceManager.TextureData tdata)
        {
            synchronized (textures)
            {
                textures.add(tdata);
            }
        }

        public void Bake() throws IOException
        {
            bakedTexture = new AssetTexture(new ManagedImage(bakeWidth, bakeHeight,
            		ImageChannels.get(ManagedImage.ImageChannels.Color.getIndex() 
            				| ManagedImage.ImageChannels.Alpha.getIndex() 
            				| ManagedImage.ImageChannels.Bump.getIndex())));

            // Base color for eye bake is white, color of layer0 for others
            if (bakeType == BakeType.Eyes)
            {
                InitBakedLayerColor(Color4.White);
            }
            else if (textures.size() > 0)
            {
                InitBakedLayerColor(textures.get(0).Color);
            }

            // Do we have skin texture?
            boolean SkinTexture = textures.size() > 0 && textures.get(0).Texture != null;

            if (bakeType == BakeType.Head)
            {
                DrawLayer(LoadResourceLayer("head_color.tga"), false);
                AddAlpha(bakedTexture.Image, LoadResourceLayer("head_alpha.tga"));
                MultiplyLayerFromAlpha(bakedTexture.Image, LoadResourceLayer("head_skingrain.tga"));
            }

            if (!SkinTexture && bakeType == BakeType.UpperBody)
            {
                DrawLayer(LoadResourceLayer("upperbody_color.tga"), false);
            }

            if (!SkinTexture && bakeType == BakeType.LowerBody)
            {
                DrawLayer(LoadResourceLayer("lowerbody_color.tga"), false);
            }

            ManagedImage alphaWearableTexture = null;

            // Layer each texture on top of one other, applying alpha masks as we go
            for (int i = 0; i < textures.size(); i++)
            {
                // Skip if we have no texture on this layer
                if (textures.get(i).Texture == null) continue;

                // Is this Alpha wearable and does it have an alpha channel?
                if ((textures.get(i).TextureIndex.getIndex() >= AvatarTextureIndex.LowerAlpha.getIndex()) &&
                    (textures.get(i).TextureIndex.getIndex() <= AvatarTextureIndex.HairAlpha.getIndex()))
                {
                    if (textures.get(i).Texture.Image.Alpha != null)
                    {
                        alphaWearableTexture = textures.get(i).Texture.Image.Clone();
                    }
                    continue;
                }

                // Don't draw skin on head bake first
                // For head bake skin texture is drawn last, go figure
                if (bakeType == BakeType.Head && i == 0) continue;

                ManagedImage texture = textures.get(i).Texture.Image.Clone();
                //File.WriteAllBytes(bakeType + "-texture-layer-" + i + ".tga", texture.ExportTGA());

                // Resize texture to the size of baked layer
                // FIXME: if texture is smaller than the layer, don't stretch it, tile it
                if (texture.Width != bakeWidth || texture.Height != bakeHeight)
                {
                    try { texture.ResizeNearestNeighbor(bakeWidth, bakeHeight); }
                    catch (Exception e) { JLogger.warn(Utils.getExceptionStackTraceAsString(e));continue; }
                }

                // Special case for hair layer for the head bake
                // If we don't have skin texture, we discard hair alpha
                // and apply hair pattern over the texture
                if (!SkinTexture && bakeType == BakeType.Head && i == 1)
                {
                    if (texture.Alpha != null)
                    {
                        for (int j = 0; j < texture.Alpha.length; j++) texture.Alpha[j] = (byte)255;
                    }
                    MultiplyLayerFromAlpha(texture, LoadResourceLayer("head_hair.tga"));
                }

                // Aply tint and alpha masks except for skin that has a texture
                // on layer 0 which always overrides other skin settings
                if (!(isSkin() && i == 0))
                {
                    ApplyTint(texture, textures.get(i).Color);

                    // For hair bake, we skip all alpha masks
                    // and use one from the texture, for both
                    // alpha and morph layers
                    if (bakeType == BakeType.Hair)
                    {
                        if (texture.Alpha != null)
                        {
                            bakedTexture.Image.Bump = texture.Alpha;
                        }
                        else
                        {
                            for (int j = 0; j < bakedTexture.Image.Bump.length; j++) 
                            	bakedTexture.Image.Bump[j] = (byte)Utils.UByteMaxValue;
                        }
                    }
                    // Apply parametrized alpha masks
                    else if (textures.get(i).AlphaMasks != null && textures.get(i).AlphaMasks.size() > 0)
                    {
                        // Combined mask for the layer, fully transparent to begin with
                        ManagedImage combinedMask = new ManagedImage(bakeWidth, bakeHeight, ImageChannels.get(ManagedImage.ImageChannels.Alpha.getIndex()));

                        int addedMasks = 0;

                        // First add mask in normal blend mode
                        for (Entry<VisualAlphaParam, Float> kvp : textures.get(i).AlphaMasks.entrySet())
                        {
                            if (!MaskBelongsToBake(kvp.getKey().TGAFile)) continue;

                            if (kvp.getKey().MultiplyBlend == false && (kvp.getValue() > 0f || !kvp.getKey().SkipIfZero))
                            {
                                ApplyAlpha(combinedMask, kvp.getKey(), kvp.getValue());
                                //File.WriteAllBytes(bakeType + "-layer-" + i + "-mask-" + addedMasks + ".tga", combinedMask.ExportTGA());
                                addedMasks++;
                            }
                        }

                        // If there were no mask in normal blend mode make aplha fully opaque
                        if (addedMasks == 0) 
                        	for (int l = 0; l < combinedMask.Alpha.length; l++) 
                        		combinedMask.Alpha[l] = (byte)Utils.UByteMaxValue;

                        // Add masks in multiply blend mode
                        for (Entry<VisualAlphaParam, Float> kvp : textures.get(i).AlphaMasks.entrySet())
                        {
                            if (!MaskBelongsToBake(kvp.getKey().TGAFile)) continue;

                            if (kvp.getKey().MultiplyBlend == true && (kvp.getValue() > 0f || !kvp.getKey().SkipIfZero))
                            {
                                ApplyAlpha(combinedMask, kvp.getKey(), kvp.getValue());
                                //File.WriteAllBytes(bakeType + "-layer-" + i + "-mask-" + addedMasks + ".tga", combinedMask.ExportTGA());
                                addedMasks++;
                            }
                        }

                        if (addedMasks > 0)
                        {
                            // Apply combined alpha mask to the cloned texture
                            AddAlpha(texture, combinedMask);

                            // Is this layer used for morph mask? If it is, use its
                            // alpha as the morth for the whole bake
                            if (textures.get(i).TextureIndex == AppearanceManager.MorphLayerForBakeType(bakeType))
                            {
                                bakedTexture.Image.Bump = combinedMask.Alpha;
                            }
                        }
                        //File.WriteAllBytes(bakeType + "-masked-texture-" + i + ".tga", texture.ExportTGA());
                    }
                }

                boolean useAlpha = i == 0 && (bakeType == BakeType.Skirt || bakeType == BakeType.Hair);
                DrawLayer(texture, useAlpha);
                //File.WriteAllBytes(bakeType + "-layer-" + i + ".tga", texture.ExportTGA());
            }

            // For head, we add skin last
            if (SkinTexture && bakeType == BakeType.Head)
            {
                ManagedImage texture = textures.get(0).Texture.Image.Clone();
                if (texture.Width != bakeWidth || texture.Height != bakeHeight)
                {
                    try { texture.ResizeNearestNeighbor(bakeWidth, bakeHeight); }
                    catch (Exception e) {JLogger.warn(Utils.getExceptionStackTraceAsString(e)); }
                }
                DrawLayer(texture, false);
            }

            // Apply any alpha wearable textures to make parts of the avatar disappear
            if (alphaWearableTexture != null)
            {
                AddAlpha(bakedTexture.Image, alphaWearableTexture);
            }

            // We are done, encode asset for finalized bake
            bakedTexture.Encode();
            //File.WriteAllBytes(bakeType + ".tga", bakedTexture.Image.ExportTGA());
        }

        private static Object ResourceSync = new Object();

        public static ManagedImage LoadResourceLayer(String fileName)
        {
            try
            {
                IBitmap bitmap = null;
                synchronized (ResourceSync)
                {
                	String resourcePath = null;
                    if((resourcePath = Helpers.GetResourcePath(fileName, Settings.RESOURCE_DIR))!=null)
                    {
                        bitmap = LoadTGAClass.LoadTGA(resourcePath);
                    }
                }
                if (bitmap == null)
                {
                	JLogger.error(String.format("Failed loading resource file: %s", fileName));
                    return null;
                }
                else
                {
                    return new ManagedImage(bitmap);
                }
            }
            catch (Exception e)
            {
                JLogger.error(String.format("Failed loading resource file: %s (%s)", fileName, e.getMessage()));
                return null;
            }
        }

        /// <summary>
        /// Converts avatar texture index (face) to Bake type
        /// </summary>
        /// <param name="index">Face number (AvatarTextureIndex)</param>
        /// <returns>BakeType, layer to which this texture belongs to</returns>
        public static BakeType BakeTypeFor(AvatarTextureIndex index)
        {
            switch (index)
            {
                case HeadBodypaint:
                    return BakeType.Head;

                case UpperBodypaint:
                case UpperGloves:
                case UpperUndershirt:
                case UpperShirt:
                case UpperJacket:
                    return BakeType.UpperBody;

                case LowerBodypaint:
                case LowerUnderpants:
                case LowerSocks:
                case LowerShoes:
                case LowerPants:
                case LowerJacket:
                    return BakeType.LowerBody;

                case EyesIris:
                    return BakeType.Eyes;

                case Skirt:
                    return BakeType.Skirt;

                case Hair:
                    return BakeType.Hair;

                default:
                    return BakeType.Unknown;
            }
        }
        //endregion

        //region Private layer compositing methods

        private boolean MaskBelongsToBake(String mask)
        {
            if ((bakeType == BakeType.LowerBody && mask.contains("upper"))
                || (bakeType == BakeType.LowerBody && mask.contains("shirt"))
                || (bakeType == BakeType.UpperBody && mask.contains("lower")))
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        private boolean DrawLayer(ManagedImage source, boolean addSourceAlpha)
        {
            if (source == null) return false;

            boolean sourceHasColor;
            boolean sourceHasAlpha;
            boolean sourceHasBump;
            int i = 0;

            sourceHasColor = ((ImageChannels.and(source.Channels , ManagedImage.ImageChannels.Color)) != 0 &&
                    source.Red != null && source.Green != null && source.Blue != null);
            sourceHasAlpha = ((ImageChannels.and(source.Channels, ManagedImage.ImageChannels.Alpha)) != 0 && source.Alpha != null);
            sourceHasBump = ((ImageChannels.and(source.Channels , ManagedImage.ImageChannels.Bump)) != 0 && source.Bump != null);

            addSourceAlpha = (addSourceAlpha && sourceHasAlpha);

            byte alpha = (byte)Utils.UByteMaxValue;
            byte alphaInv = (byte)(Utils.UByteMaxValue - alpha);

            byte[] bakedRed = bakedTexture.Image.Red;
            byte[] bakedGreen = bakedTexture.Image.Green;
            byte[] bakedBlue = bakedTexture.Image.Blue;
            byte[] bakedAlpha = bakedTexture.Image.Alpha;
            byte[] bakedBump = bakedTexture.Image.Bump;

            byte[] sourceRed = source.Red;
            byte[] sourceGreen = source.Green;
            byte[] sourceBlue = source.Blue;
            byte[] sourceAlpha = sourceHasAlpha ? source.Alpha : null;
            byte[] sourceBump = sourceHasBump ? source.Bump : null;

            for (int y = 0; y < bakeHeight; y++)
            {
                for (int x = 0; x < bakeWidth; x++)
                {
                    if (sourceHasAlpha)
                    {
                        alpha = sourceAlpha[i];
                        alphaInv = (byte)(Utils.UByteMaxValue - alpha);
                    }

                    if (sourceHasColor)
                    {
                        bakedRed[i] = (byte)((bakedRed[i] * alphaInv + sourceRed[i] * alpha) >> 8);
                        bakedGreen[i] = (byte)((bakedGreen[i] * alphaInv + sourceGreen[i] * alpha) >> 8);
                        bakedBlue[i] = (byte)((bakedBlue[i] * alphaInv + sourceBlue[i] * alpha) >> 8);
                    }

                    if (addSourceAlpha)
                    {
                        if (sourceAlpha[i] < bakedAlpha[i])
                        {
                            bakedAlpha[i] = sourceAlpha[i];
                        }
                    }

                    if (sourceHasBump)
                        bakedBump[i] = sourceBump[i];

                    ++i;
                }
            }

            return true;
        }

        /// <summary>
        /// Make sure images exist, resize source if needed to match the destination
        /// </summary>
        /// <param name="dest">Destination image</param>
        /// <param name="src">Source image</param>
        /// <returns>Sanitization was succefull</returns>
        private boolean SanitizeLayers(ManagedImage dest, ManagedImage src)
        {
            if (dest == null || src == null) return false;

            if ((ImageChannels.and(dest.Channels, ManagedImage.ImageChannels.Alpha)) == 0)
            {
                dest.ConvertChannels(ImageChannels.get(ImageChannels.or(dest.Channels, ManagedImage.ImageChannels.Alpha)));
            }

            if (dest.Width != src.Width || dest.Height != src.Height)
            {
                try { src.ResizeNearestNeighbor(dest.Width, dest.Height); }
                catch (Exception e) { return false; }
            }

            return true;
        }


        private void ApplyAlpha(ManagedImage dest, VisualAlphaParam param, float val)
        {
            ManagedImage src = LoadResourceLayer(param.TGAFile);

            if (dest == null || src == null || src.Alpha == null) return;

            if ((ImageChannels.and(dest.Channels, ManagedImage.ImageChannels.Alpha)) == 0)
            {
                dest.ConvertChannels(ImageChannels.get(ImageChannels.or(dest.Channels, ManagedImage.ImageChannels.Alpha)));
            }

            if (dest.Width != src.Width || dest.Height != src.Height)
            {
                try { src.ResizeNearestNeighbor(dest.Width, dest.Height); }
                catch (Exception e) { JLogger.warn(Utils.getExceptionStackTraceAsString(e));return; }
            }

            for (int i = 0; i < dest.Alpha.length; i++)
            {
                byte alpha = src.Alpha[i] <= ((1 - val) * 255) ? (byte)0 : (byte)255;

                if (param.MultiplyBlend)
                {
                    dest.Alpha[i] = (byte)((dest.Alpha[i] * alpha) >> 8);
                }
                else
                {
                    if (alpha > dest.Alpha[i])
                    {
                        dest.Alpha[i] = alpha;
                    }
                }
            }
        }

        private void AddAlpha(ManagedImage dest, ManagedImage src)
        {
            if (!SanitizeLayers(dest, src)) return;

            for (int i = 0; i < dest.Alpha.length; i++)
            {
                if (src.Alpha[i] < dest.Alpha[i])
                {
                    dest.Alpha[i] = src.Alpha[i];
                }
            }
        }

        private void MultiplyLayerFromAlpha(ManagedImage dest, ManagedImage src)
        {
            if (!SanitizeLayers(dest, src)) return;

            for (int i = 0; i < dest.Red.length; i++)
            {
                dest.Red[i] = (byte)((dest.Red[i] * src.Alpha[i]) >> 8);
                dest.Green[i] = (byte)((dest.Green[i] * src.Alpha[i]) >> 8);
                dest.Blue[i] = (byte)((dest.Blue[i] * src.Alpha[i]) >> 8);
            }
        }

        private void ApplyTint(ManagedImage dest, Color4 src)
        {
            if (dest == null) return;

            for (int i = 0; i < dest.Red.length; i++)
            {
                dest.Red[i] = (byte)((dest.Red[i] * Utils.floatToByte(src.R, 0f, 1f)) >> 8);
                dest.Green[i] = (byte)((dest.Green[i] * Utils.floatToByte(src.G, 0f, 1f)) >> 8);
                dest.Blue[i] = (byte)((dest.Blue[i] * Utils.floatToByte(src.B, 0f, 1f)) >> 8);
            }
        }

        /// <summary>
        /// Fills a baked layer as a solid *appearing* color. The colors are 
        /// subtly dithered on a 16x16 grid to prevent the JPEG2000 stage from 
        /// compressing it too far since it seems to cause upload failures if 
        /// the image is a pure solid color
        /// </summary>
        /// <param name="color">Color of the base of this layer</param>
        private void InitBakedLayerColor(Color4 color)
        {
            InitBakedLayerColor(color.R, color.G, color.B);
        }

        /// <summary>
        /// Fills a baked layer as a solid *appearing* color. The colors are 
        /// subtly dithered on a 16x16 grid to prevent the JPEG2000 stage from 
        /// compressing it too far since it seems to cause upload failures if 
        /// the image is a pure solid color
        /// </summary>
        /// <param name="r">Red value</param>
        /// <param name="g">Green value</param>
        /// <param name="b">Blue value</param>
        private void InitBakedLayerColor(float r, float g, float b)
        {
            byte rByte = Utils.floatToByte(r, 0f, 1f);
            byte gByte = Utils.floatToByte(g, 0f, 1f);
            byte bByte = Utils.floatToByte(b, 0f, 1f);

            byte rAlt, gAlt, bAlt;

            rAlt = rByte;
            gAlt = gByte;
            bAlt = bByte;

            if (rByte < Utils.UByteMaxValue)
                rAlt++;
            else rAlt--;

            if (gByte < Utils.UByteMaxValue)
                gAlt++;
            else gAlt--;

            if (bByte < Utils.UByteMaxValue)
                bAlt++;
            else bAlt--;

            int i = 0;

            byte[] red = bakedTexture.Image.Red;
            byte[] green = bakedTexture.Image.Green;
            byte[] blue = bakedTexture.Image.Blue;
            byte[] alpha = bakedTexture.Image.Alpha;
            byte[] bump = bakedTexture.Image.Bump;

            for (int y = 0; y < bakeHeight; y++)
            {
                for (int x = 0; x < bakeWidth; x++)
                {
                    if (((x ^ y) & 0x10) == 0)
                    {
                        red[i] = rAlt;
                        green[i] = gByte;
                        blue[i] = bByte;
                        alpha[i] = (byte)Utils.UByteMaxValue;
                        bump[i] = 0;
                    }
                    else
                    {
                        red[i] = rByte;
                        green[i] = gAlt;
                        blue[i] = bAlt;
                        alpha[i] = (byte)Utils.UByteMaxValue;
                        bump[i] = 0;
                    }

                    ++i;
                }
            }

        }
        //endregion
}
