package renderer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.glDisable;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import models.Mesh;
import models.Plane;
import shaders.ShaderProgram;
import shaders.StaticShader;

public class Renderer {
	
    private final float FOV = (float) Math.toRadians(60.0f);

    private final float Z_NEAR = 0.02f;

    private final float Z_FAR = 1000.f;
	
	private StaticShader shader;
	public Matrix4f projectionMatrix;
	private Matrix4f worldMatrix;
	public Camera camera;
	public Scene scene;
	int i = 0;
	public Renderer(int height, int width, Scene scene) {
		shader = new StaticShader();
		
		Plane p = new Plane(0, 0 ,-3);
		p.selected = true;
		this.scene = scene;
		scene.meshes.add(p);
		camera = new Camera();
		
		float aspectRatio = (float) width / height;
		
		try {
			shader.createUniform("projectionMatrix");
			shader.createUniform("worldMatrix");
			shader.createUniform("viewMatrix");

		} catch (Exception e) {
			System.out.println("Failed to set uniform");
			e.printStackTrace();
		}
		
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio,
			    Z_NEAR, Z_FAR);
		
		shader.setUniform("projectionMatrix", projectionMatrix);
	}
	
	public void Render() {
		shader.start();
		glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
		for(Mesh m : scene.meshes) {
			shader.setUniform("worldMatrix", m.getWorldMatrix());
			shader.setUniform("projectionMatrix", projectionMatrix);
			shader.setUniform("viewMatrix", camera.getViewMatrix());
			m.render();
		}
		
		i++;
		shader.start();
	}
	
}
