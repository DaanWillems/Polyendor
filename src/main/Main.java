package main;

import static org.lwjgl.glfw.GLFW.*;

import java.io.FileNotFoundException;

import org.joml.Vector2f;
import org.joml.Vector3f;

import modeler.Input;
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
	
	static Display d;
	static Renderer r;
	static Scene scene;
	private static MouseInput mouseInput;
	public static State State;
	
	public static void main(String[] args) {
		mouseInput = new MouseInput();
		scene = new Scene();
		d = new Display(HEIGHT, WIDTH);
		r = new Renderer(HEIGHT, WIDTH, scene);
		Input input = new Input(d, scene, r, mouseInput);
		State = State.selectMode;
		mouseInput.init(d);
		while(!d.shouldClose()) {
			d.updateDisplay();
			r.Render();
			input.input();
		}
	}
	

}

