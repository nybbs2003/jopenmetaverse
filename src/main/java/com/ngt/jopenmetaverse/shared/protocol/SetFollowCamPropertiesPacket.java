package com.ngt.jopenmetaverse.shared.protocol;


    public final class SetFollowCamPropertiesPacket extends Packet
    {
        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public UUID ObjectID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
                try
                {
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class CameraPropertyBlock extends PacketBlock
        {
            public int Type;
            public float Value;

            @Override
			public int getLength()
            {
                                {
                    return 8;
                }
            }

            public CameraPropertyBlock() { }
            public CameraPropertyBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Type = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Value = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(Type, bytes, i); i += 4;
                Utils.FloatToBytes(Value, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += ObjectData.length;
                for (int j = 0; j < CameraProperty.length; j++)
                    length += CameraProperty[j].getLength();
                return length;
            }
        }
        public ObjectDataBlock ObjectData;
        public CameraPropertyBlock[] CameraProperty;

        public SetFollowCamPropertiesPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.SetFollowCamProperties;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 159;
            header.Reliable = true;
            ObjectData = new ObjectDataBlock();
            CameraProperty = null;
        }

        public SetFollowCamPropertiesPacket(byte[] bytes, int[] i) 
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
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(CameraProperty == null || CameraProperty.length != -1) {
                CameraProperty = new CameraPropertyBlock[count];
                for(int j = 0; j < count; j++)
                { CameraProperty[j] = new CameraPropertyBlock(); }
            }
            for (int j = 0; j < count; j++)
            { CameraProperty[j].FromBytes(bytes, i); }
        }

        public SetFollowCamPropertiesPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(CameraProperty == null || CameraProperty.length != count) {
                CameraProperty = new CameraPropertyBlock[count];
                for(int j = 0; j < count; j++)
                { CameraProperty[j] = new CameraPropertyBlock(); }
            }
            for (int j = 0; j < count; j++)
            { CameraProperty[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ObjectData.length;
            length++;
            for (int j = 0; j < CameraProperty.length; j++) { length += CameraProperty[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)CameraProperty.length;
            for (int j = 0; j < CameraProperty.length; j++) { CameraProperty[j].ToBytes(bytes, i); }
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

            fixedLength += ObjectData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            ObjectData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int CameraPropertyStart = 0;
            do
            {
                int variableLength = 0;
                int CameraPropertyCount = 0;

                i = CameraPropertyStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < CameraProperty.length) {
                    int blockLength = CameraProperty[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++CameraPropertyCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)CameraPropertyCount;
                for (i = CameraPropertyStart; i < CameraPropertyStart + CameraPropertyCount; i++) { CameraProperty[i].ToBytes(packet, length); }
                CameraPropertyStart += CameraPropertyCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                CameraPropertyStart < CameraProperty.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
