package models;

public class Plane extends Mesh {
	public Plane(int x, int y, int z) {
		super(x, y, z, new float[] {
		        -10.5f,  10.5f, 0.0f,
		        -10.5f, -10.5f, 0.0f,
		         10.5f,  10.5f, 0.0f,
		         10.5f,  10.5f, 0.0f,
		        -10.5f, -10.5f, 0.0f,
		         10.5f, -10.5f, 0.0f,
		});
		bind();
		this.setScale(1f);
		this.setRotation(90f, 0, 180f);
	}
}
