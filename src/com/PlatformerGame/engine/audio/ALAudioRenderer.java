//NAME: ALAudioRenderer.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Initializes OpenAL and opens audio device and creates a context.
//		Also abstracts sound, source, and listener functions so the game programmer doesn't need
//			to call on OpenAL directly.
//NOTE: https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf - This 150 page book
//			has everything you might need about OpenAL.

package com.PlatformerGame.engine.audio;

import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC11.*;

public class ALAudioRenderer {
	private long alDevice;
	private long context;
	
	public String defaultDevice;
	public String listALDevices;
	
	//Make this return buffer instead of source, source should be created by a function call from SoundEmitterComponent.java
	public int loadSound(String filename) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			URL soundUrl = Sound.class.getResource("/res/" + filename);
			File soundFile = new File(soundUrl.toExternalForm());
			String path = soundFile.getPath();
			path = path.replace("file:\\", "");
			path = path.replace("%20", " ");
			
			int buffer = alGenBuffers();
			int error = alGetError();
			if(error != AL_NO_ERROR) {
				System.out.println("Error at alGenBuffers: " + error);
			}
			
			//Load audio data from file into stack buffer
			IntBuffer channels = stack.mallocInt(1);
			IntBuffer sampleRate = stack.mallocInt(1);
			
			ShortBuffer sndBuf = STBVorbis.stb_vorbis_decode_filename(path, channels, sampleRate);
			if(sndBuf == null) {
				System.out.println("Failure to load '" + path + "'");
			}
			
			int ch = channels.get();
			int smplRate = sampleRate.get();
			
			//Load audio data stack buffer into openAL buffer
			alBufferData(buffer, ch == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, sndBuf, smplRate);
			error = alGetError();
			if(error != AL_NO_ERROR) {
				System.out.println("Failure to load '" + path + "' into OpenAL buffer: " + alGetError());
			}
		
			return buffer;
		}
	}
	
	public int createALSource() {
		//create audio sources
		int source = alGenSources();
		int error = alGetError();
		if(error != AL_NO_ERROR) {
			System.out.println("Failure to create OpenAL Source: " + alGetError());
		}
		
		return source;
	}
	
	public void deleteALSource(int source) {
		alDeleteSources(source);
	}
	
	public void playSound(int source) {
		alSourcePlay(source);
	}
	
	public void deleteALBuffer(int buffer) {
		alDeleteBuffers(buffer);
	}
	
	public ALAudioRenderer() {
		//Create device
		if(alcIsExtensionPresent(NULL, "ALC_ENUMERATION_EXT")) {
			defaultDevice = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
			listALDevices = alcGetString(NULL, ALC_ALL_DEVICES_SPECIFIER);
			alDevice = alcOpenDevice(defaultDevice);
		} else {
			alDevice = alcOpenDevice((String) null);
		}
		
		//Create context in device
		if(alDevice != NULL) {
			ALCCapabilities deviceCapabilities = ALC.createCapabilities(alDevice);
			//Creates the context on alDevice with int[] attributes
			context = alcCreateContext(alDevice, new int[] {ALC_MAJOR_VERSION, 1, ALC_MINOR_VERSION, 1, 0});
			if(context != NULL) {
				alcMakeContextCurrent(context);
				AL.createCapabilities(deviceCapabilities);
				
			} else {
				//implement better logging
				System.out.println("Unable to create OpenAL context 'context'. Audio may not work!");
			}
		} else {
			System.out.println("Unable to open OpenAL device. Audio may not work!");
		}
		
		//set up listener
		//This is fine for a default. We'll also want this to be overridable by a listenerComponent,
		// as well as switching between listeners rapidly to deal with multiple listeners, I THINK
		alListenerfv(AL_POSITION, new float[] {
				0, 0, 0
		});
		alListenerfv(AL_VELOCITY, new float[] {
				0, 0, 0
		});
		alListenerfv(AL_ORIENTATION, new float[] {
				0, 0, -1f, 0, 1.0f, 0
		});
	}
		
	
	public void destroy() {
		context = alcGetCurrentContext();
		alcMakeContextCurrent(NULL);
		alcDestroyContext(context);
		alcCloseDevice(alDevice);
		ALC.destroy();
	}
}
