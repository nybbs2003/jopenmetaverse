package com.ngt.jopenmetaverse.shared.protocol;


    public final class PreloadSoundPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlockBlock extends PacketBlock
        {
            public UUID ObjectID;
            public UUID OwnerID;
            public UUID SoundID;

            @Override
			public int getLength()
            {
                get
                {
                    return 48;
                }
            }

            public DataBlockBlock() { }
            public DataBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    SoundID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i); i += 16;
                OwnerID.ToBytes(bytes, i); i += 16;
                SoundID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 9;
                for (int j = 0; j < DataBlock.length; j++)
                    length += DataBlock[j].length;
                return length;
            }
        }
        public DataBlockBlock[] DataBlock;

        public PreloadSoundPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.PreloadSound;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 15;
            header.Reliable = true;
            DataBlock = null;
        }

        public PreloadSoundPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(DataBlock == null || DataBlock.length != -1) {
                DataBlock = new DataBlockBlock[count];
                for(int j = 0; j < count; j++)
                { DataBlock[j] = new DataBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { DataBlock[j].FromBytes(bytes, i); }
        }

        public PreloadSoundPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = (int)bytes[i++];
            if(DataBlock == null || DataBlock.length != count) {
                DataBlock = new DataBlockBlock[count];
                for(int j = 0; j < count; j++)
                { DataBlock[j] = new DataBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { DataBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length++;
            for (int j = 0; j < DataBlock.length; j++) { length += DataBlock[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i++] = (byte)DataBlock.length;
            for (int j = 0; j < DataBlock.length; j++) { DataBlock[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 8;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
            }

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int DataBlockStart = 0;
            do
            {
                int variableLength = 0;
                int DataBlockCount = 0;

                i = DataBlockStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < DataBlock.length) {
                    int blockLength = DataBlock[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++DataBlockCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)DataBlockCount;
                for (i = DataBlockStart; i < DataBlockStart + DataBlockCount; i++) { DataBlock[i].ToBytes(packet, ref length); }
                DataBlockStart += DataBlockCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                DataBlockStart < DataBlock.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
