package com.PlatformerGame.engine.core;

import org.joml.*;

public class TransformComponent extends GameObjectComponent {
	public float x, y, z;
	public float tmpX, tmpY, tmpZ;
	
	public void onUpdate() {
		if(m_object.parent != null) {
			setCoords(m_object.parent.transform.getCoords().get(0),
					  m_object.parent.transform.getCoords().get(1),
					  m_object.parent.transform.getCoords().get(2));
		}
	}
	
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
