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
	
	public void loop() {
		// set up gameLoop and run every onCreate/onUpdate for every gameObject in the current scene in Game.java
		while (!glfwWindowShouldClose(gameWindow.window)) {
			glfwPollEvents();
			if(glfwGetKey(gameWindow.window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				glfwSetWindowShouldClose(gameWindow.window, true);
			}
			
			
			/*	Some explanation because this loop is really freakin hard to read and I'll probably need to come back to it later
			 *	Goals of this loop are:
			 *		1: Execute each GameObject's code in the current scene
			 *			a: onCreate() method should be in this loop, so that GameObjects that are created after the loop begins may execute
			 * 			b: onCreate() method should only be run once per scene load!
			 * 			c: onUpdate() method should be run every frame for every GameObject
			 * 		2: Execute each GameObject's component's scripts in the same way
			 * 		3: Do this in such an order than a child object's code will always execute after its parent's code
			 */
			
			// Only run when there is a scene loaded
			if(currentScene != null) {
				// This gets the depth of the scene tree so we don't waste time looking for objects where there are none
				// 		and then it iterates through each level of the tree starting with its roots and working through children
				for(int h = 0; h <= currentScene.getHierarchyDepth(); h++) {
					// This iterates through all GameObjects in the scene
					for (int g = 0; g < currentScene.size(); g++) {	
						// And finally, if the GameObject is on the current level 'h' in our tree, we'll check if it's onCreate() method has
						//		been run yet. If it hasn't, run it. If it has, ignore.
						if (currentScene.get(g).hierLevel == h && !currentScene.get(g).hasRunOnce) {
							currentScene.get(g).onCreate();
							currentScene.get(g).hasRunOnce = true;
						}
					}
				}
				
				game.onUpdate();

				// I do pretty much the same thing here, but after that's done to guarantee that every object has been onCreate()d before we 
				//		run the onUpdate()s [because sometimes the onUpdate()s will rely on objects to already be initialized in engine flow]
				for(int h = 0; h <= currentScene.getHierarchyDepth(); h++) {
					for (int g = 0; g < currentScene.size(); g++) {	
						if (currentScene.get(g).hierLevel == h) {
							// Runs the GameObjects' onUpdate() methods in order
							currentScene.get(g).onUpdate();
							// Then runs the onCreate() and onUpdate() methods of the object's components.
							for (int i = 0; i < currentScene.get(g).listComponents.size(); i++) {
								if (currentScene.get(g).listComponents.get(i).enabled) {
									// And I have this here so that components don't initialize before the object
									if (!currentScene.get(g).listComponents.get(i).hasRunOnce)
										currentScene.get(g).listComponents.get(i).onCreate();
									
									currentScene.get(g).listComponents.get(i).onUpdate();
								}
							}
						}
					}
				}
			} else {
				// some kind of loading screen?
			}

			
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
