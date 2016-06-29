public class Particle {
	//public float[] x;
	public float[] v;
	public float[] v_prev;
	public float[] a;
	double vx;
	double vy;
	double x;
	double y;
	int gx;
	int gy;
	double fx;
	double fy;
	double pressure;
	double density;
	int n;
	int gridx;
	int gridy;

	public Particle() {
		//x = new float[2];
		//x[0] = (float) (Math.random());
		//x[1] = (float) (Math.random());
		v = new float[2];
		v[0] = (float) ((Math.random()*2)-1);
		v[1] = (float) ((Math.random()*2)-1);
		v_prev = new float[2];
		a = new float[2];
	}

	public void move() {
		this.vy += NewSPH.gravity;
	    this.vx += this.fx;
	    this.vy += this.fy;
	    this.x += this.vx;
	    this.y += this.vy;
	    if (this.x < 5)
	        this.vx += (5 - this.x) * 0.5 - this.vx * 0.5;
	    if (this.y < 5)
	        this.vy += (5 - this.y) * 0.5 - this.vy * 0.5;
	    if (this.x > NewSPH.screenSize)
	        this.vx += (NewSPH.screenSize - this.x) * 0.5 - this.vx * 0.5;
	    if (this.y > NewSPH.screenSize)
	        this.vy += (NewSPH.screenSize - this.y) * 0.5 - this.vy * 0.5;
	}
}