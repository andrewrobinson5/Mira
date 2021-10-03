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
			throw new IllegalStateException("Unable to initialize GLFW");
	}
	
	public Window(int width, int height, String title, boolean isFullScreen) {
		init();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_SAMPLES, 2);
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		if (!isFullScreen) {
			window = glfwCreateWindow(width, height, title, NULL, NULL);
			glfwSetWindowPos(window, 50, 50);
		} else {
			window = glfwCreateWindow(width,
		        height, title, glfwGetPrimaryMonitor(), NULL);
		}
		
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		glfwMakeContextCurrent(window);
		//glfwSwapInterval(1);

		GL.createCapabilities();
	}
	
	public void destroyWindow() {
		//glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		//glfwSetErrorCallback(null).free();
	}
}
