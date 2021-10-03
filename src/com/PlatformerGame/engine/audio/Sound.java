//NAME: Texture.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Stores loaded .OGG file in a buffer, and keeps track of the buffers to avoid
//		loading an audio file multiple times at once.

package com.PlatformerGame.engine.audio;

import java.util.HashMap;

import com.PlatformerGame.engine.core.App;

//import static org.lwjgl.openal.AL10.*;
//import static org.lwjgl.openal.AL11.*;


public class Sound {
	//Hash map to keep track of everything that's already been loaded
	protected static HashMap<String, Integer> soundMap = new HashMap<String, Integer>();
	
	public int buffer;
	
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
