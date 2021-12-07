package com.PlatformerGame.game.prefabs;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.*;

import java.lang.Math;

public class TwoPipes extends GameObject {
	private float pipesVelocity = -0.5f;
	
	public void onCreate() {
		Texture pipeTexture = new Texture("/textures/pipe.png");
		
		transform.y = (float)(Math.random()*.8f)-.4f;
		transform.z = -0.5f;
		
		GameObject pipeBottom = new GameObject(0, -1.2f, 0);
		pipeBottom.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		pipeBottom.<QuadRendererComponent>getComponent("QuadRenderer").tex = pipeTexture;
		
		GameObject pipeTop= new GameObject(0, 1.2f, 0);
		pipeTop.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		pipeTop.<QuadRendererComponent>getComponent("QuadRenderer").tex = pipeTexture;
		
		App.currentScene.add(pipeBottom);
		App.currentScene.add(pipeTop);
		addChild(pipeBottom);
		addChild(pipeTop);
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
