package com.ngt.jopenmetaverse.shared.protocol;


    public final class RezObjectFromNotecardPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public UUID GroupID;

            @Override
			public int getLength()
            {
                                {
                    return 48;
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
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
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
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class RezDataBlock extends PacketBlock
        {
            public UUID FromTaskID;
            public byte BypassRaycast;
            public Vector3 RayStart;
            public Vector3 RayEnd;
            public UUID RayTargetID;
            public boolean RayEndIsIntersection;
            public boolean RezSelected;
            public boolean RemoveItem;
            public long ItemFlags;
            public long GroupMask;
            public long EveryoneMask;
            public long NextOwnerMask;

            @Override
			public int getLength()
            {
                                {
                    return 76;
                }
            }

            public RezDataBlock() { }
            public RezDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    FromTaskID.FromBytes(bytes, i[0]); i[0] += 16;
                    BypassRaycast = (byte)bytes[i[0]++];
                    RayStart.FromBytes(bytes, i[0]); i[0] += 12;
                    RayEnd.FromBytes(bytes, i[0]); i[0] += 12;
                    RayTargetID.FromBytes(bytes, i[0]); i[0] += 16;
                    RayEndIsIntersection = (bytes[i[0]++] != 0) ? true : false;
                    RezSelected = (bytes[i[0]++] != 0) ? true : false;
                    RemoveItem = (bytes[i[0]++] != 0) ? true : false;
                    ItemFlags = Utils.bytesToUInt(bytes); i[0] += 4;
                    GroupMask = Utils.bytesToUInt(bytes); i[0] += 4;
                    EveryoneMask = Utils.bytesToUInt(bytes); i[0] += 4;
                    NextOwnerMask = Utils.bytesToUInt(bytes); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FromTaskID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = BypassRaycast;
                RayStart.ToBytes(bytes, i[0]); i[0] += 12;
                RayEnd.ToBytes(bytes, i[0]); i[0] += 12;
                RayTargetID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((RayEndIsIntersection) ? 1 : 0);
                bytes[i[0]++] = (byte)((RezSelected) ? 1 : 0);
                bytes[i[0]++] = (byte)((RemoveItem) ? 1 : 0);
                Utils.uintToBytes(ItemFlags, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(GroupMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(EveryoneMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(NextOwnerMask, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class NotecardDataBlock extends PacketBlock
        {
            public UUID NotecardItemID;
            public UUID ObjectID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public NotecardDataBlock() { }
            public NotecardDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    NotecardItemID.FromBytes(bytes, i[0]); i[0] += 16;
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
                NotecardItemID.ToBytes(bytes, i[0]); i[0] += 16;
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class InventoryDataBlock extends PacketBlock
        {
            public UUID ItemID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public InventoryDataBlock() { }
            public InventoryDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ItemID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ItemID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += RezData.length;
                length += NotecardData.length;
                for (int j = 0; j < InventoryData.length; j++)
                    length += InventoryData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RezDataBlock RezData;
        public NotecardDataBlock NotecardData;
        public InventoryDataBlock[] InventoryData;

        public RezObjectFromNotecardPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.RezObjectFromNotecard;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 294;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            RezData = new RezDataBlock();
            NotecardData = new NotecardDataBlock();
            InventoryData = null;
        }

        public RezObjectFromNotecardPacket(byte[] bytes, int[] i) 
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
            RezData.FromBytes(bytes, i);
            NotecardData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(InventoryData == null || InventoryData.length != -1) {
                InventoryData = new InventoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { InventoryData[j] = new InventoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { InventoryData[j].FromBytes(bytes, i); }
        }

        public RezObjectFromNotecardPacket(Header head, byte[] bytes, int[] i)
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
            RezData.FromBytes(bytes, i);
            NotecardData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(InventoryData == null || InventoryData.length != count) {
                InventoryData = new InventoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { InventoryData[j] = new InventoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { InventoryData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += RezData.length;
            length += NotecardData.length;
            length++;
            for (int j = 0; j < InventoryData.length; j++) { length += InventoryData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RezData.ToBytes(bytes, i);
            NotecardData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)InventoryData.length;
            for (int j = 0; j < InventoryData.length; j++) { InventoryData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            fixedLength += RezData.length;
            fixedLength += NotecardData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            RezData.ToBytes(fixedBytes, i);
            NotecardData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int InventoryDataStart = 0;
            do
            {
                int variableLength = 0;
                int InventoryDataCount = 0;

              i[0] =InventoryDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < InventoryData.length) {
                    int blockLength = InventoryData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++InventoryDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)InventoryDataCount;
                for (i[0] = InventoryDataStart; i[0] < InventoryDataStart + InventoryDataCount; i[0]++) { InventoryData[i[0]].ToBytes(packet, length); }
                InventoryDataStart += InventoryDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                InventoryDataStart < InventoryData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
