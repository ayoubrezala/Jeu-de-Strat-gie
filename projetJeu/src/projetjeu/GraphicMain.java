package projetjeu;

import projetjeu.controller.GameContoller;
import projetjeu.modele.*;
import projetjeu.patterns.PlayerFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import projetjeu.view.GameView;
import projetjeu.view.ImagesAdministrator;

public class GraphicMain {

    public static void main(String[] args) {
        try {
            // Chargement de la configuration du jeu
            new GameConfig();

            PlayerFactory factory = PlayerFactory.getInstance();
            ArrayList<BufferedImage> images = ImagesAdministrator.loadImages();

            // Chargement du fichier level.xml
            InputStream levelInputStream = GraphicMain.class.getResourceAsStream("/projetjeu/Levels/level.xml");
            if (levelInputStream == null) {
                throw new FileNotFoundException("Le fichier level.xml est introuvable dans le classpath.");
            }

            GameContoller game = new GameContoller();
            game.loadGrid(levelInputStream, 4);

            BufferedImage playerim1 = loadImage("/projetjeu/Images/player1.png");
            BufferedImage playerim2 = loadImage("/projetjeu/Images/player2.png");
            BufferedImage playerim3 = loadImage("/projetjeu/Images/player3.png");
            BufferedImage playerim4 = loadImage("/projetjeu/Images/player4.png");

            // Création des joueurs
            Player p1 = createPlayer(factory, game, 0, 0, "Ayoub", playerim1);
            Player p2 = createPlayer(factory, game, 12, 0, "Georges", playerim2);
            Player p3 = createPlayer(factory, game, 0, 12, "Anis", playerim3);
            Player p4 = createPlayer(factory, game, 11, 12, "Mohamed", playerim4);
            game.getGrid().initializePlayers(); 

            // Initialisation des fenêtres pour chaque joueur
            javax.swing.SwingUtilities.invokeLater(() -> {
                new GameView(game, p1);
                new GameView(game, p2);
                new GameView(game, p3);
                new GameView(game, p4);
            });
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(GraphicMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static BufferedImage loadImage(String path) throws IOException {
        try (InputStream inputStream = GraphicMain.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Image introuvable à l'emplacement : " + path);
            }
            return ImageIO.read(inputStream);
        }
    }

    private static Player createPlayer(PlayerFactory factory, GameContoller game, int x, int y, String name, BufferedImage image) {
        Player player = factory.buildBasic(game.getGrid());
        player.setX(x);
        player.setY(y);
        player.setName(name);
        player.setImgRepr(image);
        game.addPlayer(player);
        return player;
    }
}
