import javafx.scene.canvas.GraphicsContext;

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
   
   public abstract void draw(GraphicsContext context);
    
   public void testCollision(Entity other) {
       if (this.intersects(other)) {
           double vx = this.vx;
           double vy = this.vy;

           this.vx = other.vx;
           this.vy = other.vy;

           other.vx = vx;
           other.vy = vy;

           pushOut(other);
       }
   }
   
   /**
    * Indique s'il y a intersection entre les deux balles
    *
    * @param other
    * @return true s'il y a intersection
    */
   public boolean intersects(Entity obstacle) {
       double dx = this.x - obstacle.x;
       double dy = this.y - obstacle.y;
       double d2 = dx * dx + dy * dy;

       return d2 < (this.r + obstacle.r) * (this.r + obstacle.r);
   }
    

   /**
    * Déplace les deux balles en intersection pour retrouver un déplacement
    * minimal
    *
    * @param other
    */
   public void pushOut(Entity other) {
       // Calculer la quantité qui overlap en X, same en Y
       // Déplacer les deux de ces quantités/2
       double dx = other.x - this.x;
       double dy = other.y - this.y;
       double d2 = dx * dx + dy * dy;
       double d = Math.sqrt(d2);

       // Overlap en pixels
       double overlap = d - (this.r + other.r);

       // Direction dans laquelle se déplacer (normalisée)
       double directionX = dx / d;
       double directionY = dy / d;

       double deplacementX = directionX * overlap / 2;
       double deplacementY = directionY * overlap / 2;

       this.x += deplacementX;
       this.y += deplacementY;
       other.x -= deplacementX;
       other.y -= deplacementY;
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
