package projetjeu.modele;

/**
* Class that extends Clonable, represents a mine.
*/
public class Mine extends Clonable implements Weapon {

    protected Player owner;
    protected int damage = GameConfig.MINE_DAMAGE;

    /**
    * Class constructor.
    * @param owner
    */
    public Mine(Player owner) {
        super(-1,-1);
        this.owner = owner;
        this.visible = (GameConfig.MINE_VISIBILITY == 1);
    }

    public Mine(Player owner, int x, int y) {
        super(x,y);
        this.owner = owner;
        this.visible = (GameConfig.MINE_VISIBILITY == 1);
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public void fire(Grid g, Direction d) {
        throw new UnsupportedOperationException("Non supporter.");
    }

    @Override
    public void explode(PrincipalGrid g, Player p) {
        p.takeDamage(this.damage);
        g.setTileAt(new FreeCase(this.x,this.y));
    }

    /**
    * hashCode() Override.
    * Necessary for the well functioning of equals' Override.
    * @return The object's hashcode.
    */
    @Override
    public int hashCode() {
        int code=11;
        code+=37*code+this.owner.getX();
        code+=37*code+this.owner.getY();
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
        if (!(o instanceof Mine)) {
            return false;
        }
        Mine m = (Mine)o;
        return this.owner.equals(m.getOwner());
    }

    /**
    * Returns the representation of the objective.
    * @return A character representing the objective.
    */
    @Override
    public String toString() {
        return ";";
    }
}
