package com.clubobsidian.obsidianengine.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.clubobsidian.obsidianengine.ObsidianEngine;

public class ReadConsoleTask implements Task {
	
	@Override
	public void call() 
	{
		String line = null;
		if(System.console() != null)
		{	
			line = System.console().readLine();
		}
		else
		{  
			//Fixes bug with some ides not support console
			//https://stackoverflow.com/questions/4203646/system-console-returns-null
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try 
			{
				line = reader.readLine();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		if(line != null)
		{
			ObsidianEngine.getCommandDispatcher().dispatchCommand(ObsidianEngine.getConsoleUser(), line);
		}
	}
}