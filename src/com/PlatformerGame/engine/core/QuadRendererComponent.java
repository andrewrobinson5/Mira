//QuadRenderer.java
//Copyright Andrew Robinson
//	

package com.PlatformerGame.engine.core;

import com.PlatformerGame.engine.renderer.*;
import org.joml.*;

public class QuadRendererComponent extends RendererComponent {
	public Vector3f[] bounds = new Vector3f[4];
	public Vector3f solidColor = new Vector3f(0, 0, 0);
	
	private OGLRenderer m_renderer = App.renderer;
	private final Vector3f[] defaultBounds = new Vector3f[4];
	private int myVAO;
	
	private void setBounds(Vector3f one, Vector3f two, Vector3f three, Vector3f four) {
		bounds[0] = (Vector3f)one;
		bounds[1] = (Vector3f)two;
		bounds[2] = (Vector3f)three;
		bounds[3] = (Vector3f)four;
	}	
	
	private void setBoundsRelativeToTransform(TransformComponent l_transform, Vector3f one, Vector3f two, Vector3f three, Vector3f four) {
		one.add(l_transform.getCoords(), bounds[0]);
		two.add(l_transform.getCoords(), bounds[1]);
		three.add(l_transform.getCoords(), bounds[2]);
		four.add(l_transform.getCoords(), bounds[3]);
	}
	
	//TODO: Rendering functionality
	public void onCreate() {
		super.onCreate();
	}
	
	public void onUpdate() {
		
		//super.onUpdate(); // Nothing in superclass onUpdate(), commenting out for now

		// My memory leak is here. The big one.
		// OKAY so after profiling, there are two major leaks here. The first is that there are a whole lot of Vector3f objects getting created but not destroyed until garbage collected, which takes longer than we want. By the time we GC, it eats up several
		//		megabytes of ram. So we need a way to not recreate so many Vector3fs every update.
		// To be honest, I'm not even sure that Vector3f is leaking. It just went up at an alarming rate at launch.
		
		// The second major leak is the one that really messes up the code. Somewhere in here, we're creating like millions of integers, and these don't get destroyed by the garbage collector. I don't know exactly which likes are the culprit. I wonder if
		//		it's something JOML is doing behind the scenes with the Vector3f creations.
		
		setBounds(new Vector3f(defaultBounds[0]),
				  new Vector3f(defaultBounds[1]),
				  new Vector3f(defaultBounds[2]),
				  new Vector3f(defaultBounds[3]));
		
		// Integer leak is not caused by JOML.
		
		// It is also not caused by this line:
		setBoundsRelativeToTransform(m_object.transform, bounds[0], bounds[1], bounds[2], bounds[3]);
		
		// THIS IS THE PROBLEM LINE IN THE UPDATE. TIME TO TRACE IT TO createQuad();
		myVAO = m_renderer.createQuad(bounds[0], bounds[1], bounds[2], bounds[3], solidColor);
		
		m_renderer.addToRenderQueue(myVAO);
	}	
	
	// This interface is so much nicer to work with!
	public QuadRendererComponent(float width, float height) {
		super("QuadRenderer");
		
		// the following line will probably be moved to renderercomponent.java
		
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
