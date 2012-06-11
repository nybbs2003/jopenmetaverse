package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ParcelDisableObjectsPacket extends Packet
    {
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                    return 32;
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
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

        public final class ParcelDataBlock extends PacketBlock
        {
            public int LocalID;
            public uint ReturnType;

            @Override
			public int getLength()
            {
                    return 8;
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
                    LocalID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ReturnType = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(LocalID, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(ReturnType, bytes, i[0]); i[0] += 4;
            }

        }

       
        public final class TaskIDsBlock extends PacketBlock
        {
            public UUID TaskID;

            @Override
			public int getLength()
            {
                    return 16;
            }

            public TaskIDsBlock() { }
            public TaskIDsBlock(byte[] bytes, int[] i)
            {
                try {
					FromBytes(bytes, i);
				} catch (MalformedDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TaskID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TaskID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class OwnerIDsBlock extends PacketBlock
        {
            public UUID OwnerID;

            @Override
			public int getLength()
            {
                    return 16;
            }

            public OwnerIDsBlock() { }
            public OwnerIDsBlock(byte[] bytes, int[] i)
            {
                try {
					FromBytes(bytes, i);
				} catch (MalformedDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                int length = 12;
                length += AgentData.getLength();
                length += ParcelData.getlength();
                for (int j = 0; j < TaskIDs.length; j++)
                    length += TaskIDs[j].getlength();
                for (int j = 0; j < OwnerIDs.length; j++)
                    length += OwnerIDs[j].getlength();
                return length;
        }
        public AgentDataBlock AgentData;
        public ParcelDataBlock ParcelData;
        public TaskIDsBlock[] TaskIDs;
        public OwnerIDsBlock[] OwnerIDs;

        public ParcelDisableObjectsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ParcelDisableObjects;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 201;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ParcelData = new ParcelDataBlock();
            TaskIDs = null;
            OwnerIDs = null;
        }

        public ParcelDisableObjectsPacket(byte[] bytes, int[] i) 
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
            ParcelData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(TaskIDs == null || TaskIDs.length != -1) {
                TaskIDs = new TaskIDsBlock[count];
                for(int j = 0; j < count; j++)
                { TaskIDs[j] = new TaskIDsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { TaskIDs[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(OwnerIDs == null || OwnerIDs.length != -1) {
                OwnerIDs = new OwnerIDsBlock[count];
                for(int j = 0; j < count; j++)
                { OwnerIDs[j] = new OwnerIDsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { OwnerIDs[j].FromBytes(bytes, i); }
        }

        public ParcelDisableObjectsPacket(Header head, byte[] bytes, int[] i)
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
            if(TaskIDs == null || TaskIDs.length != count) {
                TaskIDs = new TaskIDsBlock[count];
                for(int j = 0; j < count; j++)
                { TaskIDs[j] = new TaskIDsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { TaskIDs[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(OwnerIDs == null || OwnerIDs.length != count) {
                OwnerIDs = new OwnerIDsBlock[count];
                for(int j = 0; j < count; j++)
                { OwnerIDs[j] = new OwnerIDsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { OwnerIDs[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ParcelData.length;
            length++;
            for (int j = 0; j < TaskIDs.length; j++) { length += TaskIDs[j].length; }
            length++;
            for (int j = 0; j < OwnerIDs.length; j++) { length += OwnerIDs[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ParcelData.ToBytes(bytes, i);
            bytes[i++] = (byte)TaskIDs.length;
            for (int j = 0; j < TaskIDs.length; j++) { TaskIDs[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)OwnerIDs.length;
            for (int j = 0; j < OwnerIDs.length; j++) { OwnerIDs[j].ToBytes(bytes, i); }
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
            fixedLength += ParcelData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            ParcelData.ToBytes(fixedBytes, i);
            fixedLength += 2;

            int TaskIDsStart = 0;
            int OwnerIDsStart = 0;
            do
            {
                int variableLength = 0;
                int TaskIDsCount = 0;
                int OwnerIDsCount = 0;

                i = TaskIDsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < TaskIDs.length) {
                    int blockLength = TaskIDs[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++TaskIDsCount;
                    }
                    else { break; }
                    ++i;
                }

                i = OwnerIDsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < OwnerIDs.length) {
                    int blockLength = OwnerIDs[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++OwnerIDsCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)TaskIDsCount;
                for (i = TaskIDsStart; i < TaskIDsStart + TaskIDsCount; i++) { TaskIDs[i].ToBytes(packet, ref length); }
                TaskIDsStart += TaskIDsCount;

                packet[length++] = (byte)OwnerIDsCount;
                for (i = OwnerIDsStart; i < OwnerIDsStart + OwnerIDsCount; i++) { OwnerIDs[i].ToBytes(packet, ref length); }
                OwnerIDsStart += OwnerIDsCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                TaskIDsStart < TaskIDs.length ||
                OwnerIDsStart < OwnerIDs.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
