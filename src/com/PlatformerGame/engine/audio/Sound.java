package com.PlatformerGame.engine.audio;

import java.util.HashMap;

import com.PlatformerGame.engine.core.App;

//import static org.lwjgl.openal.AL10.*;
//import static org.lwjgl.openal.AL11.*;


public class Sound {
	//Hash map to keep track of everything that's already been loaded
	protected static HashMap<String, Integer> soundMap = new HashMap<String, Integer>();
	
	public int buffer;
	
	public static void dumpHashMap() {
		for (String name: soundMap.keySet()) {
		    String key = name.toString();
		    String value = soundMap.get(name).toString();
		    System.out.println(key + " " + value);
		}
	}
	
	
	
	
	public int getSound(String soundLoc) {
		if(soundMap.containsKey(soundLoc)) {
			return soundMap.get(soundLoc);
		}
		return 0;
	}
	
	public int getBufferId() {
		return buffer;
	}
	
	public Sound(String soundLoc) {
		if(getSound(soundLoc) == 0) {
			buffer = App.audioRenderer.loadSound(soundLoc);
			soundMap.put(soundLoc, buffer);
		}
		
		if(buffer == 0) {
			throw new RuntimeException("Failed to load sound");
		}
	}
}
