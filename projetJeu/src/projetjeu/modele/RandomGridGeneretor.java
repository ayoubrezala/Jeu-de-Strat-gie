package projetjeu.modele;

import projetjeu.patterns.GridStrategy;
import java.util.*;

public class RandomGridGeneretor implements GridStrategy {

  private final PrincipalGrid client;

  public RandomGridGeneretor(PrincipalGrid client) {
        this.client = client;
  }

    @Override
    public void generate() {
        Clonable[] random_grid=client.getGrid();
        Random r = new Random();
        for (int i=0 ; i<random_grid.length ; i++) {
            Clonable n = new FreeCase(i%client.getWidth(),i/client.getWidth());
            double nr = r.nextDouble();
            if (nr < 0.2) {
                n = new Pastille(i%client.getWidth(),i/client.getWidth());
            } else if (nr >= 0.2 && nr < 0.5) {
                n = new Mur(i%client.getWidth(),i/client.getWidth());
            }
            random_grid[i]=n;
        }

        for (Player p : client.getPlayers()) {
            int rx = r.nextInt(client.getWidth());
            int ry = r.nextInt(client.getHeight());
            p.setPosition(rx,ry);
            client.setTileAt(p);
            for (Clonable t : client.getNeighbouringTiles(p,1)) {
                if (!(t instanceof Player)) {
                    client.setTileAt(new FreeCase(t.getX(),t.getY()));
                }
            }
        }
        client.setGrid(random_grid);
    }
}
