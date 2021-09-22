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
	
	public void hierarchyHelperFunctionCreate(GameObject g) {
		g.onCreate();
		g.hasRunOnce = true;
		for(int e = 0; e < listObjects.size(); e++) {
			if(listObjects.get(e).parent == g) {
				alreadyIteratedObjects.add(g);
				hierarchyHelperFunctionCreate(g);
			}
		}
	}
	
	public void hierarchyHelperFunctionUpdate(GameObject g) {
		g.onUpdate();
		g.hasRunOnce = true;
		for(int e = 0; e < listObjects.size(); e++) {
			if(listObjects.get(e).parent == g) {
				alreadyIteratedObjects.add(g);
				hierarchyHelperFunctionUpdate(g);
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
		}
	}
}
