import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Ghost extends Entity {
	private int vitesseX;  
	// NB: c'est la vitesse de l'image de fond. alors que vx represente la vitesse du vrai x sur l'ecran 
	// (vx=0 car ne bouge pas sur le plan droite/gauche)

	private double vy;
	private double ay; // Gravité
	
	private Image img = new Image(System.getProperty("user.dir") + "/src/ghost.png");

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
		vx = 0; //vx=0 car ne bouge pas sur le plan droite/gauche (sur l'ecran)
		vy= 190; // 190 dans balle 
		ay=500; // gravite, vers le bas
		vitesseX = -120; // c'est l'image de fond qui se déplace : 
		// énoncé : Le fantôme se déplace vers la droite à une vitesse constante initiale de 120 pixels par seconde. 

		/*La gravité (une accélération en y seulement) est initialement de 500 px/s^2
		 * vers le bas*/
	}

	/**
	 * affiche le fantôme en fonction du statut debug et statut collision (en mode debug,
	 * ronds coloriés noir/rouge selon statut collision, et image si non debug) 
	 */
	@Override
	public void draw(GraphicsContext context, boolean debug) {
		if (debug) {
		Color couleur = this.intersects ? Color.RED : Color.BLACK;
		context.setFill(couleur);
		context.fillOval(
				(x - r),
				y - r,
				2 * r,
				2 * r);
		} else {
			context.drawImage(img, x-r, y-r, 2*r, 2*r); // (img, position x, position y, w, h)
		}

	}


	/**
	 * Met à jour la position et la vitesse du ghost
	 *
	 * @param dt deltatime (Temps écoulé depuis le dernier update() en secondes)
	 */
	public void update(double dt) {

		vy += dt * ay;
		x += dt * vx;
		y += dt * vy;

		if (x + r > FlappyGhost.WIDTH || x - r < 0) { // TODO remove (va jamais toucher murs cotes) 
			vx *= -0.5; // apres mur, vitesse diminue 
		}
		if (y + r > FlappyGhost.HEIGHTCANVAS || y - r < 0) { // si touche plafond/sol
			vy *= -0.5;
			vy = plafonnerVy(vy);
		}

		x = Math.min(x, FlappyGhost.WIDTH - r); // corriger glitch travserse mur
		x = Math.max(x, r);

		y = Math.min(y, FlappyGhost.HEIGHTCANVAS - r);
		y = Math.max(y, r);

	}

	/**
	 * La vitesse en y ne doit jamais dépasser 300 vers le haut ou vers le bas. Si jamais la vitesse
    dépasse les 300, on la force à rester à une magnitude de 300 (en considérant sa direction haut/bas)
	 * @param vy
	 * @return vy ajusté
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

	/**
	 * permet de savoir si le joueur dépasse horizontalement un obstacle 
	 * (autrement dit, lorsque son extrémité gauche dépasse l’extrémité droite de l’obstacle)
	 * (Important car si oui, son score augmente de 5 points)
	 * 
	 * @param obstacle
	 * @return true si le joueur depasse l'obstacle
	 */
	public boolean depasse(Obstacle obstacle) {
		double extremiteGaucheGhost = this.x - this.r;
		double extremiteDroiteObst = obstacle.x + obstacle.r;
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
