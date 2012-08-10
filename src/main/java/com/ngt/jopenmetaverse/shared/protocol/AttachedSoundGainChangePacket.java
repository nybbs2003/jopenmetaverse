package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AttachedSoundGainChangePacket extends Packet
    {
        /// <exclude/>
        public static final class DataBlockBlock extends PacketBlock
        {
            public UUID ObjectID = new UUID();
            public float Gain;

            @Override
			public int getLength()
            {
                                {
                    return 20;
                }
            }

            public DataBlockBlock() { }
            public DataBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    Gain = Utils.bytesToFloatLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.floatToBytesLit(Gain, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += DataBlock.getLength();
                return length;
            }
        }
        public DataBlockBlock DataBlock;

        public AttachedSoundGainChangePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AttachedSoundGainChange;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 14;
            header.Reliable = true;
            DataBlock = new DataBlockBlock();
        }

        public AttachedSoundGainChangePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            DataBlock.FromBytes(bytes, i);
        }

        public AttachedSoundGainChangePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            DataBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += DataBlock.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            DataBlock.ToBytes(bytes, i);
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
