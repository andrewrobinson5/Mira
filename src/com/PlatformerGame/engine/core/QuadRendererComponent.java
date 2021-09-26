//QuadRenderer.java
//Copyright Andrew Robinson

package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.renderer.*;
import org.joml.*;

public class QuadRendererComponent extends RendererComponent {
	public Vector3f[] bounds = new Vector3f[4];
	
	public Vector4f solidColor = new Vector4f(0, 0, 0, 0);
	public Texture tex = new Texture("/textures/pipe.png");
	
	private OGLRenderer m_renderer = App.renderer;
	
	private float quad_vertex_buffer_data[];
	private int quad_vertex_indices_data[] = { 0, 1, 2, 1, 2, 3 };
	
	public void onCreate() {
		super.onCreate();
	}
	
	public void onUpdate() {
		//super.onUpdate(); // Nothing in superclass onUpdate(), commenting out for now
		
		
		// We'll simplify this after I get texture loading working. We don't need to use solidColor or pass it at all if we're loading a texture onto it.
		// This is the point where I realize my code is all very, very ugly
		
		// Ooh didn't know I could do all this addition per bound on the gpu. We can just pass the transform in a matrix to a uniform and it'll take care of drawing for us. TODO or something.
		quad_vertex_buffer_data = new float[] {
				//vertex 1 location data
				bounds[0].get(0) + m_object.transform.getGlobalCoords().get(0),	bounds[0].get(1) + m_object.transform.getGlobalCoords().get(1), bounds[0].get(2) + m_object.transform.getGlobalCoords().get(2),
				//vertex 1 color data
				solidColor.get(0), solidColor.get(1), solidColor.get(2), solidColor.get(3),
				//vertex 1 texture coordinates
				-1f, 1f,
				
				//vertex 2 location data
				bounds[1].get(0) + m_object.transform.getGlobalCoords().get(0),	bounds[1].get(1) + m_object.transform.getGlobalCoords().get(1), bounds[1].get(2) + m_object.transform.getGlobalCoords().get(2),
				//vertex 2 color data
				solidColor.get(0), solidColor.get(1), solidColor.get(2), solidColor.get(3),
				//vertex 2 texture coordinates
				1f, 1f,
				
				//vertex 3 location data
				bounds[2].get(0) + m_object.transform.getGlobalCoords().get(0),	bounds[2].get(1) + m_object.transform.getGlobalCoords().get(1), bounds[2].get(2) + m_object.transform.getGlobalCoords().get(2),
				//vertex 3 color data
				solidColor.get(0), solidColor.get(1), solidColor.get(2), solidColor.get(3),
				//vertex 3 texture coordinates
				-1f, -1f,
				
				//vertex 4 location data
				bounds[3].get(0) + m_object.transform.getGlobalCoords().get(0),	bounds[3].get(1) + m_object.transform.getGlobalCoords().get(1), bounds[3].get(2) + m_object.transform.getGlobalCoords().get(2),
				//vertex 4 color data
				solidColor.get(0), solidColor.get(1), solidColor.get(2), solidColor.get(3),
				//vertex 4 texture coordinates
				1f, -1f,
		};
		
		m_renderer.createTexturedMesh(quad_vertex_buffer_data, quad_vertex_indices_data, tex);
	}	
	
	// This interface is so much nicer to work with!
	public QuadRendererComponent(float width, float height) {
		super("QuadRenderer");
		
		//center the local vertex coordinates based on the width and height
		float leftX = 0-(width/2);
		float rightX = width/2;
		float topY = height/2;
		float bottomY = 0-(height/2);
		
		
		bounds[0] = new Vector3f(leftX, topY, 0); //top left vert
		bounds[1] = new Vector3f(rightX, topY, 0); //top right vert
		bounds[2] = new Vector3f(leftX, bottomY, 0); //bottom left vert
		bounds[3] = new Vector3f(rightX, bottomY, 0); //bottom right vert
	}
	
	//leaving in here for non-rectangular quads, but don't like the format at all.
	public QuadRendererComponent(Vector3f l_one, Vector3f l_two, Vector3f l_three, Vector3f l_four) {
		super("QuadRenderer");
		
		bounds[0] = new Vector3f(l_one); //top left vert 
		bounds[1] = new Vector3f(l_two); //top right vert
		bounds[2] = new Vector3f(l_three); //bottom left vert
		bounds[3] = new Vector3f(l_four); //bottom right vert
	}
}
