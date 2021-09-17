package com.PlatformerGame.engine.core;

import static org.lwjgl.glfw.GLFW.*;

import com.PlatformerGame.engine.renderer.*;
import com.PlatformerGame.game.*;

public class App {
	public static Window gameWindow;
	public static OGLRenderer renderer;
	public static GameTime gameTimer;
	private Game game;

	private int width, height;
	
	private int frameCounter;
	private double timer;
	
	public App() {
		// init window - width and height should be in an ini file
		width = 800;
		height = 600;
		gameWindow = new Window(width, height, "GLFW window. Press esc to close...", false);

		// init static renderer
		renderer = new OGLRenderer();

		// init game timer
		gameTimer = new GameTime();
		frameCounter = 0;
		timer = 0;

		// create game class
		game = new Game();

		// run Game.onCreate()
		game.onCreate();
	}
	
	public void loop() {
		// set up gameLoop and run every onCreate/onUpdate for every gameObject in the current scene in Game.java
		// at beginning of gameLoop push current and deltaTime onto timer, at end push oldTime.
		while (!glfwWindowShouldClose(gameWindow.window)) {
			glfwPollEvents();
			if(glfwGetKey(gameWindow.window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				glfwSetWindowShouldClose(gameWindow.window, true);
			}

			game.onUpdate();
			//TODO: iterate through every game object in scene, calling the gameObject's onCreate() once and
			//	onUpdate() every frame, as well as each of its components onCreate() and onUpdate() functions.
			//TODO: before I do that, I need a scene graph. Using arrayList myScene as temp.
			for (GameObject g : game.myScene) {
				g.onCreate();
				g.onUpdate();
				for (GameObjectComponent i : g.listComponents) {
					if (i.enabled) {
						i.onCreate();
						i.onUpdate();
					}
				}
			}
			
			//Drawing
			renderer.updateRender(gameWindow);

			// GameTimer handling
			frameCounter++;
			gameTimer.currentTime = glfwGetTime();
			gameTimer.unaffectedDeltaTime = (gameTimer.currentTime-gameTimer.oldTime);
			gameTimer.deltaTime = gameTimer.unaffectedDeltaTime*gameTimer.getTimeScale();
			if (timer >= 1) {
				System.out.println(frameCounter);
				frameCounter = 0;
				timer = 0;
			} else {
				timer += gameTimer.unaffectedDeltaTime;
			}
			gameTimer.oldTime = gameTimer.currentTime;
		}
		
	}
	
	public void destroy() {
		gameWindow.destroyWindow();
	}
	
	public static void main(String[] args) {
		
		App miraGame = new App();
		miraGame.loop();
		miraGame.destroy();
	}

}
