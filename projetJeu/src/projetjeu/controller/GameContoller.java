package projetjeu.controller;

import projetjeu.view.listeners.AbstractListenableModel;
import projetjeu.modele.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import projetjeu.view.ImagesAdministrator;
import projetjeu.view.LevelHandlerParser;


public class GameContoller extends AbstractListenableModel {
    
    private PrincipalGrid grid;
    private HashMap<Integer, ArrayList<Clonable>> tile_map;
    protected HashMap<Player, BufferedImage> listPlayers = new HashMap<>();

    /**
    * Construct a nex Game
    *
    */
    public GameContoller() {
        this(new PrincipalGrid());
    }

    /**
    * Construct a nex Game
    * @param grid Grid of the game.
    */
    public GameContoller(PrincipalGrid grid) {
        this.grid = grid;
    }

    /**
    * Get the grid of the game
    * 
    */
    public PrincipalGrid getGrid() {
        return this.grid;
    }

    public void addPlayer(Player p) {
        listPlayers.put(p, p.getImgRepr());
        grid.addPlayer(p);
    }


    public HashMap<Player, BufferedImage> getListPlayers() {
        return listPlayers;
    }

    public HashMap<Integer, ArrayList<Clonable>> getTileMap() {
        return tile_map;
    }

    public ArrayList<Clonable> loadSimpleGrid() {
        ArrayList<Clonable> res = new ArrayList<>();
        Random r = new Random();
        for (int i = 0 ; i < 13 ; i++) {
            for (int j = 0 ; j < 8 ; j++) {
                int nb = r.nextInt(4);
                res.add(new FreeCase(i,j));
            }
        }
        return res;
    }

   public void loadGrid(InputStream inputStream, int nbPlayers) throws IOException, ParserConfigurationException, SAXException {
    LevelHandlerParser lvlHandler = new LevelHandlerParser();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    
    try {
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputStream, lvlHandler);
    } catch (SAXException ex) {
        Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
    }

    this.grid = new PrincipalGrid(lvlHandler.x, lvlHandler.y, 4);
    String myString = "";

    for (int i = 0; i < lvlHandler.listCase.size(); i++) {
        for (int j = 0; j < lvlHandler.listCase.get(i).length(); j++) {
            if (lvlHandler.listCase.get(i).charAt(j) != ' ' || lvlHandler.listCase.get(i).charAt(j) != '\n') {
                myString += lvlHandler.listCase.get(i).charAt(j);
            }
        }
    }

    myString = myString.replaceAll("[^\\d,]", "");
    String[] cases = myString.split(",");

    int nbLayer = lvlHandler.nbLayer;
    HashMap<Integer, ArrayList<ArrayList<Integer>>> layers = new HashMap<>();
    for (int layer = 0; layer < nbLayer; layer++) {
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
        for (int y = 0; y < lvlHandler.y; y++) {
            temp.add(new ArrayList<>());
            for (int x = 0; x < lvlHandler.x; x++) {
                temp.get(y).add(Integer.parseInt(cases[((lvlHandler.x * y) + x) + layer * lvlHandler.x * lvlHandler.y]));
            }
        }
        layers.put(layer, copyList(temp));
    }

    // La HashMap représente les différentes couches.
    computeTileGrid(layers);
}


    public ArrayList<ArrayList<Integer>> copyList(ArrayList<ArrayList<Integer>> l) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for (int i = 0; i < l.size() ; i++) {
            res.add(new ArrayList<>());
            for (int j = 0; j < l.get(i).size() ; j++) {
                res.get(i).add(Integer.valueOf(l.get(i).get(j)));
            }
        }
        return res;
    }
    
    public void computeTileGrid(HashMap<Integer,ArrayList<ArrayList<Integer>>> l) {
        int size = l.get(0).get(0).size()*l.get(0).size();
        Clonable[] res = new Clonable[l.get(0).get(0).size()*l.get(0).size()];
        HashMap<Integer, ArrayList<Clonable>> hashTile = new HashMap<>();
        int indice = 0;
        for (int i = 0; i < l.size() ; i++) {
            ArrayList<ArrayList<Integer>> list = l.get(i);
            ArrayList<Clonable> tileList = new ArrayList<>();
            for (int j = 0; j < list.size() ; j++) {
                for (int x = 0; x < list.get(j).size() ; x++) {
                    int index = list.get(j).get(x);
                    if (index != 0) {
                        if (index > 1) {
                            index--;
                        }
                        if(index == 1){
                            index = new Random().nextInt(4);
                        }
                        BufferedImage img = null;
                        if(index == 999){
                            img = ImagesAdministrator.bonus;
                        }else{                            
                            img = ImagesAdministrator.imageList.get(index);
                        }
                        Clonable element = null;
                        if(i == 1){
                            img = ImagesAdministrator.imageList.get(41);
                            element = new Mur(x,j, img);
                        }else{
                            img = ImagesAdministrator.imageList.get(18);
                            element = new FreeCase(x,j, img);
                        }
                        if(index == 999){
                            img = ImagesAdministrator.bonus;
                            element = new Pastille(x,j,img);
                        }
                        tileList.add(element);
                        if(i < 2){
                            res[x+(j*this.grid.getWidth())]=element;
                        }
                    }
                }
            }
            hashTile.put(i, tileList);
        }
        this.tile_map = hashTile;
        this.grid.setGrid(res);
    }

}
