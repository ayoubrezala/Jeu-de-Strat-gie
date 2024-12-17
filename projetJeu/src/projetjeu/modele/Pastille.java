package projetjeu.modele;

import java.awt.image.BufferedImage;

/**
* Class that extends Clonable, represents a bonus.
*/

public class Pastille extends Clonable {

  //Valeur de la pastille
  private final int value = GameConfig.BONUS_VALUE;

    /**
    * Class constructor.
    * @param x
    * Square ordinate.
    * @param y
    * Square abscissa.
    */
    public Pastille(int x, int y) {
        super(x,y);
    }
    
    public Pastille(int x, int y, BufferedImage img){
        super(x,y,img);
    }

    public int getValue() {
        return this.value;
    }

    public void boost(Player p) {
      p.setBaseEnergy(p.getBaseEnergy()+1);
    }

    /**
    * Returns the representation of the objective.
    * @return A character representing the objective.
    */
    @Override
    public String toString() {
        return ".";
    }
}
