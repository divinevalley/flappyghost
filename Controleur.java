import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Contrôleur demande à Modèle de faire les calculs, 
 * demande à Vue de mettre à jour des infos pour l'affichage (selon calculs du Modèle)
 */
public class Controleur extends AnimationTimer {
	private Modele modele;
	private FlappyGhost vue;
	
	private Ghost ghost;
	
	// parametres bouton/checkbox
	private boolean pause = false;
	private boolean debug = false;

	// pour Animation Timer : 
	private long lastTimeObstacle = 0;
	private double positionXImageBg = 0;
	private final double frameRate = 1e-9;
	private long lastTime = 0;
	
	// paramètres pause
	private boolean reprise = false;
	private long lastPause = 0;
	
	
	/** constructeur
	 * @param modele
	 * @param vue
	 */
	public Controleur(FlappyGhost vue) {
		this.modele = new Modele();
		this.vue = vue;
	}
	
	/**
	 * Handler pour AnimationTimer : créer l'animation
	 */
	@Override
	public void handle(long now) {
		if (lastTime == 0) {
			lastTime = now;
			return;
		}

		if (!pause) { // rouler seulement si pas en pause
			if(reprise) { // si reprise, ajuster selon duree de pause
				double dureeDePause = (now - lastPause); 
				lastTime += dureeDePause; // avancer lastTime de duree de pause
				lastTimeObstacle += dureeDePause;
				reprise = false;
			}
			
			double deltaTime = (now - lastTime) * frameRate;
			double deltaTimeObstacle = (now - lastTimeObstacle) * frameRate;
			
			// draw bg. à noter : c'est l'image bg qui bouge à la vitesse du ghost
			positionXImageBg = (positionXImageBg + deltaTime * ghost.getVitesseX()) % FlappyGhost.WIDTH;
			vue.drawBg(positionXImageBg);

			// ghost
			drawUpdateGhost(deltaTime);

			// toutes les 3 secondes, nouvel obstacle
			if (deltaTimeObstacle>=3) {
				nouvelObstacle(ghost);
				lastTimeObstacle = now; // reinitialiser compteur 3 sec
			}

			// Chaque fois que le joueur dépasse un obst, son score augmente de 5 points.
			calculerEtAfficherScore();
			
			// draw obstacles, supprimer anciens obstacles passés 
			drawUpdateObstacles(deltaTime);
			
			//update time
			lastTime = now;
			lastPause = now;
		} 
	}
	

	/**
	 * demande a Modele de creer obstacle
	 * @param ghost
	 */
	public void nouvelObstacle(Ghost ghost) {
		modele.creerNouvelObstacle(ghost);
	}
	
	
	/**
	 * Chaque fois que le joueur dépasse un obstacle, son score augmente de 5 points.
	 * mettre a jour l'affichage score 
	 */
	public void calculerEtAfficherScore() {
		int score = modele.calculerScore(ghost);
		// update scoretext
		vue.initialiserScoreText(score);		
	}
	
	/**Demande à Modèle de draw tous les obstacles, supprimer les anciens, et detecter collisions 
	 * @param deltaTime
	 */
	public void drawUpdateObstacles(double deltaTime) {
		
		boolean collision = modele.drawUpdateObstacles(ghost, deltaTime, vue.getGc(), debug); 
		if (collision) {
			this.recommencerJeu();
		}
	}
	
	/**
	 * Mettre à jour la position du ghost, draw ghost
	 * @param deltaTime
	 */
	public void drawUpdateGhost(double deltaTime) {
		ghost.update(deltaTime);
		ghost.draw(vue.getGc(), debug);
	}
	
	/**
	 * Recommencer jeu, réinitialiser tous les parametres dans modèle et objet ghost
	 */
	public void recommencerJeu() {
		if(!debug) { // si on joue pour de vrai, recommencer
			modele.recommencerJeu();	
			instancierGhost();
		}
	}
	
	/** 
	 * recommence le jeu en réinitialisant ghost et ses attributs
	 */
	public void instancierGhost() { 
		ghost = new Ghost(FlappyGhost.WIDTH/2, FlappyGhost.HEIGHTCANVAS/2); 
		// position au centre
	}

	
	/**gere actions keypress: space fait sauter le fantome, Esc fait terminer le programme
	 * @param KeyEvent
	 * @param ghost
	 */
	public void gererKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.SPACE) {
			ghost.sauter();
		}

		if (event.getCode() == KeyCode.ESCAPE) {
			Platform.exit(); //ESC => exit
		}
	}
	
	/**
	 * gere action checkbox (activer/desactiver mode debug)
	 */
	public void gererCheckbox() {
		if(vue.getCheckbox().isSelected()) {
			debug = true;
		} else {
			debug = false;
		}
		vue.requestFocus(); // pour que "espace" fonctionne sur canvas
	}
	
	/**
	 * gere action bouton pause (modifie les attributs pause et reprise, 
	 * pour que l'AnimationTimer sache où/comment reprendre)
	 */
	public void gererBoutonPause() {
		pause = !pause;
		if (!pause) {
			reprise = true;
		}
		vue.requestFocus();// pour que "espace" fonctionne sur canvas
	}
	
	
	
}
