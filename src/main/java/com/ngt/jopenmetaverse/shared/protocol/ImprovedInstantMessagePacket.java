package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ImprovedInstantMessagePacket extends Packet
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

        /// <exclude/>
        public final class MessageBlockBlock extends PacketBlock
        {
            public boolean FromGroup;
            public UUID ToAgentID;
            public long ParentEstateID;
            public UUID RegionID;
            public Vector3 Position;
            public byte Offline;
            public byte Dialog;
            public UUID ID;
            public long Timestamp;
            public byte[] FromAgentName;
            public byte[] Message;
            public byte[] BinaryBucket;

            @Override
			public int getLength()
            {
                                {
                    int length = 76;
                    if (FromAgentName != null) { length += FromAgentName.length; }
                    if (Message != null) { length += Message.length; }
                    if (BinaryBucket != null) { length += BinaryBucket.length; }
                    return length;
                }
            }

            public MessageBlockBlock() { }
            public MessageBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    FromGroup = (bytes[i[0]++] != 0) ? true : false;
                    ToAgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    ParentEstateID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    RegionID.FromBytes(bytes, i[0]); i[0] += 16;
                    Position.fromBytes(bytes, i[0]); i[0] += 12;
                    Offline = (byte)bytes[i[0]++];
                    Dialog = (byte)bytes[i[0]++];
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                    Timestamp = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    FromAgentName = new byte[length];
                    Utils.arraycopy(bytes, i[0], FromAgentName, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i[0], Message, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    BinaryBucket = new byte[length];
                    Utils.arraycopy(bytes, i[0], BinaryBucket, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((FromGroup) ? 1 : 0);
                ToAgentID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(ParentEstateID, bytes, i[0]); i[0] += 4;
                RegionID.ToBytes(bytes, i[0]); i[0] += 16;
                Position.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = Offline;
                bytes[i[0]++] = Dialog;
                ID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(Timestamp, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)FromAgentName.length;
                Utils.arraycopy(FromAgentName, 0, bytes, i[0], FromAgentName.length); i[0] +=  FromAgentName.length;
                bytes[i[0]++] = (byte)(Message.length % 256);
                bytes[i[0]++] = (byte)((Message.length >> 8) % 256);
                Utils.arraycopy(Message, 0, bytes, i[0], Message.length); i[0] +=  Message.length;
                bytes[i[0]++] = (byte)(BinaryBucket.length % 256);
                bytes[i[0]++] = (byte)((BinaryBucket.length >> 8) % 256);
                Utils.arraycopy(BinaryBucket, 0, bytes, i[0], BinaryBucket.length); i[0] +=  BinaryBucket.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += MessageBlock.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MessageBlockBlock MessageBlock;

        public ImprovedInstantMessagePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ImprovedInstantMessage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 254;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            MessageBlock = new MessageBlockBlock();
        }

        public ImprovedInstantMessagePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            AgentData.FromBytes(bytes, i);
            MessageBlock.FromBytes(bytes, i);
        }

        public ImprovedInstantMessagePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            MessageBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += MessageBlock.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            MessageBlock.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
