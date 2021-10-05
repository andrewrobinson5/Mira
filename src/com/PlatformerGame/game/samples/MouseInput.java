// NAME: MouseInput.java
// DESC: Sample - how to use GLFW to get mouse input.
// NOTE: I'm really happy with the format of this. This is how the game class is supposed to look - just game code and
//	no engine crap but the import.

package com.PlatformerGame.game.samples;

// Import Game Engine!
import com.PlatformerGame.engine.core.*;
import com.PlatformerGame.engine.core.components.QuadRendererComponent;
import com.PlatformerGame.engine.core.components.TransformComponent;

// Required for vector math
import org.joml.*;

// Required for stack manipulation voodoo
import org.lwjgl.system.MemoryStack;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

// Required for the makeshift scene setup
import java.util.ArrayList;

// Required for currently un-abstracted GLFW function calls
import static org.lwjgl.glfw.GLFW.*;


public class MouseInput {
	// Makeshift scene list
	public ArrayList<GameObject> myScene = new ArrayList<GameObject>();

	// Runs once when the game is launched for the first time.
	public void onCreate() {
		GameObject cursor = new GameObject();
		cursor.addComponent(new QuadRendererComponent(0.15f, 0.15f));
		cursor.<QuadRendererComponent>getComponent("QuadRenderer").solidColor = new Vector4f(0.8f, 0, 0.2f, 1f);
		myScene.add(cursor);
	}
	
	// Runs every frame before the logic of each gameObject.
	public void onUpdate() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			DoubleBuffer xPos = stack.mallocDouble(1);
			DoubleBuffer yPos = stack.mallocDouble(1);
			glfwGetCursorPos(App.gameWindow.window, xPos, yPos);
			float tempX = (float) xPos.get();
			float tempY = (float) yPos.get();
			
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			glfwGetWindowSize(App.gameWindow.window, w, h);

			int width = w.get();
			int height = h.get();
			
			// So glfwGetCursorPos() returns the exact pixel location of the cursor on the window using the top
			//  left corner as 0,0. So we need to convert that position into something relative to openGL's NDC.
			tempX -= (width/2); 
			tempY -= (height/2);
			
			tempX = tempX/width*2;
			tempY = -tempY/height*2;
			
			myScene.get(0).<TransformComponent>getComponent("Transform").setCoords(tempX, tempY, 0);
		}
	}
}

