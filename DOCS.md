# Mira is a Java game engine built on LWJGL
Developed by Andrew Robinson for use in my Introductory Java Programming final project
### Notes:
- This project was developed by a beginner programmer. Before starting this project, I had no more than a couple of weeks of experience with Java, and no experience in software design.
- This project is built on naïve code. I wrote this from scratch without following tutorials or code-borrowing as a challenge. Structurally, I wrote what made sense to write to solve problems. Its optimization is probably very bad and its solutions far from elegant/standard.
- As of 10/5/2021, Mira has been alive for less than 2 months. It's not even close to feature-complete or battle-tested. It's got the bare minimum to make the game I'm currently making.

### License
As of 10/5/2021 this project is unlicensed. I reserve full copyright. I plan on releasing it to MIT license in the future.

### Build instructions
To be honest I don't know how to build this without Eclipse. The process for me is creating a runnable jar from the project and putting it in a directory alongisde a /res/ folder with textures and shaders in it.

## Documentation
### Core package
These are cool, I guess. Most of the super basic game structure methods are going to be in here. The game programmer should know most of these.

### App.java
Description: Mira Entry point and execution manager. This is the class that actually sets up the core engine and runs the game/scene/game object/game object component methods.
Note: Game programmer should ignore this class. This is core engine functionality.

Methods:
 - **void** main(String[] args): entry point, instantiates App and executes main loop
 - **constructor** App(): Initializes engine core
 - **void** loop(): Main app loop, executes all onCreate() and onUpdate() methods in game class, game objects, and game object components.
 - **void** destroy(): Destroys game window and openAL before terminating app.

### GameObject.java
Description: A bag of components that keeps track of its own parents and children.

Methods:
 - **void** addChild(GameObject child): parents 'child' GameObject to the owner of the function call.
 - <T extends GameObjectComponent> **T** addComponent(T component): adds 'component' to the owner of the function call. Returns the added component.
 - **private** incrementLvlAfterParenting(): hierarchy helper function.
 - **private** decrementLvlAfterUnparenting(): hierarchy helper function.
 - **constructor** GameObject(): creates a default GameObject object with a location of 0, 0, 0.
 - **constructor** GameObject(float x, float y, float z): creates a new GameObject object at location x, y, z.
 - **<T extends GameObjectComponent> T** getComponent(String component): returns a component whose name exactly matches the name given by 'component'. Useful if you don't have a way to pass the component directly to a script, otherwise just keep track of the component object yourself.
 - **void** makeChildOf(GameObject parent): makes the owner of the function call a child of 'parent'.
 - **void** onCreate(): sets bool hasRunOnce to true so App.java knows not to run any object's onCreate() more than once after a scene is loaded. **NOTE:** if you create a prefab that extends GameObject, in its onCreate() function, you must run 'super.onCreate();' for it to function correctly.
 - **void** onUpdate(): empty, but necessary for the program to compile with GameObject objects that don't have an onUpdate() function of their own.
 - **void** removeChild(GameObject child): disowns a child from the owner of function call, makes child a top-level GameObject.
 - **void** <T extends GameObjectComponent> removeComponent(T component): removes 'component' from a GameObject object.
 - **void** removeComponent(String component): removes component with name exactly matching given string from GameObject.
 - **void** unParent(): orphans the owner of the function call, makes it top-level.

### GameTime.java
Description: A few globally accessible variables that handle and scale the passage of time in game. Don't set these variables in game code unless you're looking for unexpected behavior.

Variables:
 - **private float** timeScale: scales time in game to realtime
 - **double** currentTime: current time in seconds since GLFW was initialized
 - **double** oldTime: time in seconds between the time GLFW was initialized and the previous frame
 - **double** deltaTime: difference between currentTime and oldTime multiplied by timeScale
 - **double** unaffectedDeltaTime: difference between currentTime and oldTime

Methods:
 - **static float** getTimeScale(): returns timeScale.
 - **static void** pause(): sets timeScale equal to 0f.
 - **static void** play(): sets timeScale equal to 1f.
 - **static void** setTimeScale(float scale): sets timeScale equal to 'scale'.
 
