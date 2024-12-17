package projetjeu;

import projetjeu.patterns.PlayerFactory;
import java.util.*;
import java.io.*;
import projetjeu.modele.Bomb;
import projetjeu.modele.Direction;
import projetjeu.modele.FreeCase;
import projetjeu.modele.GameConfig;
import projetjeu.modele.Mine;
import projetjeu.modele.Player;
import projetjeu.modele.PrincipalGrid;
import projetjeu.modele.PrintThread;

public class ConsoleMain {

    public static void main(String[] args) {
        args = new String[]{"-p0"};
        new GameConfig();
        Scanner sc= new Scanner(System.in);
        PlayerFactory factory = PlayerFactory.getInstance();
        PrincipalGrid g = new PrincipalGrid();
        boolean autoplay = true;

        if (args.length>0) {
            if (args[0].equals("-p0") || args[0].equals("-p1")) {//p0 config simple, p1 config avancée
                System.out.println("\033[H\033[2J");
                
                 // Saisie des dimensions de la grille
                int longueur = 0;
                int largeur = 0;
                boolean dimensionsValides = false;
                while (!dimensionsValides) {
                    try {
                        System.out.println("Entrez les dimensions de la grille sous la forme (Longueur x Largeur) :");
                        String[] dim = sc.nextLine().trim().split("x");
                        if (dim.length != 2) {
                            throw new IllegalArgumentException("Format invalide. Exemple attendu : 10x10");
                        }
                        longueur = Integer.parseInt(dim[0].trim());
                        largeur = Integer.parseInt(dim[1].trim());
                        if (longueur <= 0 || largeur <= 0) {
                            throw new IllegalArgumentException("Les dimensions doivent être des nombres positifs.");
                        }
                        dimensionsValides = true;
                    } catch (Exception e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
                
                // Saisie du nombre de joueurs
                int nbPlayers = 0;
                boolean nbPlayersValide = false;
                while (!nbPlayersValide) {
                    try {
                        System.out.println("Entrez le nombre de joueurs :");
                        nbPlayers = Integer.parseInt(sc.nextLine().trim());
                        if (nbPlayers <= 0) {
                            throw new IllegalArgumentException("Le nombre de joueurs doit être supérieur à 0.");
                        }
                        nbPlayersValide = true;
                    } catch (Exception e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
                
                g = new PrincipalGrid(longueur,largeur,nbPlayers);
                // Choix pour le mode humain ou automatique
                boolean choixValide = false;
                while (!choixValide) {
                    try {
                        System.out.println("Voulez-vous jouer sans humains ? (y/n) :");
                        String response = sc.nextLine().trim().toLowerCase();
                        if (response.equals("y")) {
                            choixValide = true;
                        } else if (response.equals("n")) {
                            choixValide = true;
                        } else {
                            throw new IllegalArgumentException("Réponse invalide. Entrez 'y' pour oui ou 'n' pour non.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }
                if (args[0].equals("-p1")) {//classe de chaque joueur
                    for (int i=1 ; i<=nbPlayers ; i++) {
                        System.out.println("Classe du joueur " + i);
                        System.out.println("Choisissez une classe : basic, tank, marksman, engineer");
                        switch (sc.nextLine()) {
                            case "basic" :
                                g.addPlayer(factory.buildBasic(g));
                                break;
                            case "tank" :
                                g.addPlayer(factory.buildTank(g));
                                break;
                            case "marksman" :
                                g.addPlayer(factory.buildMarksman(g));
                                break;
                            case "engineer" :
                                g.addPlayer(factory.buildEngineer(g));
                                break;
                            default :
                                System.out.println(PlayerFactory.nb_instances);
                        }
                    }
                } else {//p0 : tous les joueurs sont basic
                    for (int i=1 ; i<=nbPlayers ; i++) {
                        g.addPlayer(factory.buildBasic(g));
                    }
                }
            }

        } else {//setup standard
            g = new PrincipalGrid(10,10,4);
            for (int i=1 ; i<=4 ; i++) {
                g.addPlayer(factory.buildBasic(g));
            }
        }
        g.createGrid();

        Player p=null;
        end :
        while(!(g.gameIsOver())) {
            p = g.nextPlayer();
            next :
            while (p.getEnergy()>0 && !g.gameIsOver()) {
                if (args.length>0 && !autoplay) {//jeu manuel
                    Runnable thread = new PrintThread(g,p);
                    Thread t = new Thread(thread);
                    t.start();
                    String input=sc.nextLine();
                    t.interrupt();
                    switch (input) {
                        case "E"://quitter
                        case "e":
                            break end;
                        case "P"://fin de tour même avec AP>0
                        case "p":
                            break next;
                        case "N"://action de l'IA
                        case "n":
                            p.act();
                            break;
                        case "A"://bouclier
                        case "a":
                            p.enableShield();
                            break;
                        case "M"://posage d'explosif
                        case "m":
                        case "B":
                        case "b":
                            ArrayList<FreeCase> sites = g.getNeighbouringFreeTiles(p,1);
                            String site_list = "";
                            for (FreeCase f : sites) {
                                site_list+=f.printCoords()+" ";
                            }
                            System.out.println(site_list+"\nChoisissez un emplacement :");
                            if (input.equals("M") || input.equals("m")) {
                                p.plant(new Mine(p), sites.get(Integer.parseInt(sc.nextLine())));
                            }
                            if (input.equals("B") || input.equals("b")) {
                                p.plant(new Bomb(p), sites.get(Integer.parseInt(sc.nextLine())));
                            }
                            break;
                        case "T"://tir
                        case "t":
                            System.out.println("\nChoisissez une direction (z,q,s,d):");
                            switch (sc.nextLine()) {
                                case "Z" :
                                case "z" :
                                    p.fire(Direction.z);
                                    break;
                                case "Q" :
                                case "q" :
                                    p.fire(Direction.q);
                                    break;
                                case "S" :
                                case "s" :
                                    p.fire(Direction.s);
                                    break;
                                case "D" :
                                case "d" :
                                    p.fire(Direction.d);
                                    break;
                                default :
                                    System.out.println("Non");
                                    break;
                            }
                            break;
                        case "Z"://mouvements
                        case "z":
                            p.move(Direction.z);
                            break;
                        case "Q":
                        case "q":
                            p.move(Direction.q);
                            break;
                        case "D":
                        case "d":
                            p.move(Direction.d);
                            break;
                        case "S":
                        case "s":
                            p.move(Direction.s);
                            break;
                        default:
                            System.out.println("Entrez une commande valide.");
                            break;
                    }
                } else {//jeu auto
                    System.out.println("\033[H\033[2J");
                    System.out.println("Tour " + g.getTurnNumber());
                    System.out.println(p.getName() + "\n");
                    //System.out.println(g + "\n");//vue globale
                    System.out.println(p.getView() + "\n");//vues joueur
                    System.out.println("# : mur");
                    System.out.println("; : mine");
                    System.out.println("3 : bombe (délai avant détonation)");
                    System.out.println(". : bonus");
                    System.out.println("@ : joueur (€ si bouclier actif)");
                    System.out.println("\n" + p.printStats());
                    System.out.println(p.printControls());
                    p.act();
                }
            }
        }
        if (g.gameIsOver()) {
            System.out.println("\033[H\033[2J");
            System.out.println("Tour " + g.getTurnNumber());
            System.out.println(p.getName() + "\n");
            //System.out.println(g + "\n");//vue globale
            System.out.println(p.getView() + "\n");//vues joueur
            System.out.println("# : mur");
            System.out.println("; : mine");
            System.out.println("3 : bombe (délai avant détonation)");
            System.out.println(". : bonus");
            System.out.println("@ : joueur (€ si bouclier actif)");
            System.out.println("\n" + p.printStats());
            System.out.println(p.printControls());
            System.out.println(p.getName() + " a gagné !");
        } else {
          System.out.println("Jeu quitté");
        }
    }
}
