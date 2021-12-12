package com.PlatformerGame.engine.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GameTime {
	private static float timeScale = 1.0f;
	// deltaTime has timeScale applied, unaffectedDeltaTime does not.
	public static double currentTime, oldTime, deltaTime, unaffectedDeltaTime;
	
	public static double getTime() {
		return glfwGetTime();
	}
	
	public static void setTimeScale(float l_scale) {
		timeScale = l_scale;
	}
	
	public static float getTimeScale() {
		return timeScale;
	}
	
	public static void pause() {
		timeScale = 0;
	}
	
	public static void play() {
		timeScale = 1.0f;
	}
}
