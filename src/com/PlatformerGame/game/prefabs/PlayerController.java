//NAME: PlayerController.java
//COPYRIGHT: Andrew Robinson
//DESC: Player movement

package com.PlatformerGame.game.prefabs;

import static org.lwjgl.glfw.GLFW.*;

//these imports were required to use the mouse. I left that snippet in a comment, I think it's pretty cool.
//import java.nio.DoubleBuffer;
//import java.nio.IntBuffer;
//import org.lwjgl.system.MemoryStack;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.*;

public class PlayerController extends GameObject {
	//class member variables
	private final float gravity = 6.8f;
	private float playerVelocityY;
	private boolean canJump;
	public boolean isRestarting = false;
	
	public int score;
	private double scoreLimiter;
	public boolean shouldUpdateScore;

	//resources
	private Sound jumpSound = new Sound("/sounds/jump.ogg");
	
	//components
	public QuadRendererComponent birdRenderer = new QuadRendererComponent(0.2f, 0.16f);
	private ColliderComponent birdCollider = new ColliderComponent(0.2f, 0.16f);
	private SoundEmitterComponent jumpEmitterComponent = new SoundEmitterComponent(jumpSound, "Jump Emitter");
	
	public void onCreate() {
		playerVelocityY = 0;
		canJump = true;
		
		score = 0;

		transform.setCoords(-0.6f, 0.0f, 0.0f); //starting position on screen for bird
		
		addComponent(birdRenderer); //adding the component to this gameobject's component bag tells App.java to run its methods
		birdRenderer.tex = new Texture("/textures/bird.png"); //setting the texture so that the bird picture displays instead of a square
		
		addComponent(birdCollider); //AABB collision detection component
		addComponent(jumpEmitterComponent); //sound component
		
		scoreLimiter = GameTime.getTime();
	}
	
	public void onUpdate() {
		shouldUpdateScore = false;
		if ((birdRenderer.bounds[3].get(1) + transform.getCoords().get(1)) > -1f && (birdRenderer.bounds[3].get(1) + transform.getCoords().get(1)) < 1f) { //check if the player tried to go off screen
			playerVelocityY -= gravity*GameTime.deltaTime; //gravity. We multiply it by change in time so the bird falls at the same rate on fast and slow computers
		} else {
			playerVelocityY = 0; //set velocity to none so the bird doesn't move around when paused
			isRestarting = true; //fail state. Tells the game to restart
		}
		
		boolean restartafter = false;
		for(int i = 0; i < ColliderComponent.colliders.size(); i++) {
			if (birdCollider != ColliderComponent.colliders.get(i)) {
				if (Collider.checkCollision(birdCollider, ColliderComponent.colliders.get(i))) {
//					System.out.println("Collision!"); //debug comment
					restartafter = true; //using this because restarting inside the loop gave me a weird concurrency error and I'm not using concurrency
				}
			}
		}
		
		//check for interaction with triggers. This way we can tell if the player is between the pipe
		for(int i = 0; i < TriggerComponent.triggers.size(); i++) {
			//if the player is in the trigger zone, we'll add one to the score.
			if (Collider.checkCollision(birdCollider, TriggerComponent.triggers.get(i))) {
				if (GameTime.getTime() > scoreLimiter + 1) {
					score++; //increment score
					shouldUpdateScore = true; //this tells the Game class to ask ScoreCounter to increment (I don't have messaging between gameobjects figured out very well)
					scoreLimiter = GameTime.getTime(); //limits number of points a player can get per second to one.
					System.out.println("Score: " + score);
				}
			}
		}

		if (restartafter) {
			playerVelocityY = 0;	
			isRestarting = true;
		}
		
//		this is the mouse input code. this snippet makes the bird stick to the mouse cursor
//		try (MemoryStack stack = MemoryStack.stackPush()) {
//			DoubleBuffer xPos = stack.mallocDouble(1);
//			DoubleBuffer yPos = stack.mallocDouble(1);
//			glfwGetCursorPos(App.gameWindow.window, xPos, yPos);
//			float tempX = (float) xPos.get();
//			float tempY = (float) yPos.get();
//			
//			IntBuffer w = stack.mallocInt(1);
//			IntBuffer h = stack.mallocInt(1);
//			glfwGetWindowSize(App.gameWindow.window, w, h);
//
//			int width = w.get();
//			int height = h.get();
//			
//			// So glfwGetCursorPos() returns the exact pixel location of the cursor on the window using the top
//			//  left corner as 0,0. So we need to convert that position into something relative to openGL's NDC.
//			tempX -= (width/2); 
//			tempY -= (height/2);
//			
//			tempX = tempX/width*2;
//			tempY = -tempY/height*2;
//			
//			transform.setCoords(tempX, tempY, 0);
//		}
		
		
		// HACK: glfw shouldn't be exposed to game code but it's more work than it's worth to abstract it away for this project's scope.
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS && canJump) {
			playerVelocityY = 2.1f; // this single line makes the player jump.
			canJump = false; // prevents jump spam from holding down space for longer than 1 frame
			
			jumpEmitterComponent.startSound(); //play sound
			
		}
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_RELEASE) {
			canJump = true; //after the player releases the space bar, then they may press it again
		}
		
		transform.y += (playerVelocityY*GameTime.deltaTime); //we only need to move the bird on the Y axis for this demo so I'm only gonna calculate it for the Y
	}
}
