package com.ngt.jopenmetaverse.shared.protocol.primitives;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.types.Color4;
import com.ngt.jopenmetaverse.shared.util.Utils;


  /// <summary>
    /// Information on the light properties of a primitive
    /// </summary>
    public class LightData
    {
        /// <summary></summary>
        public Color4 Color;
        /// <summary></summary>
        public float Intensity;
        /// <summary></summary>
        public float Radius;
        /// <summary></summary>
        public float Cutoff;
        /// <summary></summary>
        public float Falloff;

        /// <summary>
        /// Default finalructor
        /// </summary>
        public LightData()
        {
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="data"></param>
        /// <param name="pos"></param>
        public LightData(byte[] data, int pos)
        {
            if (data.length - pos >= 16)
            {
                Color = new Color4(data, pos, false);
                Radius = Utils.bytesToFloatLit(data, pos + 4);
                Cutoff = Utils.bytesToFloatLit(data, pos + 8);
                Falloff = Utils.bytesToFloatLit(data, pos + 12);

                // Alpha in color is actually intensity
                Intensity = Color.A;
                Color.A = 1f;
            }
            else
            {
                Color = Color4.Black;
                Radius = 0f;
                Cutoff = 0f;
                Falloff = 0f;
                Intensity = 0f;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public byte[] GetBytes()
        {
            byte[] data = new byte[16];

            // Alpha channel in color is intensity
            Color4 tmpColor = Color;
            tmpColor.A = Intensity;
            System.arraycopy(tmpColor.getBytes(), 0, data, 0, 4);
            System.arraycopy(Utils.floatToBytesLit(Radius), 0, data, 4, 4);
            System.arraycopy(Utils.floatToBytesLit(Cutoff), 0, data, 8, 4);
            System.arraycopy(Utils.floatToBytesLit(Falloff), 0, data, 12, 4);
            return data;
        }

        public OSD GetOSD()
        {
            OSDMap map = new OSDMap();

            map.put("color", OSD.FromColor4(Color));
            map.put("intensity", OSD.FromReal(Intensity));
            map.put("radius", OSD.FromReal(Radius));
            map.put("cutoff", OSD.FromReal(Cutoff));
            map.put("falloff", OSD.FromReal(Falloff));

            return map;
        }

        public static LightData FromOSD(OSD osd)
        {
            LightData light = new LightData();

            if (osd.getType() == OSDType.Map)
            {
                OSDMap map = (OSDMap)osd;

                light.Color = map.get("color").asColor4();
                light.Intensity = (float)map.get("intensity").asReal();
                light.Radius = (float)map.get("radius").asReal();
                light.Cutoff = (float)map.get("cutoff").asReal();
                light.Falloff = (float)map.get("falloff").asReal();
            }

            return light;
        }

        public  int hashCode()
        {
            return
                Color.hashCode() ^
                new Float(Intensity).hashCode() ^
                new Float(Radius).hashCode() ^
                new Float(Cutoff).hashCode() ^
                new Float(Falloff).hashCode();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public  String ToString()
        {
            return String.format("Color: %f Intensity: %f Radius: %f Cutoff: %f Falloff: %f",
                Color, Intensity, Radius, Cutoff, Falloff);
        }
    }