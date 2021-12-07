//NAME: OGLRenderer.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Mira's OpenGL abstraction layer.
//			Provides a RenderQueue, which holds everything in a frame that will be drawn.
//			Provides functions to add meshes to the RenderQueue.
//			Provides shader loading functionality.
//			Sets up OpenGL defaults.
//			Abstracts any OpenGL function the engine needs to keep rendering code internal.
//				(this helps expand later; if I added a software or vulkan renderer, I could
//				call on VKRenderer and not OGLRenderer and it would have the same feature set.)


package com.PlatformerGame.engine.renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.PlatformerGame.engine.core.Texture;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

public class OGLRenderer {
	//need to declare the shader program ints up here so I can use them from anywhere
	public int shaderSolidColor;
	public int shaderTexture;

	private ArrayList<Integer> listVAOs = new ArrayList<Integer>(); //arraylist to store Vertex Array Objects
	private HashMap<Integer, ArrayList<Integer>> VAOParams = new HashMap<Integer, ArrayList<Integer>>(); //maps parameters of a VAO to the VAO. This lets me use specific shader programs
	
	public ArrayList<Integer> renderQueue = new ArrayList<Integer>(); //also creates array of VAOs, but only the ones that should be rendered on current frame.
	
	//the following are lists of buffer objects. These store vertex data (vertex positions, colors, texture coordinates)
	private ArrayList<Integer> listVBOs = new ArrayList<Integer>();
	private ArrayList<Integer> listIndices = new ArrayList<Integer>(); //this one stores the order of vertices so I can make shapes with the vertex data
	
	//adapted to java from public domain C++ shader loader
	//this loads a shader program into GPU memory and passes an integer unique identifier back to my program to access it.
	public int loadShaders(String vertex_file_path, String fragment_file_path) {
		System.out.println("loadShaders() begin");
		// Create the shaders -- a shader program consists at minimum of a vertex shader. A fragment shader is required to process pixels as well.
		int VertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		int FragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

		int ProgramID = glCreateProgram(); //ProgramID becomes a reference to the shader program that lives on the GPU

		try {
			// Read the Vertex Shader code from the resource file
			InputStreamReader vsisr = new InputStreamReader(getClass().getResourceAsStream(vertex_file_path), StandardCharsets.UTF_8);
			BufferedReader vsbr = new BufferedReader(vsisr);
			String VertexShaderCode = vsbr.lines().collect(Collectors.joining("\n"));
	
			// Read the Fragment Shader code from the file
			InputStreamReader fsisr = new InputStreamReader(getClass().getResourceAsStream(fragment_file_path), StandardCharsets.UTF_8);
			BufferedReader fsbr = new BufferedReader(fsisr);
			String FragmentShaderCode = fsbr.lines().collect(Collectors.joining("\n"));
	
			int InfoLogLength;
	
			// Compile Vertex Shader
			System.out.println("Compiling shader: " + vertex_file_path);
			glShaderSource(VertexShaderID, VertexShaderCode); //move the contents of the shader source file onto the GPU
			glCompileShader(VertexShaderID);
			
			// Check Vertex Shader
			InfoLogLength = glGetShaderi(VertexShaderID, GL_INFO_LOG_LENGTH);
			if ( InfoLogLength > 0 ){
				System.out.println(glGetShaderInfoLog(VertexShaderID));
			}
	
			// Compile Fragment Shader
			System.out.println("Compiling shader: " + fragment_file_path);
			glShaderSource(FragmentShaderID, FragmentShaderCode); //move the contents of the shader source file onto the GPU
			glCompileShader(FragmentShaderID);
	
			// Check Fragment Shader
			InfoLogLength = glGetShaderi(FragmentShaderID, GL_INFO_LOG_LENGTH);
			if ( InfoLogLength > 0 ){
				System.out.println(glGetShaderInfoLog(FragmentShaderID));
			}
	
			// Link the program
			System.out.println("Attaching Shaders");
			glAttachShader(ProgramID, VertexShaderID); //tells opengl we want to store this shader
			glAttachShader(ProgramID, FragmentShaderID); //and this shader
			System.out.println("Linking program");
			glLinkProgram(ProgramID); //and use them here.
	
			// Check the program
			InfoLogLength = glGetProgrami(ProgramID, GL_INFO_LOG_LENGTH);
			if ( InfoLogLength > 0 ){
				System.out.println(glGetProgramInfoLog(ProgramID));
			}
			
			//and now that the shaders are compiled and the program is linked, we can get rid of the shader sources and free some GPU memory
			glDetachShader(ProgramID, VertexShaderID);
			glDetachShader(ProgramID, FragmentShaderID);
			glDeleteShader(VertexShaderID);
			glDeleteShader(FragmentShaderID);

			//and then close all the InputStreamReaders I used to read the shader resources
			vsisr.close();
			fsisr.close();
			vsbr.close();
			fsbr.close();
		} catch (IOException e) {
			System.out.println("WARNING: Shader InputStreamReader & BufferedReaders never closed!");
		}
		
		System.out.println("loadShaders() done.");
		return ProgramID;
	}
	
