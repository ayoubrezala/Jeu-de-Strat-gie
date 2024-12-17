package projetjeu.patterns;


import projetjeu.modele.Direction;
import projetjeu.modele.Bomb;
import projetjeu.modele.Mine;
import projetjeu.modele.Player;
import projetjeu.modele.FreeCase;
import projetjeu.modele.GameConfig;
import java.util.*;

public class RandomStrategy implements PlayerStrategy {

    private final Player client;

    public RandomStrategy(Player client) {
        this.client = client;
    }

    @Override
    public void execute() {
        int nb_actions = 4;
        int r = new Random().nextInt(nb_actions);//Taks the bot's action
        int r_dir = new Random().nextInt(Direction.getDirections().size());//Takes the movement's or shooting's direction
        switch (r) {
            case 0 ://Movements
                if (client.actionIsAvailable(GameConfig.MOVE_COST,-1)) {
                  client.move(Direction.getDirections().get(r_dir));
                }
                break;
            case 1 ://Explosive plating
                boolean r_plant = new Random().nextBoolean();//tirage du type d'explosif
                ArrayList<FreeCase> sites = client.getView().getModel().getNeighbouringFreeTiles(this.client,1);
                if (sites.size()>0) {
                    int r_site = new Random().nextInt(sites.size());//tirage du site de posage
                    if (r_plant && client.actionIsAvailable(GameConfig.PLANT_COST,1)) {
                        client.plant(new Mine(this.client), sites.get(r_site));
                    } else if (client.actionIsAvailable(GameConfig.PLANT_COST,2)){
                        client.plant(new Bomb(this.client), sites.get(r_site));
                    }
                }
                break;
            case 2 ://Shield
                if (client.actionIsAvailable(GameConfig.SHIELD_COST,-1) && !client.isShield_up()) {
                    client.enableShield();
                }
                break;
            case 3 ://Firing
                if (client.actionIsAvailable(GameConfig.FIRE_COST,0)) {
                    client.fire(Direction.getDirections().get(r_dir));
                }
                break;
            default :
                System.out.println("Out of range");
        }
    }
}
