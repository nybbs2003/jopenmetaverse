package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class LoadURLPacket extends Packet
    {
        /// <exclude/>
        public static final class DataBlock extends PacketBlock
        {
            public byte[] ObjectName;
            public UUID ObjectID = new UUID();
            public UUID OwnerID = new UUID();
            public boolean OwnerIsGroup;
		/** Unsigned Byte */ 
		public byte[] Message;
		/** Unsigned Byte */ 
		public byte[] URL;

            @Override
			public int getLength()
            {
                                {
                    int length = 36;
                    if (ObjectName != null) { length += ObjectName.length; }
                    if (Message != null) { length += Message.length; }
                    if (URL != null) { length += URL.length; }
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
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ObjectName = new byte[length];
                    Utils.arraycopy(bytes, i[0], ObjectName, 0, length); i[0] +=  length;
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerIsGroup = (bytes[i[0]++] != 0) ? true : false;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i[0], Message, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    URL = new byte[length];
                    Utils.arraycopy(bytes, i[0], URL, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)ObjectName.length;
                Utils.arraycopy(ObjectName, 0, bytes, i[0], ObjectName.length); i[0] +=  ObjectName.length;
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((OwnerIsGroup) ? 1 : 0);
                bytes[i[0]++] = (byte)Message.length;
                Utils.arraycopy(Message, 0, bytes, i[0], Message.length); i[0] +=  Message.length;
                bytes[i[0]++] = (byte)URL.length;
                Utils.arraycopy(URL, 0, bytes, i[0], URL.length); i[0] +=  URL.length;
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

        public LoadURLPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.LoadURL;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 194;
            header.Reliable = true;
            Data = new DataBlock();
        }

        public LoadURLPacket(byte[] bytes, int[] i) throws MalformedDataException 
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

        public LoadURLPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
