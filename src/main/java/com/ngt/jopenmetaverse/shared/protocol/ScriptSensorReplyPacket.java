package com.ngt.jopenmetaverse.shared.protocol;


    public final class ScriptSensorReplyPacket extends Packet
    {
        /// <exclude/>
        public final class RequesterBlock extends PacketBlock
        {
            public UUID SourceID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public RequesterBlock() { }
            public RequesterBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    SourceID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                SourceID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class SensedDataBlock extends PacketBlock
        {
            public UUID ObjectID;
            public UUID OwnerID;
            public UUID GroupID;
            public Vector3 Position;
            public Vector3 Velocity;
            public Quaternion Rotation;
            public byte[] Name;
            public int Type;
            public float Range;

            @Override
			public int getLength()
            {
                                {
                    int length = 93;
                    if (Name != null) { length += Name.length; }
                    return length;
                }
            }

            public SensedDataBlock() { }
            public SensedDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ObjectID.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
                    Position.FromBytes(bytes, i); i += 12;
                    Velocity.FromBytes(bytes, i); i += 12;
                    Rotation.FromBytes(bytes, i, true); i += 12;
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    Type = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Range = Utils.BytesToFloat(bytes, i); i += 4;
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
                GroupID.ToBytes(bytes, i); i += 16;
                Position.ToBytes(bytes, i); i += 12;
                Velocity.ToBytes(bytes, i); i += 12;
                Rotation.ToBytes(bytes, i); i += 12;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                Utils.IntToBytes(Type, bytes, i); i += 4;
                Utils.FloatToBytes(Range, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += Requester.length;
                for (int j = 0; j < SensedData.length; j++)
                    length += SensedData[j].length;
                return length;
            }
        }
        public RequesterBlock Requester;
        public SensedDataBlock[] SensedData;

        public ScriptSensorReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ScriptSensorReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 248;
            header.Reliable = true;
            header.Zerocoded = true;
            Requester = new RequesterBlock();
            SensedData = null;
        }

        public ScriptSensorReplyPacket(byte[] bytes, int[] i) 
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
            Requester.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(SensedData == null || SensedData.length != -1) {
                SensedData = new SensedDataBlock[count];
                for(int j = 0; j < count; j++)
                { SensedData[j] = new SensedDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { SensedData[j].FromBytes(bytes, i); }
        }

        public ScriptSensorReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Requester.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(SensedData == null || SensedData.length != count) {
                SensedData = new SensedDataBlock[count];
                for(int j = 0; j < count; j++)
                { SensedData[j] = new SensedDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { SensedData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Requester.length;
            length++;
            for (int j = 0; j < SensedData.length; j++) { length += SensedData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Requester.ToBytes(bytes, i);
            bytes[i++] = (byte)SensedData.length;
            for (int j = 0; j < SensedData.length; j++) { SensedData[j].ToBytes(bytes, i); }
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
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
            }

            fixedLength += Requester.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            Requester.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int SensedDataStart = 0;
            do
            {
                int variableLength = 0;
                int SensedDataCount = 0;

                i = SensedDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < SensedData.length) {
                    int blockLength = SensedData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++SensedDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)SensedDataCount;
                for (i = SensedDataStart; i < SensedDataStart + SensedDataCount; i++) { SensedData[i].ToBytes(packet, ref length); }
                SensedDataStart += SensedDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                SensedDataStart < SensedData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
