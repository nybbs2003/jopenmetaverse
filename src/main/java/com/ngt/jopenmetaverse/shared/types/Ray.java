package com.ngt.jopenmetaverse.shared.types;

public class Ray {
	public Vector3 Origin;
	public Vector3 Direction;

	public Ray(Vector3 origin, Vector3 direction) {
		Origin = origin;
		Direction = direction;
	}
}
