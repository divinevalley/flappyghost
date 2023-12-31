import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;

/**
 * Modele : fait calculs 
 */
public class Modele {
	// attributs 
	private ArrayList<Obstacle> tousObstacles = new ArrayList<>();
	private ArrayList<Obstacle> obstaclesASupprimer = new ArrayList<>();
	private HashSet<Obstacle> obstaclesDepasses = new HashSet<>(); // Set pour éviter doublons
	private int nbObstaclesRencontres = 0;
	private int score = 0;
	
	
	/**Creer un nouvel obstacle, l'ajouter à la liste d'obstacles à gérer.
	 * Augmenter la vitesse/gravité du jeu au besoin.   
	 * @param ghost
	 */
	public void creerNouvelObstacle(Ghost ghost) {

		//creer nouvel obstacle
		nbObstaclesRencontres++; //compter nb obstacles
		Random random = new Random();
		int positionYObstacle = random.nextInt(0, 400);
		Obstacle obstacle = new Obstacle(FlappyGhost.WIDTH, positionYObstacle, ghost.getVitesseX());
		tousObstacles.add(obstacle);
		
		// À chaque deux obstacles rencontrés, la gravité doit augmenter de 15 vers le bas
		// À chaque deux obstacles dépassés, la vitesse horizontale du joueur augmente de 15 pixels par seconde.
		if (nbObstaclesRencontres % 2 == 0) {
			ghost.setAy(ghost.getAy()+15);
			ghost.setVitesseX(ghost.getVitesseX()-15);
		}
	}
	
	/**
	 * Chaque fois que le joueur dépasse un obstacle, son score augmente de 5 points.
	 * calcul, retourner int
	 * @param ghost
	 * @return int score
	 */
	public int calculerScore(Ghost ghost) {
		for(Obstacle obstacle : tousObstacles) {
			if (ghost.depasse(obstacle)) {
				obstaclesDepasses.add(obstacle); 
				score = obstaclesDepasses.size() * 5;
			}
		}
		return score;
	}
	
	public void recommencerJeu() {
		tousObstacles.clear();
		obstaclesDepasses.clear();
		nbObstaclesRencontres = 0;
		score = 0; // pas vraiment besoin car calculer score sera appelé 
	}
	
	/**Boucler sur tous les obstacles et mettre à jour les positions et draw tous les obstacles  
	 * Supprimer les obstacles déjà passés (libérer mémoire)
	 * 
	 * @param ghost
	 * @param deltaTime
	 * @param gc (GraphicsContext)
	 * @param statut debug (pour informer la fonction draw())
	 * @return true si collision
	 */
	public boolean drawUpdateObstacles(Ghost ghost, double deltaTime, GraphicsContext gc, boolean debug) {
		boolean estCollision = false; 
		//supprimer anciens obstacles 
		for(Obstacle obstacle : tousObstacles) {
			// si obstacle depasse mur gauche, supprimer, liberer memoire
			if (obstacle.getX() + obstacle.getR() < 0) { 
				obstaclesASupprimer.add(obstacle);	// pour éviter erreur, garder en mémoire les obst à supprimer...
			}
			
			// partie draw/update: 
			obstacle.setVx(ghost.getVitesseX()); // màj vitesse "apparente" des obstacles selon la vitesse du ghost 
			obstacle.update(deltaTime); // update position
			if (obstacle.testCollision(ghost)) {
				estCollision = true; //collision
			}
			
			obstacle.draw(gc, debug);
		}
		//ensuite les supprimer
		tousObstacles.removeAll(obstaclesASupprimer);
		return estCollision;
	}
	



}
