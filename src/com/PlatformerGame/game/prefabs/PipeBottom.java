package com.PlatformerGame.game.prefabs;

//import java.lang.Math;

import org.joml.*;

import com.PlatformerGame.engine.core.*;

public class PipeBottom extends GameObject {
	
	public void onCreate() {
		super.onCreate();
		
	}
	
	public void onUpdate() {
		
	}
	
	public PipeBottom() {
		transform.y = -1.2f;
		//super(0f, ((float)(Math.random()*1)-1.5f), 0);
		this.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.8f));
		this.<QuadRendererComponent>getComponent("QuadRenderer").solidColor = new Vector4f(0.0f, 0.5f, 0.0f, 1f);
	}
}
