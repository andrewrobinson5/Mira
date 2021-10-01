package com.PlatformerGame.engine.audio;

import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import com.PlatformerGame.engine.core.Sound;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC11.*;

public class ALAudioRenderer {
	private long alDevice;
	private long context;
	
	public ALAudioRenderer() {
		//Create device
		
		//these both do the same thing, I'm just learning extensions
		//if(alcIsExtensionPresent(NULL, "ALC_ENUMERATION_EXT")) {
		//	alDevice = alcOpenDevice(alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER));
		//} else {
		alDevice = alcOpenDevice((String) null);
		//}
		
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
		
		//load test file
		try {
			// Url of the sound file
			URL soundUrl = Sound.class.getResource("/res/sounds/jump.ogg");
			File soundFile = new File(soundUrl.toExternalForm());
			String path = soundFile.getPath();
			path = path.replace("file:\\", "");
			path = path.replace("%20", " ");
						
			
			int buffer = alGenBuffers();
			if(alGetError() != AL_NO_ERROR) {
				
			}
			
			try (MemoryStack stack = MemoryStack.stackPush()) {
				//Load audio data from file into stack buffer
				IntBuffer channels = stack.mallocInt(1);
				IntBuffer sampleRate = stack.mallocInt(1);
				
				ShortBuffer sndBuf = STBVorbis.stb_vorbis_decode_filename(path, channels, sampleRate);
				
				int ch = channels.get();
				int smplRate = sampleRate.get();
				
				//Load audio data stack buffer into openAL buffer
				alBufferData(buffer, ch == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, sndBuf, smplRate);
			}
			//create audio sources
			int source = alGenSources();
			alSourcei(source, AL_BUFFER, buffer);
			alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
			
			//set up listener
			alListenerfv(AL_POSITION, new float[] {
					0, 0, 0
			});
			alListenerfv(AL_VELOCITY, new float[] {
					0, 0, 0
			});
			alListenerfv(AL_ORIENTATION, new float[] {
					0, 0, -1f, 0, 1.0f, 0
			});
			
			alSourcePlay(source);
		} finally {}
		
	}
	
	public void destroy() {
		context = alcGetCurrentContext();
		alcMakeContextCurrent(NULL);
		alcDestroyContext(context);
		alcCloseDevice(alDevice);
		ALC.destroy();
	}
}
