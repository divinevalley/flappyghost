
public abstract class Entity {


    protected double x, y;
    protected double r;
    protected double vx, vy;
    
    // Gravité
    protected double ax, ay;
    
    public Entity(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
    
	
	 /**
    * Met à jour la position et la vitesse de l'obstacle
    *
    * @param dt Temps écoulé depuis le dernier update() en secondes
    */
   public void update(double dt) {

       vx += dt * ax;
       vy += dt * ay;
       
       x += dt * vx;
       y += dt * vy;

   }
    
    

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getR() {
		return r;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}


    
	
}
