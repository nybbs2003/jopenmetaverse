package com.ngt.jopenmetaverse.shared.sim.asset.archiving;

import com.ngt.jopenmetaverse.shared.sim.asset.archiving.TarArchiveReader.TarEntryType;

public class TarEntry {

	String filePath;
	TarEntryType entryType;
	byte[] data;
	int bytesRead;
	long totalBytes;
	
	public TarEntry() {
		super();
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public TarEntryType getEntryType() {
		return entryType;
	}
	public void setEntryType(TarEntryType entryType) {
		this.entryType = entryType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

	public int getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(int bytesRead) {
		this.bytesRead = bytesRead;
	}

	public long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}
}
