package com.PlatformerGame.game.prefabs;

import java.lang.Math;

import org.joml.Vector3f;

import com.PlatformerGame.engine.core.*;

public class Pipe extends GameObject {
	private TransformComponent m_transform = this.<TransformComponent>getComponent("Transform");
	
	public void onCreate() {
		super.onCreate();
		
	}
	
	public void onUpdate() {
		if (m_transform.x <= -1.325f) {
			m_transform.y = (float)(Math.random()*1)-1.5f;
			m_transform.x = 2.075f;
		}

		System.out.println("test2");
	}
	
	public Pipe() {
		super(0f, ((float)(Math.random()*1)-1.5f), 0);
		this.<QuadRendererComponent>addComponent(new QuadRendererComponent(0.3f, 1.5f));
		this.<QuadRendererComponent>getComponent("QuadRenderer").solidColor = new Vector3f(0.0f, 0.5f, 0.0f);
	}
}
