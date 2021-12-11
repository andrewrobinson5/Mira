package com.PlatformerGame.game.prefabs;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.*;

import java.lang.Math;
//import java.util.ArrayList;

public class TwoPipes extends GameObject {
	
	private float pipesVelocity = -0.5f;
	GameObject pipeBottom;
	GameObject pipeTop;
	
	public void onCreate() {
		Texture pipeTexture = new Texture("/textures/pipe.png");
		
		transform.y = (float)(Math.random()*.8f)-.4f;
		transform.z = -0.5f;
		
		pipeBottom = new GameObject(0, -1.4f, 0);
		pipeBottom.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		pipeBottom.<QuadRendererComponent>getComponent("QuadRenderer").tex = pipeTexture;
		pipeBottom.<ColliderComponent>addComponent(new ColliderComponent(0.3f, 1.8f));
		
//		GameObject pipeTop= new GameObject(0, 1.4f, 0);
		pipeTop= new GameObject(0, 1.4f, 0);
		pipeTop.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		pipeTop.<QuadRendererComponent>getComponent("QuadRenderer").tex = pipeTexture;
		pipeTop.<ColliderComponent>addComponent(new ColliderComponent(0.3f, 1.8f));
		
		
		addChild(pipeBottom);
		addChild(pipeTop);
		App.currentScene.add(pipeBottom);
		App.currentScene.add(pipeTop);
	}
	
	public void onUpdate() {
		if (transform.x <= -1.325f) {
			transform.y = (float)(Math.random()*.8f)-.4f;
			transform.x = 2.075f;
		}
		
//		System.out.println("DEBUG: pipetop transform: " + pipeTop.transform.getGlobalCoords());
//		System.out.println("DEBUG: pipetop collider transform: x-" + pipeTop.<ColliderComponent>getComponent("ColliderComponent").x + "  y-" + pipeTop.<ColliderComponent>getComponent("ColliderComponent").y);
//		System.out.println("DEBUG: pipebottom transform: " + pipeBottom.transform.getGlobalCoords());
//		System.out.println("DEBUG: pipebottom collider transform: x-" + pipeBottom.<ColliderComponent>getComponent("ColliderComponent").x + "  y-" + pipeBottom.<ColliderComponent>getComponent("ColliderComponent").y);
		
		//scroll the pipes across the screen to make it appear as though the player is moving
		transform.x += (pipesVelocity*GameTime.deltaTime);
	}
}
