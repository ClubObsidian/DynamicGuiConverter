package com.clubobsidian.dynamicguiconverter.converter;

public interface Transformer<T> {

	public boolean canTransform(Object toTransform);
	public T transform(T toTransform);
	
}