
public class Neighbor {
	Particle p1;
	Particle p2;
	double distance;
	double nx;
	double ny;
	double weight;
	
	public Neighbor() {
		p1 = null;
		p2 = null;
		distance = 0;
		nx = 0;
		ny = 0;
		weight = 0;
	}
	
	void setParticle(Particle p1, Particle p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.nx = p1.x - p2.x;
		this.ny = p1.y - p2.y;
		this.distance = Math.sqrt(this.nx * this.nx + this.ny * this.ny);
		this.weight = 1 - this.distance / NewSPH.range;
		double temp = this.weight * this.weight * this.weight;
        p1.density += temp;
        p2.density += temp;
        temp = 1 / this.distance;
        this.nx *= temp;
        this.ny *= temp;
	}
	
	void calcForce() {
		double pressureWeight = this.weight * (p1.pressure + p2.pressure) / (p1.density + p2.density) * NewSPH.pressure;
		double viscosityWeight = this.weight / (p1.density + p2.density) * NewSPH.viscosity;
		p1.fx += this.nx * pressureWeight;
        p1.fy += this.ny * pressureWeight;
        p2.fx -= this.nx * pressureWeight;
        p2.fy -= this.ny * pressureWeight;
        double rvx = p2.vx - p1.vx;
        double rvy = p2.vy - p1.vy;
        p1.fx += rvx * viscosityWeight;
        p1.fy += rvy * viscosityWeight;
        p2.fx -= rvx * viscosityWeight;
        p2.fy -= rvy * viscosityWeight;
	}
}
