package com.PlatformerGame.engine.core;

import static org.lwjgl.glfw.GLFW.*;

import com.PlatformerGame.engine.renderer.*;
import com.PlatformerGame.game.*;

public class App {
	public static Window gameWindow;
	public static OGLRenderer renderer;
	public static GameTime gameTimer;
	public static Scene currentScene;
	private Game game;

	private int width, height;
	
	private int frameCounter;
	private double timer, gcTimer, maxFPSTimer;
	
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
		timer = gcTimer = maxFPSTimer = 0;

		// create game class
		game = new Game();

		// run Game.onCreate()
		game.onCreate();
	}
	
	public void loop() {
		// set up gameLoop and run every onCreate/onUpdate for every gameObject in the current scene in Game.java
		while (!glfwWindowShouldClose(gameWindow.window)) {
			glfwPollEvents();
			if(glfwGetKey(gameWindow.window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				glfwSetWindowShouldClose(gameWindow.window, true);
			}

			game.onUpdate();
			//TODO: iterate through every game object in scene, calling the gameObject's onCreate() once and
			//	onUpdate() every frame, as well as each of its components onCreate() and onUpdate() functions.
			
			//TODO: iterate thru items in current scene in correct order
			if(currentScene != null) {
				// Iterate through all GameObject onCreates in order
				for(int h = 0; h <= currentScene.getHierarchyDepth(); h++) {
					for (int g = 0; g < currentScene.size(); g++) {	
						if (currentScene.get(g).hierLevel == h && !currentScene.get(g).hasRunOnce) {
							currentScene.hierarchyHelperFunctionCreate(currentScene.get(g));
						}
					}
				}
				currentScene.alreadyIteratedObjects.clear();
				
				// Iterate through all GameObject onUpdates in order
				for(int h = 0; h <= currentScene.getHierarchyDepth(); h++) {
					for (int g = 0; g < currentScene.size(); g++) {	
						if (currentScene.get(g).hierLevel == h) {
							currentScene.hierarchyHelperFunctionUpdate(currentScene.get(g));
						}
					}
				}
				currentScene.alreadyIteratedObjects.clear();
			} else {
				// some kind of loading screen?
			}
			
			//Drawing
			renderer.updateRender(gameWindow);

			// GameTimer handling
			gameTimer.currentTime = glfwGetTime();
			gameTimer.unaffectedDeltaTime = (gameTimer.currentTime-gameTimer.oldTime);
//			System.out.println(gameTimer.unaffectedDeltaTime);
			gameTimer.deltaTime = gameTimer.unaffectedDeltaTime*gameTimer.getTimeScale();
			if (timer >= 1) {
				System.out.println(frameCounter);
				frameCounter = 0;
				timer = 0;
			} else {
				timer += gameTimer.unaffectedDeltaTime;
			}
			
			gameTimer.oldTime = gameTimer.currentTime;
			
			// Sleep to avoid resource hogging
			while (maxFPSTimer < 0.008333) {
				Thread.yield();
				
				//sleep for 1ms
				try {
					Thread.sleep(1);
				} catch(Exception e) {} 
				
				maxFPSTimer += gameTimer.unaffectedDeltaTime;
			}
			maxFPSTimer = 0;
			frameCounter++;
			
			// Asks politely for garbage to be collected every minute.
//			if (gcTimer >= 60) {
//				System.gc();
//				gcTimer = 0;
//			} else {
//				gcTimer += gameTimer.unaffectedDeltaTime;
//			}
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
