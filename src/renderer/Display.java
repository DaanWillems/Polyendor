package renderer;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Display {
	
	public long windowID;
	
	public int width, height;
	
	public Display(int height, int width) {
		GLFWErrorCallback.createPrint(System.err).set();
		this.width = width;
		this.height = height;
		
		if (!glfwInit())
		{
		    System.err.println("Error initializing GLFW");
		    System.exit(1);
		}
		
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		
		windowID = glfwCreateWindow(width, height, "My GLFW Window", NULL, NULL);

		if (windowID == NULL)
		{
		    System.err.println("Error creating a window");
		    System.exit(1);
		}
		
		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(1);
		GL.createCapabilities();
		glfwShowWindow(windowID);
		glEnable(GL_DEPTH_TEST);		
	}
	
	public void updateDisplay() {
		 glfwPollEvents();
		 glfwSwapBuffers(windowID);
	}
	
	public void closeDisplay() {
		glfwDestroyWindow(windowID);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(windowID);
	}

    public boolean isKeyPressed(int keyCode) {
		int state = glfwGetKey(windowID, keyCode);
		if (state == GLFW_PRESS) {
		    return true;
		}
		return false;
    }
	
}
