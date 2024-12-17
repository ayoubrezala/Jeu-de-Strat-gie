package projetjeu.view;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;


public class ImagesAdministrator {

    public static ArrayList<BufferedImage> imageList;
    public static HashMap<Integer, ArrayList<BufferedImage>> imagePlayers;
    public static BufferedImage fog;
    public static BufferedImage shield;
    public static BufferedImage bomb;
    public static BufferedImage mine;
    public static BufferedImage bullet;
    public static BufferedImage bonus;


    public static BufferedImage lookUp(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(height, width, img.getType());

        for(int y = 0; y < height ; y++){
            for(int x = 0; x < width; x++){
                rotated.setRGB(y, (width-1)-x, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    public static BufferedImage lookDown(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(height, width, img.getType());

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height ; y++){
                rotated.setRGB(y, x, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    public static BufferedImage lookRight(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(width, height, img.getType());

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height ; y++){
                rotated.setRGB(x, y, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    public static BufferedImage lookLeft(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(width, height, img.getType());

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height ; y++){
                rotated.setRGB((width-1)-x, y, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    //Méthode a appelé une seule, ensuite les images sont accessibles de n'importe ou.
    public static ArrayList<BufferedImage> loadImages(){
        imageList = new ArrayList<>();

        BufferedImage tilesheet = null;

        try (InputStream inputStream = ImagesAdministrator.class.getResourceAsStream("/projetjeu/Images/Tilesheet/tilesheet_complete.png")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Image tilesheet_complete.png introuvable !");
            }
            tilesheet = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de tilesheet : " + e.getMessage());
        }

        try {
            shield = ImageIO.read(ImagesAdministrator.class.getResourceAsStream("/projetjeu/Images/shield.png"));
            bomb = ImageIO.read(ImagesAdministrator.class.getResourceAsStream("/projetjeu/Images/bomb2.png"));
            mine = ImageIO.read(ImagesAdministrator.class.getResourceAsStream("/projetjeu/Images/mine2.png"));
            bonus = ImageIO.read(ImagesAdministrator.class.getResourceAsStream("/projetjeu/Images/bonus.png"));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des autres images : " + e.getMessage());
        }

        
        int width = tilesheet.getWidth();
        int height = tilesheet.getHeight();
        int size = 64;
        int nbImagesWidth = width / size;
        int nbImagesHeight = height / size;

        for(int y = 0 ; y < nbImagesHeight ; y++){
            for(int x = 0 ; x < nbImagesWidth ; x++){
                BufferedImage temp = tilesheet.getSubimage(x*size , y*size, size, size);
                imageList.add(temp);
            }
        }
        return imageList;
    }
}
