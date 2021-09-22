//QuadRenderer.java
//Copyright Andrew Robinson

package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.renderer.*;
import org.joml.*;

public class QuadRendererComponent extends RendererComponent {
	public Vector3f[] bounds = new Vector3f[4];
	
	public Vector3f solidColor = new Vector3f(0, 0, 0);
	
	private OGLRenderer m_renderer = App.renderer;
	
	private float quad_vertex_buffer_data[] = new float[24];
	private int quad_vertex_indices_data[] = { 0, 1, 2, 1, 2, 3 };
	
	public void onCreate() {
		super.onCreate();
	}
	
	public void onUpdate() {
		//super.onUpdate(); // Nothing in superclass onUpdate(), commenting out for now
		
		quad_vertex_buffer_data[0] = bounds[0].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[1] = bounds[0].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[2] = bounds[0].get(2) + m_object.transform.getGlobalCoords().get(2);

		quad_vertex_buffer_data[3] = solidColor.get(0);
		quad_vertex_buffer_data[4] = solidColor.get(1);
		quad_vertex_buffer_data[5] = solidColor.get(2);

		quad_vertex_buffer_data[6] = bounds[1].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[7] = bounds[1].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[8] = bounds[1].get(2) + m_object.transform.getGlobalCoords().get(2);

		quad_vertex_buffer_data[9] = solidColor.get(0);
		quad_vertex_buffer_data[10] = solidColor.get(1);
		quad_vertex_buffer_data[11] = solidColor.get(2);
		
		quad_vertex_buffer_data[12] = bounds[2].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[13] = bounds[2].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[14] = bounds[2].get(2) + m_object.transform.getGlobalCoords().get(2);

		quad_vertex_buffer_data[15] = solidColor.get(0);
		quad_vertex_buffer_data[16] = solidColor.get(1);
		quad_vertex_buffer_data[17] = solidColor.get(2);

		quad_vertex_buffer_data[18] = bounds[3].get(0) + m_object.transform.getGlobalCoords().get(0);
		quad_vertex_buffer_data[19] = bounds[3].get(1) + m_object.transform.getGlobalCoords().get(1);
		quad_vertex_buffer_data[20] = bounds[3].get(2) + m_object.transform.getGlobalCoords().get(2);

		quad_vertex_buffer_data[21] = solidColor.get(0);
		quad_vertex_buffer_data[22] = solidColor.get(1);
		quad_vertex_buffer_data[23] = solidColor.get(2);
		
		m_renderer.createMesh(quad_vertex_buffer_data, quad_vertex_indices_data);
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
