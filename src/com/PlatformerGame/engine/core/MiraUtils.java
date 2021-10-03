package com.PlatformerGame.engine.core;

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
}
