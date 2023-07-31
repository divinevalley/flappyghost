import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {

	protected boolean intersects; // pour indiquer si en collision ou pas, ce qui va impacter sa couleur
    protected double x, y;
    protected double r;
    protected double vx;
    

    
    public Entity(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
    
    public Entity(double x, double y, double vx) {
    	this.x = x;
    	this.y = y;
    	this.vx = vx;
    }
    
	
	 /**
    * Met à jour la position et la vitesse de l'objet
    *
    * @param dt Temps écoulé depuis le dernier update() en secondes
    */
   public abstract void update(double dt);
   
   public abstract void draw(GraphicsContext context);
    
   public void testCollision(Entity other) {
       if (this.intersects(other)) {
    	   intersects = true;
    	   other.intersects = true; 

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

	
}
