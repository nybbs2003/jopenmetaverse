package com.ngt.jopenmetaverse.shared.types;

public interface Predicate<T>
{
	public boolean match(T t);
}