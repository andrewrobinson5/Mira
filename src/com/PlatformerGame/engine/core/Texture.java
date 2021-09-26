package com.PlatformerGame.engine.core;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL45.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {
	// Having this static HashMap lets us keep track of the texture created by opengl
	private static HashMap<String, Integer> textureMap = new HashMap<String, Integer>();
	public static int myLocation;

	public int getTexture (String textureURL) {
		// We need to check if the texture is in the textureMap already, and if it is we won't need to reload it
		if(textureMap.containsKey(textureURL)) {
			return textureMap.get(textureURL);
		}
		return -1;
	}
	
	private void loadTexture(String textureLoc) {
		int imgWidth, imgHeight;
		ByteBuffer imgBuf;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			// Stack pointers to the data STB Image will fill data with
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			// Url of the texture
			URL textureUrl = Texture.class.getResource("/res/" + textureLoc);
			File file = new File(textureUrl.toExternalForm());
			//String path = file.getAbsolutePath();
			String path = file.getPath();
			path = path.replace("file:\\", "");
			
			//STB Image (public domain lib) converts PNG we passed to it to openGL usable RGBA data
			imgBuf = STBImage.stbi_load(path, w, h, channels, 4);
			imgWidth = w.get();
			imgHeight = h.get();
		
			if (imgBuf == null)
				throw new RuntimeException("Unable to load texture file '" + path + "'");
		
			// Quickly store the loaded texture into our hashmap so we don't have to reload it later
			// This is what we're actually keeping track of - it's sort of a "pointer" to VRAM where the texture is really stored.
			int texID = glGenTextures();
			textureMap.put(textureLoc, texID);
			
			// Now that we have the image data decoded, we can pass it to openGL
			glBindTexture(GL_TEXTURE_2D, texID);
			
			//docs for the next line can be found below.
			//https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glTexImage2D.xhtml
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imgWidth, imgHeight, 0,
					GL_RGBA, GL_UNSIGNED_BYTE, imgBuf);
			
			// opengl handles mipmaps on its own, just need to pass this next command.
			glGenerateMipmap(GL_TEXTURE_2D);
			
			//Free this image from stack to keep the stack from running out of space
			STBImage.stbi_image_free(imgBuf);
			System.out.println(imgWidth + "x" + imgHeight);
		}
	}
	
	public Texture(String location) {
		while(getTexture(location) == -1)
			loadTexture(location);
			
		myLocation = getTexture(location);
	}
}
