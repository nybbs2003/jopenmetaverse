package com.ngt.jopenmetaverse.shared.protocol.primitives;

import java.util.EnumSet;

import com.ngt.jopenmetaverse.shared.protocol.primitives.Enums.Bumpiness;
import com.ngt.jopenmetaverse.shared.protocol.primitives.Enums.MappingType;
import com.ngt.jopenmetaverse.shared.protocol.primitives.Enums.Shininess;
import com.ngt.jopenmetaverse.shared.protocol.primitives.Enums.TextureAttributes;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDArray;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.types.Color4;
import com.ngt.jopenmetaverse.shared.types.UUID;

        //region Subclasses

        /// <summary>
        /// A single textured face. Don't instantiate this class yourself, use the
        /// methods in TextureEntry
        /// </summary>
        public class TextureEntryFace 
        {
            // +----------+ S = Shiny
            // | SSFBBBBB | F = Fullbright
            // | 76543210 | B = Bumpmap
            // +----------+
            private final byte BUMP_MASK = 0x1F;
            private final byte FULLBRIGHT_MASK = 0x20;
            private final byte SHINY_MASK = (byte)0xC0;
            // +----------+ M = Media Flags (web page)
            // | .....TTM | T = Texture Mapping
            // | 76543210 | . = Unused
            // +----------+
            private final byte MEDIA_MASK = 0x01;
            private final byte TEX_MAP_MASK = 0x06;

            private Color4 rgba;
            private float repeatU;
            private float repeatV;
            private float offsetU;
            private float offsetV;
            private float rotation;
            private float glow;
            private byte materialb;
            private byte mediab;
            private EnumSet<TextureAttributes> hasAttribute;
            private UUID textureID;
            private TextureEntryFace DefaultTexture;


            //region Properties

            /// <summary></summary>
            public byte getMaterial()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Material.getIndex()) != 0)
                        return materialb;
                    else
                        return DefaultTexture.getMaterial();
            }

            
            public void setMaterial(byte value)
            {
                    materialb = value;
                    hasAttribute = TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Material.getIndex());
            }
            
            /// <summary></summary>
            public byte getMedia()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Media.getIndex()) != 0)
                        return mediab;
                    else
                        return DefaultTexture.getMedia();
            }

            public void setMedia(byte value)
            {
                    mediab = value;
                    hasAttribute = TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Media.getIndex());
            }
            
            
            /// <summary></summary>
            public Color4 getRGBA()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.RGBA.getIndex()) != 0)
                        return rgba;
                    else
                        return DefaultTexture.getRGBA();
            }

            public void setRGBA(Color4 value)
            {
                    rgba = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.RGBA.getIndex());
            }
            
            /// <summary></summary>
            public float getRepeatU()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.RepeatU.getIndex()) != 0)
                        return repeatU;
                    else
                        return DefaultTexture.repeatU;
            }

            
            /// <summary></summary>
            public void setRepeatU(float value)
            {
                    repeatU = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.RepeatU.getIndex());
            }
            
            /// <summary></summary>
            public float getRepeatV()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.RepeatV.getIndex()) != 0)
                        return repeatV;
                    else
                        return DefaultTexture.repeatV;
            }

            
            public void setRepeatV(float value)
            {
                    repeatV = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.RepeatV.getIndex());
            }
            
            /// <summary></summary>
            public float getOffsetU()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.OffsetU.getIndex()) != 0)
                        return offsetU;
                    else
                        return DefaultTexture.offsetU;
 
            }

            public void setOffsetU(float value)
            {
                    offsetU = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.OffsetU.getIndex());
            }
            
            /// <summary></summary>
            public float getOffsetV()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.OffsetV.getIndex()) != 0)
                        return offsetV;
                    else
                        return DefaultTexture.offsetV;

            }

            public void setOffsetV(float value)
            {
                    offsetV = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.OffsetV.getIndex());
            }
            
            /// <summary></summary>
            public float getRotation()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Rotation.getIndex()) != 0)
                        return rotation;
                    else
                        return DefaultTexture.rotation;
            }

            public void setRotation(float value)
            {
                    rotation = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Rotation.getIndex());
            }
            
            /// <summary></summary>
            public float getGlow()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Glow.getIndex()) != 0)
                        return glow;
                    else
                        return DefaultTexture.glow;
            }

            public void setGlow(float value)
            {
                    glow = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Glow.getIndex());
            }
            
            /// <summary></summary>
            public Bumpiness getBump()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Material.getIndex()) != 0)
                        return Bumpiness.get((byte)(getMaterial() & BUMP_MASK));
                    else
                        return DefaultTexture.getBump();
            }

            public void setBump(Bumpiness value)
            {
                    // Clear out the old material value
                    setMaterial((byte)(getMaterial() & 0xE0));
                    // Put the new bump value in the material byte
                    setMaterial((byte)(TextureAttributes.getIndex(hasAttribute) | value.getIndex()));
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Material.getIndex());
            }
            
            public Shininess getShiny()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Material.getIndex()) != 0)
                        return Shininess.get((byte)(getMaterial() & SHINY_MASK));
                    else
                        return DefaultTexture.getShiny();
            }

            
            public void setShiny(Shininess value)
            {
                    // Clear out the old shiny value
                    setMaterial((byte)(getMaterial() & 0x3F));
                    // Put the new shiny value in the material byte
                    setMaterial( (byte) (TextureAttributes.getIndex(hasAttribute) | (byte)value.getIndex()) );
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Material.getIndex());
            }

            public boolean getFullbright()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Material.getIndex()) != 0)
                        return (getMaterial() & FULLBRIGHT_MASK) != 0;
                    else
                        return DefaultTexture.getFullbright();
            }
            
            public void setFullbright(boolean value)
            {
                    // Clear out the old fullbright value
            	 setMaterial((byte)(getMaterial() & 0xDF));
                    if (value)
                    {
                    	 setMaterial( (byte)(TextureAttributes.getIndex(hasAttribute) | 0x20));
                        hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Material.getIndex());
                    }
            }

            /// <summary>In the future this will specify whether a webpage is
            /// attached to this face</summary>
            public boolean getMediaFlags()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Media.getIndex()) != 0)
                        return (getMedia() & MEDIA_MASK) != 0;
                    else
                        return DefaultTexture.getMediaFlags();
            }

            public void setMediaFlags(boolean value)
            {
                    // Clear out the old mediaflags value
                    setMedia((byte)(getMedia() & 0xFE));
                    if (value)
                    {
                        setMedia((byte)(TextureAttributes.getIndex(hasAttribute) | 0x01));
                        hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Media.getIndex());
                    }
            }
            
            public MappingType getTexMapType()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.Media.getIndex()) != 0)
                        return MappingType.get((byte)(getMedia() & TEX_MAP_MASK));
                    else
                        return DefaultTexture.getTexMapType();
            }

            public void setTexMapType(MappingType value)
            {
                    // Clear out the old texmap value
            		setMedia((byte)(getMedia() & 0xF9));
                    // Put the new texmap value in the media byte
                    setMedia((byte)(TextureAttributes.getIndex(hasAttribute) | (byte)value.getIndex()));
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.Media.getIndex());
            }
            
            /// <summary></summary>
            public UUID getTextureID()
            {
                    if ((TextureAttributes.getIndex(hasAttribute) & TextureAttributes.TextureID.getIndex()) != 0)
                        return textureID;
                    else
                        return DefaultTexture.textureID;
            }

            public void setTextureID(UUID value)
            {
                    textureID = value;
                    hasAttribute =TextureAttributes.get(TextureAttributes.getIndex(hasAttribute) | TextureAttributes.TextureID.getIndex());
            }
            
            //endregion Properties

            /// <summary>
            /// Contains the definition for individual faces
            /// </summary>
            /// <param name="defaultTexture"></param>
            public TextureEntryFace(TextureEntryFace defaultTexture)
            {
                rgba = Color4.White;
                repeatU = 1.0f;
                repeatV = 1.0f;

                DefaultTexture = defaultTexture;
                if (DefaultTexture == null)
                    hasAttribute = TextureAttributes.get(TextureAttributes.All.getIndex());
                else
                    hasAttribute = TextureAttributes.get(TextureAttributes.None.getIndex());
            }

            public OSD GetOSD(int faceNumber)
            {
                OSDMap tex = new OSDMap(10);
                if (faceNumber >= 0) tex.put("face_number", OSD.FromInteger(faceNumber));
                tex.put("colors",  OSD.FromColor4(getRGBA()));
                tex.put("scales", OSD.FromReal(getRepeatU()));
                tex.put("scalet",  OSD.FromReal(getRepeatV()));
                tex.put("offsets",  OSD.FromReal(getOffsetU()));
                tex.put("offsett",  OSD.FromReal(getOffsetV()));
                tex.put("imagerot", OSD.FromReal(getRotation()));
                tex.put("bump", OSD.FromInteger((int)getBump().getIndex()));
                tex.put("shiny",  OSD.FromInteger((int)getShiny().getIndex()));
                tex.put("fullbright", OSD.FromBoolean(getFullbright()));
                tex.put("media_flags", OSD.FromInteger(getMediaFlags() ? 1 : 0));
                tex.put("mapping", OSD.FromInteger((int)getTexMapType().getIndex()));
                tex.put("glow", OSD.FromReal(getGlow()));

                if (getTextureID() != TextureEntry.WHITE_TEXTURE)
                    tex.put("imageid",  OSD.FromUUID(getTextureID()));
                else
                    tex.put("imageid", OSD.FromUUID(UUID.Zero));

                return tex;
            }

            public static TextureEntryFace FromOSD(OSD osd, TextureEntryFace defaultFace, int[] faceNumber)
            {
                OSDMap map = (OSDMap)osd;

                TextureEntryFace face = new TextureEntryFace(defaultFace);
                faceNumber[0] = (map.containsKey("face_number")) ? map.get("face_number").asInteger() : -1;
                Color4 rgba = face.getRGBA();
                rgba = ((OSDArray)map.get("colors")).asColor4();
                face.setRGBA(rgba);
                face.setRepeatU((float)map.get("scales").asReal());
                face.setRepeatV((float)map.get("scalet").asReal());
                face.setOffsetU((float)map.get("offsets").asReal());
                face.setOffsetV((float)map.get("offsett").asReal());
                face.setRotation((float)map.get("imagerot").asReal());
                face.setBump(Bumpiness.get((byte)map.get("bump").asInteger()));
                face.setShiny(Shininess.get((byte)map.get("shiny").asInteger()));
                face.setFullbright(map.get("fullbright").asBoolean());
                face.setMediaFlags(map.get("media_flags").asBoolean());
                face.setTexMapType(MappingType.get((byte)map.get("mapping").asInteger()));
                face.setGlow((float)map.get("glow").asReal());
                face.setTextureID(map.get("imageid").asUUID());

                return face;
            }

            public Object clone()
            {
                TextureEntryFace ret = new TextureEntryFace(this.DefaultTexture == null ? null : (TextureEntryFace)this.DefaultTexture.clone());
                ret.rgba = rgba;
                ret.repeatU = repeatU;
                ret.repeatV = repeatV;
                ret.offsetU = offsetU;
                ret.offsetV = offsetV;
                ret.rotation = rotation;
                ret.glow = glow;
                ret.materialb = materialb;
                ret.mediab = mediab;
                ret.hasAttribute = hasAttribute;
                ret.textureID = textureID;
                return ret;
            }

            public  int GetHashCode()
            {
                return
                    getRGBA().GetHashCode() ^
                    (new Float(getRepeatU()).hashCode()) ^
                    (new Float(getRepeatV()).hashCode()) ^
                    new Float(getOffsetU()).hashCode() ^
                    new Float(getOffsetV()).hashCode() ^
                    new Float(getRotation()).hashCode() ^
                    new Float(getGlow()).hashCode() ^
                    getBump().hashCode() ^
                    getShiny().hashCode() ^
                    new Boolean(getFullbright()).hashCode() ^
                    new Boolean(getMediaFlags()).hashCode() ^
                    getTexMapType().hashCode() ^
                    getTextureID().hashCode()
                    ;
                
            }

            /// <summary>
            /// 
            /// </summary>
            /// <returns></returns>
            public  String ToString()
            {
                return String.format("Color: %s RepeatU: %f RepeatV: %f OffsetU: %f OffsetV: %f " +
                    "Rotation: %f Bump: %s Shiny: %s Fullbright: %b Mapping: %s Media: %b Glow: %f ID: %s",
                    getRGBA().toString(), getRepeatU(), getRepeatV(), getOffsetU(), getOffsetV(), getRotation(), getBump().toString(), getShiny().toString(), getFullbright(), getTexMapType().toString(),
                    getMediaFlags(), getGlow(), getTextureID().toString());
            }
        }
