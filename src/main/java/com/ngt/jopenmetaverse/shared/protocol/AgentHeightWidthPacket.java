package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AgentHeightWidthPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            /**
             * Unsigned Integer, Only 4 bytes should be used and stored
             */
            public long CircuitCode; //Unsigned Int

            @Override
			public int getLength()
            {
                                {
                    return 36;
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
                    CircuitCode = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
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
                Utils.uintToBytes(CircuitCode, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class HeightWidthBlockBlock extends PacketBlock
        {
            public long GenCounter;
            /**
             * Actual Data Type is ushort (unsigned short)
             */
            public int Height; //ushort
            /**
             * Actual Data Type is ushort (unsigned short)
             */
            public int Width; //ushort

            @Override
			public int getLength()
            {
                                {
                    return 8;
                }
            }

            public HeightWidthBlockBlock() { }
            public HeightWidthBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GenCounter = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Height = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Width = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(GenCounter, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)(Height % 256);
                bytes[i[0]++] = (byte)((Height >> 8) % 256);
                bytes[i[0]++] = (byte)(Width % 256);
                bytes[i[0]++] = (byte)((Width >> 8) % 256);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += HeightWidthBlock.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public HeightWidthBlockBlock HeightWidthBlock;

        public AgentHeightWidthPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AgentHeightWidth;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 83;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            HeightWidthBlock = new HeightWidthBlockBlock();
        }

        public AgentHeightWidthPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            HeightWidthBlock.FromBytes(bytes, i);
        }

        public AgentHeightWidthPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            HeightWidthBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += HeightWidthBlock.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            HeightWidthBlock.ToBytes(bytes, i);
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
