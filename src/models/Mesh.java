package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import util.Utils;

public class Mesh {
	
	public ArrayList<Vertice> vertices;
	public int[] indices;
	public float[] normals;
	public int vaoID;
	private Matrix4f worldMatrix;
	
	private int xPos;
	private int yPos;
	private int zPos;
	
	private float scale;
	private float xRotation;
	private float yRotation;
	private float zRotation;
	public boolean selected;
	public Vector3f position;
	
	private ArrayList<Integer> vboList;
	
	public Mesh(int x, int y, int z, float[] vertices) {
		xPos = x;
		yPos = y;
		zPos = z;
		position = new Vector3f(x, y, z);
		selected = false;
		
		xRotation = 0f;
		yRotation = 0f;
		zRotation = 0f;
		
		this.vertices = transformVertices(vertices);
		
		vboList = new ArrayList<Integer>();
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		worldMatrix = new Matrix4f();
	}
	
    public Vector3f getPosition() {
        return new Vector3f(xPos, yPos, zPos);
    }
	
    public float getScale() {
        return scale;
    }
	
	private ArrayList<Vertice> transformVertices(float[] vertices) {
		int x = 1;
		Vector3f pos = new Vector3f();
		ArrayList<Vertice> newVertices = new ArrayList<>();
		for(int i = 0; i < vertices.length; i++) {
			switch(x) {
			case 1:
				pos.x = vertices[i];
				break;
			case 2:
				pos.y = vertices[i];
				break;
			case 3:
				pos.z = vertices[i];
				newVertices.add(new Vertice(pos));
				pos = new Vector3f();
				x = 0;
				break;
			}
			x++;
		}
		return newVertices;
	}
	
	private float[] transformVertices(ArrayList<Vertice> vertices) {
		float[] newVertices = new float[vertices.size()*3];
		int i = 0;
		for(Vertice v : this.vertices) {
			newVertices[i] = v.position.x;
			i++;
			newVertices[i] = v.position.y;
			i++;
			newVertices[i] = v.position.z;
			i++;
		}
		return newVertices;
	}
	
	public void setRotation(float x, float y, float z) {
		xRotation = x;
		yRotation = y;
		zRotation = z;
	}
	
	public void bind() {
		//add vertex
		glBindVertexArray(vaoID);
		
		//Add vertices
		int vboID = glGenBuffers();
		vboList.add(vboID);
		FloatBuffer buffer = Utils.storeDataInFloatBuffer(transformVertices(vertices));
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		if(indices != null) {
			vboID = glGenBuffers();
			IntBuffer indicesBuffer = Utils.storeDataInIntBuffer(indices);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public int getVertexCount() {
		if(indices != null) {
			return indices.length;
		}
		return vertices.size()*3;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void render() {
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		
		if(indices != null) {
			glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		} else {
			glDrawArrays(GL_TRIANGLES, 0, getVertexCount());
		}
		
	    glDisableVertexAttribArray(0);
	    glBindVertexArray(0);
	}
	
	public void move(float x, float y, float z) {
		for(Vertice v : vertices) {
			v.position.x += x;
			v.position.y += y;
			v.position.z += z;
		}
		bind();
	}
	
	public Matrix4f getWorldMatrix() {
		worldMatrix.identity().translate(xPos, yPos, zPos).
	    rotateX((float)Math.toRadians(xRotation)).
	    rotateY((float)Math.toRadians(yRotation)).
	    rotateZ((float)Math.toRadians(zRotation)).
	    scale(scale);
		return worldMatrix;
	}
}
