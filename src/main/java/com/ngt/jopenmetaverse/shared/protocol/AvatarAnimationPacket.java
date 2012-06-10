package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarAnimationPacket extends Packet
    {
        /// <exclude/>
        public final class SenderBlock extends PacketBlock
        {
            public UUID ID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
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
            }

        }

        /// <exclude/>
        public final class AnimationListBlock extends PacketBlock
        {
            public UUID AnimID;
            public int AnimSequenceID;

            @Override
			public int getLength()
            {
                get
                {
                    return 20;
                }
            }

            public AnimationListBlock() { }
            public AnimationListBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AnimID.FromBytes(bytes, i); i += 16;
                    AnimSequenceID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AnimID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(AnimSequenceID, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class AnimationSourceListBlock extends PacketBlock
        {
            public UUID ObjectID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public AnimationSourceListBlock() { }
            public AnimationSourceListBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
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
                ObjectID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class PhysicalAvatarEventListBlock extends PacketBlock
        {
            public byte[] TypeData;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 1;
                    if (TypeData != null) { length += TypeData.length; }
                    return length;
                }
            }

            public PhysicalAvatarEventListBlock() { }
            public PhysicalAvatarEventListBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    TypeData = new byte[length];
                    Buffer.BlockCopy(bytes, i, TypeData, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)TypeData.length;
                Buffer.BlockCopy(TypeData, 0, bytes, i, TypeData.length); i += TypeData.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += Sender.length;
                for (int j = 0; j < AnimationList.length; j++)
                    length += AnimationList[j].length;
                for (int j = 0; j < AnimationSourceList.length; j++)
                    length += AnimationSourceList[j].length;
                for (int j = 0; j < PhysicalAvatarEventList.length; j++)
                    length += PhysicalAvatarEventList[j].length;
                return length;
            }
        }
        public SenderBlock Sender;
        public AnimationListBlock[] AnimationList;
        public AnimationSourceListBlock[] AnimationSourceList;
        public PhysicalAvatarEventListBlock[] PhysicalAvatarEventList;

        public AvatarAnimationPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AvatarAnimation;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 20;
            header.Reliable = true;
            Sender = new SenderBlock();
            AnimationList = null;
            AnimationSourceList = null;
            PhysicalAvatarEventList = null;
        }

        public AvatarAnimationPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(AnimationList == null || AnimationList.length != -1) {
                AnimationList = new AnimationListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationList[j] = new AnimationListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationList[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AnimationSourceList == null || AnimationSourceList.length != -1) {
                AnimationSourceList = new AnimationSourceListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationSourceList[j] = new AnimationSourceListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationSourceList[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(PhysicalAvatarEventList == null || PhysicalAvatarEventList.length != -1) {
                PhysicalAvatarEventList = new PhysicalAvatarEventListBlock[count];
                for(int j = 0; j < count; j++)
                { PhysicalAvatarEventList[j] = new PhysicalAvatarEventListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { PhysicalAvatarEventList[j].FromBytes(bytes, i); }
        }

        public AvatarAnimationPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i++];
            if(AnimationList == null || AnimationList.length != count) {
                AnimationList = new AnimationListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationList[j] = new AnimationListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationList[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AnimationSourceList == null || AnimationSourceList.length != count) {
                AnimationSourceList = new AnimationSourceListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationSourceList[j] = new AnimationSourceListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationSourceList[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(PhysicalAvatarEventList == null || PhysicalAvatarEventList.length != count) {
                PhysicalAvatarEventList = new PhysicalAvatarEventListBlock[count];
                for(int j = 0; j < count; j++)
                { PhysicalAvatarEventList[j] = new PhysicalAvatarEventListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { PhysicalAvatarEventList[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += Sender.length;
            length++;
            for (int j = 0; j < AnimationList.length; j++) { length += AnimationList[j].length; }
            length++;
            for (int j = 0; j < AnimationSourceList.length; j++) { length += AnimationSourceList[j].length; }
            length++;
            for (int j = 0; j < PhysicalAvatarEventList.length; j++) { length += PhysicalAvatarEventList[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Sender.ToBytes(bytes, i);
            bytes[i++] = (byte)AnimationList.length;
            for (int j = 0; j < AnimationList.length; j++) { AnimationList[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)AnimationSourceList.length;
            for (int j = 0; j < AnimationSourceList.length; j++) { AnimationSourceList[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)PhysicalAvatarEventList.length;
            for (int j = 0; j < PhysicalAvatarEventList.length; j++) { PhysicalAvatarEventList[j].ToBytes(bytes, i); }
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

            fixedLength += Sender.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            Sender.ToBytes(fixedBytes, i);
            fixedLength += 3;

            int AnimationListStart = 0;
            int AnimationSourceListStart = 0;
            int PhysicalAvatarEventListStart = 0;
            do
            {
                int variableLength = 0;
                int AnimationListCount = 0;
                int AnimationSourceListCount = 0;
                int PhysicalAvatarEventListCount = 0;

                i = AnimationListStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AnimationList.length) {
                    int blockLength = AnimationList[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AnimationListCount;
                    }
                    else { break; }
                    ++i;
                }

                i = AnimationSourceListStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AnimationSourceList.length) {
                    int blockLength = AnimationSourceList[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AnimationSourceListCount;
                    }
                    else { break; }
                    ++i;
                }

                i = PhysicalAvatarEventListStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < PhysicalAvatarEventList.length) {
                    int blockLength = PhysicalAvatarEventList[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++PhysicalAvatarEventListCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)AnimationListCount;
                for (i = AnimationListStart; i < AnimationListStart + AnimationListCount; i++) { AnimationList[i].ToBytes(packet, ref length); }
                AnimationListStart += AnimationListCount;

                packet[length++] = (byte)AnimationSourceListCount;
                for (i = AnimationSourceListStart; i < AnimationSourceListStart + AnimationSourceListCount; i++) { AnimationSourceList[i].ToBytes(packet, ref length); }
                AnimationSourceListStart += AnimationSourceListCount;

                packet[length++] = (byte)PhysicalAvatarEventListCount;
                for (i = PhysicalAvatarEventListStart; i < PhysicalAvatarEventListStart + PhysicalAvatarEventListCount; i++) { PhysicalAvatarEventList[i].ToBytes(packet, ref length); }
                PhysicalAvatarEventListStart += PhysicalAvatarEventListCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                AnimationListStart < AnimationList.length ||
                AnimationSourceListStart < AnimationSourceList.length ||
                PhysicalAvatarEventListStart < PhysicalAvatarEventList.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
