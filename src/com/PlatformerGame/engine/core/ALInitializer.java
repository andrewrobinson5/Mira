package com.PlatformerGame.engine.core;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.openal.ALC11.*;

public class ALInitializer {
	private long device;

    private long context;

    public ALInitializer() {
	    	
		device = alcOpenDevice((ByteBuffer) null);
	    if (device == NULL) {
	        throw new IllegalStateException("Failed to open the default OpenAL device.");
	    }
	    ALCCapabilities deviceCaps = ALC.createCapabilities(device);
	    context = alcCreateContext(device, (IntBuffer) null);
	    if (context == NULL) {
	        throw new IllegalStateException("Failed to create OpenAL context.");
	    }
	    alcMakeContextCurrent(context);
	    AL.createCapabilities(deviceCaps);
    }
    
    public void finalize() {
    	alcCloseDevice(device);
    }
}