### MiraUtils.java
Description: Simple static utility functions I used commonly enough that java doesn't already have for good reasons. Mostly just behind-the-scenes stuff to make formatting nicer for the game programmer.
 
Methods:
 - **boolean** intToBool(int i): returns true if 'i' is greater than 0, returns false if 'i' is 0 or negative.
 - **int** boolToInt(boolean b): returns 1 if 'b' is true, returns 0 if 'b' is false.

### Scene.java
Description: Just a cheap bag of GameObjects with hierarchy parsing functions.

Methods:
 - **<T extends GameObject> void** add(T item): Probably the most useful function in Scene.java. Adds a GameObject to the Scene object which owns the function call.
 - **private** addHelperFunction(GameObject g): helper function for hierarchy management. This function is not exposed to the game programmer.
 - **void** clear(): removes all GameObjects from a scene. Doesn't destroy GameObject, only removes it from execution loop.
 - **<T extends GameObject> void** delete(T item): Removes an item from the Scene object which owns the function call.
 - **GameObject** get(int i): retrieves GameObject from Scene at index 'i'. Not particularly useful for game programmer unless used to loop through all objects.
 - **int** getHierarchyDepth(): Ignore. Returns the deepest level contained in the scene hierarchy. Not useful for game programmer.
 - **void** hierarchyHelperFunctionCreate(GameObject g): Ignore. Recursive helper function for running all of a scene's GameObject's onCreate() methods in the correct order.
 - **void** hierarchyHelperFunctionUpdate(GameObject g): Ignore. Recursive helper function for running all of a scene's GameObject's onUpdate() methods in the correct order.
 - **void** loadScene(): Tells the engine to execute your scene.
 - **int** size(): returns number of GameObjects in a scene.
 - **void** unloadScene(): Tells the engine to stop executing your scene.
 
### Sound.java
Description: Stores loaded .OGG file in a buffer and keeps track of the buffers to avoid loading an audio file multiple times at once.

Methods:
 - **int** getBufferId(): Returns the buffer of the Sound object which owns the function call.
 - **int** getSound(String soundLoc): Gets the buffer ID of a loaded audio file at the file location 'soundLoc'.
 - **constructor** Sound(String soundLoc): Loads an audio file into a buffer, stores the bufferID in a map and in the object's own private 'buffer' variable.
 
### Texture.java
Description: Loads textures from file, stores them in buffers, and keeps track of the buffers to avoid loading a texture multiple times at once. The methods of this class are almost entirely internal, and the game programmer only needs to know the constructor to create a texture to pass to a renderer component. TODO: Make this more elegant so the texture is loaded when used and not when created.

Methods:
 - **void** freeTexture(): Removes the OpenGL buffer associated with the texture object that owns the function call from memory. You should avoid using this unless you know the image being freed is not going to be used again.
 - **int** getTexture (String textureURL): Returns the OpenGL buffer ID that holds the texture.
 - **void** loadTexture(String textureLoc): Loads a texture using STB Image, then puts that texture into VRAM.
 - **constructor** Texture(String location): Creates the texture object, loads texture into a GL buffer, keeps track of its location.

## Components Package
Game programmer will need to know these. There are several components that may be added to a GameObject. These are the basic building blocks for GameObject function. These interface directly with engine internals.

### GameObjectComponent.java
Description: Component base class. Component with basic functionality that defines how components interface with GameObjects and Mira's App class.

Methods:
 - **void** disableComponent(): Disables a component. Disabled component methods do not get run by App.java.
 - **void** enableComponent(): Enables a component. Only enabled components get their onCreate() and onUpdate() run by App.java.
 - **constructor** GameObjectComponent(String name): Creates an empty GameObjectComponent with the given name, enabled by default.
 - **String** getName(): Returns the name of the Component object which owns this function call.
 - **void** onCreate(): Holds variable to check if onCreate() has already been run. Don't call this. That's weird.
 - **void** onUpdate(): Empty function that has to exist so App.java isn't calling a function that doesn't exist. Don't call this. That's weird.
 - **void** setGameObject(GameObject object): Used by GameObject class to add a component to a GameObject. Don't call this, it only updates the component object and not the GameObject.
 - **void** setName(String name): Sets the name of a Component to 'name'.

