//NAME: Window.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Create OpenGL capable window using GLFW and create GL Capabilities with it.

package com.PlatformerGame.engine.renderer;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

//import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	public long window;
	
	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW"); //glfw needs to be initialized in order to use any of its methods
	}
	
	public Window(int width, int height, String title, boolean isFullScreen) {
		init();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); //window should be visible
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); //window should not be resizable
		glfwWindowHint(GLFW_SAMPLES, 8); //number of fragments per pixel. I barely notice a difference, and for proper antialiasing I should probably just implement that in a shader.
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); //use opengl 3.3, modern opengl but also compatible with older hardware
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		if (!isFullScreen) {
			window = glfwCreateWindow(width, height, title, NULL, NULL); //make the window with dimensions width*height and with title 'title', and null on the monitor and device so it uses the defaults
			glfwSetWindowPos(window, 50, 50);
		} else {
			window = glfwCreateWindow(width,
		        height, title, glfwGetPrimaryMonitor(), NULL); //same as above but in fullscreen
		}
		
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		glfwMakeContextCurrent(window); //sets the window we just made to the current context so we can bind it to openGL
		//glfwSwapInterval(1);

		GL.createCapabilities(); //sets up openGL to use on our window. After this line, we can use opengl and everything we do will show up in window
	}
	
	public void destroyWindow() {
		//clean up
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
