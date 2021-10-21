//NAME: App.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Mira Entry point and execution manager. This is the class that actually sets up the engine
//		and runs the game inside it's execution.

package com.PlatformerGame.engine.core;

import static org.lwjgl.glfw.GLFW.*;

import com.PlatformerGame.engine.renderer.*;
import com.PlatformerGame.engine.audio.*;
import com.PlatformerGame.engine.core.components.GameObjectComponent;
import com.PlatformerGame.game.*;

public class App {
	public static Window gameWindow;
	public static OGLRenderer renderer;
	public static ALAudioRenderer audioRenderer;
	public static Scene currentScene;
	private Game game;
	
	private int width, height;
	
	private int frameCounter;
	private double timer, gcTimer;
//	private double maxFPSTimer;
	
	
	public App() {
		// init window - width and height should be in an ini file
		width = 800;
		height = 600;
		gameWindow = new Window(width, height, "GLFW window. Press esc to close...", false);

		// init static renderers
		renderer = new OGLRenderer();
		audioRenderer = new ALAudioRenderer();
		
		// init game timer
		frameCounter = 0;
		timer = gcTimer = 0;
//		maxFPSTimer = 0;

		// create game class
		game = new Game();
		
		// run Game.onCreate()
		game.onCreate();
		
	}
	
	//Execute a component free from normal engine flow.
	//	Sometimes, a component will need to execute before its owner gameobject does. Use this in the GameObject's code.
	//	This won't be an issue when I finally move to a real script component and move logic out of the GameObject class.
	//		(this makes me think the whole structure is getting needlessly convoluted when I could probably have almost all
	//			logic in that class and not have components altogether. Oy vey.)
	public static <T extends GameObjectComponent> void miraPriorityExecuteComponentOnCreate(T component) {
		component.onCreate();
		component.hasRunOnce = true;
	}
	
	public void loop() {
		// set up gameLoop and run every onCreate/onUpdate for every gameObject in the current scene in Game.java
		while (!glfwWindowShouldClose(gameWindow.window)) {
			glfwPollEvents();
			if(glfwGetKey(gameWindow.window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				glfwSetWindowShouldClose(gameWindow.window, true);
			}
			
			if(currentScene != null) {
				// Iterate through all GameObject onCreates in order
				for(int h = 0; h <= currentScene.getHierarchyDepth(); h++) {
					for (int g = 0; g < currentScene.size(); g++) {	
						if (currentScene.get(g).hierLevel == h && !currentScene.get(g).hasRunOnce) {
							currentScene.get(g).onCreate();
							currentScene.get(g).hasRunOnce = true;
						}
					}
				}
				currentScene.alreadyIteratedObjects.clear();

				// Iterate through all GameObject onUpdates in order
				for(int h = 0; h <= currentScene.getHierarchyDepth(); h++) {
					for (int g = 0; g < currentScene.size(); g++) {	
						if (currentScene.get(g).hierLevel == h) {
							currentScene.get(g).onUpdate();
							for (int i = 0; i < currentScene.get(g).listComponents.size(); i++) {
								if (currentScene.get(g).listComponents.get(i).enabled) {
									if (!currentScene.get(g).listComponents.get(i).hasRunOnce)
										currentScene.get(g).listComponents.get(i).onCreate();
									
									currentScene.get(g).listComponents.get(i).onUpdate();
								}
							}
						}
					}
				}
				currentScene.alreadyIteratedObjects.clear();
			} else {
				// some kind of loading screen?
			}

			game.onUpdate();
			
			//Drawing
			renderer.updateRender(gameWindow);

			// GameTimer handling
			GameTime.currentTime = glfwGetTime();
			GameTime.unaffectedDeltaTime = (GameTime.currentTime-GameTime.oldTime);
			GameTime.deltaTime = GameTime.unaffectedDeltaTime*GameTime.getTimeScale();
			if (timer >= 1) {
				System.out.println(frameCounter);
				frameCounter = 0;
				timer = 0;
			} else {
				timer += GameTime.unaffectedDeltaTime;
			}
			
			GameTime.oldTime = GameTime.currentTime;
			
			// Sleep to avoid resource hogging
//			while (maxFPSTimer < 0.008333) {
//				Thread.yield();
//				
//				//sleep for 1ms
//				try {
//					Thread.sleep(3);
//				} catch(Exception e) {} 
//				
//				maxFPSTimer += GameTime.unaffectedDeltaTime;
//			}
//			maxFPSTimer = 0;
			frameCounter++;
			
			// Asks politely for garbage to be collected every minute. Doesn't happen quite as often as I'd prefer.
			if (gcTimer >= 60) {
				System.gc();
				gcTimer = 0;
			} else {
				gcTimer += GameTime.unaffectedDeltaTime;
			}
		}
		
	}
	
	public void destroy() {
		gameWindow.destroyWindow();
		audioRenderer.destroy();
	}
	
	public static void main(String[] args) {
		
		App miraGame = new App();
		miraGame.loop();
		miraGame.destroy();
	}

}
