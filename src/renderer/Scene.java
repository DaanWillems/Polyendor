package renderer;

import java.util.ArrayList;

import models.Mesh;

public class Scene {
	
	public Mesh selectedMesh;
	public ArrayList<Mesh> meshes;
	
	public Scene() {
		meshes = new ArrayList<>();
	}

	public void moveSelectedItems(float x, float y, float z) {
		selectedMesh.move(x, y, z);
	}

	public void unSelect() {
		if(selectedMesh != null) {
			selectedMesh.selected = false;
			selectedMesh = null;
		}
	}

	public Mesh getSelectedMesh() {
		return selectedMesh;
	}

	public void setSelectedMesh(Mesh m) {
		m.selected = true;
		unSelect();
		selectedMesh = m;
	}
}
