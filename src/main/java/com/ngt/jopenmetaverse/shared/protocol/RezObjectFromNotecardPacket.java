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
                get
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
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
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
                GroupID.ToBytes(bytes, i); i += 16;
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
            public bool RayEndIsIntersection;
            public bool RezSelected;
            public bool RemoveItem;
            public uint ItemFlags;
            public uint GroupMask;
            public uint EveryoneMask;
            public uint NextOwnerMask;

            @Override
			public int getLength()
            {
                get
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
                    FromTaskID.FromBytes(bytes, i); i += 16;
                    BypassRaycast = (byte)bytes[i++];
                    RayStart.FromBytes(bytes, i); i += 12;
                    RayEnd.FromBytes(bytes, i); i += 12;
                    RayTargetID.FromBytes(bytes, i); i += 16;
                    RayEndIsIntersection = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    RezSelected = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    RemoveItem = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    ItemFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    EveryoneMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    NextOwnerMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FromTaskID.ToBytes(bytes, i); i += 16;
                bytes[i++] = BypassRaycast;
                RayStart.ToBytes(bytes, i); i += 12;
                RayEnd.ToBytes(bytes, i); i += 12;
                RayTargetID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((RayEndIsIntersection) ? 1 : 0);
                bytes[i++] = (byte)((RezSelected) ? 1 : 0);
                bytes[i++] = (byte)((RemoveItem) ? 1 : 0);
                Utils.UIntToBytes(ItemFlags, bytes, i); i += 4;
                Utils.UIntToBytes(GroupMask, bytes, i); i += 4;
                Utils.UIntToBytes(EveryoneMask, bytes, i); i += 4;
                Utils.UIntToBytes(NextOwnerMask, bytes, i); i += 4;
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
                get
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
                    NotecardItemID.FromBytes(bytes, i); i += 16;
                    ObjectID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                NotecardItemID.ToBytes(bytes, i); i += 16;
                ObjectID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class InventoryDataBlock extends PacketBlock
        {
            public UUID ItemID;

            @Override
			public int getLength()
            {
                get
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
                    ItemID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ItemID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += RezData.length;
                length += NotecardData.length;
                for (int j = 0; j < InventoryData.length; j++)
                    length += InventoryData[j].length;
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            RezData.FromBytes(bytes, i);
            NotecardData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
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
            for (int j = 0; j < InventoryData.length; j++) { length += InventoryData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RezData.ToBytes(bytes, i);
            NotecardData.ToBytes(bytes, i);
            bytes[i++] = (byte)InventoryData.length;
            for (int j = 0; j < InventoryData.length; j++) { InventoryData[j].ToBytes(bytes, i); }
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

                i = InventoryDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < InventoryData.length) {
                    int blockLength = InventoryData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++InventoryDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)InventoryDataCount;
                for (i = InventoryDataStart; i < InventoryDataStart + InventoryDataCount; i++) { InventoryData[i].ToBytes(packet, ref length); }
                InventoryDataStart += InventoryDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                InventoryDataStart < InventoryData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
