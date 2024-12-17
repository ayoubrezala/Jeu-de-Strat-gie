package projetjeu.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import projetjeu.controller.GameContoller;
import projetjeu.modele.*;
import java.awt.event.*;
import javax.swing.*;

public class GameView extends JFrame {

    private ComponentsView view;
    private GameContoller gameController;
    //private Integer[] coordPlayer = new Integer[2];
    public Player playerTour = null;
    private boolean isShooting = false;
    private boolean isMoving = false;
    private Player player;
    // Pour la barre d'information
    private JPanel infoPanel;
    private JLabel infoLabel;

    public GameView(Player p) {
        this(new GameContoller(), p);
    }

    public GameView(GameContoller gameController, Player p) {
        this.player = p;
        this.gameController = gameController;
        playerTour = gameController.getGrid().getPlayerToPlay();
        this.view = new ComponentsView(null, gameController, player, this);
        view.setEntities(gameController.getGrid().getGrid());

        // Panneau principal pour contenir la barre d'information et la grille
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout()); // Utilisation de BorderLayout pour diviser l'espace

        // Création de la barre d'informations
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoLabel = new JLabel();
        infoPanel.add(infoLabel);
        mainPanel.add(infoPanel, BorderLayout.EAST);  // Ajouter la barre en haut de la fenêtre

        // Ajouter la vue de la grille au centre
        mainPanel.add(view, BorderLayout.CENTER);

        // Définir le panneau principal comme contenu de la fenêtre
        setContentPane(mainPanel);

        setTitle("Jeu de combat (" + player.getName() + ")");
        setSize(832, 832);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation de la barre d'informations
        updateInfoPanel();

        // Pour lire les entrées clavier
        setFocusable(true);
        requestFocus();

        setVisible(true);

        setTitle("Jeu de combat (" + player.getName() + "). Tour de: " + playerTour.getName());

        //Création de menu
        final JPopupMenu popup = new JPopupMenu();

