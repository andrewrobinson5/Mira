//NAME: Texture.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Stores loaded .OGG file in a buffer, and keeps track of the buffers to avoid
//		loading an audio file multiple times at once.

package com.PlatformerGame.engine.core;

import java.util.HashMap;

public class Sound {
	//Hash map to keep track of everything that's already been loaded
	protected static HashMap<String, Integer> soundMap = new HashMap<String, Integer>();
	
	private int buffer;
	
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
			soundMap.put(soundLoc, App.audioRenderer.loadSound(soundLoc));
		} 
		buffer = getSound(soundLoc);
		
		if(buffer == 0) {
			throw new RuntimeException("Failed to load sound");
		}
	}
}
