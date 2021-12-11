package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.core.components.*;

public class Collider {
	
	//checks one perfectly rectangular collider against another. All colliders in my demo will use perfectly rectangular colliders, so I don't need to do much work here.
	//	adapted from https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection
	//	Detects if there is a gap or no gap between two objects
	public static boolean checkCollision(ColliderComponent col1, ColliderComponent col2) {
//		if (col1.x < col2.x + col2.w &&
//	        col1.x + col1.w > col2.x &&
//	        col1.y < col2.y + col2.h &&
//	        col1.h + col1.y > col2.y) {
	    
		if (col1.x + (0.5*col1.w) >= col2.x - (0.5*col2.w) &&
			col1.x - (0.5*col1.w) <= col2.x + (0.5*col2.w) &&
			col1.y + (0.5*col1.h) >= col2.y - (0.5*col2.h) &&
			col1.y - (0.5*col1.h) <= col2.y + (0.5*col2.h)
				) {
			// collision detected!
	        return true;
	    } else {
	        // no collision
	        return false;
	    }
	}
}
