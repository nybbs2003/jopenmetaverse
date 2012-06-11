package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarAppearancePacket extends Packet
    {
        /// <exclude/>
        public final class SenderBlock extends PacketBlock
        {
            public UUID ID;
            public bool IsTrial;

            @Override
			public int getLength()
            {
                get
                {
                    return 17;
                }
            }

            public SenderBlock() { }
            public SenderBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i); i += 16;
                    IsTrial = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((IsTrial) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public byte[] TextureEntry;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 2;
                    if (TextureEntry != null) { length += TextureEntry.length; }
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
                    length = (bytes[i++] + (bytes[i++] << 8));
                    TextureEntry = new byte[length];
                    Buffer.BlockCopy(bytes, i, TextureEntry, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)(TextureEntry.length % 256);
                bytes[i++] = (byte)((TextureEntry.length >> 8) % 256);
                Buffer.BlockCopy(TextureEntry, 0, bytes, i, TextureEntry.length); i += TextureEntry.length;
            }

        }

        /// <exclude/>
        public final class VisualParamBlock extends PacketBlock
        {
            public byte ParamValue;

            @Override
			public int getLength()
            {
                get
                {
                    return 1;
                }
            }

            public VisualParamBlock() { }
            public VisualParamBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ParamValue = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = ParamValue;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += Sender.length;
                length += ObjectData.length;
                for (int j = 0; j < VisualParam.length; j++)
                    length += VisualParam[j].length;
                return length;
            }
        }
        public SenderBlock Sender;
        public ObjectDataBlock ObjectData;
        public VisualParamBlock[] VisualParam;

        public AvatarAppearancePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AvatarAppearance;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 158;
            header.Reliable = true;
            header.Zerocoded = true;
            Sender = new SenderBlock();
            ObjectData = new ObjectDataBlock();
            VisualParam = null;
        }

        public AvatarAppearancePacket(byte[] bytes, int[] i) 
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
            Sender.FromBytes(bytes, i);
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(VisualParam == null || VisualParam.length != -1) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
        }

        public AvatarAppearancePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Sender.FromBytes(bytes, i);
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(VisualParam == null || VisualParam.length != count) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Sender.length;
            length += ObjectData.length;
            length++;
            for (int j = 0; j < VisualParam.length; j++) { length += VisualParam[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Sender.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i++] = (byte)VisualParam.length;
            for (int j = 0; j < VisualParam.length; j++) { VisualParam[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
            }

            fixedLength += Sender.length;
            fixedLength += ObjectData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            Sender.ToBytes(fixedBytes, i);
            ObjectData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int VisualParamStart = 0;
            do
            {
                int variableLength = 0;
                int VisualParamCount = 0;

                i = VisualParamStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < VisualParam.length) {
                    int blockLength = VisualParam[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++VisualParamCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)VisualParamCount;
                for (i = VisualParamStart; i < VisualParamStart + VisualParamCount; i++) { VisualParam[i].ToBytes(packet, ref length); }
                VisualParamStart += VisualParamCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                VisualParamStart < VisualParam.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>