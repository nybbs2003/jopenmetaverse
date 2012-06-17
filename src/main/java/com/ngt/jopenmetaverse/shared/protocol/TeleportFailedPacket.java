package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class TeleportFailedPacket extends Packet
    {
        /// <exclude/>
        public final class InfoBlock extends PacketBlock
        {
            public UUID AgentID;
		/** Unsigned Byte */ 
		public byte[] Reason;

            @Override
			public int getLength()
            {
                                {
                    int length = 17;
                    if (Reason != null) { length += Reason.length; }
                    return length;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Reason = new byte[length];
                    Utils.arraycopy(bytes, i[0], Reason, 0, length); i[0] +=  length;
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
                bytes[i[0]++] = (byte)Reason.length;
                Utils.arraycopy(Reason, 0, bytes, i[0], Reason.length); i[0] +=  Reason.length;
            }

        }

        /// <exclude/>
        public final class AlertInfoBlock extends PacketBlock
        {
            public byte[] Message;
		/** Unsigned Byte */ 
		public byte[] ExtraParams;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (Message != null) { length += Message.length; }
                    if (ExtraParams != null) { length += ExtraParams.length; }
                    return length;
                }
            }

            public AlertInfoBlock() { }
            public AlertInfoBlock(byte[] bytes, int[] i) throws MalformedDataException
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
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i[0], Message, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ExtraParams = new byte[length];
                    Utils.arraycopy(bytes, i[0], ExtraParams, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)Message.length;
                Utils.arraycopy(Message, 0, bytes, i[0], Message.length); i[0] +=  Message.length;
                bytes[i[0]++] = (byte)ExtraParams.length;
                Utils.arraycopy(ExtraParams, 0, bytes, i[0], ExtraParams.length); i[0] +=  ExtraParams.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += Info.getLength();
                for (int j = 0; j < AlertInfo.length; j++)
                    length += AlertInfo[j].getLength();
                return length;
            }
        }
        public InfoBlock Info;
        public AlertInfoBlock[] AlertInfo;

        public TeleportFailedPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.TeleportFailed;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 74;
            header.Reliable = true;
            Info = new InfoBlock();
            AlertInfo = null;
        }

        public TeleportFailedPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Info.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AlertInfo == null || AlertInfo.length != -1) {
                AlertInfo = new AlertInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AlertInfo[j] = new AlertInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AlertInfo[j].FromBytes(bytes, i); }
        }

        public TeleportFailedPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Info.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AlertInfo == null || AlertInfo.length != count) {
                AlertInfo = new AlertInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AlertInfo[j] = new AlertInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AlertInfo[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Info.getLength();
            length++;
            for (int j = 0; j < AlertInfo.length; j++) { length += AlertInfo[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)AlertInfo.length;
            for (int j = 0; j < AlertInfo.length; j++) { AlertInfo[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += Info.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            Info.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int AlertInfoStart = 0;
            do
            {
                int variableLength = 0;
                int AlertInfoCount = 0;

              i[0] =AlertInfoStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AlertInfo.length) {
                    int blockLength = AlertInfo[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AlertInfoCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)AlertInfoCount;
                for (i[0] = AlertInfoStart; i[0] < AlertInfoStart + AlertInfoCount; i[0]++) { AlertInfo[i[0]].ToBytes(packet, length); }
                AlertInfoStart += AlertInfoCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                AlertInfoStart < AlertInfo.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
