package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectUpdateCompressedPacket extends Packet
    {
        /// <exclude/>
        public final class RegionDataBlock extends PacketBlock
        {
            public ulong RegionHandle;
            public ushort TimeDilation;

            @Override
			public int getLength()
            {
                                {
                    return 10;
                }
            }

            public RegionDataBlock() { }
            public RegionDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionHandle = (ulong)((ulong)bytes[i[0]++] + ((ulong)bytes[i[0]++] << 8) + ((ulong)bytes[i[0]++] << 16) + ((ulong)bytes[i[0]++] << 24) + ((ulong)bytes[i[0]++] << 32) + ((ulong)bytes[i[0]++] << 40) + ((ulong)bytes[i[0]++] << 48) + ((ulong)bytes[i[0]++] << 56));
                    TimeDilation = (ushort)(bytes[i[0]++] + (bytes[i[0]++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                bytes[i[0]++] = (byte)(TimeDilation % 256);
                bytes[i[0]++] = (byte)((TimeDilation >> 8) % 256);
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public uint UpdateFlags;
            public byte[] Data;

            @Override
			public int getLength()
            {
                                {
                    int length = 6;
                    if (Data != null) { length += Data.length; }
                    return length;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    UpdateFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                Utils.UIntToBytes(UpdateFlags, bytes, i); i += 4;
                bytes[i[0]++] = (byte)(Data.length % 256);
                bytes[i[0]++] = (byte)((Data.length >> 8) % 256);
                Utils.arraycopy(Data, 0, bytes, i, Data.length); i[0] +=  Data.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += RegionData.length;
                for (int j = 0; j < ObjectData.length; j++)
                    length += ObjectData[j].getLength();
                return length;
            }
        }
        public RegionDataBlock RegionData;
        public ObjectDataBlock[] ObjectData;

        public ObjectUpdateCompressedPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectUpdateCompressed;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 13;
            header.Reliable = true;
            RegionData = new RegionDataBlock();
            ObjectData = null;
        }

        public ObjectUpdateCompressedPacket(byte[] bytes, int[] i) 
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
            RegionData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(ObjectData == null || ObjectData.length != -1) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        public ObjectUpdateCompressedPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            RegionData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(ObjectData == null || ObjectData.length != count) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += RegionData.length;
            length++;
            for (int j = 0; j < ObjectData.length; j++) { length += ObjectData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            RegionData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ObjectData.length;
            for (int j = 0; j < ObjectData.length; j++) { ObjectData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int i = 0;
            int fixedLength = 7;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += RegionData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            RegionData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ObjectDataStart = 0;
            do
            {
                int variableLength = 0;
                int ObjectDataCount = 0;

                i = ObjectDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < ObjectData.length) {
                    int blockLength = ObjectData[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ObjectDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ObjectDataCount;
                for (i = ObjectDataStart; i < ObjectDataStart + ObjectDataCount; i++) { ObjectData[i].ToBytes(packet, length); }
                ObjectDataStart += ObjectDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ObjectDataStart < ObjectData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
