package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AbortXferPacket extends Packet
    {
        /// <exclude/>
        public static final class XferIDBlock extends PacketBlock
        {
            public BigInteger ID;
            public int Result;

            @Override
			public int getLength()
            {
                    return 12;
            }

            public XferIDBlock() { }
            public XferIDBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID = Utils.bytesToULongLit(bytes, i[0]); i[0] += 8;
                    Result = Utils.bytesToIntLit(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytesLit(ID, bytes, i[0]); i[0] += 8;
                Utils.intToBytesLit(Result, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                int length = 10;
                length += XferID.getLength();
                return length;
        }
        public XferIDBlock XferID;

        public AbortXferPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AbortXfer;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 157;
            header.Reliable = true;
            XferID = new XferIDBlock();
        }

        public AbortXferPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            XferID.FromBytes(bytes, i);
        }

        public AbortXferPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            XferID.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += XferID.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            XferID.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }
