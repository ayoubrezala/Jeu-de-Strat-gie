package projetjeu.modele;

public interface Grid {
    public Clonable getTileAt(int x, int y);

    public void setTileAt(Clonable t);

    @Override
    public String toString();
}
