package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ScriptQuestionPacket extends Packet
    {
        /// <exclude/>
        public static final class DataBlock extends PacketBlock
        {
            public UUID TaskID = new UUID();
            public UUID ItemID = new UUID();
		/** Unsigned Byte */ 
		public byte[] ObjectName;
		/** Unsigned Byte */ 
		public byte[] ObjectOwner;
            public int Questions;

            @Override
			public int getLength()
            {
                                {
                    int length = 38;
                    if (ObjectName != null) { length += ObjectName.length; }
                    if (ObjectOwner != null) { length += ObjectOwner.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TaskID.FromBytes(bytes, i[0]); i[0] += 16;
                    ItemID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ObjectName = new byte[length];
                    Utils.arraycopy(bytes, i[0], ObjectName, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ObjectOwner = new byte[length];
                    Utils.arraycopy(bytes, i[0], ObjectOwner, 0, length); i[0] +=  length;
                    Questions = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TaskID.ToBytes(bytes, i[0]); i[0] += 16;
                ItemID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)ObjectName.length;
                Utils.arraycopy(ObjectName, 0, bytes, i[0], ObjectName.length); i[0] +=  ObjectName.length;
                bytes[i[0]++] = (byte)ObjectOwner.length;
                Utils.arraycopy(ObjectOwner, 0, bytes, i[0], ObjectOwner.length); i[0] +=  ObjectOwner.length;
                Utils.intToBytes(Questions, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += Data.getLength();
                return length;
            }
        }
        public DataBlock Data;

        public ScriptQuestionPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ScriptQuestion;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 188;
            header.Reliable = true;
            Data = new DataBlock();
        }

        public ScriptQuestionPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Data.FromBytes(bytes, i);
        }

        public ScriptQuestionPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
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
