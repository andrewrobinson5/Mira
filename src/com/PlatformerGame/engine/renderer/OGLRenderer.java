package com.PlatformerGame.engine.renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

public class OGLRenderer {
	private int programID;

	// create array of VAOs, probably won't need this later
	private ArrayList<Integer> listVAOs = new ArrayList<Integer>();
	// also creates array of VAOs, but only the ones that should be rendered on current frame.
	public ArrayList<Integer> renderQueue = new ArrayList<Integer>();
	
	private ArrayList<Integer> listVBOs = new ArrayList<Integer>();
	private ArrayList<Integer> listIndices = new ArrayList<Integer>();
	
	// This will hold all of the texture data before passing it to opengl
	private HashMap<String, Integer> textureMap = new HashMap<String, Integer>();
	
	public int loadShaders(String vertex_file_path, String fragment_file_path) {
		System.out.println("loadShaders() begin");
		// Create the shaders
		int VertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		int FragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

		int ProgramID = glCreateProgram();

		// Read the Vertex Shader code from the file
		try {
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
			glShaderSource(VertexShaderID, VertexShaderCode);
			glCompileShader(VertexShaderID);
			
			// Check Vertex Shader
			InfoLogLength = glGetShaderi(VertexShaderID, GL_INFO_LOG_LENGTH);
			
			if ( InfoLogLength > 0 ){
				System.out.println(glGetShaderInfoLog(VertexShaderID));
			}
	
			// Compile Fragment Shader
			System.out.println("Compiling shader: " + fragment_file_path);
			glShaderSource(FragmentShaderID, FragmentShaderCode);
			glCompileShader(FragmentShaderID);
	
			// Check Fragment Shader
			InfoLogLength = glGetShaderi(FragmentShaderID, GL_INFO_LOG_LENGTH);
			
			if ( InfoLogLength > 0 ){
				System.out.println(glGetShaderInfoLog(FragmentShaderID));
			}
	
			// Link the program
			System.out.println("Attaching Shaders");
			glAttachShader(ProgramID, VertexShaderID);
			glAttachShader(ProgramID, FragmentShaderID);
			System.out.println("Linking program");
			glLinkProgram(ProgramID);
	
			// Check the program
			InfoLogLength = glGetProgrami(ProgramID, GL_INFO_LOG_LENGTH);
			
			if ( InfoLogLength > 0 ){
				System.out.println(glGetProgramInfoLog(ProgramID));
			}
			
			glDetachShader(ProgramID, VertexShaderID);
			glDetachShader(ProgramID, FragmentShaderID);
			
			glDeleteShader(VertexShaderID);
			glDeleteShader(FragmentShaderID);
			
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
	
//	public int loadTexture(String texture) {
//		// We need to check if the texture is in the textureMap already, and if it is we won't need to reload it
//		if(textureMap.containsKey(texture)) {
//			return textureMap.get(texture);
//		}
//		// but if it isn't, then we'll load the texture with STBImage, store the string in textureMap, and then return the integer associated with the texture.
//		return 0;
//	}
	
	public void addToRenderQueue(int l_vao) {
		renderQueue.add(l_vao);
	}
	
	public void createTexturedMesh(float[] vertBuf, int[] indexBuf, int texLocation) {
		// Create mesh with vertBuf vertices, indexBuf indices, and render a texture to it.
		listVBOs.add(glCreateBuffers());
		listIndices.add(glCreateBuffers());
		listVAOs.add(glCreateVertexArrays());

		// Binds the most recently created VAO.
		glBindVertexArray(listVAOs.get(listVAOs.size()-1));
		
		// THEN DOWN HERE, We'll use texLocation, which is the integer associated with our texture in textureMap.
		// We'll then grab the texture and send it off to openGl.
		// TODO: That^
		
		
		
		
		
		
		// In the meanwhile, I'd like to figure out how to make a String texture by hand. This will come in handy when we move the interface of QuadRendererComponent entirely to this function, because we can just gen
		//		the texture on our own on the CPU really quickly based on the SolidColor variable or a material or something.
		//		Somewhere down the line, I'd like to have materials stored in a file before runtime and that file referenced by the renderer.

		glBindBuffer(GL_ARRAY_BUFFER, listVBOs.get(listVBOs.size()-1));
		glBufferData(GL_ARRAY_BUFFER, vertBuf, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, listIndices.get(listIndices.size()-1));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuf, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 3,	GL_FLOAT, false, 9*Float.BYTES, NULL);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 9*Float.BYTES, NULL + (3*Float.BYTES));
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 9*Float.BYTES, NULL + (7*Float.BYTES));
		glEnableVertexAttribArray(2);
		
		addToRenderQueue(listVAOs.get(listVAOs.size()-1));
	}
	
	public void createMesh(float[] vertBuf, int[] indexBuf) {
		listVBOs.add(glCreateBuffers());
		listIndices.add(glCreateBuffers());
		
		// Syntactically, glCreateVertexArrays is the same as glGenVertexArrays.
		// The difference between the two methods is that glGenVertexArrays() only
		//  returns an int ID of the vertex array and marks it as used, but it
		//  is not initialized until bound. glCreateVertexArrays() returns an int
		//  pointing to a vertex array that's initialized at creation, (and whose state can be accessed directly?).
		
		// This is how you would create multiple Vertex Array Objects at once.
		//int VertexArrays[] = new int[1];
		//glCreateVertexArrays(VertexArrays);
		//glBindVertexArray(VertexArrays[0]);
		
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
		
		addToRenderQueue(listVAOs.get(listVAOs.size()-1));
	}
	
	public void draw(int l_vao) {
		glUseProgram(programID);
		glBindVertexArray(l_vao);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	public void draw(int l_vao, int l_shader) {
		glUseProgram(l_shader);
		glBindVertexArray(l_vao);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	// DONE: add a proper renderer update and render queue
	// THIS SHOULD PROBABLY CLEAR VAOs AND RENDER QUEUE AFTER EVERY FRAME
	// AND ONLY RENDER WHAT IS CURRENTLY IN THE RENDER QUEUE AT THE TIME OF EXECUTION
	public void updateRender(Window gameWindow) {
		glClear(GL_COLOR_BUFFER_BIT);
		for (int i = 0; i!=renderQueue.size(); i++) {
			draw(renderQueue.get(i));
		}
		
		glfwSwapBuffers(gameWindow.window);
		renderQueue.clear();
		
		// This fixes most of the memory leak. Thank God.
		for (int i = 0; i < listVAOs.size(); i++) {
			glDeleteVertexArrays(listVAOs.get(i));
		}
		listVAOs.clear();
		
		for (int i = 0; i < listVBOs.size(); i++) {
			glDeleteBuffers(listVBOs.get(i));
		}
		listVBOs.clear();
		
		for (int i = 0; i < listIndices.size(); i++) {
			glDeleteBuffers(listIndices.get(i));
		}
		listIndices.clear();
	}
	
	public OGLRenderer() {
		// Setup Clear Color. Will probably change to white or something later.
		glClearColor(0.3f, 0.2f, 0.5f, 0.0f);
		// create default shaders
		programID = loadShaders("/shaders/shader_vert.glsl", "/shaders/shader_frag.glsl");
	}
}
