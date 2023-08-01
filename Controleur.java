
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// demandé à Modèle de faire les calculs, demande à Vue de màj infos (selon Modèle)
public class Controleur {
	private Modele modele;
	private FlappyGhost vue;
	
	private boolean pause = false;
	private boolean debug = false;
	
	// pause
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
	
	public void nouvelObstacle(Ghost ghost) {
		modele.creerNouvelObstacle(ghost); // demande a Modele de creer obstacle
	}
	
	// Chaque fois que le joueur dépasse un obst, son score augmente de 5 points.
	// maj affichage score 
	public void calculerEtAfficherScore(Text scoreText) {
		int score = modele.calculerScore(vue.getGhost(), scoreText);
		// update scoretext
		vue.getScoreText().setText("Score: " + score);
		if(score>0) {
			vue.getScoreText().setFill(Color.BLACK);			
		}

	}
	
	public void drawUpdateObstacles(double deltaTime) {
		// demande à modèle de supprimer anciens obstacles, et detecter collisions
		boolean collision = modele.drawUpdateObstacles(vue.getGhost(), deltaTime, vue.getGc(), debug); 
		if (collision) {
			this.recommencerJeu();
		}
	}
	
	public void drawUpdateGhost(double deltaTime) {
		vue.getGhost().update(deltaTime);
		vue.getGhost().draw(vue.getGc(), debug);
	}
	
	public void recommencerJeu() {
		if(!debug) { // si on joue pour de vrai, recommencer
			modele.recommencerJeu();	
			vue.instancierGhost();
			vue.getScoreText().setFill(Color.RED);
//			vue.getScoreText().setFont(Font.font(null, FontWeight.BOLD, 20));
			System.out.println("red");
		}

	}

	
	/**gere actions keypress: space fait sauter le fantome, Esc fait terminer le programme
	 * @param KeyEvent
	 * @param ghost
	 */
	public void gererKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.SPACE) {
			vue.getGhost().sauter();
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
		vue.requestFocus();
	}
	
	/**
	 * gere action bouton pause
	 */
	public void gererBoutonPause(long lastPause) {
		pause = !pause;
		
		if (pause) {
			this.lastPause = lastPause;
		} else {
			reprise = true;
		}
		
		vue.requestFocus();
	}
	
	public void mettrePause(long now) {
		lastPause = now;
	}


	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	

	public boolean isReprise() {
		return reprise;
	}
	
	public void setReprise(boolean reprise) {
		this.reprise = reprise;
	}

	public long getLastPause() {
		return lastPause;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	
	
}
