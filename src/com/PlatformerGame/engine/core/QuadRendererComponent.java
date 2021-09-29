//QuadRenderer.java
//Copyright Andrew Robinson

package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.renderer.*;
import org.joml.*;

public class QuadRendererComponent extends RendererComponent {
	public Vector3f[] bounds = new Vector3f[4];
	
	public Vector4f solidColor = new Vector4f(0, 0, 0, 0);
	public Texture tex;
//	public Texture tex = new Texture("/textures/pipe.png");
	
	private OGLRenderer m_renderer = App.renderer;
	
	private float quad_vertex_buffer_data[] = new float[36];
	private int quad_vertex_indices_data[] = { 0, 1, 2, 1, 2, 3 };
	
	private void updatePositionTextured() {
		// Doing everything the hard way to avoid creating new floats every frame. Why create a lot when I can just reuse them?

		// Ooh didn't know I could do all this addition per bound on the gpu. We can just pass the transform in a matrix to a uniform and it'll take care of drawing for us. TODO or something.
		quad_vertex_buffer_data[0] = bounds[0].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[1] = bounds[0].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[2] = bounds[0].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[3] = solidColor.get(0);
		quad_vertex_buffer_data[4] = solidColor.get(1);
		quad_vertex_buffer_data[5] = solidColor.get(2);
		quad_vertex_buffer_data[6] = solidColor.get(3);
		quad_vertex_buffer_data[7] = -1f;
		quad_vertex_buffer_data[8] = 1f;
		
		quad_vertex_buffer_data[9] = bounds[1].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[10] = bounds[1].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[11] = bounds[1].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[12] = solidColor.get(0);
		quad_vertex_buffer_data[13] = solidColor.get(1);
		quad_vertex_buffer_data[14] = solidColor.get(2);
		quad_vertex_buffer_data[15] = solidColor.get(3);
		quad_vertex_buffer_data[16] = 1f;
		quad_vertex_buffer_data[17] = 1f;
		
		quad_vertex_buffer_data[18] = bounds[2].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[19] = bounds[2].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[20] = bounds[2].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[21] = solidColor.get(0);
		quad_vertex_buffer_data[22] = solidColor.get(1);
		quad_vertex_buffer_data[23] = solidColor.get(2);
		quad_vertex_buffer_data[24] = solidColor.get(3);
		quad_vertex_buffer_data[25] = -1f;
		quad_vertex_buffer_data[26] = -1f;
		
		quad_vertex_buffer_data[27] = bounds[3].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[28] = bounds[3].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[29] = bounds[3].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[30] = solidColor.get(0);
		quad_vertex_buffer_data[31] = solidColor.get(1);
		quad_vertex_buffer_data[32] = solidColor.get(2);
		quad_vertex_buffer_data[33] = solidColor.get(3);
		quad_vertex_buffer_data[34] = 1f;
		quad_vertex_buffer_data[35] = -1f;
	}
	
	private void updatePositionUntextured() {
		// Doing everything the hard way to avoid creating new floats every frame. Why create a lot when I can just reuse them?

		// Ooh didn't know I could do all this addition per bound on the gpu. We can just pass the transform in a matrix to a uniform and it'll take care of drawing for us. TODO or something.
		quad_vertex_buffer_data[0] = bounds[0].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[1] = bounds[0].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[2] = bounds[0].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[3] = solidColor.get(0);
		quad_vertex_buffer_data[4] = solidColor.get(1);
		quad_vertex_buffer_data[5] = solidColor.get(2);
		quad_vertex_buffer_data[6] = solidColor.get(3);
		
		quad_vertex_buffer_data[7] = bounds[1].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[8] = bounds[1].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[9] = bounds[1].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[10] = solidColor.get(0);
		quad_vertex_buffer_data[11] = solidColor.get(1);
		quad_vertex_buffer_data[12] = solidColor.get(2);
		quad_vertex_buffer_data[13] = solidColor.get(3);
		
		quad_vertex_buffer_data[14] = bounds[2].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[15] = bounds[2].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[16] = bounds[2].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[17] = solidColor.get(0);
		quad_vertex_buffer_data[18] = solidColor.get(1);
		quad_vertex_buffer_data[19] = solidColor.get(2);
		quad_vertex_buffer_data[20] = solidColor.get(3);
		
		quad_vertex_buffer_data[21] = bounds[3].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[22] = bounds[3].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[23] = bounds[3].get(2) + m_object.transform.getGlobalCoords().get(2);
		quad_vertex_buffer_data[24] = solidColor.get(0);
		quad_vertex_buffer_data[25] = solidColor.get(1);
		quad_vertex_buffer_data[26] = solidColor.get(2);
		quad_vertex_buffer_data[27] = solidColor.get(3);
	}
	
	public void onCreate() {
		super.onCreate();
	}
	
	public void onUpdate() {
		//super.onUpdate(); // Nothing in superclass onUpdate(), commenting out for now
		
		if(tex == null) {
			updatePositionUntextured();
			m_renderer.createMesh(quad_vertex_buffer_data, quad_vertex_indices_data);
		} else {
			updatePositionTextured();
			m_renderer.createTexturedMesh(quad_vertex_buffer_data, quad_vertex_indices_data, tex);
		}		
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
