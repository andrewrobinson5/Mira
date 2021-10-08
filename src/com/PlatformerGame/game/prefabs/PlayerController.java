//NAME: PlayerController.java
//COPYRIGHT: Andrew Robinson
//DESC: Player movement and collision detection

package com.PlatformerGame.game.prefabs;

import static org.lwjgl.glfw.GLFW.*;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.*;

public class PlayerController extends GameObject {
	// class member variables
	final float gravity = 6.8f;
	float playerVelocityY;
	boolean canJump;

	//initialize sounds once because for some reason the second time it messes up here
	Sound jumpSound = new Sound("sounds/jump.ogg");
	
	QuadRendererComponent birdRenderer;
	SoundEmitterComponent jumpEmitterComponent;
	
	public void onCreate() {
		playerVelocityY = 0;
		canJump = true;
		
		birdRenderer = addComponent(new QuadRendererComponent(0.2f, 0.16f));
		birdRenderer.tex = new Texture("textures/bird.png");
		transform.setCoords(-0.6f, 0.0f, 0.0f);
		
		jumpEmitterComponent = addComponent(new SoundEmitterComponent(jumpSound, "Jump Emitter"));

	}
	
	public void onUpdate() {
		if ((birdRenderer.bounds[3].get(1) + transform.getCoords().get(1)) > -1f) {
			playerVelocityY -= gravity*GameTime.deltaTime;
		} else {
			playerVelocityY = 0;
			//SEND RESET GAME MESSAGE TO GAMEDIRECTOR?
		}
		
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS && canJump) {
			playerVelocityY = 2.1f; // this single line makes the player jump.
			canJump = false; // prevents jump spam from holding down space for longer than 1 frame
			
			jumpEmitterComponent.startSound(); //play sound
			
		}
		if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_RELEASE) {
			canJump = true;
		}
		
		transform.y += (playerVelocityY*GameTime.deltaTime); //we only need to move the bird on the Y axis for this demo so I'm only gonna calculate it for the Y
	}
}
