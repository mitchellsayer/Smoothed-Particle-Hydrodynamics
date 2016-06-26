import java.util.ArrayList;

public class GridCell {
	ArrayList<Particle> particleList;
	int numParticles = 0;
	
	public GridCell() {
		particleList = new ArrayList<Particle>();
	}

	public void add(Particle p) {
		if (!particleList.contains(p)) {
			particleList.add(p);
			numParticles++;
		}
	}

	public void remove(Particle p) {
		if (particleList.contains(p)) {
			particleList.remove(p);
		}
	}
	
	public ArrayList<Particle> getParticleList() {
		return particleList;
	}

	public void clear() {
		numParticles = 0;
		particleList.clear();
	}

}
