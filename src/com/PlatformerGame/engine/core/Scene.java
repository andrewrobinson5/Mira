//NAME: Scene.java
//COPYRIGHT: Andrew Robinson 2021
//DESC: Scene class is just a cheap bag of GameObjects with hierarchy parsing functions.

package com.PlatformerGame.engine.core;

import java.util.ArrayList;

//I have an evil plan: Let's make this class extend gameobject and just use ArrayList children instead of listObjects. That way, when we add an object to the scene, it's just adding it to a big container GameObject.
public class Scene extends GameObject {
	//TODO REPLACE THIS NEXT LINE WITH GAMEOBJECT FN
	private ArrayList<GameObject> listObjects = new ArrayList<GameObject>();
	public ArrayList<GameObject> alreadyIteratedObjects = new ArrayList<GameObject>();
	
	public void clear() {
		listObjects.clear();
	}
	
	public GameObject get(int i) {
		return listObjects.get(i);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GameObject> T get(String go) {
		for(int i = 0; i < listObjects.size(); i++) {
			if(listObjects.get(i).name == go) {
				return (T)listObjects.get(i);
			}
		}
		//do not use this function if you don't know the name.
		throw new RuntimeException("Illegal behavior: Attempted to get non-existant GameObject '" + go + "'.");
	}
	
	public int size() {
		return listObjects.size();
	}
	
	// I really don't know how to do this any other way, I'll learn later
	private void addHelperFunction(GameObject g) {
		listObjects.add(g);
		for(int i = 0; i < g.children.size(); i++) {
			addHelperFunction(g.children.get(i));
		}
	}
	
	public <T extends GameObject> void add(T item) {
		addHelperFunction(item);
	}
	
	public <T extends GameObject> void delete(T item) {
		if(listObjects.contains(item))
			listObjects.remove(item);
	}
	
	public int getHierarchyDepth() {
		int x = 0;
		for(int i = 0; i < listObjects.size(); i++) {
			if (listObjects.get(i).hierLevel > x)
				x = listObjects.get(i).hierLevel;
		}
		return x;
	}
	
	// I don't really like that this is in here but I shouldn't need to fix it.
	public void hierarchyHelperFunctionCreate(GameObject g) {
		g.onCreate(); //run the onCreate() for gameobject g
		g.hasRunOnce = true; //set hasRunOnce to true so that it isn't run twice
		System.out.println("test1"); //debug
		
		//IF the object hasn't already been iterated through
		if(!alreadyIteratedObjects.contains(g)) {
			alreadyIteratedObjects.add(g);
		
			System.out.println("test2"); //debug
			
			for(int e = 0; e < g.children.size(); e++) {
			
				System.out.println("test3"); //debug
				
				// this is never true for some reason
				if(listObjects.get(e).parent == g) {
					System.out.println("test4"); //debug
					hierarchyHelperFunctionCreate(g.children.get(e));
				}
			}
		}
	}

	// I don't really like that this is in here but I shouldn't need to fix it.
	public void hierarchyHelperFunctionUpdate(GameObject g) {
		g.onUpdate();
		g.hasRunOnce = true;
		if(!alreadyIteratedObjects.contains(g)) { 
			for (int i = 0; i < g.listComponents.size(); i++) {
				if (g.listComponents.get(i).enabled) {
					if (!g.listComponents.get(i).hasRunOnce)
						g.listComponents.get(i).onCreate();
					
					g.listComponents.get(i).onUpdate();
				}
			}
			alreadyIteratedObjects.add(g);
			for(int e = 0; e < g.children.size(); e++) {
				hierarchyHelperFunctionUpdate(g.children.get(e));
			}
		}
		
	}

	public void loadScene() {
		App.currentScene = this;
	}
	
	public void unloadScene() {
		App.currentScene = null;
		for (int g = 0; g < size(); g++) {	
			get(g).hasRunOnce = false;
//			System.out.println("test");
			for (int f = 0; f < get(g).listComponents.size(); f++) {
				get(g).listComponents.get(f).hasRunOnce = false;
			}
//			get(g).listComponents.clear();
		}
//		clear();
	}
}
