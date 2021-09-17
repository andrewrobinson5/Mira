package com.PlatformerGame.engine.core;

public class GameTime {
	private float timeScale = 1.0f;
	// deltaTime has timeScale applied, unaffectedDeltaTime does not.
	public double currentTime, oldTime, deltaTime, unaffectedDeltaTime;
	
	public void setTimeScale(float l_scale) {
		timeScale = l_scale;
	}
	
	public float getTimeScale() {
		return timeScale;
	}
	
	public void pause() {
		timeScale = 0;
	}
	
	public void play() {
		timeScale = 1.0f;
	}
}
