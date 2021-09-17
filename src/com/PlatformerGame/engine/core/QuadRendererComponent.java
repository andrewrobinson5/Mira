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
		bounds[0] = one;
		bounds[1] = two;
		bounds[2] = three;
		bounds[3] = four;
	}	
	
	private void setBoundsRelativeToTransform(TransformComponent l_transform, Vector3f one, Vector3f two, Vector3f three, Vector3f four) {
		one.add(l_transform.getCoords(), bounds[0]);
		two.add(l_transform.getCoords(), bounds[1]);
		three.add(l_transform.getCoords(), bounds[2]);
		four.add(l_transform.getCoords(), bounds[3]);
	}
	
	//TODO: Rendering functionality
	public int onCreate() {
		// THIS IS CLUNKY, BUT FOR NOW ONCREATE() CONTENTS GO IN THIS IF
		if (super.onCreate() == 1) {
			
		}
		return 0;
	}
	
	public void onUpdate() {
		//super.onUpdate(); // Nothing in superclass onUpdate(), commenting out for now

		setBounds(new Vector3f(defaultBounds[0]),
				  new Vector3f(defaultBounds[1]),
				  new Vector3f(defaultBounds[2]),
				  new Vector3f(defaultBounds[3]));
		
		setBoundsRelativeToTransform(m_object.transform, bounds[0], bounds[1], bounds[2], bounds[3]);
		
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
