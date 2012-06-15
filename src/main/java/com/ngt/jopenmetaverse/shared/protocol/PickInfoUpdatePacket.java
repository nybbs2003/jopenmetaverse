package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3d;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class PickInfoUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

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
        public final class DataBlock extends PacketBlock
        {
            public UUID PickID;
            public UUID CreatorID;
            public boolean TopPick;
            public UUID ParcelID;
            public byte[] Name;
            public byte[] Desc;
            public UUID SnapshotID;
            public Vector3d PosGlobal;
            public int SortOrder;
            public boolean Enabled;

            @Override
			public int getLength()
            {
                                {
                    int length = 97;
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
                    PickID.FromBytes(bytes, i[0]); i[0] += 16;
                    CreatorID.FromBytes(bytes, i[0]); i[0] += 16;
                    TopPick = (bytes[i[0]++] != 0) ? true : false;
                    ParcelID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] +=  length;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    PosGlobal.fromBytes(bytes, i[0]); i[0] += 24;
                    SortOrder = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Enabled = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                PickID.ToBytes(bytes, i[0]); i[0] += 16;
                CreatorID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((TopPick) ? 1 : 0);
                ParcelID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)(Desc.length % 256);
                bytes[i[0]++] = (byte)((Desc.length >> 8) % 256);
                Utils.arraycopy(Desc, 0, bytes, i[0], Desc.length); i[0] +=  Desc.length;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                PosGlobal.toBytes(bytes, i[0]); i[0] += 24;
                Utils.intToBytes(SortOrder, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((Enabled) ? 1 : 0);
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

        public PickInfoUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.PickInfoUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 185;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public PickInfoUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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

        public PickInfoUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
