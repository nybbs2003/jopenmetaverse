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
                get
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
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                SessionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RequestImageBlock extends PacketBlock
        {
            public UUID Image;
            public sbyte DiscardLevel;
            public float DownloadPriority;
            public uint Packet;
            public byte Type;

            @Override
			public int getLength()
            {
                get
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
                    Image.FromBytes(bytes, i); i += 16;
                    DiscardLevel = (sbyte)bytes[i++];
                    DownloadPriority = Utils.BytesToFloat(bytes, i); i += 4;
                    Packet = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Type = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Image.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)DiscardLevel;
                Utils.FloatToBytes(DownloadPriority, bytes, i); i += 4;
                Utils.UIntToBytes(Packet, bytes, i); i += 4;
                bytes[i++] = Type;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 8;
                length += AgentData.getLength();
                for (int j = 0; j < RequestImage.length; j++)
                    length += RequestImage[j].length;
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
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            for (int j = 0; j < RequestImage.length; j++) { length += RequestImage[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)RequestImage.length;
            for (int j = 0; j < RequestImage.length; j++) { RequestImage[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 7;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
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

                i = RequestImageStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < RequestImage.length) {
                    int blockLength = RequestImage[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++RequestImageCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)RequestImageCount;
                for (i = RequestImageStart; i < RequestImageStart + RequestImageCount; i++) { RequestImage[i].ToBytes(packet, ref length); }
                RequestImageStart += RequestImageCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                RequestImageStart < RequestImage.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>