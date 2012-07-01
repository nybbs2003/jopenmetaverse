package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class RegionInfoPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID SessionID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class RegionInfoBlock extends PacketBlock
        {
            public byte[] SimName;
            public long EstateID;
            public long ParentEstateID;
            public long RegionFlags;
		/** Unsigned Byte */ 
		public byte SimAccess;
		/** Unsigned Byte */ 
		public byte MaxAgents;
            public float BillableFactor;
            public float ObjectBonusFactor;
            public float WaterHeight;
            public float TerrainRaiseLimit;
            public float TerrainLowerLimit;
            public int PricePerMeter;
            public int RedirectGridX;
            public int RedirectGridY;
            public boolean UseEstateSun;
            public float SunHour;

            @Override
			public int getLength()
            {
                                {
                    int length = 52;
                    if (SimName != null) { length += SimName.length; }
                    return length;
                }
            }

            public RegionInfoBlock() { }
            public RegionInfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SimName, 0, length); i[0] +=  length;
                    EstateID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    ParentEstateID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    RegionFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SimAccess = (byte)bytes[i[0]++];
                    MaxAgents = (byte)bytes[i[0]++];
                    BillableFactor = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    ObjectBonusFactor = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    WaterHeight = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    TerrainRaiseLimit = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    TerrainLowerLimit = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    PricePerMeter = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    RedirectGridX = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    RedirectGridY = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    UseEstateSun = (bytes[i[0]++] != 0) ? true : false;
                    SunHour = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i[0], SimName.length); i[0] +=  SimName.length;
                Utils.uintToBytes(EstateID, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(ParentEstateID, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(RegionFlags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = SimAccess;
                bytes[i[0]++] = MaxAgents;
                Utils.floatToBytes(BillableFactor, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(ObjectBonusFactor, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(WaterHeight, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(TerrainRaiseLimit, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(TerrainLowerLimit, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PricePerMeter, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(RedirectGridX, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(RedirectGridY, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((UseEstateSun) ? 1 : 0);
                Utils.floatToBytes(SunHour, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class RegionInfo2Block extends PacketBlock
        {
            public byte[] ProductSKU;
		/** Unsigned Byte */ 
		public byte[] ProductName;
            public long MaxAgents32;
            public long HardMaxAgents;
            public long HardMaxObjects;

            @Override
			public int getLength()
            {
                                {
                    int length = 14;
                    if (ProductSKU != null) { length += ProductSKU.length; }
                    if (ProductName != null) { length += ProductName.length; }
                    return length;
                }
            }

            public RegionInfo2Block() { }
            public RegionInfo2Block(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ProductSKU = new byte[length];
                    Utils.arraycopy(bytes, i[0], ProductSKU, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ProductName = new byte[length];
                    Utils.arraycopy(bytes, i[0], ProductName, 0, length); i[0] +=  length;
                    MaxAgents32 = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    HardMaxAgents = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    HardMaxObjects = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)ProductSKU.length;
                Utils.arraycopy(ProductSKU, 0, bytes, i[0], ProductSKU.length); i[0] +=  ProductSKU.length;
                bytes[i[0]++] = (byte)ProductName.length;
                Utils.arraycopy(ProductName, 0, bytes, i[0], ProductName.length); i[0] +=  ProductName.length;
                Utils.uintToBytes(MaxAgents32, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(HardMaxAgents, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(HardMaxObjects, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += RegionInfo.getLength();
                length += RegionInfo2.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RegionInfoBlock RegionInfo;
        public RegionInfo2Block RegionInfo2;

        public RegionInfoPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RegionInfo;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 142;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            RegionInfo = new RegionInfoBlock();
            RegionInfo2 = new RegionInfo2Block();
        }

        public RegionInfoPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            RegionInfo.FromBytes(bytes, i);
            RegionInfo2.FromBytes(bytes, i);
        }

        public RegionInfoPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            RegionInfo.FromBytes(bytes, i);
            RegionInfo2.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += RegionInfo.getLength();
            length += RegionInfo2.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RegionInfo.ToBytes(bytes, i);
            RegionInfo2.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
