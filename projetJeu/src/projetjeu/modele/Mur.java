package projetjeu.modele;

import java.awt.image.BufferedImage;

/**
* Class that extends Clonable, represents a wall.
*/
public class Mur extends Clonable {
    /**
    * Class constructor.
    * @param x
    * Square ordinate.
    * @param y
    * Square abscissa.
    */
    public Mur(int x, int y) {
        super(x,y);
        this.walkable = false;
    }

    public Mur(int x, int y, BufferedImage img){
        super(x,y,img);
        this.walkable = false;
    }

    /**
    * Returns the representation of a wall.
    * @return a character representing a wall.
    */
    @Override
    public String toString() {
        return "#";
    }
}
