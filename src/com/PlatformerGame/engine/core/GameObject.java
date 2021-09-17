package com.PlatformerGame.engine.core;

import java.util.ArrayList;

public class GameObject {
	public ArrayList<GameObjectComponent> listComponents = new ArrayList<GameObjectComponent>();
	private GameObjectComponent emptyComponent;
	protected TransformComponent transform;
	private boolean hasRunOnce = false;
	
	public int onCreate() {
		if (!hasRunOnce) {
			
			hasRunOnce = true;
			return 1;
		}
		return 0;
	}
	public void onUpdate() {}
	
	public <T extends GameObjectComponent> int addComponent(T component) {
		boolean nameExists = false;
		for (GameObjectComponent comp : listComponents) {
			if (component.getName() == comp.getName()) {			
				nameExists = true;
			}
		}
		
		if (nameExists) {
			throw new RuntimeException("Error: cannot create more than one component with one name");
		} else {
			//don't fucking touch this next line.
			component.setGameObject(this);
			
			listComponents.add(component);
			
			return listComponents.size()-1;
		}
	}
	
	public int removeComponent(int comp) {
		if (comp <= 0 || comp >= listComponents.size()) {
			System.out.println("Attempted access of invalid component. Nothing removed.");
			return comp;
		} else if (listComponents.get(comp).getName() == "Transform") {
			System.out.println("Cannot remove component 'Transform'.");
			return comp;
		} else {
			listComponents.remove(comp);
			return -1;
		}
	}
		
	public <T extends GameObjectComponent> void removeComponent(T comp) {
		for (int i = 0; i < listComponents.size(); i++) {
			if (listComponents.get(i).equals(comp)) {
				listComponents.remove(i);
			} else {
				System.out.println("Attempted access of invalid component. Nothing removed.");
			}
		}
	}
	
	public void removeComponent(String comp) {
		for (int i = 0; i < listComponents.size(); i++) {
			if (listComponents.get(i).getName() == comp) {
				listComponents.remove(i);
			} else {
				System.out.println("Attempted access of invalid component. Nothing removed.");
			}
		}
	}
	
	// Ugly unchecked typecasting. Don't know if this is the only way to do it,
	//   but now I have access to any GameObjectComponent subclass's public data from game code,
	//   which is what I needed. If the Game class doesn't do something stupid this should be fine.
	@SuppressWarnings("unchecked")
	public <T extends GameObjectComponent> T getComponent(String comp) {
		for (int i = 0; i < listComponents.size(); i++) {
			if (listComponents.get(i).getName() == comp) {
				return (T)listComponents.get(i);
			}
		}
		System.out.println("No such component.");
		
		return (T)emptyComponent;
	}
	
	public GameObject(float x, float y, float z) {
		emptyComponent = new GameObjectComponent("");
		addComponent(emptyComponent);
		
		transform = new TransformComponent(x, y, z);
		addComponent(transform);
	}
	
	public GameObject() {
		// Adds an empty component for methods to work if the component being accessed does not exist
		emptyComponent = new GameObjectComponent("");
		addComponent(emptyComponent);

		transform = new TransformComponent();
		addComponent(transform);
	}
}
