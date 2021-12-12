package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.core.components.*;

public class Collider {
	
	//checks one perfectly rectangular collider against another. All colliders in my demo will use perfectly rectangular colliders, so I don't need to do much work here.
	public static boolean checkCollision(ColliderComponent col1, ColliderComponent col2) {

		if (col1.x + (0.5*col1.w) >= col2.x - (0.5*col2.w) && // checks if right side of collider 1 is past the left side of collider 2
			col1.x - (0.5*col1.w) <= col2.x + (0.5*col2.w) && // checks if left side of collider 1 is past the right side of collider 2
			col1.y + (0.5*col1.h) >= col2.y - (0.5*col2.h) && // checks if top side of collider 1 is past the bottom side of collider 2
			col1.y - (0.5*col1.h) <= col2.y + (0.5*col2.h) // checks if bottom side of collider 1 is past the top side of collider 2
				) {
			// if all conditions are true, then the two rectangles do overlap
	        return true;
	    } else {
	        // no collision
	        return false;
	    }
	}
	
	//overloading for trigger detection
	public static boolean checkCollision(ColliderComponent col1, TriggerComponent col2) {

		if (col1.x + (0.5*col1.w) >= col2.x - (0.5*col2.w) && // checks if right side of collider 1 is past the left side of collider 2
			col1.x - (0.5*col1.w) <= col2.x + (0.5*col2.w) && // checks if left side of collider 1 is past the right side of collider 2
			col1.y + (0.5*col1.h) >= col2.y - (0.5*col2.h) && // checks if top side of collider 1 is past the bottom side of collider 2
			col1.y - (0.5*col1.h) <= col2.y + (0.5*col2.h) // checks if bottom side of collider 1 is past the top side of collider 2
				) {
			// if all conditions are true, then the two rectangles do overlap
	        return true;
	    } else {
	        // no collision
	        return false;
	    }
	}
}
