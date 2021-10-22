//NAME: SphereColliderComponent.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Detect collision with another collider component by taking the radius of the sphere and the origin and detecting
//		whether it is close enough to another object's edge to "Collide" with it.

package com.PlatformerGame.engine.core.components;

public class SphereColliderComponent extends ColliderComponent {
	private float radius;
	
	public void onUpdate() {
		//
	}
	
	public SphereColliderComponent(float r) {
		radius = r;
	}
}
