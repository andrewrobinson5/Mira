//NAME: Game.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: This is the game code. It consists of two functions that get called by Mira:
//			onCreate() - runs when the game is started
//			onUpdate() - runs once per frame
//		There is no real entry point inside this Game class, only in the App class, which instantiates
//		this class and calls those two functions. This way, the only thing the game programmer of this
//		class has to worry about is game logic and not setting up the engine.

package com.PlatformerGame.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import com.PlatformerGame.engine.core.*;
//import com.PlatformerGame.engine.core.components.*;
import com.PlatformerGame.game.prefabs.*;

public class Game {
	//class member variables
	private boolean paused;
	
	public Scene myScene = new Scene();
	
	TwoPipes wall1;
	TwoPipes wall2;
	TwoPipes wall3;
	TwoPipes wall4;
	
	PlayerController player = new PlayerController();
	BackgroundPlate backdrop = new BackgroundPlate();
	
	public void onCreate() {
		paused = true;
		
		//GameObject creation and adding to scene.
		player.name = "player";
		myScene.add(player);
		
		wall1 = new TwoPipes();
		wall1.name = "wall1";
		wall1.transform.x = 0f;
		myScene.add(wall1);

		wall2= new TwoPipes();
		wall2.name = "wall2";
		wall2.transform.x = 0.85f;
		myScene.add(wall2);

		wall3 = new TwoPipes();
		wall3.name = "wall3";
		wall3.transform.x = 1.7f;
		myScene.add(wall3);

		wall4 = new TwoPipes();
		wall4.name = "wall4";
		wall4.transform.x = 2.55f;
		myScene.add(wall4);
		
		myScene.add(backdrop);
		
		myScene.loadScene(); // After everything is in the scene that should be, load it.
	}	
	
	public void onUpdate() {
		if (paused) {
			GameTime.pause();
			
			//TODO: Input handling abstraction
			if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS) {
				paused = !paused;
			}
		} else if (!paused)
			GameTime.play();
		
		if (player.isRestarting) {
			//REPLACE THIS WITH LOSS STATE
			paused = true;
			myScene.unloadScene();
			wall1.transform.x = 0f;
			wall2.transform.x = 0.85f;
			wall3.transform.x = 1.7f;
			wall4.transform.x = 2.55f;
			onCreate();
			myScene.loadScene();
			player.isRestarting = false;
		}
	}
}
