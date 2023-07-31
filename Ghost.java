import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ghost extends Entity {
	private int vitesseX;  
	// NB: c'est la vitesse de l'image de fond. vx du ghost represente la vitesse du vrai x sur l'ecran (0 car ne bouge pas sur le plan droite/gauche)
	
    
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
		vx = 0; // 500 dans balle
		vy= 190; // 190 dans balle 
		ax=0;
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

        super.update(dt);
        
//        if (y + r > FlappyGhost.HEIGHT || y - r < 0) { // si depasse haut, rebondir
//            vy *= -0.5;
//        } else if (y + r > FlappyGhost.HEIGHT*2/3 ) { // si descend trop bas
//        	vy *= -0.1;
//        }
        
        
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
    public static double plafonnerVy(double vy) {
    	if (vy>300) {
    		return 300;
    	} else if (vy<-300) {
    		return -300;
    	} else {
    		return vy;
    	}
    }

	public int getVitesseX() {
		return vitesseX;
	}

	public void setVitesseX(int vitesseX) {
		this.vitesseX = vitesseX;
	}
    
    
    
    
	
}
