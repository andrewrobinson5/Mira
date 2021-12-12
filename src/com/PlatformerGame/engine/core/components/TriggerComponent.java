package com.PlatformerGame.engine.core.components;

import java.util.ArrayList;

public class TriggerComponent extends GameObjectComponent {
	//this isn't particularly good but it works for this project. It makes the collision system less reusable for other games, though.
	public static ArrayList<TriggerComponent> triggers = new ArrayList<TriggerComponent>(); // we store an array of all colliders in a scene so that one object can check itself against everything else. This is game specific, not engine specific.
	private int index;
	
	public float x, y; //location variables for the collider
	public float w, h; //size of the collider
	
	public void onUpdate() {
		//we want the collider to stick to the position of its GameObject
		x = m_object.transform.getGlobalCoords().get(0);
		y = m_object.transform.getGlobalCoords().get(1);
	}
	
	public void destroy() {
		triggers.remove(index); //removes an object from a list of colliders. Buggy. Don't like this.
	}
	
	public TriggerComponent(float width, float height) {
		super("TriggerComponent");
		w = width;
		h = height;
	
		triggers.add(this); //add colliders to list. This is bad, but I need a hacky thing to keep the pipe colliders
		index = triggers.size()-1; // store the index. This only works if you never destroy. This is bad. I dunno man
	}
}
