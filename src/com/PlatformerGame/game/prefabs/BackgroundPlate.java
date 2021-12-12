package com.PlatformerGame.game.prefabs;

import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.MIRA_SOUND_GLOBAL;
import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.MIRA_SOUND_LOOPING;
import static com.PlatformerGame.engine.core.MiraUtils.*;

import com.PlatformerGame.engine.core.GameObject;
import com.PlatformerGame.engine.core.Sound;
import com.PlatformerGame.engine.core.Texture;
import com.PlatformerGame.engine.core.components.*;

public class BackgroundPlate extends GameObject {
	//member variables
	private int iterations = 0;

	//resources -- i have no idea why I named this after pipe, I think I copied it over from there to save time. Regardless, it works so I'm not going to mess with it.
	private Texture pipeTexture = new Texture("/textures/backdrop.png");
	private Sound backgroundMusic = new Sound("/sounds/song.ogg"); //load the music

	//components
	public QuadRendererComponent pipeRenderer = new QuadRendererComponent(2.1f, 2.1f);
	public SoundEmitterComponent BGMusicEmitterComponent = new SoundEmitterComponent(backgroundMusic, "BGMusic Emitter");
	
	public void onCreate() {
		pipeRenderer.tex = pipeTexture;
		pipeRenderer.tileX = 2.2f;
		addComponent(pipeRenderer);
		
		addComponent(BGMusicEmitterComponent);
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_LOOPING, 1); //set the music to start over when it finishes
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_GLOBAL, 1); //OpenAL is a 3d audio library, meaning it can handle spatial sound. We set the sound to global so the sound is not used as spatial.
		miraPriorityExecuteComponentOnCreate(BGMusicEmitterComponent); //hacky way of executing the onCreate() method of a component out of the natural order
		
		if(iterations == 0) {
			BGMusicEmitterComponent.startSound(); //and finally, we only want it to play once, so we keep track of whether or not it has been played with iterations.
		}
		iterations++;
	}
}
