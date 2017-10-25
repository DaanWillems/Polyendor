package modeler;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import models.Mesh;
import models.Vertice;
import renderer.Camera;
import renderer.Display;
import renderer.Scene;
import renderer.Transformation;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
/**
 * This class manages everything selection related
 * @author solaw
 *
 */
public class Selector {
    private final float Z_NEAR = 0.01f;
    private final float FOV = (float) Math.toRadians(60.0f);
    private final float Z_FAR = 1000.f;
	IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4);
	int[] viewport = new int[4];
	private Vector3f dir;
	
	public Selector() {
		dir = new Vector3f();
	}
	
	public void selectVertice(Camera camera, Scene s) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        SelectVertice(camera.getPosition(), s, dir);
	}
	
	public void SelectVertice(Vector3f center, Scene s, Vector3f dir) {
	    Vector3f max = new Vector3f();
	    Vector3f min = new Vector3f();
	    Vector2f nearFar = new Vector2f();
		ArrayList<Mesh> meshes = s.getSelectedMeshes();
		
        float closestDistance = Float.POSITIVE_INFINITY;
        
        Vertice sv = null;
        
        for(Mesh m : meshes) {
            for (Vertice v : m.vertices) {
                v.selected = false;
                min.set(v.position);
                max.set(v.position);
                min.add(-0.2f, -0.2f, -0.2f);
                max.add(0.2f, 0.2f, 0.2f);
                if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                    closestDistance = nearFar.x;
                    sv = v;
                    m.bind();
                }
            }	
        }

        if (sv != null) {
            sv.position.x += 0.01;
            for(Mesh m : meshes) {
            	m.bind();
            }
        }
	}
}
