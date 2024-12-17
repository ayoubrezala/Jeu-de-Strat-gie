package projetjeu.modele;

import java.awt.image.BufferedImage;

/**
* Class that extends Clonable, represents an empty tile.
*/
public class FreeCase extends Clonable {

    /**
    * Class constructor.
    * @param x
    * Square ordinate.
    * @param y
    * Square abscissa.
    */
    public FreeCase(int x, int y){
        super(x,y);
    }

    public FreeCase(int x, int y, BufferedImage img){
        super(x,y,img);
    }

    /**
    * Returns the representation of an empty tile.
    * @return a character representing an empty tile.
    */

    @Override
    public String toString() {
        return "_";
    }

    public String printCoords() {
        return "(" + this.x + "," + this.y + ")";
    }
}
