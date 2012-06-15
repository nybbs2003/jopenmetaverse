package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class EstateCovenantReplyPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID CovenantID;
            public long CovenantTimestamp;
            public byte[] EstateName;
            public UUID EstateOwnerID;

            @Override
			public int getLength()
            {
                                {
                    int length = 37;
                    if (EstateName != null) { length += EstateName.length; }
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
                    CovenantID.FromBytes(bytes, i[0]); i[0] += 16;
                    CovenantTimestamp = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    EstateName = new byte[length];
                    Utils.arraycopy(bytes, i[0], EstateName, 0, length); i[0] +=  length;
                    EstateOwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                CovenantID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(CovenantTimestamp, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)EstateName.length;
                Utils.arraycopy(EstateName, 0, bytes, i[0], EstateName.length); i[0] +=  EstateName.length;
                EstateOwnerID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += Data.getLength();
                return length;
            }
        }
        public DataBlock Data;

        public EstateCovenantReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.EstateCovenantReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 204;
            header.Reliable = true;
            Data = new DataBlock();
        }

        public EstateCovenantReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Data.FromBytes(bytes, i);
        }

        public EstateCovenantReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
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
