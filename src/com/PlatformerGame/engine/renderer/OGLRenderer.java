package com.PlatformerGame.engine.renderer;

//import com.PlatformerGame.engine.core.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.*;

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
	private ArrayList<Integer> renderQueue = new ArrayList<Integer>();
	
	// these just aren't needed as far as I can tell
	//private ArrayList<Integer> listVBOs = new ArrayList<Integer>();
	//private ArrayList<Integer> listIndices = new ArrayList<Integer>();
	
	public int loadShaders(String vertex_file_path, String fragment_file_path) {
		System.out.println("loadShaders() begin");
		// Create the shaders
		int VertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		int FragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

		// Read the Vertex Shader code from the file
		String VertexShaderCode = new BufferedReader(
			      new InputStreamReader(getClass().getResourceAsStream(vertex_file_path), StandardCharsets.UTF_8))
			        .lines().collect(Collectors.joining("\n"));

		// Read the Fragment Shader code from the file
		String FragmentShaderCode = new BufferedReader(
			      new InputStreamReader(getClass().getResourceAsStream(fragment_file_path), StandardCharsets.UTF_8))
			        .lines().collect(Collectors.joining("\n"));

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
		int ProgramID = glCreateProgram();
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
		
		System.out.println("loadShaders() done.");
		return ProgramID;
	}
	
	public void addToRenderQueue(int l_vao) {
		renderQueue.add(l_vao);
	}
	
	public int createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f color) {
		int indexBuffer = glCreateBuffers();
		int vertexBuffer = glCreateBuffers();
		
		// Colors hard-coded for now. Will probably put color/texture data
		// in separate buffer object or something.
		float vertex_buffer_data[] = {
				v1.get(0), v1.get(1), v1.get(2), color.get(0), color.get(1), color.get(2),
				v2.get(0), v2.get(1), v2.get(2), color.get(0), color.get(1), color.get(2),
				v3.get(0), v3.get(1), v3.get(2), color.get(0), color.get(1), color.get(2),
				v4.get(0), v4.get(1), v4.get(2), color.get(0), color.get(1), color.get(2),
		};
		
		int vertex_indices_data[] = { 0, 1, 2, 1, 2, 3 };

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

		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, vertex_buffer_data, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, vertex_indices_data, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3,	GL_FLOAT, false, 6 * 4,	NULL);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4,	NULL + (3*4));
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		return listVAOs.get(listVAOs.size()-1);
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
		//Temporary drawing function; not final functionality
		for (int i = 0; i!=renderQueue.size(); i++) {
			draw(renderQueue.get(i));
		}
		
		glfwSwapBuffers(gameWindow.window);
		renderQueue.clear();
	}
	
	public OGLRenderer() {
		// Setup Clear Color. Will probably change to white or something later.
		glClearColor(0.3f, 0.2f, 0.5f, 0.0f);
		// create default shaders
		programID = loadShaders("/shaders/shader_vert.glsl", "/shaders/shader_frag.glsl");
	}
}
