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
	private double timer, gcTimer;
	
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
		timer = gcTimer = 0;

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
			
			// The memory leak is inside this for loop.
			// CORRECTION, the memory leak IS this for loop. Apparently for-each loops inside an infinite while that iterate over a collection cause iterators to be created that aren't destroyed until garbage collected.
			//for (GameObject g : game.myScene) {
			for (int g = 0; g < game.myScene.size(); g++) {	
				if (!game.myScene.get(g).hasRunOnce)
					game.myScene.get(g).onCreate();

				game.myScene.get(g).onUpdate();
				for (int i = 0; i < game.myScene.get(g).listComponents.size(); i++) {
					if (game.myScene.get(g).listComponents.get(i).enabled) {
						if (!game.myScene.get(g).listComponents.get(i).hasRunOnce)
							game.myScene.get(g).listComponents.get(i).onCreate(); // for some reason there's a small leak on this line, also not arrayList
						
						game.myScene.get(g).listComponents.get(i).onUpdate(); // big leak on this line, specifically quadRenderer
					}
				}
			}
			
			// This slows the leak down a whole hell of a lot, but probably only because it slows the program down a whole hell of a lot. It destroys the iterators, but not the integer leak caused by QuadRendererComponent's onUpdate() function call
			// I think what I'm gonna do is call System.gc() something like every 30 seconds or a minute. It shouldn't cause much of a performance issue, I know this line will run at least 100 times per second on my desktop computer.
			//System.gc();
			
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
			
			// Asks politely for garbage to be collected every minute.
			if (gcTimer >= 60) {
				System.gc();
				gcTimer = 0;
			} else {
				gcTimer += gameTimer.unaffectedDeltaTime;
			}
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
