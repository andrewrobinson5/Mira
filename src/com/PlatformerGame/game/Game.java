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
	// temporary scene arrangement before we get classes/whatever systems set up.
	public Scene myScene = new Scene();
	
	private boolean canJump, paused;
	private float playerVelocityY, pipesVelocity, gravity;
	
	float[] test = {
			-1f, 1f, 0, 0, 0, 0,
			-1f, -1f, 0, 0, 0, 0,
			1f, 1f, 0, 0, 0, 0,
			1f, -1f, 0, 0, 0, 0,
	};
	
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
		player.addComponent(new QuadRendererComponent(0.15f, 0.12f));
		player.<QuadRendererComponent>getComponent("QuadRenderer").solidColor = new Vector3f(0.1f, 0.0f, 0.8f);
		myScene.add(player);
		
		
		
		// Also we probably need to have two different game objects per wall. Parenting would help right about now.
		Pipe wallBottom1 = new Pipe();
		wallBottom1.<TransformComponent>getComponent("Transform").x = 0f;
		
		Pipe wallBottom2 = new Pipe();
		wallBottom2.<TransformComponent>getComponent("Transform").x = 0.85f;
		
		Pipe wallBottom3 = new Pipe();
		wallBottom3.<TransformComponent>getComponent("Transform").x = 1.7f;
		
		Pipe wallBottom4 = new Pipe();
		wallBottom4.<TransformComponent>getComponent("Transform").x = 2.55f;
		
		wallBottom1.makeChildOf(player);
		
		myScene.add(wallBottom1);
		//myScene.add(wallTop1);
		myScene.add(wallBottom2);
		//myScene.add(wallTop2);
		myScene.add(wallBottom3);
		//myScene.add(wallTop3);
		myScene.add(wallBottom4);
		//myScene.add(wallTop4);
		
	}	
	
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
		
		myScene.get(1).<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		myScene.get(2).<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		myScene.get(3).<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		myScene.get(4).<TransformComponent>getComponent("Transform").x += (pipesVelocity*App.gameTimer.deltaTime);
		
		// also we probably ought to have a handling of IndexOutOfBoundsException eventually. Make it nicer for 
		//	the game programmer. For now I don't mind much because I know not to do that.
		//	But really we should probably be asserting that a get() isn't exceeding the bounds of our list, so I'm
		//  going to leave this the way it is because it functionally achieves that.
		
		// Really the whole myScene.get(x) is a legacy format that I'm using now because it works, but as the
		//	project progresses it'll probably be more and more valuable to keep track of game objects and game logic
		//  outside of this class, in their own classes and just instantiate them.
	}
}
