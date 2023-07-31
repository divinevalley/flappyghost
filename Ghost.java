import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ghost extends Entity {
	private int vitesseX;  
	// NB: c'est la vitesse de l'image de fond. vx du ghost represente la vitesse du vrai x sur l'ecran (vx=0 car ne bouge pas sur le plan droite/gauche)
	
	private double vy;
    private double ay;// Gravité
    
    /*Un saut change instantanément la vitesse en y du fantôme à 300 vers le haut*/
    public void sauter() {
    	vy = -300;
    }
    
	/** constructeur
	 * @param x
	 * @param y
	 */
	public Ghost(double x, double y) {
		super(x,y);
		r = 30;
		vx = 0; //vx=0 car ne bouge pas sur le plan droite/gauche
		vy= 190; // 190 dans balle 
		ay=500; // gravite, vers le bas
		vitesseX = -120; // Le fantôme se déplace vers la droite à une vitesse constante initiale de 120 pixels par seconde. 
		
		/*La gravité (une accélération en y seulement) est initialement de 500 px/s^2
     * vers le bas*/
		
	}
	
	@Override
	public void draw(GraphicsContext context) {
		Color couleur = this.intersects ? Color.RED : Color.BLACK;
		context.setFill(couleur);
		
		context.fillOval(
	               (this.x - this.r),
	               this.y - this.r,
	               2 * this.r,
	               2 * this.r);
	}
	

    /**
     * Met à jour la position et la vitesse du ghost
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

	       vy += dt * ay;
	       x += dt * vx;
	       y += dt * vy;
      
        if (x + r > FlappyGhost.WIDTH || x - r < 0) { // TODO remove (va jamais toucher murs cotes) 
            vx *= -0.5; // apres mur, vitesse diminue 
        }
        if (y + r > FlappyGhost.HEIGHT || y - r < 0) { // si touche plafond/sol
            vy *= -0.5;
            vy = plafonnerVy(vy);
        }

        x = Math.min(x, FlappyGhost.WIDTH - r); // corriger glitch travserse mur
        x = Math.max(x, r);

        y = Math.min(y, FlappyGhost.HEIGHT - r);
        y = Math.max(y, r);

    }
    
    /*La vitesse en y ne doit jamais dépasser 300 vers le haut ou vers le bas. Si jamais la vitesse
    dépasse les 300, on la force à rester à une magnitude de 300 (en considérant sa direction haut/bas)*/
    /**
     * @param vy
     * @return
     */
    public static double plafonnerVy(double vy) {
    	if (vy>300) {
    		return 300;
    	} else if (vy<-300) {
    		return -300;
    	} else {
    		return vy;
    	}
    }
    
    /*Chaque fois que le joueur dépasse horizontalement un obstacle (autrement dit, lorsque son extrémité
     * gauche dépasse l’extrémité droite de l’obstacle), son score augmente de 5 points.*/
    /**
     * @param obstacle
     * @return true si le joueur depasse l'obstacle
     */
    public boolean depasse(Obstacle obstacle) {
    	double extremiteGaucheGhost = this.x - this.r;
//    	System.out.println("extremiteGaucheGhost:"+extremiteGaucheGhost);
    	double extremiteDroiteObst = obstacle.x + obstacle.r;
//    	System.out.println("extremiteDroiteObst:"+extremiteDroiteObst);
//    	System.out.println((extremiteDroiteObst<extremiteGaucheGhost));
    	return (extremiteDroiteObst<extremiteGaucheGhost);
    }

	public int getVitesseX() {
		return vitesseX;
	}

	public void setVitesseX(int vitesseX) {
		this.vitesseX = vitesseX;
	}
    
	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}
    
    
	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}


}
