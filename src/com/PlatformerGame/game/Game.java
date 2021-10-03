//NAME: Game.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: This is the game code. It consists of two functions that get called by Mira:
//			onCreate() - runs when the game is started
//			onUpdate() - runs once per frame
//		There is no real entry point inside this Game class, only in the App class, which instantiates
//		this class and calls those two functions. This way, the only thing the programmer of this
//		class has to worry about is game logic and not setting up the engine.

package com.PlatformerGame.game;

import com.PlatformerGame.engine.audio.Sound;
import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.QuadRendererComponent;
import com.PlatformerGame.engine.core.components.SoundEmitterComponent;
import com.PlatformerGame.game.prefabs.*;

import static com.PlatformerGame.engine.core.components.SoundEmitterComponent.MIRA_SOUND_ATTRIB.*;
import static org.lwjgl.glfw.GLFW.*;

public class Game {
	public Scene myScene = new Scene();
	
	private boolean canJump, paused;
	private float playerVelocityY, pipesVelocity, gravity;
	
	TwoPipes wall1;
	TwoPipes wall2;
	TwoPipes wall3;
	TwoPipes wall4;
	
	Sound jumpSound = new Sound("sounds/jump.ogg");
	Sound backgroundMusic = new Sound("sounds/song.ogg");

	SoundEmitterComponent BGMusicEmitterComponent = new SoundEmitterComponent(backgroundMusic, "BGMusic Emitter");
	
	GameObject player;
	
	public void trueInit() {
		// Sound emitters don't technically need to be attached to a GameObject to work because they
		//	 are all immediate-mode functions and get called directly from game code and not from App.java
		//	 When I develop an editor, they'll only show up if bound to one.
		BGMusicEmitterComponent.setMiraSoundAttrib(MIRA_SOUND_LOOPING, 1);
		BGMusicEmitterComponent.startSound();
	}
	
	public void onCreate() {
		playerVelocityY = 0;
		pipesVelocity = -0.5f;
		canJump = true;
		paused = true;
		gravity = 6.8f;
		
		//GameObject creation and adding to scene.
		player = new GameObject(-0.6f, 0.0f, 0.0f);
		player.addComponent(new QuadRendererComponent(0.2f, 0.16f));
		player.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/bird.png");
		myScene.add(player);
		
		wall1 = new TwoPipes();
		wall2= new TwoPipes();
		wall3 = new TwoPipes();
		wall4 = new TwoPipes();
		
		wall1.<TransformComponent>getComponent("Transform").x = 0f;
		wall2.<TransformComponent>getComponent("Transform").x = 0.85f;
		wall3.<TransformComponent>getComponent("Transform").x = 1.7f;
		wall4.<TransformComponent>getComponent("Transform").x = 2.55f;
		
		GameObject backdrop = new GameObject(0, 0, 0.8f);
		backdrop.addComponent(new QuadRendererComponent(2.1f, 2.1f));
		backdrop.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/backdrop.png");
		backdrop.<QuadRendererComponent>getComponent("QuadRenderer").tileX = 2.2f;
		
		myScene.add(wall1);
		myScene.add(wall2);
		myScene.add(wall3);
		myScene.add(wall4);
		myScene.add(backdrop);
		
		player.addComponent(new SoundEmitterComponent(jumpSound, "Jump Emitter"));
		
		// After everything is in the scene that should be, load it.
		myScene.loadScene();
	}	
	
	// This is poorly arranged and convoluted but that's okay. Game logic would be better off in a director GameObject
	public void onUpdate() {
		// starts paused, is unpaused logic.
		if (paused) {
			App.gameTimer.pause();
			
			//TODO: Input handling abstraction
			if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS) {
				paused = !paused;
			}
		} else if (!paused)
			App.gameTimer.play();
		
		if ((player.<QuadRendererComponent>getComponent("QuadRenderer").bounds[3].get(1) + myScene.get(0).transform.getCoords().get(1)) > -1f) {
			playerVelocityY -= gravity*App.gameTimer.deltaTime;
		} else {
			//REPLACE THIS WITH LOSS STATE
			playerVelocityY = 0;
			paused = true;
			
			myScene.unloadScene();
			// this is cheating haha
			// absolutely awful. Good enough for a school project for now
			// Really this next line restarts the entire game, except for persistent data. I won't want that in the future. I'll want to restart the scene. This can be done by using a game director object I guess.
			onCreate();
		}
		
		// Player input handling. This should probably be moved into a PlayerController extends GameObject class
		//	 in its own onUpdate() fn purely for organization purposes. As should the gravity code above.
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS && canJump) {
			// this single line makes the player jump.
			playerVelocityY = 2.1f;
			// prevents jump spam from holding down space for longer than 1 frame
			canJump = false;
			
			//play sound
			player.<SoundEmitterComponent>getComponent("Jump Emitter").startSound();
			
		}
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_RELEASE) {
			canJump = true;
		}
		
		// Changes position of object per frame by the velocity of that object (unit/second) * time in seconds
		//			since last frame:  velocity*deltaTime
		player.<TransformComponent>getComponent("Transform").y += (playerVelocityY*App.gameTimer.deltaTime);
		
		wall1.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		wall2.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		wall3.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		wall4.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		
		// Really the whole myScene.get(x) is a legacy format that I'm using now because it works, but as the
		//	project progresses it'll probably be more and more valuable to keep track of game objects and game logic
		//  outside of this class, in their own classes and just instantiate them.
	}
}
