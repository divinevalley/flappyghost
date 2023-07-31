import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {

	protected boolean intersects;
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
   
   public abstract void draw(GraphicsContext context);
    
   public void testCollision(Entity other) {
       if (this.intersects(other)) {
    	   intersects = true;
    	   other.intersects = true; 
    	   System.out.println("intersects");
    	   System.out.println("this.x: " + this.x  + " other.x: " + other.x);
    	   System.out.println("this.y: " + this.y  + " other.y: " + other.y);

       } else {
    	   this.intersects = false;
    	   other.intersects = false;
       }
   }
   
   /**
    * Indique s'il y a intersection entre les deux balles
    *
    * @param other
    * @return true s'il y a intersection
    */
   public boolean intersects(Entity other) {
       double dx = this.x - other.x;
       double dy = this.y - other.y;
       double d2 = dx * dx + dy * dy;

       // d^2 < r^2 
       return d2 < (this.r + other.r) * (this.r + other.r);
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
