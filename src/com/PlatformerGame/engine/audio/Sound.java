package com.PlatformerGame.engine.audio;

import org.joml.Vector3f;
//import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import com.PlatformerGame.engine.core.App;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_SOURCE_RELATIVE;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.AL10.alListenerfv;
import static org.lwjgl.openal.AL10.alSourcei;
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
	
	public int getSource() {
		return Integer.valueOf(source);
	}
	
	public int getBufferId() {
		return buffer;
	}
	
	public void startSound() {
		//Create source from openAL buffer
		source = alGenSources();
		
		if(isGlobal) {
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
		
		source = ALAudioRenderer.loadSound(soundLoc);
		if(source == 0) {
			throw new RuntimeException("Failed to load sound");
		}
	}
}