        JMenuItem depItem = new JMenuItem("Déplacement");
        depItem.getAccessibleContext().setAccessibleDescription("Déplacer le personnage");
        depItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getEnergy() >= GameConfig.MOVE_COST) {
                    player.select();
                    isMoving = true;
                    gameController.stateChange();
                }
            }
        });
        popup.add(depItem);

        JMenuItem shieldItem = new JMenuItem("Activer bouclier");
        shieldItem.getAccessibleContext().setAccessibleDescription("Activer le bouclier");
        shieldItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player p = player;
                if (!p.isShield_up() && p.getEnergy() >= GameConfig.SHIELD_COST) {
                    p.enableShield();
                    if (p.getEnergy() == 0) {
                        changeTurn();
                    } else {
                        gameController.stateChange();
                    }
                }
            }
        });
        popup.add(shieldItem);

        JMenuItem shootItem = new JMenuItem("Tirer");
        shootItem.getAccessibleContext().setAccessibleDescription("Tirer dans une direction");
        shootItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getEnergy() >= GameConfig.FIRE_COST) {
                    isShooting = true;
                    
                }
            }
        });
        popup.add(shootItem);

        JMenuItem plantMine = new JMenuItem("Poser une mine");
        plantMine.getAccessibleContext().setAccessibleDescription("Poser une mine");
        plantMine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getEnergy() >= GameConfig.PLANT_COST) {
                    player.enablePlant();
                    gameController.stateChange();
                }
            }
        });
        popup.add(plantMine);

        JMenuItem plantBomb = new JMenuItem("Poser une bombe");
        plantBomb.getAccessibleContext().setAccessibleDescription("Poser une bombe");
        plantBomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getEnergy() >= GameConfig.PLANT_COST) {
                    player.enablePlant();
                    player.enablePlantingBomb();
                    gameController.stateChange();
                }
            }
        });
        popup.add(plantBomb);

        JMenuItem passTurn = new JMenuItem("Passer");
        passTurn.getAccessibleContext().setAccessibleDescription("Passer son tour");
        passTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTurn();
            }
        });
        popup.add(passTurn);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (player.getName() == playerTour.getName()) {
                    if (player.getEnergy() >= GameConfig.MOVE_COST && player.getName().equals(playerTour.getName())) {
                        if (e.getKeyCode() == KeyEvent.VK_Z) {
                            if (player.possibleMoves().contains(Direction.z)) {
                                player.move(Direction.z);
                                gameController.stateChange();
                            }
                        }
                        if (e.getKeyCode() == KeyEvent.VK_Q) {
                            if (player.possibleMoves().contains(Direction.q)) {
                                player.move(Direction.q);
                                gameController.stateChange();
                            }

                        }
                        if (e.getKeyCode() == KeyEvent.VK_S) {
                            if (player.possibleMoves().contains(Direction.s)) {
                                player.move(Direction.s);
                                gameController.stateChange();
                            }
                        }
                        if (e.getKeyCode() == KeyEvent.VK_D) {
                            if (player.possibleMoves().contains(Direction.d)) {
                                player.move(Direction.d);
                                gameController.stateChange();
                            }
                        }
                    }
                    if (player.getEnergy() == 0) {
                        changeTurn();
                    }
                }
            }
        });

        getContentPane().addMouseListener(new MouseListener() {
            public void showPopup(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;
                playerTour = gameController.getGrid().getPlayerToPlay();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int popX = e.getX();
                int popY = e.getY();
                int x = e.getX() / 64;
                int y = e.getY() / 64;

                if (gameController.getGrid().getTileAt(x, y) instanceof Player) { //Test si il est le joueur qui doit jouer
                    Player p = (Player) gameController.getGrid().getTileAt(x, y);
                    if (p.getAsTurn()) {
                        if (!p.isSelected() && p.getName().equals(player.getName()) && !gameController.getGrid().gameIsOver()) {
                            showPopup(e);
                            if (p.getEnergy() >= GameConfig.MOVE_COST) {
                                depItem.setText("Déplacement ");
                            } else {
                                depItem.setText("D̶é̶p̶l̶a̶c̶e̶m̶e̶n̶t̶");
                            }
                            if (p.getEnergy() >= GameConfig.SHIELD_COST && !p.isShield_up()) {
                                shieldItem.setText("Activer bouclier");
                            } else {
                                shieldItem.setText("A̶c̶t̶i̶v̶e̶r̶ ̶b̶o̶u̶c̶l̶i̶e̶r̶");
                            }
                            if (p.getEnergy() >= GameConfig.FIRE_COST) {
                                shootItem.setText("Tirer");
                            } else {
                                shootItem.setText("T̶i̶r̶e̶r̶");
                            }
                            if (p.getEnergy() >= GameConfig.PLANT_COST) {
                                plantBomb.setText("Poser une bombe");
                                plantMine.setText("Poser une mine");
                            } else {
                                plantBomb.setText("P̶o̶s̶e̶r̶ ̶u̶n̶e̶ ̶b̶o̶m̶b̶e̶");
                                plantMine.setText("P̶o̶s̶e̶r̶ ̶u̶n̶e̶ ̶m̶i̶n̶e̶");
                            }
                        } else {
                            p.unselect();
                        }
                    }
                    gameController.stateChange();
                }

                Player p = playerTour;
                if (p.getEnergy() > 0) {
                    if (isMoving) {
                        int depX = x - p.getX();
                        int depY = y - p.getY();

                        Direction d = null;
                        if (depX == 0 && depY == -1) {
                            d = Direction.z;
                        } else if (depX == 0 && depY == 1) {
                            d = Direction.s;
                        } else if (depX == -1 && depY == 0) {
                            d = Direction.q;
                        } else if (depX == 1 && depY == 0) {
                            d = Direction.d;
                        }

                        if (p.possibleMoves().contains(d)) {

                            p.move(d);
                            isMoving = false;
                            p.unselect();
                            if (p.getEnergy() == 0) {
                                changeTurn();
                            }
                            gameController.stateChange();
                        }
                        isMoving = false;
                        p.unselect();
                        gameController.stateChange();
                    } else if (isShooting) {
                        Direction d = null;
                        int depX = x - p.getX();
                        int depY = y - p.getY();
                        d = block2dir(x, y);

                        if (gameController.getGrid().getTileAt(depX, depY) instanceof Player) {

                        } else {
                            p.fire(d);
                        }

                        isShooting = false;
                        if (p.getEnergy() == 0) {
                            changeTurn();
                        }
                        gameController.stateChange();

                    } else if (p.isPlanting() && x == p.getX() && y == p.getY()) {
                        p.disablePlant();
                    } else if (p.isPlanting() && gameController.getGrid().getTileAt(x, y) instanceof FreeCase) {
                        int distance = (int) Math.sqrt(Math.pow(playerTour.getX() - x, 2) + Math.pow(playerTour.getY() - y, 2));
                        if (distance == 1) {
                            if (playerTour.isPlantingBomb()) {
                                playerTour.plant(new Bomb(playerTour), gameController.getGrid().getTileAt(x, y));
                            } else {
                                playerTour.plant(new Mine(playerTour), gameController.getGrid().getTileAt(x, y));
                            }
                        }
                        p.disablePlant();
                        p.disablePlantingBomb();
                        gameController.stateChange();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        setVisible(true);
        int difX = 832 + (1000 - this.getContentPane().getWidth());
        int difY = 832 + (832 - this.getContentPane().getHeight());
        this.setSize(difX, difY);
    }

    public ComponentsView getView() {
        return this.view;
    }

    public GameContoller getGame() {
        return this.gameController;
    }
    
    // Méthode pour mettre à jour la barre d'informations
    public void updateInfoPanel() {
        StringBuilder infoText = new StringBuilder("<html>");
        for (Player player : gameController.getGrid().getPlayers()) {
            // Ajouter le nom en majuscules
            infoText.append(player.getName().toUpperCase()).append("<br>");

            infoText.append("Vie: ").append(player.getLife()).append("<br>");

            infoText.append("Énergie: ").append(player.getEnergy()).append("<br>");

            infoText.append(player.isShield_up() ? "Bouclier: Activé" : "Bouclier: Désactivé").append("<br>");

            infoText.append("<br>");
        }
        infoText.append("</html>");

        // Met à jour l'étiquette avec les nouvelles informations
        infoLabel.setText(infoText.toString());
    }
    
    
    public void changeTurn() {
        playerTour.setAsTurn(false);
        playerTour = gameController.getGrid().nextPlayer();
        playerTour.setAsTurn(true);
        setTitle("Jeu de combt (" + player.getName() + "). Tour de: " + playerTour.getName());

        if (gameController.getGrid().hearExplosion()) {
            gameController.getGrid().endExplosion();
        }
        // Mettre à jour les informations des joueurs
        updateInfoPanel();
        gameController.stateChange();
    }

    public Direction block2dir(int x, int y) {
        int varX = playerTour.getX() - x;
        int varY = playerTour.getY() - y;

        if (varX == 0) {
            if (varY < 0) {
                return Direction.s;
            } else if (varY > 0) {
                return Direction.z;
            }
        } else {
            if (varX < 0) {
                return Direction.d;
            } else {
                return Direction.q;
            }
        }
        return null;
    }
    


}
