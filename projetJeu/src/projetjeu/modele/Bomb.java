package projetjeu.modele;

/**
* Class that extends Clonable, represents a bomb.
*/
public class Bomb extends Mine {
    //Configure les valeur par defaut en utilisans le fichier de configuration qu'on a creer
    private int delay = GameConfig.BOMB_DELAY; //Nombre de tour avant explosion de la bombe
    private int range = GameConfig.BOMB_RANGE;
    
    /**
    * Class constructor.
    * @param owner
    */
    public Bomb(Player owner) {
        super(owner);
        this.damage=GameConfig.BOMB_DAMAGE;
        this.visible=(GameConfig.BOMB_VISIBILITY == 1);
    }

    public Bomb(Player owner, int x, int y) {
        super(owner,x,y);
        this.damage=GameConfig.BOMB_DAMAGE;
        this.visible=(GameConfig.BOMB_VISIBILITY == 1);

    }
     
    //Methode qui permet de decrementer la bombe
    public void tick() {
        this.delay--;
    }

    @Override
    public void explode(PrincipalGrid g, Player p) {
        p.takeDamage(this.damage);
        g.setTileAt(new FreeCase(this.x,this.y));
        g.removeBomb(this);
    }

    public void explode(PrincipalGrid g) {
        if (this.delay==0) {
            for (Clonable t : g.getNeighbouringTiles(this, this.range)) {
                if (!(t.isWalkable())) {
                  try {
                    ((Player)t).takeDamage(this.damage);
                  } catch (ClassCastException not_a_player) {}
                }
            }
            g.setTileAt(new FreeCase(this.x,this.y));
            g.removeBomb(this);
        }
    }

    /**
    * hashCode() Override.
    * Necessary for the well functioning of equals' Override.
    * @return The object's hashcode.
    */
    @Override
    public int hashCode() {
        int code=11;
        code+=77*code+this.owner.getX();
        code+=77*code+this.owner.getY();
        return code;
    }

    /**
    * equals Override.
    * Checks the equality of the coordinates.
    * @param o
    * The object to compare to a node.
    * @return Equality test result.
    */
    @Override
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        if (!(o instanceof Bomb)) {
            return false;
        }
        Bomb b = (Bomb)o;
        return this.owner.equals(b.getOwner());
    }

    /**
    * Returns the representation of the objective.
    * @return A character representing the objective.
    */
    @Override
    public String toString() {
        return ""+this.delay;
    }

    public int getDelay() {
        return delay;
    }
}
