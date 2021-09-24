package com.PlatformerGame.game.prefabs;

import com.PlatformerGame.engine.core.*;
import java.lang.Math;

public class TwoPipes extends GameObject {
	public void onCreate() {
//		transform.y = (float)(Math.random()*1)-1.5f;
	}
	
	public void onUpdate() {
		if (transform.x <= -1.325f) {
			transform.y = (float)(Math.random()*.75)-.375f;
			transform.x = 2.075f;
		}
	}
	
	public TwoPipes() {
		transform.y = (float)(Math.random()*.75f)-.375f;
		addChild(new PipeBottom());
		addChild(new PipeTop());
	}
}
