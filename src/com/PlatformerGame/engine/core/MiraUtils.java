package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.core.components.GameObjectComponent;

public class MiraUtils {
	
	//Returns false if 0 or negative, true if positive.
	//Avoid using unless you know the input is a 1 or 0.
	public static boolean intToBool(int i) {
		return i>0 ? true : false;
	}
	
	//Returns 1 if true, 0 if false
	//Again, avoid if the output needs to be anything other than 1 or 0
	public static int boolToInt(boolean i) {
		return i ? 1 : 0;
	}
	
	//Execute a component free from normal engine flow.
	//	Sometimes, a component will need to execute before its owner gameobject does. Use this in the GameObject's code.
	//	This won't be an issue when I finally move to a real script component and move logic out of the GameObject class.
	//		(this makes me think the whole structure is getting needlessly convoluted when I could probably have almost all
	//			logic in that class and not have components altogether. Oy vey.)
	public static <T extends GameObjectComponent> void miraPriorityExecuteComponentOnCreate(T component) {
		component.onCreate();
		component.hasRunOnce = true;
	}
}
