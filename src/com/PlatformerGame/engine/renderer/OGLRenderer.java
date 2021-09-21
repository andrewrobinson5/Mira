package com.PlatformerGame.engine.renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class OGLRenderer {
	private int programID;

	// create array of VAOs, probably won't need this later
	private ArrayList<Integer> listVAOs = new ArrayList<Integer>();
	
	// also creates array of VAOs, but only the ones that should be rendered on current frame.
	public ArrayList<Integer> renderQueue = new ArrayList<Integer>();
	
	// these just aren't needed as far as I can tell
	// Correction: I need these now. I need to keep track of these so I can delete them after rendering.
	private ArrayList<Integer> listVBOs = new ArrayList<Integer>();
	private ArrayList<Integer> listIndices = new ArrayList<Integer>();
	
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
	
	public void addToRenderQueue(int l_vao) {
		renderQueue.add(l_vao);
	}
	
	public void createMesh(float[] vertBuf, int[] indexBuf) {
		// I think the issue may be that I'm creating a new indexBuffer and vertexBuffer for each object every frame. I should find a way to check and see if there's already an index and vertex buffer for a single object.
		// UPDATE: It was in fact the issue.
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

		glVertexAttribPointer(0, 3,	GL_FLOAT, false, 6 * 4,	NULL);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4,	NULL + (3*4));
		glEnableVertexAttribArray(0);
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
