//QuadRenderer.java
//Copyright Andrew Robinson
//	

package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.renderer.*;
import org.joml.*;

// THIS CLASS NEEDS A BIG REDESIGN

public class QuadRendererComponent extends RendererComponent {
	public Vector3f[] bounds = new Vector3f[4];
	public Vector3f solidColor = new Vector3f(0, 0, 0);
	
	private OGLRenderer m_renderer = App.renderer;
	private Vector3f[] defaultBounds = new Vector3f[4];
	
	private void setBounds(Vector3f one, Vector3f two, Vector3f three, Vector3f four) {
		bounds[0] = new Vector3f(one);
		bounds[1] = new Vector3f(two);
		bounds[2] = new Vector3f(three);
		bounds[3] = new Vector3f(four);
	}	
	
	private void setBoundsRelativeToTransform(TransformComponent l_transform, Vector3f one, Vector3f two, Vector3f three, Vector3f four) {
		one.add(l_transform.getCoords(), bounds[0]);
		two.add(l_transform.getCoords(), bounds[1]);
		three.add(l_transform.getCoords(), bounds[2]);
		four.add(l_transform.getCoords(), bounds[3]);
	}
	
	public void onCreate() {
		super.onCreate();
	}
	

	float quad_vertex_buffer_data[] = new float[24];
	
	// What the fuck
	public void onUpdate() {
		
		//super.onUpdate(); // Nothing in superclass onUpdate(), commenting out for now

		// My memory leak is here. The big one.
		// OKAY so after profiling, there are two major leaks here. The first is that there are a whole lot of Vector3f objects getting created but not destroyed until garbage collected, which takes longer than we want. By the time we GC, it eats up several
		//		megabytes of ram. So we need a way to not recreate so many Vector3fs every update.
		// To be honest, I'm not even sure that Vector3f is leaking. It just went up at an alarming rate at launch.
		
		// UPDATE: Vector3fs are leaking, I think it's the main issue.
		
		// The second major leak is the one that really messes up the code. Somewhere in here, we're creating like millions of integers, and these don't get destroyed by the garbage collector. I don't know exactly which likes are the culprit. I wonder if
		//		it's something JOML is doing behind the scenes with the Vector3f creations.

		setBounds(defaultBounds[0], defaultBounds[1], defaultBounds[2], defaultBounds[3]); 
		
		setBoundsRelativeToTransform(m_object.transform, bounds[0], bounds[1], bounds[2], bounds[3]);
		
		// THIS IS THE PROBLEM LINE IN THE UPDATE. TIME TO TRACE IT TO createQuad();
		// This line eats up all of my system ram, but doesn't show up in the profiler as doing so.
		quad_vertex_buffer_data[0] = bounds[0].get(0);
		quad_vertex_buffer_data[1] = bounds[0].get(1);
		quad_vertex_buffer_data[2] = bounds[0].get(2);

		quad_vertex_buffer_data[3] = solidColor.get(0);
		quad_vertex_buffer_data[4] = solidColor.get(1);
		quad_vertex_buffer_data[5] = solidColor.get(2);

		quad_vertex_buffer_data[6] = bounds[1].get(0);
		quad_vertex_buffer_data[7] = bounds[1].get(1);
		quad_vertex_buffer_data[8] = bounds[1].get(2);

		quad_vertex_buffer_data[9] = solidColor.get(0);
		quad_vertex_buffer_data[10] = solidColor.get(1);
		quad_vertex_buffer_data[11] = solidColor.get(2);
		
		quad_vertex_buffer_data[12] = bounds[2].get(0);
		quad_vertex_buffer_data[13] = bounds[2].get(1);
		quad_vertex_buffer_data[14] = bounds[2].get(2);

		quad_vertex_buffer_data[15] = solidColor.get(0);
		quad_vertex_buffer_data[16] = solidColor.get(1);
		quad_vertex_buffer_data[17] = solidColor.get(2);

		quad_vertex_buffer_data[18] = bounds[3].get(0);
		quad_vertex_buffer_data[19] = bounds[3].get(1);
		quad_vertex_buffer_data[20] = bounds[3].get(2);

		quad_vertex_buffer_data[21] = solidColor.get(0);
		quad_vertex_buffer_data[22] = solidColor.get(1);
		quad_vertex_buffer_data[23] = solidColor.get(2);
		
		m_renderer.createQuad(quad_vertex_buffer_data);
	}	
	
	// This interface is so much nicer to work with!
	public QuadRendererComponent(float width, float height) {
		super("QuadRenderer");
		
		float leftX = 0-(width/2);
		float rightX = width/2;
		float topY = height/2;
		float bottomY = 0-(height/2);
		
		//top left
		defaultBounds[0] = new Vector3f(leftX, topY, 0);
		//top right
		defaultBounds[1] = new Vector3f(rightX, topY, 0);
		//bottom left
		defaultBounds[2] = new Vector3f(leftX, bottomY, 0);
		//bottom right
		defaultBounds[3] = new Vector3f(rightX, bottomY, 0);
		
		setBounds(new Vector3f(defaultBounds[0]), new Vector3f(defaultBounds[1]), new Vector3f(defaultBounds[2]), new Vector3f(defaultBounds[3]));
	}
	
	//leaving in here for non-rectangular quads, but don't like the format at all.
	public QuadRendererComponent(Vector3f l_one, Vector3f l_two, Vector3f l_three, Vector3f l_four) {
		super("QuadRenderer");
		
		defaultBounds[0] = new Vector3f(l_one);
		defaultBounds[1] = new Vector3f(l_two);
		defaultBounds[2] = new Vector3f(l_three);
		defaultBounds[3] = new Vector3f(l_four);
		
		setBounds(new Vector3f(l_one), new Vector3f(l_two), new Vector3f(l_three), new Vector3f(l_four));
	}
}
