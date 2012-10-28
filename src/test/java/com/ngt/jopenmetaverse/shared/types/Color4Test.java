/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.ngt.jopenmetaverse.shared.types;

import junit.framework.Assert;

import org.junit.Test;

public class Color4Test {

	@Test
	public void colorCreationTests()
	{
		Color4 color1 = new Color4(0.0, 0.0, 0.0, 0.0);
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), 0.0, 0.0);
		Assert.assertEquals(color1.getG(), 0.0, 0.0);
		Assert.assertEquals(color1.getB(), 0.0, 0.0);
		Assert.assertEquals(color1.getA(), 0.0, 0.0);

		color1 = new Color4(1.0, 1.0, 1.0, 1.0);
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), 1.0, 1.0);
		Assert.assertEquals(color1.getG(), 1.0, 1.0);
		Assert.assertEquals(color1.getB(), 1.0, 1.0);
		Assert.assertEquals(color1.getA(), 1.0, 1.0);		
		
		color1 = new Color4(0.5, 0.2, 0.9, 0.01);
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), 0.5, 0.001);
		Assert.assertEquals(color1.getG(), 0.2, 0.001);
		Assert.assertEquals(color1.getB(), 0.9, 0.001);
		Assert.assertEquals(color1.getA(), 0.01, 0.001);

		color1 = new Color4(1.5, 1.8, 1.02, .5);
		double total = 1.8;
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), 1.5/total, 0.001);
		Assert.assertEquals(color1.getG(), 1.8/total, 0.001);
		Assert.assertEquals(color1.getB(), 1.02/total, 0.001);
		Assert.assertEquals(color1.getA(), 0.5/total, 0.001);		

		colorCreationTest(23, 11, 200, 250);
		
		colorCreationTest((short)23, (short)11, (short)200, (short)250);
	
//		Assert.assertEquals(-3, (byte)((0xff*0xfe) >> 8));
//		Assert.assertEquals(0xff * 0xfe, (((byte)0xff*(byte)0xfe)));
//		Assert.assertEquals( (0xff * 0xfe) >> 8, (((int)(byte)0xff*(int)(byte)0xfe)) >> 8);		
		
	}
	
	public void colorCreationTest(double r, double g, double b, double a)
	{
		Color4 color1 = new Color4(r, g, b, a);
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), r, 0.001);
		Assert.assertEquals(color1.getG(), g, 0.001);
		Assert.assertEquals(color1.getB(), b, 0.001);
		Assert.assertEquals(color1.getA(), a, 0.001);
	}
	
	public void colorCreationTest(short r, short g, short b, short a)
	{
		Color4 color1 = new Color4(r, g, b, a);
		
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), (float)r/255, 0.001);
		Assert.assertEquals(color1.getG(), (float)g/255, 0.001);
		Assert.assertEquals(color1.getB(), (float)b/255, 0.001);
		Assert.assertEquals(color1.getA(), (float)a/255, 0.001);
	}
	
	public void colorCreationTest(int r, int g, int b, int a)
	{
		Color4 color1 = new Color4(r, g, b, a);
		
		System.out.println(color1.toRGBString());
		Assert.assertEquals(color1.getR(), (float)r/255, 0.001);
		Assert.assertEquals(color1.getG(), (float)g/255, 0.001);
		Assert.assertEquals(color1.getB(), (float)b/255, 0.001);
		Assert.assertEquals(color1.getA(), (float)a/255, 0.001);
	}
	
}
