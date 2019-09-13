package com.clubobsidian.dynamicguiconverter.converter.transformer;

import java.util.ArrayList;
import java.util.List;

import com.clubobsidian.dynamicguiconverter.converter.Transformer;

public class SetLoreTransformer implements Transformer<List<String>> {

	@Override
	public List<String> transform(List<String> toTransform) 
	{
		//Just incase it is an unmodifiable list
		List<String> newList = new ArrayList<>();
		
		for(String str : toTransform)
		{
			if(str.toLowerCase().contains("setlore"))
			{
				newList.add(str.replace(";", "\n"));
			}
			else
			{
				newList.add(str);
			}
		}
		
		return newList;
	}

	@Override
	public boolean canTransform(Object toTransform) 
	{
		if(toTransform instanceof List)
		{
			@SuppressWarnings("rawtypes")
			List list = (List) toTransform;
			if(list.size() > 0)
			{
				return list.get(0) instanceof String;
			}
		}
		return false;
	}
}