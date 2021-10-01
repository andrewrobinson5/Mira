package com.PlatformerGame.engine.core;

import org.joml.Vector3f;
//import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.openal.AL11.*;

import java.io.File;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Sound {
	public boolean isGlobal;
	public boolean isLooping;
	public boolean isRelative;
	public Vector3f position = new Vector3f(0, 0, 0);
	
	private int buffer;
	private int source;
	private int ch;
	private int smplRate;
	
	//Playback Speed manipulation
	//private float speed;
	
	public int getBufferId() {
		return buffer;
	}
	
	private boolean loadSound(String file) {
		// Url of the sound file
		URL soundUrl = Sound.class.getResource("/res/" + file);
		File soundFile = new File(soundUrl.toExternalForm());
		String path = soundFile.getPath();
		path = path.replace("file:\\", "");
		path = path.replace("%20", " ");
					
		
		buffer = alGenBuffers();
		if(alGetError() != AL_NO_ERROR) {
			return false;
		}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			//Load audio data from file into stack buffer
			IntBuffer channels = stack.mallocInt(1);
			IntBuffer sampleRate = stack.mallocInt(1);
			
			ShortBuffer sndBuf = STBVorbis.stb_vorbis_decode_filename(path, channels, sampleRate);
			
			ch = channels.get();
			smplRate = sampleRate.get();
			
			//Load audio data stack buffer into openAL buffer
			alBufferData(buffer, ch == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, sndBuf, smplRate);
			
			//Clean up STBVorbis loading stuff
			
		}
		
		return true;
	}
	
	public void startSound() {
		//Create source from openAL buffer
		source = alGenSources();
		
		if(isGlobal) {
			alListener3f(AL_POSITION, position.x, position.y, position.z);
	        alListener3f(AL_VELOCITY, 0, 0, 0);
			alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
			position.set(0, 0, 0);
		}
	
		//Learned relativity and looping from https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/chapter22/chapter22.html
		//	Awesome tutorial, but its project structure feels unintuitive to me right now, so I do it my own way.
		if(isRelative) {
			alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
		}
		
		if(isLooping) {
			alSourcei(source, AL_LOOPING, AL_TRUE);
		}
		alSource3f(source, AL_POSITION, position.x, position.y, position.z);
	}
	
	public void pauseSound() {
		alSourcePause(source);
	}
	
	public void playSound() {
		alSourcePlay(source);
	}
	
	public void cleanUp() {
		alDeleteBuffers(buffer);
	}
	
	public Sound(String soundLoc) {
		isGlobal = true;
		isLooping = false;
		
		if(!loadSound(soundLoc)) {
			throw new RuntimeException("Failed to load sound");
		}
	}
}
