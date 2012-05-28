package com.ngt.jopenmetaverse.shared.protocol.primitives;

import com.ngt.jopenmetaverse.shared.protocol.primitives.Enums.TextureAnimMode;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.util.Utils;



  /// <summary>
        /// Controls the texture animation of a particular prim
        /// </summary>
        public class TextureAnimation
        {
            /// <summary></summary>
            public TextureAnimMode Flags;
            /// <summary></summary>
            public long Face;
            /// <summary></summary>
            public long SizeX;
            /// <summary></summary>
            public long SizeY;
            /// <summary></summary>
            public float Start;
            /// <summary></summary>
            public float Length;
            /// <summary></summary>
            public float Rate;


            public TextureAnimation()
            {
            	this(new byte[0], 0);
            }
            
            /// <summary>
            /// 
            /// </summary>
            /// <param name="data"></param>
            /// <param name="pos"></param>
            public TextureAnimation(byte[] data, int pos)
            {
                if (data.length >= 16)
                {
                    Flags = TextureAnimMode.get((long)data[pos++]);
                    Face = (long)data[pos++];
                    SizeX = (long)data[pos++];
                    SizeY = (long)data[pos++];

                    Start = Utils.bytesToFloat(data, pos);
                    Length = Utils.bytesToFloat(data, pos + 4);
                    Rate = Utils.bytesToFloat(data, pos + 8);
                }
                else
                {
                    Flags = TextureAnimMode.get(0L);
                    Face = 0;
                    SizeX = 0;
                    SizeY = 0;

                    Start = 0.0f;
                    Length = 0.0f;
                    Rate = 0.0f;
                }
            }

            /// <summary>
            /// 
            /// </summary>
            /// <returns></returns>
            public byte[] GetBytes()
            {
                byte[] data = new byte[16];
                int pos = 0;

                data[pos++] = (byte)Flags.getIndex();
                data[pos++] = (byte)Face;
                data[pos++] = (byte)SizeX;
                data[pos++] = (byte)SizeY;

//                Utils.floatToBytes(Start).CopyTo(data, pos);
//                Utils.floatToBytes(Length).CopyTo(data, pos + 4);
//                Utils.floatToBytes(Rate).CopyTo(data, pos + 4);

                byte[] startBytes = Utils.floatToBytes(Start);
                byte[] lengthBytes = Utils.floatToBytes(Length);
                byte[] rateBytes = Utils.floatToBytes(Rate);
                
                System.arraycopy(startBytes, 0, data, pos, startBytes.length);
                System.arraycopy(lengthBytes, 0, data, pos+4, startBytes.length);
                System.arraycopy(rateBytes, 0, data, pos+8, startBytes.length);
                
                return data;
            }

            public OSD GetOSD()
            {
                OSDMap map = new OSDMap();

                map.put("face", OSD.FromInteger((int)Face));
                map.put("flags", OSD.FromInteger((int)Flags.getIndex()));
                map.put("length", OSD.FromReal(Length));
                map.put("rate", OSD.FromReal(Rate));
                map.put("size_x", OSD.FromInteger((int)SizeX));
                map.put("size_y", OSD.FromInteger((int)SizeY));
                map.put("start", OSD.FromReal(Start));

                return map;
            }

            public static TextureAnimation FromOSD(OSD osd)
            {
                TextureAnimation anim = new TextureAnimation();
                OSDMap map = (OSDMap) osd;

                if (map != null)
                {
                    anim.Face = map.get("face").asLong();
                    anim.Flags = TextureAnimMode.get(map.get("flags").asLong());
                    anim.Length = (float)map.get("length").asReal();
                    anim.Rate = (float)map.get("rate").asReal();
                    anim.SizeX = map.get("size_x").asLong();
                    anim.SizeY = map.get("size_y").asLong();
                    anim.Start = (float)map.get("start").asReal();
                }

                return anim;
            }
        }