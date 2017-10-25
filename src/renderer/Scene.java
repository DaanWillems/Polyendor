package renderer;

import java.util.ArrayList;

import models.Mesh;

public class Scene {
	
	public ArrayList<Mesh> meshes;
	
	public Scene() {
		meshes = new ArrayList<Mesh>();
	}

	public void moveSelectedItems(float x, float y, float z) {
		for(Mesh m : meshes) {
			if(m.selected) {
				m.move(x, y, z);
			}
		}
	}

	public void unselectAll() {
		for(Mesh m : meshes) {
			m.selected = false;
		}
	}
	
	public ArrayList<Mesh> getSelectedMeshes() {
		ArrayList<Mesh> selectedMeshes = new ArrayList<>();
		for(Mesh m : meshes) {
			if(m.selected) {
				selectedMeshes.add(m);
			}
		}
		return selectedMeshes;
	}
}
