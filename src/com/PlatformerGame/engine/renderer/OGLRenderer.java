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
	public int shaderSolidColor;
	public int shaderTexture;

	// create array of VAOs, probably won't need this later
	private ArrayList<Integer> listVAOs = new ArrayList<Integer>();
	private HashMap<Integer, ArrayList<Integer>> VAOParams = new HashMap<Integer, ArrayList<Integer>>();
	
	// also creates array of VAOs, but only the ones that should be rendered on current frame.
	public ArrayList<Integer> renderQueue = new ArrayList<Integer>();
	
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
	
	public void createTexturedMesh(float[] vertBuf, int[] indexBuf, Texture tex) {
		// Create mesh with vertBuf vertices, indexBuf indices, and render a texture to it.
		listVBOs.add(glCreateBuffers());
		listIndices.add(glCreateBuffers());
		listVAOs.add(glCreateVertexArrays());

		// Binds the most recently created VAO.
		glBindVertexArray(listVAOs.get(listVAOs.size()-1));
		
		
		
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
		
		ArrayList<Integer> tmpParams = new ArrayList<Integer>();
		// Add shader to use to arraylist
		tmpParams.add(shaderTexture);
		// Add texture to arraylist
		tmpParams.add(tex.myLocation);
		VAOParams.put(listVAOs.get(listVAOs.size()-1), tmpParams);
		
		addToRenderQueue(listVAOs.get(listVAOs.size()-1));
	}
	
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
	
	public void draw(int l_vao) {
		glUseProgram(shaderTexture);
		glBindVertexArray(l_vao);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	public void draw(int l_vao, int l_shader) {
		glUseProgram(l_shader);
		glBindVertexArray(l_vao);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	public void updateRender(Window gameWindow) {
		glClear(GL_COLOR_BUFFER_BIT);
		glClear(GL_DEPTH_BUFFER_BIT);
		for (int i = 0; i!=renderQueue.size(); i++) {
			// If texture is not empty, bind texture
			if(VAOParams.get(renderQueue.get(i)).get(1) != -1) {
				glBindTexture(GL_TEXTURE_2D, VAOParams.get(renderQueue.get(i)).get(1));
			}
			draw(renderQueue.get(i), VAOParams.get(renderQueue.get(i)).get(0));
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
		// Setup openGL default behaviors. I probably won't want to change these but I might.
		//   If I do, I'll create a function restoreDefaults() and put these there instead.
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.3f, 0.2f, 0.5f, 0.0f);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		// create default shaders
		shaderTexture = loadShaders("/shaders/shader_vert.glsl", "/shaders/shader_frag.glsl");
		shaderSolidColor = loadShaders("/shaders/shader_vert.glsl", "/shaders/shader_frag_solid.glsl");
	}
}
