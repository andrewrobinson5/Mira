package com.PlatformerGame.game.prefabs;

import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.MIRA_SOUND_GLOBAL;
import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.MIRA_SOUND_LOOPING;
import static com.PlatformerGame.engine.core.MiraUtils.*;

import com.PlatformerGame.engine.core.GameObject;
import com.PlatformerGame.engine.core.Sound;
import com.PlatformerGame.engine.core.Texture;
import com.PlatformerGame.engine.core.components.*;
import com.PlatformerGame.engine.core.*;

public class BackgroundPlate extends GameObject {
	//member variables
	int iterations = 0;

	//resources
	Texture pipeTexture = new Texture("/textures/backdrop.png");
	Sound backgroundMusic = new Sound("/sounds/song.ogg");

	//components
	QuadRendererComponent pipeRenderer = new QuadRendererComponent(2.1f, 2.1f);
	SoundEmitterComponent BGMusicEmitterComponent = new SoundEmitterComponent(backgroundMusic, "BGMusic Emitter");
	
	public void onCreate() {
		pipeRenderer.tex = pipeTexture;
		pipeRenderer.tileX = 2.2f;
		addComponent(pipeRenderer);
		
		addComponent(BGMusicEmitterComponent);
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_LOOPING, 1);
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_GLOBAL, 1);
		miraPriorityExecuteComponentOnCreate(BGMusicEmitterComponent);
		
		if(iterations == 0) {
			BGMusicEmitterComponent.startSound();
		}
		iterations++;
	}
	
	public void onUpdate() {
		
	}
}
