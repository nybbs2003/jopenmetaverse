package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ViewerEffectPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID SessionID = new UUID();

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
        public static final class EffectBlock extends PacketBlock
        {
            public UUID ID = new UUID();
            public UUID AgentID = new UUID();
		/** Unsigned Byte */ 
		public byte Type;
            public float Duration;
		/** Unsigned Byte */ 
		public byte[] Color;
		/** Unsigned Byte */ 
		public byte[] TypeData;

            @Override
			public int getLength()
            {
                                {
                    int length = 42;
                    if (TypeData != null) { length += TypeData.length; }
                    return length;
                }
            }

            public EffectBlock() { }
            public EffectBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    Type = (byte)bytes[i[0]++];
                    Duration = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Color = new byte[4];
                    Utils.arraycopy(bytes, i[0], Color, 0, 4); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    TypeData = new byte[length];
                    Utils.arraycopy(bytes, i[0], TypeData, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = Type;
                Utils.floatToBytes(Duration, bytes, i[0]); i[0] += 4;
                Utils.arraycopy(Color, 0, bytes, i[0], 4); i[0] += 4;
                bytes[i[0]++] = (byte)TypeData.length;
                Utils.arraycopy(TypeData, 0, bytes, i[0], TypeData.length); i[0] +=  TypeData.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 9;
                length += AgentData.getLength();
                for (int j = 0; j < Effect.length; j++)
                    length += Effect[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public EffectBlock[] Effect;

        public ViewerEffectPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ViewerEffect;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 17;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Effect = null;
        }

        public ViewerEffectPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(Effect == null || Effect.length != -1) {
                Effect = new EffectBlock[count];
                for(int j = 0; j < count; j++)
                { Effect[j] = new EffectBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Effect[j].FromBytes(bytes, i); }
        }

        public ViewerEffectPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(Effect == null || Effect.length != count) {
                Effect = new EffectBlock[count];
                for(int j = 0; j < count; j++)
                { Effect[j] = new EffectBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Effect[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < Effect.length; j++) { length += Effect[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Effect.length;
            for (int j = 0; j < Effect.length; j++) { Effect[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 8;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int EffectStart = 0;
            do
            {
                int variableLength = 0;
                int EffectCount = 0;

              i[0] =EffectStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < Effect.length) {
                    int blockLength = Effect[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++EffectCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)EffectCount;
                for (i[0] = EffectStart; i[0] < EffectStart + EffectCount; i[0]++) { Effect[i[0]].ToBytes(packet, length); }
                EffectStart += EffectCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                EffectStart < Effect.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
