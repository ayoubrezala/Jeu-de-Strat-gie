package projetjeu.modele;

import projetjeu.patterns.PlayerFactory;
import projetjeu.patterns.GridStrategy;
import java.util.*;

/**
 * The game grid, where everything in the game takes place.
 */
public class PrincipalGrid implements Grid {

    private int turn_number = 0;
    private int width;
    private Clonable[] tiles;
    private Player[] players;
    private Queue<Player> ordering;
    private boolean random_order = (GameConfig.RANDOMIZED_PLAYER_ORDER==1);
    private ArrayList<Bomb> bombs;
    private GridStrategy generator;
    private Player playerToPlay;
    private boolean explode = false;

    /** Class constructor.
     * @param width
     * Grid width.
     * @param height
     * Grid height.
     * @param nb_players
     * Number of players in game.
    */
    public PrincipalGrid(int width, int height, int nb_players) {
        this.width = width;
        this.tiles = new Clonable[width*height];
        this.players = new Player[nb_players];
        this.ordering = new LinkedList<>();
        this.bombs = new ArrayList<>();
        this.generator = new RandomGridGeneretor(this);
    }

    public PrincipalGrid() {
        this(0,0,2);
    }

    /** Fills an empty Clonable array with Clonable nb_instances
 according to a generation strategy.*/
    public void createGrid() {
        generator.generate();
    }

    /** Checks if the game is over,
     * i.e. if only one player is alive.
     * @return the test result.
    */
    public boolean gameIsOver() {
        int cpt = 0;
        for (Player p : players) {
            if (p.getLife() > 0) {
                cpt++;
            }
        }
        return cpt==1;
    }

    /** Finds and prepares the next player to play his turn.
      * @return the next player.
      */
    public Player nextPlayer() {
        if (this.ordering==null || this.ordering.isEmpty()) {
            nextTurn();
        }
        Player p = this.ordering.poll();
        p.setEnergy(p.getBaseEnergy());
        p.disableShield();
        playerToPlay = p;
        p.setAsTurn(true);
        return p;
    }

    /** Prepares the next turn by making bombs tick
      * and refilling the player queue.
      */
    public void nextTurn() {
        ArrayList<Bomb> copy_bombs = new ArrayList<>(this.bombs);
        for (Bomb b : copy_bombs) {
            if(b.getDelay() == 1){
                this.explode = true;
            }
            b.tick();
            b.explode(this);
        }
        this.ordering = new LinkedList<>();
        this.fillPlayerQueue();
        if (this.random_order) {
            Collections.shuffle((List)this.ordering);
        }

        this.turn_number++;
    }

    public boolean checkDamage(){
        for(Player p : players){
            int current_life = p.getLife();
            int copy_life = p.getCopyLife();
            if(copy_life != current_life){
                p.setCopyLife(current_life);
                return true;
            }
        }
        return false;
    }

    public boolean hearExplosion(){
        return explode;
    }

    public void endExplosion(){
        this.explode = false;
    }

    /** 
     * Fills the player queue with all alive players.
     */
    public void fillPlayerQueue() {
      for (Player p : this.players) {
        if (p.getLife() > 0) {
            this.ordering.add(p);
        }
      }
    }

    /** Checks if the parameter coordinates are those of a grid tile.
      * @param x
      * X-axis coordinate to test.
      * @param y
      * Y-axis coordinate to test.
      * @return the test result.
    */
    public boolean isInBounds(int x, int y) {
        boolean check = false;
        if (x>=0 && x<this.width && y>=0 && y<this.tiles.length/this.width) {
            check = true;
        }
        return check;
    }

    /** Creates a list of every tile around the parameter tile
      * in the given range.
      * @param t
      * The center tile.
      * @param size
      * Size of the area to cover.
      * @return the list of all neighbouring tiles.
    */
    public ArrayList<Clonable> getNeighbouringTiles(Clonable t, int size) {
        ArrayList<Clonable> neighbours = new ArrayList<>();
        for (int i=-size ; i<=size ; i++) {
          for (int j=-size; j<=size; j++) {
            Clonable candidate = this.getTileAt(t.getX()+i,t.getY()+j);
            if (candidate!=null) {//gestion du résultat négatif de getTileAt
                neighbours.add(candidate);
            }
          }
        }
        neighbours.remove(this.getTileAt(t.getX(),t.getY()));//on ne garde pas la position du joueur lui-même
        return neighbours;
    }

    /** Filtering method listing only the free tiles
      * around the parameter tile, used to find planting sites.
      * @param t
      * The center tile.
      * @param size
      * Size of the area to cover.
      * @return the list of all neighbouring free tiles.
    */
    public ArrayList<FreeCase> getNeighbouringFreeTiles(Clonable t, int size) {
        ArrayList<FreeCase> valids = new ArrayList<>();
        for (Clonable d : getNeighbouringTiles(t,size)) {
            if (d instanceof FreeCase) {
                valids.add((FreeCase)d);
            }
        }
        return valids;
    }

    public Player getPlayerToPlay() {
      return playerToPlay;
    }

    @Override
    public Clonable getTileAt(int x, int y) {
        return isInBounds(x,y) ? this.tiles[x+(y*this.width)] : null;
    }

    @Override
    public void setTileAt(Clonable t) {
        this.tiles[t.getX()+(t.getY()*this.width)]=t;
    }

    public Queue<Player> getActivePlayers() {
      return this.ordering;
    }

    public int getTurnNumber() {
        return this.turn_number;
    }

    public Clonable[] getGrid() {
        return this.tiles;
    }

    public void setGrid(Clonable[] new_grid) {
        this.tiles = new_grid;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight(){
        return this.tiles.length/this.width;
    }


    public void setWidth(int new_width) {
        this.width = new_width;
    }

    /** Adds a player to the array of registered players.
      * @param p
      * The player to add.
    */
    public void addPlayer(Player p){
        this.players[PlayerFactory.nb_instances-1]=p;
        this.tiles[p.getX()+(p.getY()*this.width)]=p;
    }

    /** Adds a bomb to the list of active bombs.
      * @param b
      * The bomb to add.
    */
    public void addBomb(Bomb b){
        this.bombs.add(b);
    }

    /** Removes a bomb from the list of active bombs.
      * @param b
      * The bomb to remove.
    */
    public void removeBomb(Bomb b) {
        this.bombs.remove(b);
    }

    /** Provides a string representing the full grid without proxy filtering. Useful for debug purposes.
      * @return the string version of the grid.
    */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i=0 ; i<this.tiles.length ; i++) {
            if (i>0 && i%this.width == 0) {
                res.append("\n");
            }
            res.append(this.tiles[i]);
        }
        return res.toString();
    }
    
    public void initializePlayers() {
        fillPlayerQueue();
        nextPlayer(); // Définir le premier joueur
    }
}
