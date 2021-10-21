//NAME: PlayerController.java
//COPYRIGHT: Andrew Robinson
//DESC: Player movement and collision detection

package com.PlatformerGame.game.prefabs;

import static org.lwjgl.glfw.GLFW.*;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.*;

public class PlayerController extends GameObject {
	//class member variables
	private final float gravity = 6.8f;
	private float playerVelocityY;
	private boolean canJump;
	public boolean isRestarting = false;

	//resources
	Sound jumpSound = new Sound("/sounds/jump.ogg");
	
	//intialize components outside onCreate
	public QuadRendererComponent birdRenderer = new QuadRendererComponent(0.2f, 0.16f);
	private SoundEmitterComponent jumpEmitterComponent = new SoundEmitterComponent(jumpSound, "Jump Emitter");
	
	public void onCreate() {
		playerVelocityY = 0;
		canJump = true;
		
		addComponent(birdRenderer);
		birdRenderer.tex = new Texture("/textures/bird.png");
		transform.setCoords(-0.6f, 0.0f, 0.0f);
		
		addComponent(jumpEmitterComponent);
	}
	
	public void onUpdate() {
		if ((birdRenderer.bounds[3].get(1) + transform.getCoords().get(1)) > -1f) {
			playerVelocityY -= gravity*GameTime.deltaTime;
		} else {
			playerVelocityY = 0;
			isRestarting = true;
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