### QuadRendererComponent.java
Description: Simple component that holds a texture and a color and passes them with a quad to the OpenGL renderer to be displayed. Mira decides which shader to use depending on whether or not there is a texture attached to the Component.

Methods:
 - **void** onUpdate(): Decides which shader to send to the renderer and sends data to render queue. Don't call this. That's weird.
 - **constructor** QuadRendererComponent(float width, float height): Constructor for an upright rectangle shape.
 - **constructor** QuadRendererComponent(Vector3f one, Vector3f two, Vector3f three, Vector3f four): Ugly constructor for non-rectangular quads or quads whose vertices are skewed (e.g. a diamond-shape).
 - **private** updatePositionTextured(): Updates the position of each vertex relative to the GameObject that owns the component in a format that works with the textured shader.
 - **private** updatePositionUntextured(): Updates the position of each vertex relative to the GameObject that owns the component in a format that works with the untextured/solid color shader.
 
### RendererComponent.java
Description: Superclass for XXXXRendererComponent. As of 10/5/2021, this class is empty. No function.

### SoundEmitterComponent.java
Description: Component that creates an audio source in 3d space. As of 10/5/2021, there is limited functionality for 3d sounds, only global sounds work as expected.

**enum** MIRA_SOUND_ATTRIB:
   * MIRA_SOUND_GLOBAL: can the sound be heard globally? I.E. Will be heard no matter where listener is. Set this to true for background music or similar. Accepts 1 or 0.
   * MIRA_SOUND_RELATIVE: sound's position will be set relative to the listener. Accepts 1 or 0.
   * MIRA_SOUND_LOOPING: sound will replay after it is finished playing. Accepts 1 or 0.

Methods:
 - **int** getMiraSoundAttrib(MIRA_SOUND_ATTRIB attrib):
 - **void** onCreate(): Asks AudioRenderer to create a source, puts a sound in that source, and initializes it with attributes given before component was bound to a GameObject. Don't call.
 - **void** onUpdate(): Updates attributes to send to AudioRenderer, including position.
 - **void** pauseSound(): Pauses the sound being played by a source.
 - **private** sendAttribsToAudioRenderer(): Sets audio engine source attributes based on object's settings.
 - **void** setMiraSoundAttrib(MIRA_SOUND_ATTRIB attrib, int value): Sets a Mira sound attribute. Game programmer, these attributes are worth knowing. If the attribute is boolean, it accepts 1 or 0 as 'value' with 1 being true and 0 being false.
 - **constructor** SoundEmitterComponent(Sound sound, String name): Creates a new SoundEmitterComponent with the name 'name' and takes in a Sound object 'sound' to be bound to the component.
 - **void** startSound(): Begins playback of a source.
 
### TransformComponent.java
Description: Location, Rotation, and Scale properties of a GameObject. Every GameObject should be initialized with one of these. Do not remove it.
Note: As of 10/5/2021, there is only a location vector. Rotation and scale will come later. Sorry.

Methods:
 - **Vector3f** getCoords(): Gets the coordinates of a GameObject relative to its parent.
 - **Vector3f** getGlobalCoords(): Gets the coordinates of a GameObject in world space.
 - **void** onUpdate(): Adds object's transform to the transform of its parent. Don't call this. That's weird.
 - **void** setCoords(float x, float y, float z): Sets coordinates of an object relative to its parent.
 - **constructor** TransformComponent(float x, float y, float z): Initializes a TransformComponent with local coordinates x, y, z. You shouldn't need to use this yourself, the engine should handle it for you when you create a GameObject.
 - **constructor** TransformComponent(): Initializes a TransformComponent with local coordinates 0, 0, 0. You shouldn't need to use this yourself, the engine should handle it for you when you create a GameObject.


## Mira Engine Internals
The game programmer will not need to use these. Components work with these, and the game programmer will work with components.

### Renderer > Window.java
Description: Creates a window and OpenGL context.

### Renderer > OGLRenderer.java
Description: Sets up OGL defaults and abstracts mesh rendering away from raw OpenGL.

### Audio > ALAudioRenderer.java
Description: Sets up OpenAL defaults and abstracts sound loading/playing functionality from raw OpenAL.
