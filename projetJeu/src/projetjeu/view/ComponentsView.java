package projetjeu.view;

import projetjeu.controller.GameContoller;
import projetjeu.view.listeners.ModelListener;
import projetjeu.modele.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JPanel;

public class ComponentsView extends JPanel implements ModelListener{
    private Clonable[] entities;
    private final GameContoller game;
    private final Dimension sizeImg;
    private boolean startAnim = true;
    private final Player player;
    private final GameView observer;

    public ComponentsView(Clonable[] entities, GameContoller game, Player p, GameView observer) {
        this.player = p;
        this.entities = entities;
        this.game = game;
        this.sizeImg = new Dimension(35,43);
        this.observer = observer;
        game.addListener(this);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < game.getTileMap().size(); i++){
            ArrayList<Clonable> list = game.getTileMap().get(i);
            try{
                for(Clonable t : list){
                    int x = 64 * t.getX();
                    int y = 64 * t.getY();
                    if(player.getView().playerCanSee(t)){
                        if(t instanceof Pastille){
                            if(player.getX() == t.getX() && player.getY() == t.getY()){
                                ((Pastille) t).boost(player);
                                try{
                                    game.getTileMap().get(i).remove(t);
                                }catch(java.util.ConcurrentModificationException e){

                                }
                            }
                            g.drawImage(t.getImgRepr(), x+15 , y+15, this);
                        }else{
                            g.drawImage(t.getImgRepr(), x , y, this);
                        }

                    }
                }
            }catch(java.util.ConcurrentModificationException e){

            }
        }

        if(player.getLife() > 0){
                BufferedImage img = player.getImgRepr();

                ArrayList<ArrayList<Integer>> viewableTiles = player.visibleTiles();
                ArrayList<ArrayList<Integer>> visiblesTiles = player.visiblesTiles();

		for(int i = 0; i < game.getTileMap().size(); i++){
                    ArrayList<Clonable> list = game.getTileMap().get(i);
                    for(Clonable t : list){
                        boolean playerCanSee = player.getView().playerCanSee(t);
                        int xx = t.getX();
                        int yy = t.getY();
                        ArrayList<Integer> tmp = new ArrayList<Integer>(); tmp.add(0,t.getX()); tmp.add(1,t.getY());
                        boolean isInViewable = (viewableTiles.contains(tmp));
                        boolean isInVisibles = (visiblesTiles.contains(tmp));
                        if(playerCanSee && ( isInViewable || isInVisibles ) ){
                            g.drawImage(t.getImgRepr(), xx*64 , yy*64, this);
                        }else{
                            g.drawImage(ImagesAdministrator.fog, xx*64, yy*64, this);
                        }
                    }
                }

                Set<Player> players = game.getListPlayers().keySet();
                for(Player p: players){
                    if(p.getName() != player.getName()){
                        if( (p.getLife()>0) ){
                            ArrayList<Integer> tmp = new ArrayList<Integer>(); tmp.add(0,p.getX()); tmp.add(1,p.getY());
                            boolean playerCanSee = player.getView().playerCanSee(p);
                            boolean isInViewable = (viewableTiles.contains(tmp));
                            boolean isInVisibles = (visiblesTiles.contains(tmp));
                            if(playerCanSee || ( isInViewable || isInVisibles ) ){
                                displayPlayer(g,p);
                            }
                        }
                    }
                }

                int baseX = (int)(( 64 - sizeImg.getWidth())/2);
                int baseY = (int)(( 64 - sizeImg.getHeight())/2);

                int x = 64 * player.getX() + baseX;
                int y = 64 * player.getY() + baseY;
                displayPlayer(g, player);

                //Draw shield
                if(player.isShield_up()){
                    g.drawImage(ImagesAdministrator.shield, player.getX()*64, player.getY()*64, this);
                }

                drawActionPoint(player,g);
                if(player.isPlanting()){
                    displayPlantPoints(g,player);
                }

                ArrayList<Direction> possibleMoves = player.possibleMoves();

                if(player.getEnergy() == 0 || !player.getAsTurn()){
                    g.setColor(new Color(255,0,0));
                    g.drawRect(64 * (player.getX()), 64 * (player.getY()), 64, 64);
                }

                if(player.isSelected()){
                    if(player.getEnergy() != 0){
                        g.setColor(new Color(0,255,0));
                        for(Direction d : possibleMoves){
                            g.fillOval(64 * (player.getX()+d.x()) + 17, 64* (player.getY()+d.y()) +17 , 30, 30);
                        }
                    }
                }
            }

