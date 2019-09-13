package com.clubobsidian.dynamicguiconverter.converter;

import java.util.ArrayList;
import java.util.List;

import com.clubobsidian.wrappy.Configuration;

public class Converter {

	private Configuration inputConfig;
	private Configuration outputConfig;
	private String inputPath;
	private String outputPath;
	private List<Transformer> transformers;
	
	public Converter inputConfig(Configuration inputConfig)
	{
		this.inputConfig = inputConfig;
		return this;
	}
	
	public Converter inputPath(String inputPath)
	{
		this.inputPath = inputPath;
		return this;
	}
	
	public Converter outputConfig(Configuration outputConfig)
	{
		this.outputConfig = outputConfig;
		return this;
	}
	
	public Converter outputPath(String outputPath)
	{
		this.outputPath = outputPath;
		return this;
	}
	
	public Converter setIfPathExists(String inputPath, String outputPath, Object toSet)
	{
		if(this.inputConfig.get(inputPath) != null)
		{
			this.outputConfig.set(outputPath, toSet);
		}
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public Converter addTransformer(Transformer transformer)
	{
		if(this.transformers == null)
		{
			this.transformers = new ArrayList<>();
		}
		
		this.transformers.add(transformer);
		return this;
	}

	public Converter convert()
	{
		if(this.inputConfig.get(this.inputPath) != null)
		{
			Object input = this.inputConfig.get(this.inputPath);

			if(this.transformers != null)
			{
				for(Transformer transformer : this.transformers)
				{
					if(transformer.canTransform(input))
					{
						input = transformer.transform(input);
					}
				}
			}
			
			this.outputConfig.set(this.outputPath, input);
		}
		
		
		return this;
	}
}