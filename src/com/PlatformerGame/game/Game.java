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
import com.PlatformerGame.engine.core.components.QuadRendererComponent;
//import com.PlatformerGame.engine.core.components.*;
import com.PlatformerGame.game.prefabs.*;

public class Game {
	//class member variables
	private boolean paused;
	
	public Scene myScene = new Scene();
	
	public Scene winScreen = new Scene();
	
	private TwoPipes wall1;
	private TwoPipes wall2;
	private TwoPipes wall3;
	private TwoPipes wall4;
	
	private PlayerController player = new PlayerController();
	private BackgroundPlate backdrop = new BackgroundPlate();
	private ScoreCounter sc = new ScoreCounter();
	private GameObject spaceText = new GameObject(0f, 0.4f, -0.9f);
	
	public void onCreate() {
		paused = true;

		//GameObject creation and adding to scene. Everything that is going to be in the scene should be added to it here

		//set up instruction text
		spaceText.addComponent(new QuadRendererComponent(1f, 0.3f));
		spaceText.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/space.png");
		myScene.add(spaceText);
		
		sc.transform.setCoords(-0.7f, -0.7f, -0.9f);
		myScene.add(sc);
		
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
		
		//set up the scene for the win screen.
		winScreen = new Scene();
		GameObject winBG = new GameObject(0,0,0);
		winBG.addComponent(new QuadRendererComponent(2, 2));
		winBG.<QuadRendererComponent>getComponent("QuadRenderer").tex = new Texture("/textures/winscreen.png");
		winScreen.add(winBG);
		
	}	
	
	public void onUpdate() {
		if (paused) {
			GameTime.pause(); //start the game paused. I don't have a centralized physics system, so I'm just multiplying the gametime by all the velocity calculations per object
			
			//TODO: Input handling abstraction
			if (glfwGetKey(App.gameWindow.window, GLFW_KEY_SPACE) == GLFW_PRESS) {
				paused = !paused; //toggle paused state
				spaceText.getComponent("QuadRenderer").enabled = false;
			}
		} else if (!paused)
			GameTime.play();
		
		if (player.shouldUpdateScore) {
			sc.updateScoreCount(player.score); // little extra work but makes it so scoreCounter doesn't need to change textures every frame
		}
		
		if (player.score > 99 && myScene != winScreen) {
			//win state
			myScene.unloadScene();
			winScreen.loadScene(); //load the win screen
		}
		
		if (player.isRestarting) {
			//when the player loses, the game should restart. I do this by resetting the scene and loading it again
			paused = true;
			myScene.unloadScene();
			wall1.transform.x = 0f;
			wall2.transform.x = 0.85f;
			wall3.transform.x = 1.7f;
			wall4.transform.x = 2.55f;
			onCreate(); //and then I have to run this from here because the logic for the game is in this class and not in a GameObject method
			myScene.loadScene();
			player.isRestarting = false;
		}
	}
}
