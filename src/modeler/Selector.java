package modeler;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import main.Main;
import main.State;
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
    private Matrix4f invProjectionMatrix;
    
    private Matrix4f invViewMatrix;

    private Vector3f mouseDir;
    
    private Vector4f tmpVec;
	
	public Selector() {
		dir = new Vector3f();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDir = new Vector3f();
        tmpVec = new Vector4f();
	}
	
	public void selectVertice(Camera camera, Scene s, Display window, Vector2d mousePos, Matrix4f projectionMatrix) {
		 // Transform mouse coordinates into normalized spaze [-1, 1]
        int wdwWitdh = window.width;
        int wdwHeight = window.height;
        
        float x = (float)(2 * mousePos.x) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(projectionMatrix);
        invProjectionMatrix.invert();
        
        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;
        
        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);
        
        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);
        SelectVertice(camera.getPosition(), s, mouseDir);
	}
	
	public void SelectVertice(Vector3f center, Scene s, Vector3f dir) {
	    Vector3f max = new Vector3f();
	    Vector3f min = new Vector3f();
	    Vector2f nearFar = new Vector2f();
		
        float closestDistance = Float.POSITIVE_INFINITY;
                
        if(Main.State == State.editMode) {
            Vertice sv = null;
        	
            Mesh m = s.getSelectedMesh();
            
            for (Vertice v : m.vertices) {
                min.set(v.position);
                max.set(v.position);
                min.add(-0.2f, -0.2f, -0.2f);
                max.add(0.2f, 0.2f, 0.2f);
                if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                    closestDistance = nearFar.x;
                    sv = v;
                }
            }	
	        
	        if (sv != null) {
	        	sv.selected = true;
	        	sv.position.x += 0.01f;
	        	m.bind();
	        }
        } else if(Main.State == State.selectMode) {
        	   Mesh selectedMesh = null;
           	
               Mesh mesh = s.getSelectedMesh();
               
               for (Mesh m : s.meshes) {
            	   min.set(m.getAbsolutePosition());
                   max.set(m.getAbsolutePosition());
                   min.add(-m.getScale(), -m.getScale(), -m.getScale());
                   max.add(m.getScale(), m.getScale(), m.getScale());
                   if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                	   
                	   System.out.println(m.getAbsolutePosition().x);
                	   System.out.println(m.getAbsolutePosition().y);
                	   System.out.println(m.getAbsolutePosition().z);
                	   System.out.println("-------");
                	   
                       closestDistance = nearFar.x;
                       selectedMesh = m;
                   }
               }	
   	        
   	        if (selectedMesh != null) {
   	        	if(s.selectedMesh != selectedMesh) {
   	   	        	s.setSelectedMesh(selectedMesh);

   	        	}
   	        }
        }
	}
}
