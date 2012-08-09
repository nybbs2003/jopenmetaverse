package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AvatarAnimationPacket extends Packet
    {
        /// <exclude/>
        public static final class SenderBlock extends PacketBlock
        {
            public UUID ID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public SenderBlock() { }
            public SenderBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class AnimationListBlock extends PacketBlock
        {
            public UUID AnimID = new UUID();
            public int AnimSequenceID;

            @Override
			public int getLength()
            {
                                {
                    return 20;
                }
            }

            public AnimationListBlock() { }
            public AnimationListBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AnimID.FromBytes(bytes, i[0]); i[0] += 16;
                    AnimSequenceID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AnimID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(AnimSequenceID, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class AnimationSourceListBlock extends PacketBlock
        {
            public UUID ObjectID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public AnimationSourceListBlock() { }
            public AnimationSourceListBlock(byte[] bytes, int[] i) throws MalformedDataException
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
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class PhysicalAvatarEventListBlock extends PacketBlock
        {
            public byte[] TypeData;

            @Override
			public int getLength()
            {
                                {
                    int length = 1;
                    if (TypeData != null) { length += TypeData.length; }
                    return length;
                }
            }

            public PhysicalAvatarEventListBlock() { }
            public PhysicalAvatarEventListBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    TypeData = new byte[length];
                    Utils.arraycopy(bytes, i[0], TypeData, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)TypeData.length;
                Utils.arraycopy(TypeData, 0, bytes, i[0], TypeData.length); i[0] +=  TypeData.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += Sender.getLength();
                for (int j = 0; j < AnimationList.length; j++)
                    length += AnimationList[j].getLength();
                for (int j = 0; j < AnimationSourceList.length; j++)
                    length += AnimationSourceList[j].getLength();
                for (int j = 0; j < PhysicalAvatarEventList.length; j++)
                    length += PhysicalAvatarEventList[j].getLength();
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

        public AvatarAnimationPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Sender.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AnimationList == null || AnimationList.length != -1) {
                AnimationList = new AnimationListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationList[j] = new AnimationListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationList[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AnimationSourceList == null || AnimationSourceList.length != -1) {
                AnimationSourceList = new AnimationSourceListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationSourceList[j] = new AnimationSourceListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationSourceList[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(PhysicalAvatarEventList == null || PhysicalAvatarEventList.length != -1) {
                PhysicalAvatarEventList = new PhysicalAvatarEventListBlock[count];
                for(int j = 0; j < count; j++)
                { PhysicalAvatarEventList[j] = new PhysicalAvatarEventListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { PhysicalAvatarEventList[j].FromBytes(bytes, i); }
        }

        public AvatarAnimationPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Sender.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AnimationList == null || AnimationList.length != count) {
                AnimationList = new AnimationListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationList[j] = new AnimationListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationList[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AnimationSourceList == null || AnimationSourceList.length != count) {
                AnimationSourceList = new AnimationSourceListBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationSourceList[j] = new AnimationSourceListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationSourceList[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
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
            length += Sender.getLength();
            length++;
            for (int j = 0; j < AnimationList.length; j++) { length += AnimationList[j].getLength(); }
            length++;
            for (int j = 0; j < AnimationSourceList.length; j++) { length += AnimationSourceList[j].getLength(); }
            length++;
            for (int j = 0; j < PhysicalAvatarEventList.length; j++) { length += PhysicalAvatarEventList[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            Sender.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)AnimationList.length;
            for (int j = 0; j < AnimationList.length; j++) { AnimationList[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)AnimationSourceList.length;
            for (int j = 0; j < AnimationSourceList.length; j++) { AnimationSourceList[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)PhysicalAvatarEventList.length;
            for (int j = 0; j < PhysicalAvatarEventList.length; j++) { PhysicalAvatarEventList[j].ToBytes(bytes, i); }
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

            fixedLength += Sender.getLength();
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

              i[0] =AnimationListStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AnimationList.length) {
                    int blockLength = AnimationList[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AnimationListCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =AnimationSourceListStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AnimationSourceList.length) {
                    int blockLength = AnimationSourceList[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AnimationSourceListCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =PhysicalAvatarEventListStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < PhysicalAvatarEventList.length) {
                    int blockLength = PhysicalAvatarEventList[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++PhysicalAvatarEventListCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)AnimationListCount;
                for (i[0] = AnimationListStart; i[0] < AnimationListStart + AnimationListCount; i[0]++) { AnimationList[i[0]].ToBytes(packet, length); }
                AnimationListStart += AnimationListCount;

                packet[length[0]++] = (byte)AnimationSourceListCount;
                for (i[0] = AnimationSourceListStart; i[0] < AnimationSourceListStart + AnimationSourceListCount; i[0]++) { AnimationSourceList[i[0]].ToBytes(packet, length); }
                AnimationSourceListStart += AnimationSourceListCount;

                packet[length[0]++] = (byte)PhysicalAvatarEventListCount;
                for (i[0] = PhysicalAvatarEventListStart; i[0] < PhysicalAvatarEventListStart + PhysicalAvatarEventListCount; i[0]++) { PhysicalAvatarEventList[i[0]].ToBytes(packet, length); }
                PhysicalAvatarEventListStart += PhysicalAvatarEventListCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                AnimationListStart < AnimationList.length ||
                AnimationSourceListStart < AnimationSourceList.length ||
                PhysicalAvatarEventListStart < PhysicalAvatarEventList.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
