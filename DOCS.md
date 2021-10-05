# Mira is a Java game engine built on LWJGL
Developed by Andrew Robinson for use in my Introductory Java Programming final project
### Notes:
- This project was developed by a beginner programmer. Before starting this project, I had no more than a couple of weeks of experience with Java, and no experience in software design.
- This project is built on naïve code. I wrote this from scratch without following tutorials or code-borrowing as a challenge. Structurally, I wrote what made sense to write to solve problems. Its optimization is probably very bad and its solutions far from elegant/standard.
- As of 10/4/2021, Mira has been alive for less than 2 months. It's not even close to feature-complete or battle-tested. It's got the bare minimum to make the game I'm currently making.

### License
As of 10/4/2021 this project is unlicensed. I reserve full copyright. I plan on releasing it to MIT license in the future.

### Build instructions
To be honest I don't know how to build this without Eclipse. The process for me is creating a runnable jar from the project and putting it in a directory alongisde a /res/ folder with textures and shaders in it.

## Documentation
### Core package
### App.java
Description: Mira Entry point and execution manager. This is the class that actually sets up the core engine and runs the game/scene/game object/game object component methods.
Note: Game programmer should ignore this class. This is core engine functionality.

Methods:
 - **void** main(String[] args): entry point, instantiates App and executes main loop
 - **void** App(): Initializes engine core
 - **void** loop(): Main app loop, executes all onCreate() and onUpdate() methods in game class, game objects, and game object components.
 - **void** destroy(): Destroys game window and openAL before terminating app.

### GameObject.java
Description: A bag of components that keeps track of its own parents and children.

Methods:
 - **void** addChild(GameObject child): parents 'child' GameObject to the owner of the function call.
 - <T extends GameObjectComponent> **T** addComponent(T component): adds 'component' to the owner of the function call. Returns the added component.
 - **private** incrementLvlAfterParenting(): hierarchy helper function.
 - **private** decrementLvlAfterUnparenting(): hierarchy helper function.
 - **void** GameObject(): creates a default GameObject object with a location of 0, 0, 0.
 - **void** GameObject(float x, float y, float z): creates a new GameObject object at location x, y, z.
 - <T extends GameObjectComponent> **T** getComponent(String component): returns a component whose name exactly matches the name given by 'component'. Useful if you don't have a way to pass the component directly to a script, otherwise just keep track of the component object yourself.
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
 - **float** getTimeScale(): returns timeScale.
 - **void** pause(): sets timeScale equal to 0f.
 - **void** play(): sets timeScale equal to 1f.
 - **void** setTimeScale(float scale): sets timeScale equal to 'scale'.
 
### MiraUtils.java
Description: Simple static utility functions I used commonly enough that java doesn't already have for good reasons. Mostly just behind-the-scenes stuff to make formatting nicer for the game programmer.
 
Methods:
 - **boolean** intToBool(int i): returns true if 'i' is greater than 0, returns false if 'i' is 0 or negative.
 - **int** boolToInt(boolean b): returns 1 if 'b' is true, returns 0 if 'b' is false.


> Written with [StackEdit](https://stackedit.io/).