	public void addToRenderQueue(int l_vao) {
		renderQueue.add(l_vao); //exposes an interface to set an object to be drawn on the next frame 
	}
	
	// Create mesh with vertBuf vertices, indexBuf indices, and render a texture to it.
	public void createTexturedMesh(float[] vertBuf, int[] indexBuf, Texture tex) {
		listVBOs.add(glCreateBuffers());
		listIndices.add(glCreateBuffers());
		listVAOs.add(glCreateVertexArrays());

		//I think I could probably speed this up a negligible amount by keeping track of those buffers in a variable
		// Binds the most recently created VAO. (Binding tells openGL that we want to use this object until it is unbound or a newer one replaces it)
		glBindVertexArray(listVAOs.get(listVAOs.size()-1));
		
		glBindBuffer(GL_ARRAY_BUFFER, listVBOs.get(listVBOs.size()-1)); // Binds the most recently created VBO
		glBufferData(GL_ARRAY_BUFFER, vertBuf, GL_STATIC_DRAW); //Puts data onto the VBO
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, listIndices.get(listIndices.size()-1));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuf, GL_STATIC_DRAW); //Puts data onto the VBO
		
		//these next six lines depend on the layout of the float array.
		//I laid it out as:
		//	- 3 floats denote position (X, Y, Z) of one vertex
		//	- 4 floats denote color (Red, Green, Blue, Alpha) of that vertex
		//	- 2 floats denote texture coordinate (X, Y) of that vertex
		//and then it repeats for each vertex in the array.
		//This layout lets me pass all the data into the shader one vert at a time.
		glVertexAttribPointer(0, 3,	GL_FLOAT, false, 9*Float.BYTES, NULL); //so this tells opengl to pass the first 3 of every 9 floats to the shader with the attribute 0
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 9*Float.BYTES, NULL + (3*Float.BYTES)); //floats 4-7 of every 9 floats with attribute 1
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 9*Float.BYTES, NULL + (7*Float.BYTES)); //floats 8-9 of every 9 floats with attribute 2
		glEnableVertexAttribArray(2);
		
		ArrayList<Integer> tmpParams = new ArrayList<Integer>(); //array of temporary values. First in the array is the name of the shader, second is the location of the texture
		tmpParams.add(shaderTexture); // Add shader to use to arraylist
		tmpParams.add(tex.myLocation); // Add texture to arraylist
		VAOParams.put(listVAOs.get(listVAOs.size()-1), tmpParams); //and then store those in the map so we can access the params that belong to this vao
		//I don't think I need it to be an arraylist, since it's only two things but I might want there to be more parameters later on.
		
		addToRenderQueue(listVAOs.get(listVAOs.size()-1)); // and finally push the textured mesh onto the queue of items to be rendered
	}

	//same as createTexturedMesh but without the texture coordinates
	public void createMesh(float[] vertBuf, int[] indexBuf) {
		listVBOs.add(glCreateBuffers());
		listIndices.add(glCreateBuffers());
		
		listVAOs.add(glCreateVertexArrays());

		glBindVertexArray(listVAOs.get(listVAOs.size()-1));

		glBindBuffer(GL_ARRAY_BUFFER, listVBOs.get(listVBOs.size()-1));
		glBufferData(GL_ARRAY_BUFFER, vertBuf, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, listIndices.get(listIndices.size()-1));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuf, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3,	GL_FLOAT, false, 7*Float.BYTES,	NULL);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 7*Float.BYTES, NULL + (3*Float.BYTES));
		glEnableVertexAttribArray(1);
		
		ArrayList<Integer> tmpParams = new ArrayList<Integer>();
		// Add shader to use to arraylist
		tmpParams.add(shaderSolidColor);
		// Add texture to arraylist -- negative one means doesn't exist, since GL uses unsigned ints
		tmpParams.add(-1);
		VAOParams.put(listVAOs.get(listVAOs.size()-1), tmpParams);
		
		addToRenderQueue(listVAOs.get(listVAOs.size()-1));
	}
	
	//draws a VAO with default shader (textured)
	public void draw(int l_vao) {
		glUseProgram(shaderTexture); //tells opengl to use shaderTexture shader in subsequent draw calls
		glBindVertexArray(l_vao); //tells opengl to use the data in l_vao
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0); //draw call. This is the line that puts it on the screen.
	}
	
	//draws a VAO with a programmer specified shader
	public void draw(int l_vao, int l_shader) {
		glUseProgram(l_shader); //tells opengl to use l_shader in subsequent draw calls
		glBindVertexArray(l_vao); //tells opengl to use the data in l_vao
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0); //draw call. This is the line that puts it on the screen.
	}
	
	//this runs once every frame.
	public void updateRender(Window gameWindow) {
		glClear(GL_COLOR_BUFFER_BIT); //reset/clear the screen
		glClear(GL_DEPTH_BUFFER_BIT); //reset/clear the screen
		
		//iterate through all objects in the render queue
		for (int i = 0; i!=renderQueue.size(); i++) {
			// If texture is not empty, bind texture
			if(VAOParams.get(renderQueue.get(i)).get(1) != -1) {
				glBindTexture(GL_TEXTURE_2D, VAOParams.get(renderQueue.get(i)).get(1)); //bind the texture linked to the VAO
			} else {
				glBindTexture(GL_TEXTURE_2D, 0);
			}
			draw(renderQueue.get(i), VAOParams.get(renderQueue.get(i)).get(0)); //draw call
		}
		
		glfwSwapBuffers(gameWindow.window); //push the drawn objects to the screen
		renderQueue.clear(); //clear the render queue to do it all again next frame
		
		for (int i = 0; i < listVAOs.size(); i++) {
			glDeleteVertexArrays(listVAOs.get(i)); //delete VAOs to free memory
		}
		listVAOs.clear();
		
		for (int i = 0; i < listVBOs.size(); i++) {
			glDeleteBuffers(listVBOs.get(i)); //delete VAOs to free memory
		}
		listVBOs.clear();
		
		for (int i = 0; i < listIndices.size(); i++) {
			glDeleteBuffers(listIndices.get(i)); //delete VAOs to free memory
		}
		listIndices.clear();
	}
	
	public OGLRenderer() {
		// Setup openGL default behaviors. I probably won't want to change these but I might.
		//   If I do, I'll create a function restoreDefaults() and put these there instead.
		
		glEnable(GL_DEPTH_TEST); //Draw things in the right order
		
		//Enable transparency blending
//		glEnable(GL_BLEND);
//		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);  
		
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f); //sets the clear color to white
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //texture parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		// create default shaders
		shaderTexture = loadShaders("/shaders/shader_vert.glsl", "/shaders/shader_frag.glsl");
		shaderSolidColor = loadShaders("/shaders/shader_vert.glsl", "/shaders/shader_frag_solid.glsl");
	}
}
