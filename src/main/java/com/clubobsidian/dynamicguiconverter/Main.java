package com.clubobsidian.dynamicguiconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.clubobsidian.dynamicguiconverter.converter.Converter;
import com.clubobsidian.dynamicguiconverter.converter.transformer.SetLoreTransformer;
import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationSection;

public class Main {

	public static void main(String[] args)
	{
		File inputFolder = new File("input");
		if(!inputFolder.exists())
		{
			inputFolder.mkdirs();
		}
		
		File outputFolder = new File("output");

		if(outputFolder.exists())
		{
			try 
			{
				FileUtils.deleteDirectory(outputFolder);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		outputFolder.mkdirs();

		for(File inputFile : inputFolder.listFiles())
		{
			File outputFile = new File(outputFolder, inputFile.getName());
			if(!outputFile.exists())
			{
				try 
				{
					outputFile.createNewFile();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
			Configuration inputConfig = Configuration.load(inputFile);
			Configuration outputConfig = Configuration.load(outputFile);

			new Converter()
			.inputConfig(inputConfig)
			.outputConfig(outputConfig)
			.inputPath("gui-title") //Title
			.outputPath("title")
			.convert()
			.inputPath("rows") //Rows
			.outputPath("rows")
			.convert()
			.inputPath("close")//Close
			.outputPath("close")
			.convert()
			.inputPath("alias") //Alias
			.outputPath("alias")
			.convert()
			.inputPath("npc-ids") //Npcs
			.outputPath("npcs.citizens")
			.convert();

			for(int i = 0; i < 54; i++)
			{
				if(inputConfig.hasKey(String.valueOf(i)))
				{
					String path = i + ".";
					String functionPath = path + "functions.";

					//Function types

					//Fail
					List<String> fail = new ArrayList<>();
					fail.add("fail");
					
					//Load
					List<String> load = new ArrayList<>();
					load.add("load");

					//All
					List<String> allClicks = new ArrayList<>();
					allClicks.add("all");

					//Left
					List<String> leftClick = new ArrayList<>();
					leftClick.add("left");

					//Right
					List<String> rightClick = new ArrayList<>();
					rightClick.add("right");

					//Middle
					List<String> middleClick = new ArrayList<>();
					middleClick.add("middle");

					Converter converter = new Converter()
					.addTransformer(new SetLoreTransformer())
					.inputConfig(inputConfig)
					.outputConfig(outputConfig)
					.inputPath(path + "icon") //Icon
					.outputPath(path + "icon")
					.convert()
					.inputPath(path + "data") //Data
					.outputPath(path + "data")
					.convert()
					.inputPath(path + "nbt") //Nbt
					.outputPath(path + "nbt")
					.convert()
					.inputPath(path + "name") //Name
					.outputPath(path + "name")
					.convert()
					.inputPath(path + "lore") //Lore
					.outputPath(path + "lore")
					.convert()
					.setIfPathExists(path + "load-functions", functionPath + "load.type", load) //Load functions
					.inputPath(path + "load-functions")
					.outputPath(functionPath + "load.functions")
					.convert()
					.setIfPathExists(path + "functions", functionPath + "click.type", allClicks) //All click functions
					.inputPath(path + "functions")
					.outputPath(functionPath + "click.functions")
					.convert()
					.setIfPathExists(path + "leftclick-functions", functionPath + "leftclick.type", leftClick) //Left click functions
					.inputPath(path + "leftclick-functions")
					.outputPath(functionPath + "leftclick.functions")
					.convert()
					.setIfPathExists(path + "rightclick-functions", functionPath + "rightclick.type", rightClick) //Right click functions
					.inputPath(path + "rightclick-functions")
					.outputPath(functionPath + "rightclick.functions")
					.convert()
					.setIfPathExists(path + "middleclick-functions", functionPath + "middleclick.type", middleClick) //Middle click functions
					.inputPath(path + "middleclick-functions")
					.outputPath(functionPath + "middleclick.functions")
					.convert();
					
					ConfigurationSection slotSection = inputConfig.getConfigurationSection(String.valueOf(i));
					for(String key : slotSection.getKeys())
					{
						if(key.endsWith("-failfunctions"))
						{
							
						}
						else if(key.endsWith("-loadfailfunctions"))
						{
							String prefix = getPrefixFromFunction(key);
							
							converter
							.setIfPathExists(path + key, functionPath + "load." + prefix + "-fail-load.type", fail)
							.inputPath(path + key)
							.outputPath(functionPath + "load." + prefix +"-fail-load.functions")
							.convert()
							.setIfPathExists(path + key, functionPath + "load." + prefix +"-fail-load.fail-on", prefix);
						}
					}
				}
			}

			outputConfig.save();
		}
	}
	
	private static String getPrefixFromFunction(String function)
	{
		String[] split = function.split("-");
		return split[0];
	}
}