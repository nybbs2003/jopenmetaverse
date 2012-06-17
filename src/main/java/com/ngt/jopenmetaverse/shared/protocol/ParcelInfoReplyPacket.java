package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ParcelInfoReplyPacket extends Packet
    {
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
            }

        }

        public final class DataBlock extends PacketBlock
        {
            public UUID ParcelID;
            public UUID OwnerID;
		/** Unsigned Byte */ 
		public byte[] Name;
		/** Unsigned Byte */ 
		public byte[] Desc;
            public int ActualArea;
            public int BillableArea;
		/** Unsigned Byte */ 
		public byte Flags;
            public float GlobalX;
            public float GlobalY;
            public float GlobalZ;
		/** Unsigned Byte */ 
		public byte[] SimName;
            public UUID SnapshotID;
            public float Dwell;
            public int SalePrice;
            public int AuctionID;

            @Override
			public int getLength()
            {
                                {
                    int length = 84;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    if (SimName != null) { length += SimName.length; }
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
                    ParcelID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] += length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] += length;
                    ActualArea = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    BillableArea = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Flags = (byte)bytes[i[0]++];
                    GlobalX = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    GlobalY = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    GlobalZ = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SimName, 0, length); i[0] += length;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    Dwell = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    SalePrice = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    AuctionID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ParcelID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] += Name.length;
                bytes[i[0]++] = (byte)Desc.length;
                Utils.arraycopy(Desc, 0, bytes, i[0], Desc.length); i[0] += Desc.length;
                Utils.intToBytes(ActualArea, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(BillableArea, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Flags;
                Utils.floatToBytes(GlobalX, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(GlobalY, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(GlobalZ, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i[0], SimName.length); i[0] += SimName.length;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.floatToBytes(Dwell, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(SalePrice, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(AuctionID, bytes, i[0]); i[0] += 4;
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

        public ParcelInfoReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelInfoReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 55;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public ParcelInfoReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
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

        public ParcelInfoReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            int[] i = new int[1];
            i[0] = 0;
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