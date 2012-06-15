package com.ngt.jopenmetaverse.shared.protocol;


    public final class RequestImagePacket extends Packet
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
        public final class RequestImageBlock extends PacketBlock
        {
            public UUID Image;
            public sbyte DiscardLevel;
            public float DownloadPriority;
            public long Packet;
            public byte Type;

            @Override
			public int getLength()
            {
                                {
                    return 26;
                }
            }

            public RequestImageBlock() { }
            public RequestImageBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Image.FromBytes(bytes, i[0]); i[0] += 16;
                    DiscardLevel = (sbyte)bytes[i[0]++];
                    DownloadPriority = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Packet = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Type = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Image.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)DiscardLevel;
                Utils.floatToBytes(DownloadPriority, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Packet, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Type;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += AgentData.getLength();
                for (int j = 0; j < RequestImage.length; j++)
                    length += RequestImage[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RequestImageBlock[] RequestImage;

        public RequestImagePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.RequestImage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 8;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            RequestImage = null;
        }

        public RequestImagePacket(byte[] bytes, int[] i) 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(RequestImage == null || RequestImage.length != -1) {
                RequestImage = new RequestImageBlock[count];
                for(int j = 0; j < count; j++)
                { RequestImage[j] = new RequestImageBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RequestImage[j].FromBytes(bytes, i); }
        }

        public RequestImagePacket(Header head, byte[] bytes, int[] i)
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(RequestImage == null || RequestImage.length != count) {
                RequestImage = new RequestImageBlock[count];
                for(int j = 0; j < count; j++)
                { RequestImage[j] = new RequestImageBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RequestImage[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < RequestImage.length; j++) { length += RequestImage[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)RequestImage.length;
            for (int j = 0; j < RequestImage.length; j++) { RequestImage[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 7;

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

            int RequestImageStart = 0;
            do
            {
                int variableLength = 0;
                int RequestImageCount = 0;

              i[0] =RequestImageStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < RequestImage.length) {
                    int blockLength = RequestImage[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++RequestImageCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)RequestImageCount;
                for (i[0] = RequestImageStart; i[0] < RequestImageStart + RequestImageCount; i[0]++) { RequestImage[i[0]].ToBytes(packet, length); }
                RequestImageStart += RequestImageCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                RequestImageStart < RequestImage.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
