//GAME.JAVA
//DESCRIPTION: This is the "game" class. All of the logic of the game belongs here: the initialization and game loop.
//NOTE: This is not how the game class will look in the future, it is currently very messy and many things are handled
//		here directly that should probably be abstracted. I have not implemented several engine features like scene
//		loading or input handling or a better way to handle onUpdate() and onCreate(), etc... 

package com.PlatformerGame.game;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.game.prefabs.*;

import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
	public Scene myScene = new Scene();
	
	private boolean canJump, paused;
	private float playerVelocityY, pipesVelocity, gravity;
	
	TwoPipes wall1;
	TwoPipes wall2;
	TwoPipes wall3;
	TwoPipes wall4;
	
	public void onCreate() {
		// important variables for this class's logic
		
		playerVelocityY = 0;
		pipesVelocity = -0.5f;
		canJump = true;
		paused = true;
		gravity = 6.8f;
		
		myScene.loadScene();

		//GameObject creation and adding to scene.
		GameObject player = new GameObject(-0.6f, 0.0f, 0.0f);
		player.addComponent(new QuadRendererComponent(0.2f, 0.16f));
		player.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/bird.png");
//		player.<QuadRendererComponent>getComponent("QuadRenderer").solidColor = new Vector4f(0.1f, 0.0f, 0.8f, 1f);
		myScene.add(player);
		
		wall1 = new TwoPipes();
		wall1.<TransformComponent>getComponent("Transform").x = 0f;
		
		wall2= new TwoPipes();
		wall2.<TransformComponent>getComponent("Transform").x = 0.85f;
		
		wall3 = new TwoPipes();
		wall3.<TransformComponent>getComponent("Transform").x = 1.7f;
		
		wall4 = new TwoPipes();
		wall4.<TransformComponent>getComponent("Transform").x = 2.55f;
		
		GameObject backdrop = new GameObject(0, 0, 0.8f);
		backdrop.addComponent(new QuadRendererComponent(2.1f, 2.1f));
		backdrop.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/backdrop.png");
		
		myScene.add(wall1);
		myScene.add(wall2);
		myScene.add(wall3);
		myScene.add(wall4);
		
		myScene.add(backdrop);
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
		
		// this is an artifact from platforming gravity logic but it works fine enough for this demo's loss state.
		//	if we wanna make this faster, we can actually just nix three of the conditions because we just need
		//	to know when the player falls all the way down.
		// but this is definitely janky and I *should* probably add a proper collision detection and a physics component.
		//  For this demo, I do not need it.
		if (	/*myScene.get(0).<QuadRendererComponent>getComponent("QuadRenderer").bounds[0].get(1) > -1f &&
				myScene.get(0).<QuadRendererComponent>getComponent("QuadRenderer").bounds[1].get(1) > -1f &&
				myScene.get(0).<QuadRendererComponent>getComponent("QuadRenderer").bounds[2].get(1) > -1f &&*/
				(myScene.get(0).<QuadRendererComponent>getComponent("QuadRenderer").bounds[3].get(1) + myScene.get(0).transform.getCoords().get(1)) > -1f) {
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
		}
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_RELEASE) {
			canJump = true;
		}
		
		// Changes position of object per frame by the velocity of that object (unit/second) * time in seconds
		//			since last frame:  velocity*deltaTime
		myScene.get(0).<TransformComponent>getComponent("Transform").y += (playerVelocityY*App.gameTimer.deltaTime);
		
		wall1.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		wall2.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		wall3.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		wall4.<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		
		// Really the whole myScene.get(x) is a legacy format that I'm using now because it works, but as the
		//	project progresses it'll probably be more and more valuable to keep track of game objects and game logic
		//  outside of this class, in their own classes and just instantiate them.
	}
}
