package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3d;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ClassifiedInfoUpdatePacket extends Packet
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
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
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
        public static final class DataBlock extends PacketBlock
        {
            public UUID ClassifiedID = new UUID();
            public long Category;
		/** Unsigned Byte */ 
		public byte[] Name;
		/** Unsigned Byte */ 
		public byte[] Desc;
            public UUID ParcelID = new UUID();
            public long ParentEstate;
            public UUID SnapshotID = new UUID();
            public Vector3d PosGlobal;
		/** Unsigned Byte */ 
		public byte ClassifiedFlags;
            public int PriceForListing;

            @Override
			public int getLength()
            {
                                {
                    int length = 88;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ClassifiedID.FromBytes(bytes, i[0]); i[0] += 16;
                    Category = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] +=  length;
                    ParcelID.FromBytes(bytes, i[0]); i[0] += 16;
                    ParentEstate = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    PosGlobal.fromBytes(bytes, i[0]); i[0] += 24;
                    ClassifiedFlags = (byte)bytes[i[0]++];
                    PriceForListing = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ClassifiedID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(Category, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)(Desc.length % 256);
                bytes[i[0]++] = (byte)((Desc.length >> 8) % 256);
                Utils.arraycopy(Desc, 0, bytes, i[0], Desc.length); i[0] +=  Desc.length;
                ParcelID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(ParentEstate, bytes, i[0]); i[0] += 4;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                PosGlobal.toBytes(bytes, i[0]); i[0] += 24;
                bytes[i[0]++] = ClassifiedFlags;
                Utils.intToBytes(PriceForListing, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += Data.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;

        public ClassifiedInfoUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ClassifiedInfoUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 45;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public ClassifiedInfoUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Data.FromBytes(bytes, i);
        }

        public ClassifiedInfoUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
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
