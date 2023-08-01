
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

// demandé à Modèle de faire les calculs, demande à Vue de màj infos (selon Modèle)
public class Controleur {
	private Modele modele;
	private FlappyGhost vue;
	
	private boolean pause = false;
	private boolean debug = false;

	
	/** constructeur
	 * @param modele
	 * @param vue
	 */
	public Controleur(FlappyGhost vue) {
		this.modele = new Modele();
		this.vue = vue;
		// TODO initialiser affichage (de score?) 
		
	}
	
	public void nouvelObstacle(Ghost ghost) {
		modele.creerNouvelObstacle(ghost); // demande a Modele de creer obstacle
	}
	
	// Chaque fois que le joueur dépasse un obst, son score augmente de 5 points.
	// maj affichage score 
	public void calculerEtAfficherScore(Ghost ghost, Text scoreText) {
		int score = modele.calculerScore(ghost, scoreText);
		// update scoretext
		vue.scoreText.setText("Score: " + score);
	}
	
	public void drawUpdateObstacles(Ghost ghost, double deltaTime) {
		boolean collision = modele.drawUpdateObstacles(ghost, deltaTime, vue.gc, debug); // demande à modèle de supprimer anciens obstacles
		if (collision) {
			this.recommencerJeu();
		}
	}
	
	public void drawUpdateGhost(Ghost ghost, double deltaTime) {
		ghost.update(deltaTime);
		ghost.draw(vue.gc, debug);
	}
	
	public void recommencerJeu() {
		if(!debug) { // si on joue pour de vrai, recommencer
			modele.recommencerJeu();	
			vue.instancierGhost();
		}

	}

	
	/**gere actions keypress: space fait sauter le fantome, Esc fait terminer le programme
	 * @param KeyEvent
	 * @param ghost
	 */
	public void gererKeyPress(KeyEvent event, Ghost ghost) {
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
		if(vue.checkbox.isSelected()) {
			debug = true;
		} else {
			debug = false;
		}
		vue.requestFocus();
	}
	
	/**
	 * gere action bouton pause
	 */
	public void gererBoutonPause() {
		pause = !pause;
		System.out.println("pause: " + pause);
		vue.requestFocus();
	}


	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	
	
}
