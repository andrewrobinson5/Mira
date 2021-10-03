package com.PlatformerGame.game.prefabs;

//import java.lang.Math;

import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.QuadRendererComponent;

public class PipeTop extends GameObject {
	
	public void onCreate() {
		super.onCreate();
		
	}
	
	public void onUpdate() {
		
	}
	
	public PipeTop() {
		transform.y = 1.2f;
		transform.z = -0.5f;
		this.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		this.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/pipe.png");
	}
}
