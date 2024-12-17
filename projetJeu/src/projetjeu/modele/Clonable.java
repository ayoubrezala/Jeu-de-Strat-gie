package projetjeu.modele;

import java.awt.image.BufferedImage;

/**
* Abstract class representing a grid object of the game.
*/
public abstract class Clonable {

    protected int x;
    protected int y;
    protected boolean visible = true;
    protected boolean walkable = true;
    protected BufferedImage imgRepr;

    /**
    * Class constructor.
    * @param x
    * Square ordinate.
    * @param y
    * Square abscissa.
    */
    public Clonable(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
    * Graphic version of Tile
    * Receive an image if used in a graphic interface.
    * @param x
    * @param y
    * @param img
    */
    public Clonable(int x, int y, BufferedImage img){
        this(x,y);
        this.imgRepr = img;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isWalkable() {
        return this.walkable;
    }
    
    public BufferedImage getImgRepr() {
        return imgRepr;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setImgRepr(BufferedImage img) {
        this.imgRepr = img;
    }

    public void setPosition(int x,int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
    * Returns the representation of a tile.
    * @return a character representing a tile.
    */
    @Override
    public abstract String toString();
}
