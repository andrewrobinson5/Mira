//NAME: GameObject.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: A bag of components that keeps track of its own parents and children.

package com.PlatformerGame.engine.core;

import java.util.ArrayList;

import com.PlatformerGame.engine.core.components.*;

public class GameObject {
	public ArrayList<GameObjectComponent> listComponents = new ArrayList<GameObjectComponent>();
	private GameObjectComponent emptyComponent;
	public TransformComponent transform;
	public boolean hasRunOnce = false;
	
	//Children-parent tracking stuff
	public ArrayList<GameObject> children = new ArrayList<GameObject>();
	public GameObject parent = null;
	public int hierLevel;
	
	private void incrementLvlAfterParenting() {
		if (parent == null)
			hierLevel = 0;
		else
			hierLevel = parent.hierLevel;
	
		hierLevel++;
		for(int childIndex = 0; childIndex < children.size(); childIndex++) {
			children.get(childIndex).incrementLvlAfterParenting();
		}
	}
	
	private void decrementLvlAfterUnparenting() {
		if (parent == null)
			hierLevel = 0;
		else
			hierLevel = parent.hierLevel;
		
		hierLevel--;
		for(int childIndex = 0; childIndex < children.size(); childIndex++) {
			children.get(childIndex).decrementLvlAfterUnparenting();
		}
	}
	
	//THESE NEXT TWO FUNCTIONS SHOULD BE MAKING SURE THE OBJECT IS IN A SCENE HIERARCHY BEFORE ADDING THEM AS CHILDREN
	public void makeChildOf(GameObject l_parent) {
		parent = l_parent;
		l_parent.children.add(this);
		incrementLvlAfterParenting();
	}
	
	public void addChild(GameObject l_child) {
		l_child.parent = this;
		children.add(l_child);
		l_child.incrementLvlAfterParenting();
	}
	
	public void removeChild(GameObject l_child) {
		l_child.parent = null;
		l_child.hierLevel = 0;
		children.remove(l_child);
		l_child.decrementLvlAfterUnparenting();
	}
	
	public void unParent() {
		parent.children.remove(this);
		parent = null;
		hierLevel = 0;
		decrementLvlAfterUnparenting();
		
	}
	//End of children-parent tracking stuff
	
	public void onCreate() {
		hasRunOnce = true;
	}
	public void onUpdate() {
		
	}
	
	// Component Pattern implementation
	public <T extends GameObjectComponent> T addComponent(T component) {
		boolean nameExists = false;
		for (GameObjectComponent comp : listComponents) {
			if (component.getName() == comp.getName()) {			
				nameExists = true;
			}
		}
		
		if (nameExists) {
			throw new RuntimeException("Error: cannot create more than one component with one name");
		} else {
			component.setGameObject(this);
			
			listComponents.add(component);
			
			return component;
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
		
		hierLevel = 0;
	}
	
	public GameObject() {
		// Adds an empty component for methods to work if the component being accessed does not exist
		emptyComponent = new GameObjectComponent("");
		addComponent(emptyComponent);

		transform = new TransformComponent();
		addComponent(transform);
		
		hierLevel = 0;
	}
}
