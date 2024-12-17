package projetjeu.patterns;

import projetjeu.modele.PrincipalGrid;
import projetjeu.modele.Bomb;
import projetjeu.modele.Weapon;
import projetjeu.modele.Mine;
import projetjeu.modele.Player;
import projetjeu.modele.GameConfig;
import projetjeu.modele.Fuisil;
import java.util.*;
public final class PlayerFactory {

    private static PlayerFactory instance = null;
    public static int nb_instances = 0;
    public static ArrayList<Weapon> inventory = new ArrayList<Weapon>();

    private PlayerFactory() {
        super();
        weapons();
    }

    public final static PlayerFactory getInstance() {
        if (PlayerFactory.instance == null) {
            PlayerFactory.instance = new PlayerFactory();
        }
        return PlayerFactory.instance;
    }

    public ArrayList<Weapon> weapons() {
        inventory.add(new Fuisil(new Player(new PrincipalGrid(),"","")));
        inventory.add(new Mine(new Player(new PrincipalGrid(),"","")));
        inventory.add(new Bomb(new Player(new PrincipalGrid(),"","")));
        return inventory;
    }

    public Player build(PrincipalGrid g, String classname, int hp_mod, int ap_mod, int rifle_ammo_mod, int bombs_mod, int mines_mod) {
        PlayerFactory.nb_instances++;
        Player p = new Player(g,"Player "+PlayerFactory.nb_instances, classname);
        p.setLife(p.getLife()+hp_mod);
        p.setBaseEnergy(p.getBaseEnergy()+ap_mod);
        p.addWeapon(PlayerFactory.inventory.get(0), GameConfig.RIFLE_BASE_AMMO+rifle_ammo_mod);
        p.addWeapon(PlayerFactory.inventory.get(1), GameConfig.MINE_BASE_COUNT+mines_mod);
        p.addWeapon(PlayerFactory.inventory.get(2), GameConfig.BOMB_BASE_COUNT+bombs_mod);
        return p;
    }

    public Player buildBasic(PrincipalGrid g) {
        return build(g,"Basic",0,0,0,0,0);
    }

    public Player buildTank(PrincipalGrid g) {
        return build(g,"Tank",20,0,0,0,0);
    }

    public Player buildMarksman(PrincipalGrid g) {
        return build(g,"Marksman",-5,4,30,-5,-2);
    }

    public Player buildEngineer(PrincipalGrid g) {
        return build(g,"Engineer",0,0,-15,5,3);
    }
}
