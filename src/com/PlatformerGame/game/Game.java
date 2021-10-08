//NAME: Game.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: This is the game code. It consists of two functions that get called by Mira:
//			onCreate() - runs when the game is started
//			onUpdate() - runs once per frame
//		There is no real entry point inside this Game class, only in the App class, which instantiates
//		this class and calls those two functions. This way, the only thing the programmer of this
//		class has to worry about is game logic and not setting up the engine.

package com.PlatformerGame.game;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.QuadRendererComponent;
import com.PlatformerGame.engine.core.components.SoundEmitterComponent;
import com.PlatformerGame.engine.core.components.TransformComponent;
import com.PlatformerGame.game.prefabs.*;

import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.*;
import static org.lwjgl.glfw.GLFW.*;

public class Game {
	public Scene myScene = new Scene();
	
	private boolean paused;
	private float gravity;
	
	TwoPipes wall1;
	TwoPipes wall2;
	TwoPipes wall3;
	TwoPipes wall4;
	
	Sound backgroundMusic = new Sound("sounds/song.ogg"); // game director class

	SoundEmitterComponent BGMusicEmitterComponent = new SoundEmitterComponent(backgroundMusic, "BGMusic Emitter");
	
	PlayerController player;
	
	public void onCreate() {
		paused = true; // game director class
		
		//GameObject creation and adding to scene.
		player = new PlayerController();
		myScene.add(player);
		
		
		wall1 = new TwoPipes();
		wall1.<TransformComponent>getComponent("Transform").x = 0f;

		wall2= new TwoPipes();
		wall2.<TransformComponent>getComponent("Transform").x = 0.85f;

		wall3 = new TwoPipes();
		wall3.<TransformComponent>getComponent("Transform").x = 1.7f;

		wall4 = new TwoPipes();
		wall4.<TransformComponent>getComponent("Transform").x = 2.55f;
		
		myScene.add(wall1);
		myScene.add(wall2);
		myScene.add(wall3);
		myScene.add(wall4);
		
		BackgroundPlate backdrop = new BackgroundPlate();
		myScene.add(backdrop);
		
		
		player.addComponent(BGMusicEmitterComponent); // game director class
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_LOOPING, 1); // gameDirector class
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_GLOBAL, 1); // gameDirector class

		
		// After everything is in the scene that should be, load it.
		myScene.loadScene();
	}	
	
	// This is poorly arranged and convoluted but that's okay. Game logic would be better off in a director GameObject
	// TODO: PUT BACKGROUND MUSIC IN ITS OWN GAMEOBJECT OR MAKE A GAME DIRECTOR OBJECT TO AVOID HACKS LIKE THIS:
	private boolean updateHasRunOnce = false; // this is abuse
	public void onUpdate() {
		if(!updateHasRunOnce) {
			//This is running before the gameobjects are all run onCreate()
			BGMusicEmitterComponent.startSound();
			updateHasRunOnce = true;
		}
		
		// starts paused, is unpaused logic. -- This all goes in game director
		if (paused) {
			GameTime.pause();
			
			//TODO: Input handling abstraction
			if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS) {
				paused = !paused;
			}
		} else if (!paused)
			GameTime.play();
		
		if ((player.<QuadRendererComponent>getComponent("QuadRenderer").bounds[3].get(1) + myScene.get(0).transform.getCoords().get(1)) > -1f) {

		} else {
			//REPLACE THIS WITH LOSS STATE
			paused = true;
			
			myScene.unloadScene();
			for (int i = 0; i < SoundEmitterComponent.sourcesList.size(); i++) {
				App.audioRenderer.deleteALSource(SoundEmitterComponent.sourcesList.get(i));
			}
			
			// this is cheating haha
			// absolutely awful. Good enough for a school project for now
			// Really this next line restarts the entire game, except for persistent data. I won't want that in the future. I'll want to restart the scene. This can be done by using a game director object I guess.
			updateHasRunOnce = false;
			onCreate();
		}
	}
}
