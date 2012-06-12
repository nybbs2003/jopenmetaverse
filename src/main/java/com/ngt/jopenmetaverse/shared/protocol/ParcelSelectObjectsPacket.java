package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelSelectObjectsPacket extends Packet
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
        public final class ParcelDataBlock extends PacketBlock
        {
            public int LocalID;
            public uint ReturnType;

            @Override
			public int getLength()
            {
                                {
                    return 8;
                }
            }

            public ParcelDataBlock() { }
            public ParcelDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    LocalID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ReturnType = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(LocalID, bytes, i); i += 4;
                Utils.UIntToBytes(ReturnType, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class ReturnIDsBlock extends PacketBlock
        {
            public UUID ReturnID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public ReturnIDsBlock() { }
            public ReturnIDsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ReturnID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ReturnID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += ParcelData.length;
                for (int j = 0; j < ReturnIDs.length; j++)
                    length += ReturnIDs[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ParcelDataBlock ParcelData;
        public ReturnIDsBlock[] ReturnIDs;

        public ParcelSelectObjectsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ParcelSelectObjects;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 202;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ParcelData = new ParcelDataBlock();
            ReturnIDs = null;
        }

        public ParcelSelectObjectsPacket(byte[] bytes, int[] i) 
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
            ParcelData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ReturnIDs == null || ReturnIDs.length != -1) {
                ReturnIDs = new ReturnIDsBlock[count];
                for(int j = 0; j < count; j++)
                { ReturnIDs[j] = new ReturnIDsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ReturnIDs[j].FromBytes(bytes, i); }
        }

        public ParcelSelectObjectsPacket(Header head, byte[] bytes, int[] i)
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
            ParcelData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ReturnIDs == null || ReturnIDs.length != count) {
                ReturnIDs = new ReturnIDsBlock[count];
                for(int j = 0; j < count; j++)
                { ReturnIDs[j] = new ReturnIDsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ReturnIDs[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ParcelData.length;
            length++;
            for (int j = 0; j < ReturnIDs.length; j++) { length += ReturnIDs[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ParcelData.ToBytes(bytes, i);
            bytes[i++] = (byte)ReturnIDs.length;
            for (int j = 0; j < ReturnIDs.length; j++) { ReturnIDs[j].ToBytes(bytes, i); }
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

            fixedLength += AgentData.getLength();
            fixedLength += ParcelData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            ParcelData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ReturnIDsStart = 0;
            do
            {
                int variableLength = 0;
                int ReturnIDsCount = 0;

                i = ReturnIDsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ReturnIDs.length) {
                    int blockLength = ReturnIDs[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ReturnIDsCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ReturnIDsCount;
                for (i = ReturnIDsStart; i < ReturnIDsStart + ReturnIDsCount; i++) { ReturnIDs[i].ToBytes(packet, ref length); }
                ReturnIDsStart += ReturnIDsCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                ReturnIDsStart < ReturnIDs.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
