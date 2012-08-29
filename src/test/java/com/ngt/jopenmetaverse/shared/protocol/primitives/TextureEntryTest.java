package com.ngt.jopenmetaverse.shared.protocol.primitives;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.types.UUID;

public class TextureEntryTest {

	@Test
	public void FaceBitfieldByteTests()
	{
		TextureEntry te = new TextureEntry(UUID.Random());
		long l1 = 256;
		byte[] bytes1 = te.GetFaceBitfieldBytes(l1);
		int[] i = new int[]{0};
		long[] fbytes = new long[1];
		long[] flen = new long[]{1L};
		te.ReadFaceBitfield(bytes1, i, fbytes, flen);
		Assert.assertEquals(l1, fbytes[0]);
	}
}
