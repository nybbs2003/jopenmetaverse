package com.ngt.jopenmetaverse.shared.protocol;


    public final class AcceptFriendshipPacket extends Packet
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
        public final class TransactionBlockBlock extends PacketBlock
        {
            public UUID TransactionID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public TransactionBlockBlock() { }
            public TransactionBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TransactionID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class FolderDataBlock extends PacketBlock
        {
            public UUID FolderID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public FolderDataBlock() { }
            public FolderDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    FolderID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FolderID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += TransactionBlock.length;
                for (int j = 0; j < FolderData.length; j++)
                    length += FolderData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public TransactionBlockBlock TransactionBlock;
        public FolderDataBlock[] FolderData;

        public AcceptFriendshipPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AcceptFriendship;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 297;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            TransactionBlock = new TransactionBlockBlock();
            FolderData = null;
        }

        public AcceptFriendshipPacket(byte[] bytes, int[] i) 
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
            TransactionBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(FolderData == null || FolderData.length != -1) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
        }

        public AcceptFriendshipPacket(Header head, byte[] bytes, int[] i)
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
            TransactionBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(FolderData == null || FolderData.length != count) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += TransactionBlock.length;
            length++;
            for (int j = 0; j < FolderData.length; j++) { length += FolderData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            TransactionBlock.ToBytes(bytes, i);
            bytes[i++] = (byte)FolderData.length;
            for (int j = 0; j < FolderData.length; j++) { FolderData[j].ToBytes(bytes, i); }
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

            fixedLength += AgentData.getLength();
            fixedLength += TransactionBlock.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            TransactionBlock.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int FolderDataStart = 0;
            do
            {
                int variableLength = 0;
                int FolderDataCount = 0;

                i = FolderDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < FolderData.length) {
                    int blockLength = FolderData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++FolderDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)FolderDataCount;
                for (i = FolderDataStart; i < FolderDataStart + FolderDataCount; i++) { FolderData[i].ToBytes(packet, ref length); }
                FolderDataStart += FolderDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                FolderDataStart < FolderData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
