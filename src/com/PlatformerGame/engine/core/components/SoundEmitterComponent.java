//NAME: SoundEmitterComponent.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Interfaces with ALAudioRenderer to create a new OpenAL source, puts a sound object's data
//		into that source, and exposes the play and pause functions. Finally, it lays the groundwork
//		for engine emitter params like MIRA_SOUND_GLOBAL, which indicates that the sound should be heard
//		from anywhere.

package com.PlatformerGame.engine.core.components;

import com.PlatformerGame.engine.audio.Sound;
import com.PlatformerGame.engine.core.App;

import java.util.ArrayList;
import org.joml.Vector3f;

import static com.PlatformerGame.engine.core.MiraUtils.*;
import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.*;

public class SoundEmitterComponent extends GameObjectComponent {
	//TODO: INSTEAD OF DOING THIS, WE SHOULD HAVE AN ONDESTROY() METHOD THAT GETS CALLED WHEN SCENE IS UNLOADED
	//Keeps track of sources to free after scene unloaded
	public static ArrayList<Integer> sourcesList = new ArrayList<Integer>();
	
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

	private void sendAttribsToAudioRenderer() {
		App.audioRenderer.setSourceLooping(mySource, intToBool(getMiraSoundAttrib(MIRA_SOUND_LOOPING)));
		App.audioRenderer.setSourceRelative(mySource, intToBool(getMiraSoundAttrib(MIRA_SOUND_RELATIVE)));
		App.audioRenderer.setSourceGlobal(mySource, intToBool(getMiraSoundAttrib(MIRA_SOUND_GLOBAL)));
		if (!intToBool(getMiraSoundAttrib(MIRA_SOUND_GLOBAL)) && !intToBool(getMiraSoundAttrib(MIRA_SOUND_RELATIVE))) {
			position = m_object.transform.getCoords();
			App.audioRenderer.setSourcePosition(mySource, position);
		}
	}
	
	public void onCreate() {
		super.onCreate();
		mySource = App.audioRenderer.createALSource();
		sourcesList.add(mySource);
		App.audioRenderer.putSoundInSource(soundInput, mySource);
		sendAttribsToAudioRenderer();
	}
	
	public void onUpdate() {
		sendAttribsToAudioRenderer();
	}
	
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
	
	public void startSound() {
		App.audioRenderer.playSound(mySource);
	}
	
	public void pauseSound() {
		App.audioRenderer.pauseSound(mySource);
	}
	
	public SoundEmitterComponent(Sound sound, String name) {
		super(name);
		soundInput = sound;
	}
}
