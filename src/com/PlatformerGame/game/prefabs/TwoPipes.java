package com.PlatformerGame.game.prefabs;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.*;

import java.lang.Math;

public class TwoPipes extends GameObject {
	
	private float pipesVelocity = -0.5f; //how fast the pipe should be moving across the screen each second
	private GameObject pipeBottom;
	private GameObject pipeTop;
	
	public void onCreate() {
		Texture pipeTexture = new Texture("/textures/pipe.png"); //load pipe image
		
		transform.y = (float)(Math.random()*.8f)-.4f; //set the vertical position randomly
		transform.z = -0.5f; //push the pipes toward the camera so it renders in front of the bird and background
		
		pipeBottom = new GameObject(0, -1.3f, 0);
		pipeBottom.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		pipeBottom.<QuadRendererComponent>getComponent("QuadRenderer").tex = pipeTexture;
		pipeBottom.<ColliderComponent>addComponent(new ColliderComponent(0.3f, 1.8f));
		
		pipeTop= new GameObject(0, 1.3f, 0);
		pipeTop.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		pipeTop.<QuadRendererComponent>getComponent("QuadRenderer").tex = pipeTexture;
		pipeTop.<ColliderComponent>addComponent(new ColliderComponent(0.3f, 1.8f));
		
		addComponent(new TriggerComponent(0.05f, 2f));
		
		addChild(pipeBottom); //make these gameobjects children to the TwoPipes object, so they can inherit position from it
		addChild(pipeTop);
		App.currentScene.add(pipeBottom);
		App.currentScene.add(pipeTop);
	}
	
	public void onUpdate() {
		if (transform.x <= -1.325f) {
			transform.y = (float)(Math.random()*.8f)-.4f;
			transform.x = 2.075f;
		}
		
		//scroll the pipes across the screen to make it appear as though the player is moving
		transform.x += (pipesVelocity*GameTime.deltaTime);
	}
}
