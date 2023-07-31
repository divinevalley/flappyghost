import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Obstacle extends Entity {
	private int type; // type de mvmt 1, 2, ou 3 
	private double initialY; // important pour obstacles sinus
	private double timeSinceTeleport=0; 
	private boolean depasse = false; // indique si cet obstacle a déjà été "dépassé" par le joueur
	private Image img;

	
	/** constructeur
	 * @param x
	 */
	public Obstacle(double x, double y, double vx) {
		super(x,y, vx);
		initialY = y;
		Random random = new Random();
		// rayons générés aléatoirement entre 10 et 45 pixels
		r = random.nextInt(10,46);
		type = random.nextInt(1,4); // type aleatoire 1,2, ou 3
		
		// img
		int imgAleatoire = random.nextInt(0,27); 
		img = new Image(System.getProperty("user.dir") + "/src/obstacles/" + imgAleatoire + ".png");
		
	}

	
	@Override
	public void update(double dt) {

		x += dt * vx;
		
		switch(type) {
		
		case 2: // sinus
			y = initialY + 50*Math.sin(0.04*x);
			break;
			
		case 3: // quantique: se téléportent d’une distance aléatoire comprise entre -30 et 30 pixels en x
			// et en y, périodiquement à chaque 0.2 seconde.
			timeSinceTeleport += dt; // compteur
			if (timeSinceTeleport >= 0.2) {
				timeSinceTeleport=0; //reinitialiser compteur, generer position 
				Random random = new Random();
				int distanceX = random.nextInt(-30,31);
				int distanceY = random.nextInt(-30,31);
				x += distanceX;
				y += distanceY;
			} 
			break;
			
		default: // type 1
//			y += dt * vy;
			break;
		}
	}



	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

	public boolean getDepasse() {
		return depasse;
	}


	public void setDepasse(boolean depasse) {
		this.depasse = depasse;
	}

	@Override
	public void draw(GraphicsContext context, boolean debug) {
		if (debug) {
			Color couleur = this.intersects ? Color.RED : Color.YELLOW; // couleur en fonction de son statut intersects
			context.setFill(couleur);
			context.fillOval(
					this.x - this.r,
					this.y - this.r,
					2 * this.r,
					2 * this.r);
			
		} else {
			context.drawImage(img, x-r, y-r, r*2, r*2);
		}
	}



}
