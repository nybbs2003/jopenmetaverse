package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;



   /// <exclude/>
    public final class UseCircuitCodePacket extends Packet
    {
        /// <exclude/>
        public static final class CircuitCodeBlock extends PacketBlock
        {
            public long Code;
            public UUID SessionID;
            public UUID ID;

            @Override
            public  int getLength()
            {
                    return 36;
            }

            public CircuitCodeBlock() { }
            public CircuitCodeBlock(byte[] bytes, int i[]) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
            public void FromBytes(byte[] bytes, int i[]) throws MalformedDataException
            {
                try
                {
                    Code = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
            public  void ToBytes(byte[] bytes, int i[])
            {
                Utils.intToBytes((int)Code, bytes, i[0]); i[0] += 4;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                ID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
        public int getLength()
        {
                int length = 10;
                length += CircuitCode.getLength();
                return length;
        }
        public CircuitCodeBlock CircuitCode;

        public UseCircuitCodePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.UseCircuitCode;
            header = new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 3;
            header.Reliable = true;
            CircuitCode = new CircuitCodeBlock();
        }

        public UseCircuitCodePacket(byte[] bytes, int i[]) throws MalformedDataException
        {
        	this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
        public void FromBytes(byte[] bytes, int i[], int packetEnd[], byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            CircuitCode.FromBytes(bytes, i);
        }

        public UseCircuitCodePacket(Header head, byte[] bytes, int i[]) throws MalformedDataException
        {
        	this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
        public void FromBytes(Header header, byte[] bytes, int i[], int packetEnd[]) throws MalformedDataException
        {
            this.header = header;
            CircuitCode.FromBytes(bytes, i);
        }

        @Override
        public byte[] ToBytes()
        {
            int length = 10;
            length += CircuitCode.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            CircuitCode.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
        public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }
