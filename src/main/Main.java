package main;

import static org.lwjgl.glfw.GLFW.*;

import java.io.FileNotFoundException;

import org.joml.Vector2f;
import org.joml.Vector3f;

import modeler.MouseInput;
import modeler.Selector;
import models.Mesh;
import models.OBJLoader;
import models.Plane;
import renderer.Display;
import renderer.Renderer;
import renderer.Scene;

public class Main {

	private final static int HEIGHT = 720;
	private final static int WIDTH = 1280;
    private static final float MOUSE_SENSITIVITY = 0.2f;
	private static Vector3f cameraInc;
	private static final float CAMERA_POS_STEP = 0.01f;
	
	static Display d;
	static Renderer r;
	static Scene scene;
	private static MouseInput mouseInput;
	
	public static void main(String[] args) {
		mouseInput = new MouseInput();
		cameraInc = new Vector3f();
		scene = new Scene();
		d = new Display(HEIGHT, WIDTH);
		r = new Renderer(HEIGHT, WIDTH, scene);
		mouseInput.init(d);
		while(!d.shouldClose()) {
			d.updateDisplay();
			r.Render();
			input();
		}
	}
	
	public static void input() {
		cameraInc.set(0, 0, 0);
		if (d.isKeyPressed(GLFW_KEY_W)) {
	        cameraInc.z = -1;
	    } else if (d.isKeyPressed(GLFW_KEY_S)) {
	        cameraInc.z = 1;
	    }
	    if (d.isKeyPressed(GLFW_KEY_A)) {
	        cameraInc.x = -1;
	    } else if (d.isKeyPressed(GLFW_KEY_D)) {
	        cameraInc.x = 1;
	    }
	    if (d.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
	        cameraInc.y = -1;
	    } else if (d.isKeyPressed(GLFW_KEY_SPACE)) {
	        cameraInc.y = 1;
	    }
	    if (d.isKeyPressed(GLFW_KEY_Q)) {
			try {
				Mesh newMesh = OBJLoader.loadMesh("/models/cube.obj");
				newMesh.bind();
				scene.unselectAll();
				newMesh.selected = true;
				r.scene.meshes.add(newMesh);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    if(d.isKeyPressed(GLFW_KEY_LEFT)) {
	    	scene.moveSelectedItems(-0.05f, 0f, 0f);
	    }
	    if(d.isKeyPressed(GLFW_KEY_U)) {
	    	Selector s = new Selector();
	    	s.selectVertice(r.camera, scene);
	    }
	    if(d.isKeyPressed(GLFW_KEY_DOWN)) {
	    	scene.moveSelectedItems(0f, -0.05f, 0f);
	    }
	    if(d.isKeyPressed(GLFW_KEY_UP)) {
	    	scene.moveSelectedItems(0f, 0.05f, 0f);
	    }
	    if(d.isKeyPressed(GLFW_KEY_RIGHT)) {
	    	scene.moveSelectedItems(0.05f, 0f, 0f);
	    }
	    r.camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
			        cameraInc.y * CAMERA_POS_STEP,
			        cameraInc.z * CAMERA_POS_STEP);
	    
		mouseInput.input(d);
		 r.camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
			        cameraInc.y * CAMERA_POS_STEP,
			        cameraInc.z * CAMERA_POS_STEP);
		 
		 Vector2f rotVec = mouseInput.getDisplVec();
         r.camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

	}

}
