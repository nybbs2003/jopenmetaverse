package com.ngt.jopenmetaverse.shared.protocol;


    public final class UpdateMuteListEntryPacket extends Packet
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
        public final class MuteDataBlock extends PacketBlock
        {
            public UUID MuteID;
            public byte[] MuteName;
            public int MuteType;
            public uint MuteFlags;

            @Override
			public int getLength()
            {
                                {
                    int length = 25;
                    if (MuteName != null) { length += MuteName.length; }
                    return length;
                }
            }

            public MuteDataBlock() { }
            public MuteDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    MuteID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    MuteName = new byte[length];
                    Utils.arraycopy(bytes, i[0], MuteName, 0, length); i[0] +=  length;
                    MuteType = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    MuteFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                MuteID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)MuteName.length;
                Utils.arraycopy(MuteName, 0, bytes, i[0], MuteName.length); i[0] +=  MuteName.length;
                Utils.IntToBytes(MuteType, bytes, i[0]); i[0] += 4;
                Utils.UIntToBytes(MuteFlags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += MuteData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MuteDataBlock MuteData;

        public UpdateMuteListEntryPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.UpdateMuteListEntry;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 263;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            MuteData = new MuteDataBlock();
        }

        public UpdateMuteListEntryPacket(byte[] bytes, int[] i) 
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
            MuteData.FromBytes(bytes, i);
        }

        public UpdateMuteListEntryPacket(Header head, byte[] bytes, int[] i)
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
            MuteData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += MuteData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            MuteData.ToBytes(bytes, i);
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
