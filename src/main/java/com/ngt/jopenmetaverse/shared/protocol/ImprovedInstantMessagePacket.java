package com.ngt.jopenmetaverse.shared.protocol;


    public final class ImprovedInstantMessagePacket extends Packet
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
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
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
        public final class MessageBlockBlock extends PacketBlock
        {
            public bool FromGroup;
            public UUID ToAgentID;
            public uint ParentEstateID;
            public UUID RegionID;
            public Vector3 Position;
            public byte Offline;
            public byte Dialog;
            public UUID ID;
            public uint Timestamp;
            public byte[] FromAgentName;
            public byte[] Message;
            public byte[] BinaryBucket;

            @Override
			public int getLength()
            {
                                {
                    int length = 76;
                    if (FromAgentName != null) { length += FromAgentName.length; }
                    if (Message != null) { length += Message.length; }
                    if (BinaryBucket != null) { length += BinaryBucket.length; }
                    return length;
                }
            }

            public MessageBlockBlock() { }
            public MessageBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    FromGroup = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    ToAgentID.FromBytes(bytes, i); i += 16;
                    ParentEstateID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    RegionID.FromBytes(bytes, i); i += 16;
                    Position.FromBytes(bytes, i); i += 12;
                    Offline = (byte)bytes[i++];
                    Dialog = (byte)bytes[i++];
                    ID.FromBytes(bytes, i); i += 16;
                    Timestamp = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    FromAgentName = new byte[length];
                    Utils.arraycopy(bytes, i, FromAgentName, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i, Message, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    BinaryBucket = new byte[length];
                    Utils.arraycopy(bytes, i, BinaryBucket, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)((FromGroup) ? 1 : 0);
                ToAgentID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.UIntToBytes(ParentEstateID, bytes, i); i += 4;
                RegionID.ToBytes(bytes, i); i += 16;
                Position.ToBytes(bytes, i); i += 12;
                bytes[i++] = Offline;
                bytes[i++] = Dialog;
                ID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(Timestamp, bytes, i); i += 4;
                bytes[i++] = (byte)FromAgentName.length;
                Utils.arraycopy(FromAgentName, 0, bytes, i, FromAgentName.length); i += FromAgentName.length;
                bytes[i++] = (byte)(Message.length % 256);
                bytes[i++] = (byte)((Message.length >> 8) % 256);
                Utils.arraycopy(Message, 0, bytes, i, Message.length); i += Message.length;
                bytes[i++] = (byte)(BinaryBucket.length % 256);
                bytes[i++] = (byte)((BinaryBucket.length >> 8) % 256);
                Utils.arraycopy(BinaryBucket, 0, bytes, i, BinaryBucket.length); i += BinaryBucket.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += MessageBlock.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MessageBlockBlock MessageBlock;

        public ImprovedInstantMessagePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ImprovedInstantMessage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 254;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            MessageBlock = new MessageBlockBlock();
        }

        public ImprovedInstantMessagePacket(byte[] bytes, int[] i) 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            MessageBlock.FromBytes(bytes, i);
        }

        public ImprovedInstantMessagePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            MessageBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += MessageBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            MessageBlock.ToBytes(bytes, i);
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
