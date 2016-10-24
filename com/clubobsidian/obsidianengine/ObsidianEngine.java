package com.clubobsidian.obsidianengine;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

import com.clubobsidian.obsidianengine.event.EventDispatcher;
import com.clubobsidian.obsidianengine.event.EventRegistry;
import com.clubobsidian.obsidianengine.objects.classloader.BetterURLClassLoader;
import com.clubobsidian.obsidianengine.objects.managers.JarManager;
import com.clubobsidian.obsidianengine.objects.managers.ModuleManager;
import com.clubobsidian.obsidianengine.objects.module.Module;
import com.clubobsidian.obsidianengine.objects.module.ModuleLogger;
import com.clubobsidian.obsidianengine.objects.module.ModuleStack;
import com.clubobsidian.obsidianengine.objects.tasks.ConsoleThread;
import com.clubobsidian.obsidianengine.objects.tasks.EventTask;
import com.clubobsidian.obsidianengine.objects.tasks.TaskThread;


public class ObsidianEngine {
	
	private static Module engineModule = new Module();
	private static ModuleManager moduleManager = new ModuleManager();
	private static JarManager jarManager = new JarManager();
	private static BetterURLClassLoader loader;
	private static TaskThread taskThread;
	private static EventDispatcher eventDispatcher;
	private static EventRegistry eventRegistry;
	private static ArrayList<String> injectBefore = new ArrayList<String>();
	
	public static void main(final String[] args)
	{
		//for(String str : args)
		//{
		//	System.out.println(str);
		//}

		//FileConfiguration file = FileConfiguration.loadFile(new File("test.yml"));
		//System.out.println(new File("test.yml").getAbsolutePath());

		ObsidianEngine.loader = new BetterURLClassLoader(new URL[0], ObsidianEngine.class.getClassLoader());
		ObsidianEngine.eventRegistry = new EventRegistry();
		ObsidianEngine.eventDispatcher = new EventDispatcher();
		ObsidianEngine.setupEngineModule();
		ObsidianEngine.getLogger().info("Starting ObsidianEngine...");
		ObsidianEngine.moduleManager.loadModules();
		ObsidianEngine.moduleManager.preloadModules();
		ObsidianEngine.jarManager.loadJar(args, injectBefore);
		ObsidianEngine.moduleManager.enableModules();

		
		if(ObsidianEngine.jarManager.getStandalone())
		{
			new ConsoleThread().start();
			ObsidianEngine.taskThread = new TaskThread();
			ObsidianEngine.taskThread.setDaemon(false);
			//ObsidianEngine.taskThread.addTask(new EventTask());
			//ObsidianEngine.eventRegistry.register(new TestListener());
		}
		ObsidianEngine.taskThread.addTask(new EventTask());
		ObsidianEngine.taskThread.start();
	}
	
	private static void setupEngineModule()
	{
		try 
		{
			Field name = Module.class.getDeclaredField("name");
			name.setAccessible(true);
			name.set(ObsidianEngine.engineModule, "ObsidianEngine");
		
			Field logger = Module.class.getDeclaredField("logger");
			logger.setAccessible(true);
			logger.set(ObsidianEngine.engineModule, new ModuleLogger(ObsidianEngine.engineModule));
		} 
		catch (NoSuchFieldException | SecurityException| IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static ModuleLogger getLogger()
	{
		return ObsidianEngine.engineModule.getLogger();
	}
	
	public static BetterURLClassLoader getClassLoader()
	{
		return ObsidianEngine.loader;
	}

	public static ModuleStack getModules()
	{
		return ObsidianEngine.moduleManager.getModules();
	}
	
	public static EventDispatcher getEventDispatcher()
	{
		return ObsidianEngine.eventDispatcher;
	}
	
	public static EventRegistry getEventRegistry()
	{
		return ObsidianEngine.eventRegistry;
	}
	
	public static void tryToinjectClass(String clazz)
	{
		/*try 
		{
			ObsidianEngine.getClassLoader().loadClass(clazz);
		} 
		catch (NoClassDefFoundError | ClassNotFoundException e) 
		{
			ObsidianEngine.injectBefore.add(clazz);
			ObsidianEngine.getLogger().fatal("Class loading has failed for " + clazz + " trying to inject on runtime!");
		}*/
	}
}