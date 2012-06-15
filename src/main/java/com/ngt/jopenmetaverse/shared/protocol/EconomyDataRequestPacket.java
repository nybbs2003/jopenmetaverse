package com.ngt.jopenmetaverse.shared.protocol;

/// <exclude/>
public final class EconomyDataRequestPacket extends Packet
{
	@Override
	public int getLength()
	{
		int length = 10;
		return length;
	}

	public EconomyDataRequestPacket()
	{
		HasVariableBlocks = false;
		Type = PacketType.EconomyDataRequest;
		header = new Header();
		header.Frequency = PacketFrequency.Low;
		header.ID = 24;
		header.Reliable = true;
	}

	public EconomyDataRequestPacket(byte[] bytes, int[] i) throws MalformedDataException
	{
		this();
		int[] packetEnd = new int[] {bytes.length - 1};
		FromBytes(bytes, i, packetEnd, null);
	}

	@Override
	public void FromBytes(byte[] bytes, int[] i, int packetEnd[], byte[] zeroBuffer)
	{
		header.FromBytes(bytes, i, packetEnd);
		if (header.Zerocoded && zeroBuffer != null)
		{
			packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
			bytes = zeroBuffer;
		}
	}

	public EconomyDataRequestPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
	{
		this();
		int[] packetEnd = new int[] {bytes.length - 1};
		FromBytes(head, bytes, i, packetEnd);
	}

	@Override
	public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
	{
		this.header = header;
	}

	@Override
	public  byte[] ToBytes()
	{
		int length = 10;
		if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
		byte[] bytes = new byte[length];
		int[] i = new int[] {0};
		header.ToBytes(bytes, i);
		if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
		return bytes;
	}

	@Override
	public  byte[][] ToBytesMultiple()
	{
		return new byte[][] { ToBytes() };
	}
}