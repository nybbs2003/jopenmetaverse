package com.ngt.jopenmetaverse.shared.protocol.primitives;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.ngt.jopenmetaverse.shared.protocol.Helpers;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDArray;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.types.Color4;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;



 public class TextureEntry
        {
            public final int MAX_FACES = 32;
            public static final UUID WHITE_TEXTURE = new UUID("5748decc-f629-461c-9a36-a35a221fe21f");

            /// <summary></summary>
            public TextureEntryFace DefaultTexture;
            /// <summary></summary>
            public TextureEntryFace[] FaceTextures = new TextureEntryFace[MAX_FACES];

            /// <summary>
            /// Constructor that takes a default texture UUID
            /// </summary>
            /// <param name="defaultTextureID">Texture UUID to use as the default texture</param>
            public TextureEntry(UUID defaultTextureID)
            {
                DefaultTexture = new TextureEntryFace(null);
                DefaultTexture.setTextureID(defaultTextureID);
            }

            /// <summary>
            /// Constructor that takes a <code>TextureEntryFace</code> for the
            /// default face
            /// </summary>
            /// <param name="defaultFace">Face to use as the default face</param>
            public TextureEntry(TextureEntryFace defaultFace)
            {
                DefaultTexture = new TextureEntryFace(null);
                DefaultTexture.setBump(defaultFace.getBump());
                DefaultTexture.setFullbright(defaultFace.getFullbright());
                DefaultTexture.setMediaFlags(defaultFace.getMediaFlags());
                DefaultTexture.setOffsetU(defaultFace.getOffsetU());
                DefaultTexture.setOffsetV(defaultFace.getOffsetV());
                DefaultTexture.setRepeatU(defaultFace.getRepeatU());
                DefaultTexture.setRepeatV(defaultFace.getRepeatV());
                DefaultTexture.setRGBA(defaultFace.getRGBA());
                DefaultTexture.setRotation(defaultFace.getRotation());
                DefaultTexture.setGlow(defaultFace.getGlow());
                DefaultTexture.setShiny(defaultFace.getShiny());
                DefaultTexture.setTexMapType(defaultFace.getTexMapType());
                DefaultTexture.setTextureID(defaultFace.getTextureID());
            }

            /// <summary>
            /// Constructor that creates the TextureEntry class from a byte array
            /// </summary>
            /// <param name="data">Byte array containing the TextureEntry field</param>
            /// <param name="pos">Starting position of the TextureEntry field in 
            /// the byte array</param>
            /// <param name="length">Length of the TextureEntry field, in bytes</param>
            public TextureEntry(byte[] data, int pos, int length) throws Exception
            {
                FromBytes(data, pos, length);
            }

            /// <summary>
            /// This will either create a new face if a custom face for the given
            /// index is not defined, or return the custom face for that index if
            /// it already exists
            /// </summary>
            /// <param name="index">The index number of the face to create or 
            /// retrieve</param>
            /// <returns>A TextureEntryFace containing all the properties for that
            /// face</returns>
            public TextureEntryFace CreateFace(int index) throws Exception
            {
                if (index >= MAX_FACES) throw new Exception(index + " is outside the range of MAX_FACES");

                if (FaceTextures[index] == null)
                    FaceTextures[index] = new TextureEntryFace(this.DefaultTexture);

                return FaceTextures[index];
            }

            /// <summary>
            /// 
            /// </summary>
            /// <param name="index"></param>
            /// <returns></returns>
            public TextureEntryFace GetFace(int index) throws Exception
            {
                if (index >= MAX_FACES) throw new Exception(index + " is outside the range of MAX_FACES");

                if (FaceTextures[index] != null)
                    return FaceTextures[index];
                else
                    return DefaultTexture;
            }

            /// <summary>
            /// 
            /// </summary>
            /// <returns></returns>
            public OSD GetOSD()
            {
                OSDArray array = new OSDArray();

                // If DefaultTexture is null, assume the whole TextureEntry is empty
                if (DefaultTexture == null)
                    return array;

                // Otherwise, always add default texture
                array.add(DefaultTexture.GetOSD(-1));

                for (int i = 0; i < MAX_FACES; i++)
                {
                    if (FaceTextures[i] != null)
                        array.add(FaceTextures[i].GetOSD(i));
                }

                return array;
            }

            public static TextureEntry FromOSD(OSD osd)
            {
                if (osd.getType() == OSDType.Array)
                {
                    OSDArray array = (OSDArray)osd;
                    OSDMap faceSD;

                    if (array.count() > 0)
                    {
                        int[] faceNumber = new int[1];
                        faceSD = (OSDMap)array.get(0);
                        TextureEntryFace defaultFace = TextureEntryFace.FromOSD(faceSD, null, faceNumber);
                        TextureEntry te = new TextureEntry(defaultFace);

                        for (int i = 1; i < array.count(); i++)
                        {
                            TextureEntryFace tex = TextureEntryFace.FromOSD(array.get(i), defaultFace, faceNumber);
                            if (faceNumber[0] >= 0 && faceNumber[0] < te.FaceTextures.length)
                                te.FaceTextures[faceNumber[0]] = tex;
                        }

                        return te;
                    }
                }

                return new TextureEntry(UUID.Zero);
            }

            private void FromBytes(byte[] data, int pos, int length) throws Exception
            {
                if (length < 16)
                {
                    // No TextureEntry to process
                    DefaultTexture = null;
                    return;
                }
                else
                {
                    DefaultTexture = new TextureEntryFace(null);
                }

                long[] bitfieldSize = new long[] {0};
                long[] faceBits = new long[] {0};
                int[] i = new int[] {pos};

                //region Texture
                DefaultTexture.setTextureID(new UUID(data, i[0]));
                i[0] += 16;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    UUID tmpUUID = new UUID(data, i[0]);
                    i[0] += 16;

                    for (long face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace((int)face).setTextureID(tmpUUID);
                }
                //endregion Texture

                //region Color
                DefaultTexture.setRGBA(new Color4(data, i[0], true));
                i[0] += 4;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    Color4 tmpColor = new Color4(data, i[0], true);
                    i[0] += 4;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setRGBA(tmpColor);
                }
                //endregion Color

                //region RepeatU
                DefaultTexture.setRepeatU(Utils.bytesToFloat(data, i[0]));
                i[0] += 4;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    float tmpFloat = Utils.bytesToFloat(data, i[0]);
                    i[0] += 4;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setRepeatU(tmpFloat);
                }
                //endregion RepeatU

                //region RepeatV
                DefaultTexture.setRepeatV(Utils.bytesToFloat(data, i[0]));
                i[0] += 4;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    float tmpFloat = Utils.bytesToFloat(data, i[0]);
                    i[0] += 4;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setRepeatV(tmpFloat);
                }
                //endregion RepeatV

                //region OffsetU
                DefaultTexture.setOffsetU(Helpers.TEOffsetFloat(data, i[0]));
                i[0] += 2;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    float tmpFloat = Helpers.TEOffsetFloat(data, i[0]);
                    i[0] += 2;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setOffsetU(tmpFloat);
                }
                //endregion OffsetU

                //region OffsetV
                DefaultTexture.setOffsetV(Helpers.TEOffsetFloat(data, i[0]));
                i[0] += 2;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    float tmpFloat = Helpers.TEOffsetFloat(data, i[0]);
                    i[0] += 2;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setOffsetV(tmpFloat);
                }
                //endregion OffsetV

                //region Rotation
                DefaultTexture.setRotation(Helpers.TERotationFloat(data, i[0]));
                i[0] += 2;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    float tmpFloat = Helpers.TERotationFloat(data, i[0]);
                    i[0] += 2;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setRotation(tmpFloat);
                }
                //endregion Rotation

                //region Material
                DefaultTexture.setMaterial(data[i[0]]);
                i[0]++;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    byte tmpByte = data[i[0]];
                    i[0]++;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setMaterial(tmpByte);
                }
                //endregion Material

                //region Media
                DefaultTexture.setMedia(data[i[0]]);
                i[0]++;

                while (i[0]- pos < length && ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    byte tmpByte = data[i[0]];
                    i[0]++;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setMedia(tmpByte);
                }
                //endregion Media

                //region Glow
                DefaultTexture.setGlow(Helpers.TEGlowFloat(data, i[0]));
                i[0]++;

                while (ReadFaceBitfield(data, i, faceBits, bitfieldSize))
                {
                    float tmpFloat = Helpers.TEGlowFloat(data, i[0]);
                    i[0]++;

                    for (int face = 0, bit = 1; face < bitfieldSize[0]; face++, bit <<= 1)
                        if ((faceBits[0] & bit) != 0)
                            CreateFace(face).setGlow(tmpFloat);
                }
 	  	        //endregion Glow
            }

            /// <summary>
            /// 
            /// </summary>
            /// <returns></returns>
            public byte[] GetBytes() throws IOException
            {
                if (DefaultTexture == null)
                    return Utils.EmptyBytes;
                
                    	ByteArrayOutputStream binWriter = new ByteArrayOutputStream();
                        //region Bitfield Setup

                        long[] textures = new long[FaceTextures.length];
                        InitializeArray(textures);
                        long[] rgbas = new long[FaceTextures.length];
                        InitializeArray(rgbas);
                        long[] repeatus = new long[FaceTextures.length];
                        InitializeArray(repeatus);
                        long[] repeatvs = new long[FaceTextures.length];
                        InitializeArray(repeatvs);
                        long[] offsetus = new long[FaceTextures.length];
                        InitializeArray(offsetus);
                        long[] offsetvs = new long[FaceTextures.length];
                        InitializeArray(offsetvs);
                        long[] rotations = new long[FaceTextures.length];
                        InitializeArray(rotations);
                        long[] materials = new long[FaceTextures.length];
                        InitializeArray(materials);
                        long[] medias = new long[FaceTextures.length];
                        InitializeArray(medias);
                        long[] glows = new long[FaceTextures.length];
                        InitializeArray(glows);

                        for (int i = 0; i < FaceTextures.length; i++)
                        {
                            if (FaceTextures[i] == null) continue;

                            if (FaceTextures[i].getTextureID() != DefaultTexture.getTextureID())
                            {
                                if (textures[i] == Long.MAX_VALUE) textures[i] = 0;
                                textures[i] |= (long)(1 << i);
                            }
                            if (FaceTextures[i].getRGBA() != DefaultTexture.getRGBA())
                            {
                                if (rgbas[i] == Long.MAX_VALUE) rgbas[i] = 0;
                                rgbas[i] |= (long)(1 << i);
                            }
                            if (FaceTextures[i].getRepeatU() != DefaultTexture.getRepeatU())
                            {
                                if (repeatus[i] == Long.MAX_VALUE) repeatus[i] = 0;
                                repeatus[i] |= (long)(1 << i);
                            }
                            if (FaceTextures[i].getRepeatV() != DefaultTexture.getRepeatV())
                            {
                                if (repeatvs[i] == Long.MAX_VALUE) repeatvs[i] = 0;
                                repeatvs[i] |= (long)(1 << i);
                            }
                            if (Helpers.TEOffsetShort(FaceTextures[i].getOffsetU()) != Helpers.TEOffsetShort(DefaultTexture.getOffsetU()))
                            {
                                if (offsetus[i] == Long.MAX_VALUE) offsetus[i] = 0;
                                offsetus[i] |= (long)(1 << i);
                            }
                            if (Helpers.TEOffsetShort(FaceTextures[i].getOffsetV()) != Helpers.TEOffsetShort(DefaultTexture.getOffsetV()))
                            {
                                if (offsetvs[i] == Long.MAX_VALUE) offsetvs[i] = 0;
                                offsetvs[i] |= (long)(1 << i);
                            }
                            if (Helpers.TERotationShort(FaceTextures[i].getRotation()) != Helpers.TERotationShort(DefaultTexture.getRotation()))
                            {
                                if (rotations[i] == Long.MAX_VALUE) rotations[i] = 0;
                                rotations[i] |= (long)(1 << i);
                            }
                            if (FaceTextures[i].getMaterial() != DefaultTexture.getMaterial())
                            {
                                if (materials[i] == Long.MAX_VALUE) materials[i] = 0;
                                materials[i] |= (long)(1 << i);
                            }
                            if (FaceTextures[i].getMedia() != DefaultTexture.getMedia())
                            {
                                if (medias[i] == Long.MAX_VALUE) medias[i] = 0;
                                medias[i] |= (long)(1 << i);
                            }
                            if (Helpers.TEGlowByte(FaceTextures[i].getGlow()) != Helpers.TEGlowByte(DefaultTexture.getGlow()))
                            {
                                if (glows[i] == Long.MAX_VALUE) glows[i] = 0;
                                glows[i] |= (long)(1 << i);
                            }
                        }

                        //endregion Bitfield Setup

                        //region Texture
                        binWriter.write(DefaultTexture.getTextureID().GetBytes());
                        for (int i = 0; i < textures.length; i++)
                        {
                            if (textures[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(textures[i]));
                                binWriter.write(FaceTextures[i].getTextureID().GetBytes());
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion Texture

                        //region Color
                        // Serialize the color bytes inverted to optimize for zerocoding
                        binWriter.write(DefaultTexture.getRGBA().getBytes(true));
                        for (int i = 0; i < rgbas.length; i++)
                        {
                            if (rgbas[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(rgbas[i]));
                                // Serialize the color bytes inverted to optimize for zerocoding
                                binWriter.write(FaceTextures[i].getRGBA().getBytes(true));
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion Color

                        //region RepeatU
                        binWriter.write(Utils.floatToBytes(DefaultTexture.getRepeatU()));
                        for (int i = 0; i < repeatus.length; i++)
                        {
                            if (repeatus[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(repeatus[i]));
                                binWriter.write(Utils.floatToBytes(FaceTextures[i].getRepeatU()));
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion RepeatU

                        //region RepeatV
                        binWriter.write(Utils.floatToBytes(DefaultTexture.getRepeatV()));
                        for (int i = 0; i < repeatvs.length; i++)
                        {
                            if (repeatvs[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(repeatvs[i]));
                                binWriter.write(Utils.floatToBytes(FaceTextures[i].getRepeatV()));
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion RepeatV

                        //region OffsetU
                        binWriter.write(Helpers.TEOffsetShort(DefaultTexture.getOffsetU()));
                        for (int i = 0; i < offsetus.length; i++)
                        {
                            if (offsetus[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(offsetus[i]));
                                binWriter.write(Helpers.TEOffsetShort(FaceTextures[i].getOffsetU()));
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion OffsetU

                        //region OffsetV
                        binWriter.write(Helpers.TEOffsetShort(DefaultTexture.getOffsetV()));
                        for (int i = 0; i < offsetvs.length; i++)
                        {
                            if (offsetvs[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(offsetvs[i]));
                                binWriter.write(Helpers.TEOffsetShort(FaceTextures[i].getOffsetV()));
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion OffsetV

                        //region Rotation
                        binWriter.write(Helpers.TERotationShort(DefaultTexture.getRotation()));
                        for (int i = 0; i < rotations.length; i++)
                        {
                            if (rotations[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(rotations[i]));
                                binWriter.write(Helpers.TERotationShort(FaceTextures[i].getRotation()));
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion Rotation

                        //region Material
                        binWriter.write(DefaultTexture.getMaterial());
                        for (int i = 0; i < materials.length; i++)
                        {
                            if (materials[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(materials[i]));
                                binWriter.write(FaceTextures[i].getMaterial());
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion Material

                        //region Media
                        binWriter.write(DefaultTexture.getMedia());
                        for (int i = 0; i < medias.length; i++)
                        {
                            if (medias[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(medias[i]));
                                binWriter.write(FaceTextures[i].getMedia());
                            }
                        }
                        binWriter.write((byte)0);
                        //endregion Media

                        //region Glow
                        binWriter.write(Helpers.TEGlowByte(DefaultTexture.getGlow()));
                        for (int i = 0; i < glows.length; i++)
                        {
                            if (glows[i] != Long.MAX_VALUE)
                            {
                                binWriter.write(GetFaceBitfieldBytes(glows[i]));
                                binWriter.write(Helpers.TEGlowByte(FaceTextures[i].getGlow()));
                            }
                        }
                        //endregion Glow

                        return binWriter.toByteArray();
            }

            public int GetHashCode()
            {
                int hashCode = DefaultTexture != null ? DefaultTexture.GetHashCode() : 0;
                for (int i = 0; i < FaceTextures.length; i++)
                {
                    if (FaceTextures[i] != null)
                        hashCode ^= FaceTextures[i].GetHashCode();
                }
                return hashCode;
            }

            /// <summary>
            /// 
            /// </summary>
            /// <returns></returns>
            public String ToString()
            {
                String output = "";

                output += "Default Face: " + DefaultTexture.ToString() + "\n";

                for (int i = 0; i < FaceTextures.length; i++)
                {
                    if (FaceTextures[i] != null)
                        output += "Face " + i + ": " + FaceTextures[i].ToString() + "\n";
                }

                return output;
            }

            //region Helpers

            private void InitializeArray(long[] array)
            {
                for (int i = 0; i < array.length; i++)
                    array[i] = Long.MAX_VALUE;
            }

            private boolean ReadFaceBitfield(byte[] data, int[] pos, long[] faceBits, long[] bitfieldSize)
            {
                faceBits[0] = 0L;
                bitfieldSize[0] = 0;

                if (pos[0] >= data.length)
                    return false;

                byte b = 0;
                do
                {
                    b = data[pos[0]];
                    faceBits[0] = (faceBits[0] << 7) | (long)(b & 0x7F);
                    bitfieldSize[0] += 7;
                    pos[0]++;
                }
                while ((b & 0x80) != 0);

                return (faceBits[0] != 0);
            }

            private byte[] GetFaceBitfieldBytes(long bitfield)
            {
                int byteLength = 0;
                long tmpBitfield = bitfield;
                while (tmpBitfield != 0)
                {
                    tmpBitfield >>= 7;
                    byteLength++;
                }

                if (byteLength == 0)
                    return new byte[]{ 0 };

                byte[] bytes = new byte[byteLength];
                for (int i = 0; i < byteLength; i++)
                {
                    bytes[i] = (byte)((bitfield >> (7 * (byteLength - i - 1))) & 0x7F);
                    if (i < byteLength - 1)
                        bytes[i] |= 0x80;
                }
                return bytes;
            }

            //endregion Helpers
        }
      