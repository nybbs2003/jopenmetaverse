package com.ngt.jopenmetaverse.shared.protocol.primitives;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDArray;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.types.Vector3;

 /// <summary>
    /// Information on the flexible properties of a primitive
    /// </summary>
    public class FlexibleData
    {
        /// <summary></summary>
        public int Softness;
        /// <summary></summary>
        public float Gravity;
        /// <summary></summary>
        public float Drag;
        /// <summary></summary>
        public float Wind;
        /// <summary></summary>
        public float Tension;
        /// <summary></summary>
        public Vector3 Force;

        /// <summary>
        /// Default finalructor
        /// </summary>
        public FlexibleData()
        {
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="data"></param>
        /// <param name="pos"></param>
        public FlexibleData(byte[] data, int pos)
        {
            if (data.length >= 5)
            {
                Softness = ((data[pos] & 0x80) >> 6) | ((data[pos + 1] & 0x80) >> 7);

                Tension = (float)(data[pos++] & 0x7F) / 10.0f;
                Drag = (float)(data[pos++] & 0x7F) / 10.0f;
                Gravity = (float)(data[pos++] / 10.0f) - 10.0f;
                Wind = (float)data[pos++] / 10.0f;
                Force = new Vector3(data, pos);
            }
            else
            {
                Softness = 0;

                Tension = 0.0f;
                Drag = 0.0f;
                Gravity = 0.0f;
                Wind = 0.0f;
                Force = Vector3.Zero;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public byte[] GetBytes()
        {
            byte[] data = new byte[16];
            int i = 0;

            // Softness is packed in the upper bits of tension and drag
            data[i] = (byte)((Softness & 2) << 6);
            data[i + 1] = (byte)((Softness & 1) << 7);

            data[i++] |= (byte)((byte)(Tension * 10.01f) & 0x7F);
            data[i++] |= (byte)((byte)(Drag * 10.01f) & 0x7F);
            data[i++] = (byte)((Gravity + 10.0f) * 10.01f);
            data[i++] = (byte)(Wind * 10.01f);

            byte[] src = Force.getBytes();
            System.arraycopy(src, 0, data, i, src.length);
            return data;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public OSD GetOSD()
        {
            OSDMap map = new OSDMap();

            map.put("simulate_lod", OSD.FromInteger(Softness));
            map.put("gravity", OSD.FromReal(Gravity));
            map.put("air_friction", OSD.FromReal(Drag));
            map.put("wind_sensitivity", OSD.FromReal(Wind));
            map.put("tension", OSD.FromReal(Tension));
            map.put("user_force", OSD.FromVector3(Force));

            return map;
        }

        public static FlexibleData FromOSD(OSD osd)
        {
            FlexibleData flex = new FlexibleData();

            if (osd.getType() == OSDType.Map)
            {
                OSDMap map = (OSDMap)osd;

                flex.Softness = map.get("simulate_lod").asInteger();
                flex.Gravity = (float)map.get("gravity").asReal();
                flex.Drag = (float)map.get("air_friction").asReal();
                flex.Wind = (float)map.get("wind_sensitivity").asReal();
                flex.Tension = (float)map.get("tension").asReal();
                flex.Force = ((OSDArray)map.get("user_force")).asVector3();
            }
            return flex;
        }

        public int hashCode()
        {
            return
                new Integer(Softness).hashCode() ^
                new Float(Gravity).hashCode() ^
                new Float(Drag).hashCode() ^
                new Float(Wind).hashCode() ^
                new Float(Tension).hashCode() ^
                Force.hashCode();
        }
    }