        displayBomb(g);
        Grid grid = game.getGrid();


    if(observer.playerTour.getName() != null || player.getName() != null){
        observer.setTitle("Jeu de combat ("+player.getName()+"). Tour de: "+observer.playerTour.getName());
    }

    if(game.getGrid().gameIsOver() && player.getLife() > 0){
        g.setColor(Color.GREEN);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("VICTOIRE", 300, 416);
    }else if(game.getGrid().gameIsOver() && player.getLife() <= 0){
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("DEFAITE", 300, 416);
    }
}

    public void displayPlantPoints(Graphics g, Player p){
        for(int i = p.getX()-1;  i <= p.getX()+1; i++){
            for(int j = p.getY()-1 ; j <= p.getY()+1; j++){
                if(!((i == p.getX()) && j==p.getY()) && game.getGrid().getTileAt(i, j) instanceof FreeCase){
                    g.setColor(Color.RED);
                    g.fillOval(i*64 +17, j*64+17, 30, 30);
                }
            }
        }
    }

    public void displayBomb(Graphics g){
        for(Clonable t : player.getView().getModel().getGrid()){
            if(t instanceof Bomb){
                if(((Bomb) t).getOwner() == player){
                    g.setColor(Color.GREEN);
                    g.drawString(Integer.toString(((Bomb) t).getDelay()), t.getX()*64 +28, t.getY()*64 + 20);
                    g.drawImage(ImagesAdministrator.bomb, t.getX()*64 +12 , t.getY()*64 +12, this);
                }
            }else if(t instanceof Mine){
                if(((Mine) t).getOwner() == player){
                    g.drawImage(ImagesAdministrator.mine, t.getX()*64 +12 , t.getY()*64 +12, this);
                }
            }
        }
    }

    public void displayPlayer(Graphics g, Player p){

        BufferedImage img = p.getImgRepr();

        int baseX = (int)(( 64 - sizeImg.getWidth())/2);
        int baseY = (int)(( 64 - sizeImg.getHeight())/2);

        int x = 64 * p.getX() + baseX;
        int y = 64 * p.getY() + baseY;



        // Rotation information
        if(p.lastMove == Direction.z){
            g.drawImage(ImagesAdministrator.lookRight(img), x, y, this);
        }else if(p.lastMove == Direction.s){
            g.drawImage(ImagesAdministrator.lookRight(img), x, y, this);
        }else if(p.lastMove == Direction.q){
            g.drawImage(ImagesAdministrator.lookLeft(img), x, y, this);
        }else if(p.lastMove == Direction.d){
            g.drawImage(ImagesAdministrator.lookRight(img), x, y, this);
        }
        if(!p.getAsTurn()){
            g.setColor(new Color(255,0,0));
            g.drawRect(p.getX()*64, p.getY()*64, 64, 64);
        }
    }

    public void drawActionPoint(Player p, Graphics g){
        int nbAction = p.getEnergy();

        int x = 64 * (p.getX());
        int y = 64 * (p.getY()+1);

        g.setColor(new Color(0,255,255));
        g.setFont(new Font("default", Font.BOLD, 14));
        g.drawString(Integer.toString(nbAction), x+10, y-10);
    }

    public void setEntities(Clonable[] l){
        this.entities = l;
    }

    @Override
    public void update(Object source) {
        this.repaint();
    }
}
