package com.PlatformerGame.engine.core;

import java.util.ArrayList;

public class Scene {
	private ArrayList<GameObject> listObjects = new ArrayList<GameObject>();
	public ArrayList<GameObject> alreadyIteratedObjects = new ArrayList<GameObject>();
	
	public GameObject get(int i) {
		return listObjects.get(i);
	}
	
	public int size() {
		return listObjects.size();
	}
	
	public <T extends GameObject> void add(T item) {
		listObjects.add(item);
	}
	
	public <T extends GameObject> void delete(T item) {
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
		g.onCreate();
		g.hasRunOnce = true;
		if(!alreadyIteratedObjects.contains(g)) { 
			alreadyIteratedObjects.add(g);
			for(int e = 0; e < g.children.size(); e++) {
				if(listObjects.get(e).parent == g) {
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
//		System.out.println(App.currentScene.);
	}
	
	public void unloadScene() {
		App.currentScene = null;
		for (int g = 0; g < size(); g++) {	
			get(g).hasRunOnce = false;
		}
	}
}
