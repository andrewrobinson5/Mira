//NAME: GameObjectComponent.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Component superclass - empty component object that lays groundwork for common component functions

package com.PlatformerGame.engine.core;

public class GameObjectComponent {
	private String m_name;
	
	protected GameObject m_object;
	
	public boolean enabled;
	public boolean hasRunOnce;
	
	public void onCreate() {
		hasRunOnce = true;
	}
	
	public void onUpdate() {
		//empty, but necessary so the method exists on all components
	}
	
	protected void setGameObject(GameObject l_object) {
		m_object = l_object;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setName(String l_name) {
		m_name = l_name;
	}
	
	public void enableComponent() {
		enabled = true;
	}
	
	public void disableComponent() {
		enabled = false;
	}
	
	public GameObjectComponent(String l_name) {
		setName(l_name);
		hasRunOnce = false;
		enableComponent();
	}
}
