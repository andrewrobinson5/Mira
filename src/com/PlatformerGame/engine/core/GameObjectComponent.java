package com.PlatformerGame.engine.core;

public class GameObjectComponent {
	private String m_name;
	
	protected GameObject m_object;
	
	public boolean enabled;
	private boolean hasRunOnce;
	
	public int onCreate() {
		if (!hasRunOnce) {
			
			hasRunOnce = true;
			return 1;
		}
		return 0;
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
