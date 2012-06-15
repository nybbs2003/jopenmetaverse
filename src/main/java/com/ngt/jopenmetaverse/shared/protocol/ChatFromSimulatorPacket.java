package com.ngt.jopenmetaverse.shared.protocol;


    public final class ChatFromSimulatorPacket extends Packet
    {
        /// <exclude/>
        public final class ChatDataBlock extends PacketBlock
        {
            public byte[] FromName;
            public UUID SourceID;
            public UUID OwnerID;
            public byte SourceType;
            public byte ChatType;
            public byte Audible;
            public Vector3 Position;
            public byte[] Message;

            @Override
			public int getLength()
            {
                                {
                    int length = 50;
                    if (FromName != null) { length += FromName.length; }
                    if (Message != null) { length += Message.length; }
                    return length;
                }
            }

            public ChatDataBlock() { }
            public ChatDataBlock(byte[] bytes, int[] i)
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
                    FromName = new byte[length];
                    Utils.arraycopy(bytes, i[0], FromName, 0, length); i[0] +=  length;
                    SourceID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    SourceType = (byte)bytes[i[0]++];
                    ChatType = (byte)bytes[i[0]++];
                    Audible = (byte)bytes[i[0]++];
                    Position.FromBytes(bytes, i[0]); i[0] += 12;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i[0], Message, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)FromName.length;
                Utils.arraycopy(FromName, 0, bytes, i[0], FromName.length); i[0] +=  FromName.length;
                SourceID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = SourceType;
                bytes[i[0]++] = ChatType;
                bytes[i[0]++] = Audible;
                Position.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)(Message.length % 256);
                bytes[i[0]++] = (byte)((Message.length >> 8) % 256);
                Utils.arraycopy(Message, 0, bytes, i[0], Message.length); i[0] +=  Message.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += ChatData.length;
                return length;
            }
        }
        public ChatDataBlock ChatData;

        public ChatFromSimulatorPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ChatFromSimulator;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 139;
            header.Reliable = true;
            ChatData = new ChatDataBlock();
        }

        public ChatFromSimulatorPacket(byte[] bytes, int[] i) 
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
            ChatData.FromBytes(bytes, i);
        }

        public ChatFromSimulatorPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ChatData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ChatData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            ChatData.ToBytes(bytes, i);
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
