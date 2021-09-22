package com.PlatformerGame.engine.core;

import org.joml.*;

public class TransformComponent extends GameObjectComponent {
	public float x, y, z;
	public float globalX, globalY, globalZ;
	
	public void onUpdate() {
		if(m_object.parent != null) {
			globalX = x + m_object.parent.transform.getCoords().get(0);
			globalY = y + m_object.parent.transform.getCoords().get(1);
			globalZ = z + m_object.parent.transform.getCoords().get(2);
		} else {
			globalX = x;
			globalY = y;
			globalZ = z;
		}
	}
	
	public void setCoords(float xCoord, float yCoord, float zCoord) {
		x = xCoord;
		y = yCoord;
		z = zCoord;
	}
	
	public Vector3f getGlobalCoords() {
		return new Vector3f(globalX, globalY, globalZ);
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
