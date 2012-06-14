package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class PayPriceReplyPacket extends Packet
    {
        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public UUID ObjectID;
            public int DefaultPayPrice;

            @Override
			public int getLength()
            {
                                {
                    return 20;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    DefaultPayPrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(DefaultPayPrice, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class ButtonDataBlock extends PacketBlock
        {
            public int PayButton;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public ButtonDataBlock() { }
            public ButtonDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PayButton = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(PayButton, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += ObjectData.getLength();
                for (int j = 0; j < ButtonData.length; j++)
                    length += ButtonData[j].getLength();
                return length;
            }
        }
        public ObjectDataBlock ObjectData;
        public ButtonDataBlock[] ButtonData;

        public PayPriceReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.PayPriceReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 162;
            header.Reliable = true;
            ObjectData = new ObjectDataBlock();
            ButtonData = null;
        }

        public PayPriceReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(ButtonData == null || ButtonData.length != -1) {
                ButtonData = new ButtonDataBlock[count];
                for(int j = 0; j < count; j++)
                { ButtonData[j] = new ButtonDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ButtonData[j].FromBytes(bytes, i); }
        }

        public PayPriceReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(ButtonData == null || ButtonData.length != count) {
                ButtonData = new ButtonDataBlock[count];
                for(int j = 0; j < count; j++)
                { ButtonData[j] = new ButtonDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ButtonData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ObjectData.getLength();
            length++;
            for (int j = 0; j < ButtonData.length; j++) { length += ButtonData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ButtonData.length;
            for (int j = 0; j < ButtonData.length; j++) { ButtonData[j].ToBytes(bytes, i); }
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

            fixedLength += ObjectData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            ObjectData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ButtonDataStart = 0;
            do
            {
                int variableLength = 0;
                int ButtonDataCount = 0;

              i[0] =ButtonDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ButtonData.length) {
                    int blockLength = ButtonData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ButtonDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ButtonDataCount;
                for (i[0] = ButtonDataStart; i[0] < ButtonDataStart + ButtonDataCount; i[0]++) { ButtonData[i[0]].ToBytes(packet, length); }
                ButtonDataStart += ButtonDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ButtonDataStart < ButtonData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
