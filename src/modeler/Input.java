package modeler;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_U;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.sql.Time;
import java.util.Date;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

import org.joml.Vector2f;
import org.joml.Vector3f;

import main.Main;
import main.State;
import models.Mesh;
import models.OBJLoader;
import renderer.Display;
import renderer.Renderer;
import renderer.Scene;

public class Input {

	private static final float MOUSE_SENSITIVITY = 0.2f;
	private static Vector3f cameraInc;
	private static final float CAMERA_POS_STEP = 0.02f;
	private Display d;
	private Scene scene;
	private Renderer r;
	private MouseInput mouseInput;
	
	private Date lastAction;

	public Input(Display d, Scene scene, Renderer r, MouseInput mouseInput) {
		this.d = d;
		this.scene = scene;
		this.r = r;
		this.mouseInput = mouseInput;
		cameraInc = new Vector3f();
		
		lastAction = new Date();
	}

	public void input() {
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
			if(!checkCooldown()) {
				return;
			}
			
			try {
				Mesh newMesh = OBJLoader.loadMesh("/models/cube2.obj");
				newMesh.bind();
				scene.unSelect();
				r.scene.meshes.add(newMesh);
				r.scene.setSelectedMesh(newMesh);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (d.isKeyPressed(GLFW_KEY_TAB)) {
			if(!checkCooldown()) {
				return;
			}
			if (!scene.getSelectedMesh().editMode) {
				Main.State = State.editMode;
				scene.getSelectedMesh().editMode = true;
			} else {
				Main.State = State.selectMode;
				scene.getSelectedMesh().editMode = false;
			}
		}
		if (d.isKeyPressed(GLFW_KEY_LEFT)) {
			scene.moveSelectedItems(-0.05f, 0f, 0f);
		}
		if (d.isKeyPressed(GLFW_KEY_DOWN)) {
			scene.moveSelectedItems(0f, -0.05f, 0f);
		}
		if (d.isKeyPressed(GLFW_KEY_UP)) {
			scene.moveSelectedItems(0f, 0.05f, 0f);
		}
		if (d.isKeyPressed(GLFW_KEY_RIGHT)) {
			scene.moveSelectedItems(0.05f, 0f, 0f);
		}
		r.camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);

		mouseInput.input(d);

		if (mouseInput.isRightButtonPressed()) {
			Vector2f rotVec = mouseInput.getDisplVec();
			r.camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
		if (mouseInput.isLeftButtonPressed()) {
			if(!checkCooldown()) {
				return;
			}
			Selector s = new Selector();
			s.selectVertice(r.camera, scene, d, mouseInput.getCurrentPos(), r.projectionMatrix);
		}
	}
	
	private boolean checkCooldown() {
		Date now = new Date();
		long diff = now.getTime() - lastAction.getTime();
		
		lastAction = new Date();
		System.out.println(diff);
		
		return diff > 500;
	}
}
