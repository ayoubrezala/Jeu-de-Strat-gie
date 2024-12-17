package projetjeu.modele;

public interface Weapon {

    public void fire(Grid g, Direction d);

    public void explode(PrincipalGrid g, Player p);

    public Player getOwner();
}
