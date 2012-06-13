package com.ngt.jopenmetaverse.shared.protocol;


    public final class ErrorPacket extends Packet
    {
        /// <exclude/>
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

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public int Code;
            public byte[] Token;
            public UUID ID;
            public byte[] System;
            public byte[] Message;
            public byte[] Data;

            @Override
			public int getLength()
            {
                                {
                    int length = 26;
                    if (Token != null) { length += Token.length; }
                    if (System != null) { length += System.length; }
                    if (Message != null) { length += Message.length; }
                    if (Data != null) { length += Data.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    Code = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    Token = new byte[length];
                    Utils.arraycopy(bytes, i, Token, 0, length); i[0] +=  length;
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    System = new byte[length];
                    Utils.arraycopy(bytes, i, System, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i, Message, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Data = new byte[length];
                    Utils.arraycopy(bytes, i, Data, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(Code, bytes, i); i += 4;
                bytes[i[0]++] = (byte)Token.length;
                Utils.arraycopy(Token, 0, bytes, i, Token.length); i[0] +=  Token.length;
                ID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)System.length;
                Utils.arraycopy(System, 0, bytes, i, System.length); i[0] +=  System.length;
                bytes[i[0]++] = (byte)(Message.length % 256);
                bytes[i[0]++] = (byte)((Message.length >> 8) % 256);
                Utils.arraycopy(Message, 0, bytes, i, Message.length); i[0] +=  Message.length;
                bytes[i[0]++] = (byte)(Data.length % 256);
                bytes[i[0]++] = (byte)((Data.length >> 8) % 256);
                Utils.arraycopy(Data, 0, bytes, i, Data.length); i[0] +=  Data.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += Data.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;

        public ErrorPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.Error;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 423;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
        }

        public ErrorPacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
        }

        public ErrorPacket(Header head, byte[] bytes, int[] i)
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
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
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
