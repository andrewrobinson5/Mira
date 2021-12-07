//NAME: Texture.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Stores loaded .OGG file in a buffer, and keeps track of the buffers to avoid
//		loading an audio file multiple times at once.

package com.PlatformerGame.engine.core;

import java.util.HashMap;

public class Sound {
	//Hash map to keep track of everything that's already been loaded into a buffer
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
			soundMap.put(soundLoc, App.audioRenderer.loadSound(soundLoc)); //puts a loaded sound into a map so we can reuse it without loading it again
		} 
		buffer = getSound(soundLoc); //then we keep track of the openAL buffer so we can call on it from the SoundEmitterComponent
		
		if(buffer == 0) {
			throw new RuntimeException("Failed to load sound"); //in openAL, the buffer 0 is reserved for null. If we get that, we have a bad buffer and our sound didn't load into it
		}
	}
}
