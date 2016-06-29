import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class NewSPH extends JPanel implements ActionListener {
	static double gravity = 0.6;
	public static double range = 10;
	static double pressure = 1;
	static double viscosity = .07;
    double range2 = range * range;
    int particleSize = 4;
    double density = 1;
    int numGrids = 25;
    static int screenSize = 700;
    double invGridSize = 1 / (screenSize / numGrids);
    
    int numLiquidParticles = 2500;
    int numSolidParticles = 50;
    int numParticles = numLiquidParticles + numSolidParticles;
    
    Particle[] particles = new Particle[numParticles];
    ArrayList<Neighbor> neighbors = new ArrayList<Neighbor>();
    int numNeighbors = 0;
    int count = 0;
    boolean press = false;
    GridCell[][] grids = new GridCell[numGrids][numGrids];
    int delay = 10;
    
    public NewSPH() {
    	super();
    	for (int i = 0; i < numGrids; i++)
            for (int j = 0; j < numGrids; j++)
            	grids[i][j] = new GridCell();
    	this.initParticles();
    	
    	Timer timer = new Timer(20, this);
    	timer = new Timer(20, this);
 	    timer.setInitialDelay(20);
 	    timer.start();
    }
    
    void move() {
    	count++;
    	updateGrids();
    	findNeighbors();
    	calcPressure();
    	calcForce();
    	for (int i=0; i<numParticles; i++) {
    		Particle p = particles[i];
    		p.move();
    	}
    }
    
    void updateGrids() {
    	int i;
    	int j;
    	for (i = 0; i < numGrids; i++)
            for (j = 0; j < numGrids; j++)
                grids[i][j].clear();
    	
    	for (i = 0; i < numParticles; i++) {
    		Particle p = particles[i];
    		p.fx = p.fy = p.density = 0;
    		p.gx = (int) Math.floor(p.x * invGridSize);
    		p.gy = (int) Math.floor(p.y * invGridSize);
    		if (p.gx < 0)
                p.gx = 0;
            if (p.gy < 0)
                p.gy = 0;
            if (p.gx > numGrids - 1)
                p.gx = numGrids - 1;
            if (p.gy > numGrids - 1)
                p.gy = numGrids - 1;
            grids[p.gx][p.gy].add(p);
    	}
    }
    
    void findNeighbors() {
    	numNeighbors = 0;
    	for (int i=0; i<numParticles; i++) {
    		Particle p = particles[i];
    		boolean xMin = (p.gx != 0);
    		boolean xMax = (p.gx != numGrids-1);
    		boolean yMin = (p.gy != 0);
    		boolean yMax = (p.gy != numGrids-1);
    		findNeighborsInGrid(p, grids[p.gx][p.gy]);
    		if (xMin) findNeighborsInGrid(p, grids[p.gx - 1][p.gy]);
            if (xMax) findNeighborsInGrid(p, grids[p.gx + 1][p.gy]);
            if (yMin) findNeighborsInGrid(p, grids[p.gx][p.gy - 1]);
            if (yMax) findNeighborsInGrid(p, grids[p.gx][p.gy + 1]);
            if (xMin && yMin) findNeighborsInGrid(p, grids[p.gx - 1][p.gy - 1]);
            if (xMin && yMax) findNeighborsInGrid(p, grids[p.gx - 1][p.gy + 1]);
            if (xMax && yMin) findNeighborsInGrid(p, grids[p.gx + 1][p.gy - 1]);
            if (xMax && yMax) findNeighborsInGrid(p, grids[p.gx + 1][p.gy + 1]);
    	}
    }
    
    void findNeighborsInGrid(Particle pi, GridCell cell) {
    	for (int j=0; j<cell.numParticles; j++) {
    		Particle pj = cell.particleList.get(j);
    		if (pi.equals(pj))
    			continue;
    		double distance = (pi.x - pj.x) * (pi.x - pj.x) + (pi.y - pj.y) * (pi.y - pj.y);
    		if (distance < range2) {
    			if (neighbors.size() == numNeighbors)
                    neighbors.add(new Neighbor());
                neighbors.get(numNeighbors++).setParticle(pi, pj);
    		}
    	}
    }
    
    void calcPressure() {
    	for (int i=0; i<numParticles; i++) {
    		Particle p = particles[i];
    		if (p.density < density) {
    			p.density = density;
    		}
    		p.pressure = p.density - density;
    	}
    }
    
    void calcForce() {
    	for (int i=0; i<numNeighbors; i++) {
    		Neighbor n = neighbors.get(i);
    		n.calcForce();
    	}
    }
    
    void initParticles() {
    	int count = 0;
    	int increment = 5;
    	int startX = 5;
    	int startY = 450;
    	int sideLength = (int) Math.sqrt(numLiquidParticles);
    	for (int i=startX; i<(startX + (sideLength*increment)); i+=increment) {
    		for (int j=startY; j<(startY + (sideLength*increment)); j+=increment) {
	    		Particle p = new Particle();
	    		p.x = i;
	    		p.y = j;
	    		particles[count] = p;
	    		count++;
    		}
    	}
    	for (int i=0; i<(numSolidParticles*increment); i+=increment) {
    		Particle p = new Particle();
    		p.x = screenSize/2;
    		p.y = i;
    		particles[count] = p;
    		count++;
    	}
    }
    
    public void paint(Graphics g) {
    	super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;

	    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);

	    rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g.setColor(Color.LIGHT_GRAY);
	    g.fillRect(0, 0,screenSize, screenSize);

	    g2.setRenderingHints(rh);
	    for (int i=0; i< particles.length; i++) {
	    	Particle p = particles[i];
	    	double vel = Math.sqrt(p.fx*p.fx +p.fy*p.fy);
			g.setColor(new Color(clamp((int)(vel*50)), clamp((int)(p.pressure*20)), 255-clamp((int)(vel*100))));
			g.fillOval((int)(p.x-(particleSize/2)), (int)(p.y-(particleSize/2)), particleSize, particleSize);
		}
	    move();
    }
    
    int clamp(int p) {
    	if (p>255) return 255;
    	else if (p<0) return 0;
    	return p;
    }
    
    public void run() { 
		long startTime = System.currentTimeMillis();
		while (true) { 
			repaint();
			
		    try { 
		    	startTime += delay;
		        Thread.sleep(Math.max(0,
		                              startTime-System.currentTimeMillis()));
		    } catch (InterruptedException e) {
		        break;
		    }
		}
    }
    
    public void actionPerformed(ActionEvent e) {
	    repaint();
    }
    
    public static void main(String args[]) {
		NewSPH p = new NewSPH();
		JFrame frame = new JFrame("Smooth Particle Hydrodynamics");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(p);
	    frame.setSize((int)(screenSize), screenSize + 25);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	    
	    String input = "lipps";
		String char1 = "l";
		String char2 = "h";
		String letter = "abcdefghijklmnopqrstuvwxyz";
		String output = "";
	
		int shift = letter.indexOf(char1) - letter.indexOf(char2);
		
		for (String c:input.split("")) {
			int newN = (letter.indexOf(c) + shift) % (letter.length()-1);
			output += letter.substring(newN, newN+1);
		}
		
		System.out.print(output);
	}
    
}
