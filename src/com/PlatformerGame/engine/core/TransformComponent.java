package com.PlatformerGame.engine.core;

import org.joml.*;

public class TransformComponent extends GameObjectComponent {
	public float x, y, z;	
	
	public void setCoords(float xCoord, float yCoord, float zCoord) {
		x = xCoord;
		y = yCoord;
		z = zCoord;
	}
	
	public Vector3f getCoords() {
		return new Vector3f(x, y, z);
	}
	
	public TransformComponent(float xCoord, float yCoord, float zCoord) {
		super("Transform");
		setCoords(xCoord, yCoord, zCoord);
	}
	
	public TransformComponent() {
		super("Transform");
		setCoords(0, 0, 0);
	}
}
