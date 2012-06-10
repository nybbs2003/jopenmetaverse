package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelMediaCommandMessagePacket extends Packet
    {
        /// <exclude/>
        public final class CommandBlockBlock extends PacketBlock
        {
            public uint Flags;
            public uint Command;
            public float Time;

            @Override
			public int getLength()
            {
                get
                {
                    return 12;
                }
            }

            public CommandBlockBlock() { }
            public CommandBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Command = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Time = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
                Utils.UIntToBytes(Command, bytes, i); i += 4;
                Utils.FloatToBytes(Time, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += CommandBlock.length;
                return length;
            }
        }
        public CommandBlockBlock CommandBlock;

        public ParcelMediaCommandMessagePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelMediaCommandMessage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 419;
            header.Reliable = true;
            CommandBlock = new CommandBlockBlock();
        }

        public ParcelMediaCommandMessagePacket(byte[] bytes, int[] i) 
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            CommandBlock.FromBytes(bytes, i);
        }

        public ParcelMediaCommandMessagePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            CommandBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += CommandBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            CommandBlock.ToBytes(bytes, i);
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
