//DESC: Base class for all collider components. Passes resources into Collider for broad phase collision detection.

package com.PlatformerGame.engine.core.components;

import java.util.ArrayList;

public class ColliderComponent extends GameObjectComponent {
	public static ArrayList<ColliderComponent> colliders = new ArrayList<ColliderComponent>();
	private int index;
	
	public float x, y;
	public float w, h;
	
	public void onUpdate() {
		x = m_object.transform.getGlobalCoords().get(0);
		y = m_object.transform.getGlobalCoords().get(1);
	}
	
	public void destroy() {
		colliders.remove(index);
	}
	
	public ColliderComponent(float width, float height) {
		super("ColliderComponent");
		//center the local vertex coordinates based on the width and height
//		float leftX = 0-(width/2);
//		float rightX = width/2;
//		float topY = height/2;
//		float bottomY = 0-(height/2);

//		bounds[0] = new Vector3f(leftX, topY, 0); //top left vert
//		bounds[1] = new Vector3f(rightX, topY, 0); //top right vert
//		bounds[2] = new Vector3f(leftX, bottomY, 0); //bottom left vert
//		bounds[3] = new Vector3f(rightX, bottomY, 0); //bottom right vert
		w = width;
		h = height;
	
		colliders.add(this); //add colliders to list. This is bad, but I need a hacky thing to keep the pipe colliders
		index = colliders.size()-1; // store the index. This only works if you never destroy. This is bad. I dunno man
	}
}
