package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ParcelMediaCommandMessagePacket extends Packet
    {
        public final class CommandBlockBlock extends PacketBlock
        {
            public long Flags;
            public long Command;
            public float Time;

            @Override
			public int getLength()
            {
                    return 12;
            }

            public CommandBlockBlock() { }
            public CommandBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Flags = (long)Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    Command = (long)Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    Time = Utils.bytesToFloatLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytesLit(Flags, bytes, i[0]); i[0] += 4;
                Utils.uintToBytesLit(Command, bytes, i[0]); i[0] += 4;
                Utils.floatToBytesLit(Time, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += CommandBlock.getLength();
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

        public ParcelMediaCommandMessagePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            CommandBlock.FromBytes(bytes, i);
        }

        public ParcelMediaCommandMessagePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            CommandBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += CommandBlock.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[1];
            i[0] = 0;
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