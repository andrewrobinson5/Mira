package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.audio.Sound;
import org.joml.Vector3f;

import static com.PlatformerGame.engine.core.MiraUtils.*;
import static com.PlatformerGame.engine.core.SoundEmitterComponent.MIRA_SOUND_ATTRIB.*;
import static org.lwjgl.openal.AL10.*;

public class SoundEmitterComponent extends GameObjectComponent {
	// Not sure if these need to be per sound or per source. I think I'll have to move my
	//		openAL sources into the SoundEmitterComponent class and make Sound.java strictly for
	//		buffer handling.
	public enum MIRA_SOUND_ATTRIB {
	    MIRA_SOUND_GLOBAL,
	    MIRA_SOUND_RELATIVE,
	    MIRA_SOUND_LOOPING;
	}
	
	public Sound soundInput;
	
	private int mySource;
	public Vector3f position = new Vector3f(0, 0, 0);
	
	private boolean isGlobal;
	private boolean isRelative;
	private boolean isLooping;

	//THESE PROBABLY NEED TO BE MOVED TO SoundEmitterComponent because these are all per-source and not
	//		per sound file.
	public void setMiraSoundAttrib(MIRA_SOUND_ATTRIB attrib, int value) {
		switch(attrib) {
			case MIRA_SOUND_GLOBAL: isGlobal = intToBool(value);
				break;
			case MIRA_SOUND_RELATIVE: isRelative = intToBool(value);
				break;
			case MIRA_SOUND_LOOPING: isLooping = intToBool(value);
				break;
		}
	}
	
	public int getMiraSoundAttrib(MIRA_SOUND_ATTRIB attrib) {
		switch(attrib) {
			case MIRA_SOUND_GLOBAL: return boolToInt(isGlobal);
			case MIRA_SOUND_RELATIVE: return boolToInt(isRelative);
			case MIRA_SOUND_LOOPING: return boolToInt(isLooping);
			default: return -1;
		}
	}
	
	public void putSoundInSource(Sound sound, int source) {
		alSourcei(source, AL_BUFFER, sound.getBufferId());
		//figure out if the sound is to be looped or not
		alSourcei(source, AL_LOOPING, intToBool(getMiraSoundAttrib(MIRA_SOUND_LOOPING)) ? AL_TRUE : AL_FALSE);
		alSourcei(source, AL_SOURCE_RELATIVE, intToBool(getMiraSoundAttrib(MIRA_SOUND_RELATIVE)) ? AL_TRUE : AL_FALSE);
		
		if(intToBool(getMiraSoundAttrib(MIRA_SOUND_GLOBAL))) {
			//if Sound is global, we'll make the source relative and then override position to (0, 0, 0), so it plays directly on top of the listener.
			alSourcei(source, AL_SOURCE_RELATIVE, AL_TRUE);
			alSource3f(source, AL_POSITION, 0f, 0f, 0f);
		}
	}
	
	public void startSound() {
		App.audioRenderer.playSound(mySource);
	}
	
	public void pauseSound() {
		alSourcePause(mySource);
	}
	
	public SoundEmitterComponent(Sound sound, String name) {
		super(name);
		
		soundInput = sound;
		mySource = App.audioRenderer.createALSource();
		
		putSoundInSource(soundInput, mySource);
	}
}
