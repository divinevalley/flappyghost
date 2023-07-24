

public class Ghost extends Entity {


  //---------------------------------------------------------------
    /* 
     * Le fantôme se déplace vers la droite à une vitesse constante initiale de 120 pixels par seconde. 
     * 
     * La gravité (une accélération en y seulement) est initialement de 500 px/s^2
     * vers le bas
     * 
     * À chaque deux obstacles rencontrés, la gravité doit augmenter de 15 vers le bas
     *  
     * Un saut change instantanément la vitesse en y du fantôme à 300 vers le haut
    */

    /*La vitesse en y ne doit jamais dépasser 300 vers le haut ou vers le bas. Si jamais la vitesse
dépasse les 300, on la force à rester à une magnitude de 300 (en considérant sa direction haut/bas)*/
    
    //---------------------------------------------------------------
    
    
    /*Un saut change instantanément la vitesse en y du fantôme à 300 vers le haut*/
    public void sauter() {
    	vy -= 300;
    }
    

	/** constructeur
	 * @param x
	 * @param y
	 */
	public Ghost(double x, double y) {
		super(x,y);
		r = 30;
		vx = 0;
		vy=190;
		ax=0;
		ay=500;
		
		/*La gravité (une accélération en y seulement) est initialement de 500 px/s^2
     * vers le bas*/
		
	}
	
	

    /**
     * Met à jour la position et la vitesse du ghost
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

        super.update(dt);
        
        if (y + r > FlappyGhost.HEIGHT || y - r < 0) { // si depasse haut, rebondir
            vy *= -0.5;
        } else if (y + r > FlappyGhost.HEIGHT*2/3 ) { // si descend trop bas
        	vy *= -0.1;
        }

    }
    
    
	
}
