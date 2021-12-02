package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.core.components.*;
import java.util.ArrayList;

public class Collider {
	//list of colliders in scene to iterate over when using 
	private static ArrayList<ColliderComponent> colliders = new ArrayList<ColliderComponent>();
	
	//EXPOSE METHOD FOR COLLISION DETECTION, THAT WAY WE DON'T HAVE TO ITERATE EVERY OBJECT AGAINST EVERY OBJECT, ONLY
	//	WHAT THE USER NEEDS TO DETECT COLLISION AGAINST 
}
