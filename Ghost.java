

public class Ghost {

    private double x, y;
    private final double r = 30;
    private double vx = 0, vy = 190;
    
    // Gravité
    private double ax = 0, ay = 500;
    
  //---------------------------------------------------------------
    /* La gravité (une accélération en y seulement) est initialement de 500 2
    vers le bas
    • À chaque deux obstacles rencontrés, la gravité doit augmenter de 15 vers le bas */


 
    
    /*La vitesse en y ne doit jamais dépasser 300 vers le haut ou vers le bas. Si jamais la vitesse
dépasse les 300, on la force à rester à une magnitude de 300 (en considérant sa direction haut/bas)*/
    
    //---------------------------------------------------------------
    
    
    /*Un saut change instantanément la vitesse en y du fantôme à 300 vers le haut*/
    public void sauter() {
//    	y-=30;
//    	System.out.println(vy);
    	vy -= 300;
    	System.out.println(vy);
    }
    

	/** constructeur
	 * @param x
	 * @param y
	 */
	public Ghost(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	

    /**
     * Met à jour la position et la vitesse du ghost
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

        vx += dt * ax;
        vy += dt * ay;
        
        x += dt * vx;
        y += dt * vy;
        
        if (y + r > FlappyGhost.HEIGHT || y - r < 0) { // si depasse haut, rebondir
            vy *= -0.5;
        } else if (y + r > FlappyGhost.HEIGHT*2/3 ) { // si descend trop bas
        	vy *= -0.1;
        }

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
