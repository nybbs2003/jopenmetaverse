package com.ngt.jopenmetaverse.shared.protocol;


    public final class MapLayerReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public uint Flags;

            @Override
			public int getLength()
            {
                                {
                    return 20;
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
                    Flags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class LayerDataBlock extends PacketBlock
        {
            public uint Left;
            public uint Right;
            public uint Top;
            public uint Bottom;
            public UUID ImageID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public LayerDataBlock() { }
            public LayerDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Left = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Right = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Top = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Bottom = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ImageID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Left, bytes, i); i += 4;
                Utils.UIntToBytes(Right, bytes, i); i += 4;
                Utils.UIntToBytes(Top, bytes, i); i += 4;
                Utils.UIntToBytes(Bottom, bytes, i); i += 4;
                ImageID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < LayerData.length; j++)
                    length += LayerData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public LayerDataBlock[] LayerData;

        public MapLayerReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.MapLayerReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 406;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            LayerData = null;
        }

        public MapLayerReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(LayerData == null || LayerData.length != -1) {
                LayerData = new LayerDataBlock[count];
                for(int j = 0; j < count; j++)
                { LayerData[j] = new LayerDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { LayerData[j].FromBytes(bytes, i); }
        }

        public MapLayerReplyPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i[0]++];
            if(LayerData == null || LayerData.length != count) {
                LayerData = new LayerDataBlock[count];
                for(int j = 0; j < count; j++)
                { LayerData[j] = new LayerDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { LayerData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < LayerData.length; j++) { length += LayerData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)LayerData.length;
            for (int j = 0; j < LayerData.length; j++) { LayerData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int LayerDataStart = 0;
            do
            {
                int variableLength = 0;
                int LayerDataCount = 0;

                i = LayerDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < LayerData.length) {
                    int blockLength = LayerData[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++LayerDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)LayerDataCount;
                for (i = LayerDataStart; i < LayerDataStart + LayerDataCount; i++) { LayerData[i].ToBytes(packet, length); }
                LayerDataStart += LayerDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                LayerDataStart < LayerData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
