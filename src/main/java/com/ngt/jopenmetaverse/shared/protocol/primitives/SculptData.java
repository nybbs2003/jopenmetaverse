package com.ngt.jopenmetaverse.shared.protocol.primitives;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.types.EnumsPrimitive.SculptType;
import com.ngt.jopenmetaverse.shared.types.UUID;



  /// <summary>
    /// Information on the sculpt properties of a sculpted primitive
    /// </summary>
    public class SculptData
    {
        public UUID SculptTexture;
        private byte type;

        public SculptType getType()
        {
            return SculptType.get((byte) (type & 7));
        }

        public void setType(SculptType value)
        {
            type = (byte)value.getIndex();
        }
        
        /// <summary>
        /// Render inside out (inverts the normals).
        /// </summary>
        public boolean getInvert()
        {
            return ((type & (byte)SculptType.Invert.getIndex()) != 0); 
        }

        /// <summary>
        /// Render an X axis mirror of the sculpty.
        /// </summary>
        public boolean getMirror()
        {
            return ((type & (byte)SculptType.Mirror.getIndex()) != 0);
        }            

        /// <summary>
        /// Default finalructor
        /// </summary>
        public SculptData()
        {
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="data"></param>
        /// <param name="pos"></param>
        public SculptData(byte[] data, int pos)
        {
            if (data.length >= 17)
            {
                SculptTexture = new UUID(data, pos);
                type = data[pos + 16];
            }
            else
            {
                SculptTexture = UUID.Zero;
                type = (byte)SculptType.None.getIndex();
            }
        }

        public byte[] GetBytes()
        {
            byte[] data = new byte[17];
            byte[] src = SculptTexture.GetBytes();
            System.arraycopy(src, 0, data, 0, src.length);
            data[16] = type;

            return data;
        }

        public OSD GetOSD()
        {
            OSDMap map = new OSDMap();

            map.put("texture", OSD.FromUUID(SculptTexture));
            map.put("type", OSD.FromInteger(type));

            return map;
        }

        public static SculptData FromOSD(OSD osd)
        {
            SculptData sculpt = new SculptData();

            if (osd.getType() == OSDType.Map)
            {
                OSDMap map = (OSDMap)osd;

                sculpt.SculptTexture = map.get("texture").asUUID();
                sculpt.type = (byte)map.get("type").asInteger();
            }

            return sculpt;
        }

        public  int GetHashCode()
        {
            return SculptTexture.GetHashCode() ^ new Byte(type).hashCode();
        }
    }