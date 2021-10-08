package com.PlatformerGame.game.prefabs;

import com.PlatformerGame.engine.core.GameObject;
import com.PlatformerGame.engine.core.Texture;
import com.PlatformerGame.engine.core.components.*;

public class BackgroundPlate extends GameObject {
	public void onCreate() {
		QuadRendererComponent renderComp = addComponent(new QuadRendererComponent(2.1f, 2.1f));
		renderComp.tex = new Texture("/textures/backdrop.png");
		renderComp.tileX = 2.2f;
	}
	
	public void onUpdate() {
		
	}
}
