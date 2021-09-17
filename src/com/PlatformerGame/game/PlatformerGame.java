//PlatformerGame.java
//Copyright Andrew Robinson 2021
//	I'm particularly proud of this project because I wrote it all on my own.
//  Probably less than 1% of this project uses borrowed code, and that would be the loadShaders() method in OGLRenderer.java.
//  Even then the code it references was open source C++ code from a book about openGL. I ported it to java myself, making
//  several changes to it.
//
//  The game is structured in a game-engine format, in which there is an engine that abstracts most of the heavy
//	lifting for things like rendering with openGL, audio(soon), and physics(soon). The engine exists independently to
//  game code, and the game code uses engine functions and classes to easily create simple game logic.
//
//  I did not have any prior knowledge of game engine programming going into this project, and I didn't look at the
//  internals of other engines, so I can guarantee that this isn't particularly well optimized or clean, and there's
//  probably several better ways to have executed all of this. I just wrote what made sense to me after my experience
//  using other engines.
//
//  Resources I used: The Official Guide to Learning OpenGL, Version 4.5 with SPIR-V by G Sellers, D Shreiner, J Kessenich

//					  http://www.opengl-tutorial.org/beginners-tutorials/tutorial-2-the-first-triangle/
//						~ loadShaders() source (licensed under WTFPL (check opengl-tutorial.com for that one haha))

//					  Light Weight Java Game Library 3 - JNI library that enables use of openGL, openAL, and ASSIMP in Java
//						and of course the javadoc website for LWJGL for function references

//					  Java OpenGL Math Library (JOML) - Math library for dealing with things like Vectors of floats,
//						matrix math (3d transformations and projections)
//						and of course the javadoc website for JOML for function and class references


//package com.PlatformerGame.game;
//
//public class PlatformerGame {
//
//	public static void main(String[] args) {
//		Game theGame = new Game();
//		
//		theGame.init();
//		theGame.loop();
//		theGame.finalize();
//	}
//
//}
