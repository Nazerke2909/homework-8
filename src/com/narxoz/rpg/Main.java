package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.*;
import com.narxoz.rpg.tower.TowerRunner;
import com.narxoz.rpg.tower.TowerRunResult;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("----- THE HAUNTED TOWER - ASCENDING THE FLOORS -----\n");

        Hero warrior = new Hero("Sir Warka", 45, 12, 5);
        Hero mage = new Hero("Jean", 32, 15, 3);
        List<Hero> party = List.of(warrior, mage);

        List<TowerFloor> floors = new ArrayList<>();
        floors.add(new CombatFloor("Goblin Ambush", new Monster("Goblin", 30, 8)));
        floors.add(new TrapFloor("Spike Pit", 10));          
        floors.add(new CombatFloor("Orc Camp", new Monster("Orc", 45, 10))); 
        floors.add(new RestFloor("Fountain of Healing"));   
        floors.add(new BossFloor("Shadow Dragon's Lair", new Monster("Shadow Dragon", 70, 15)));

        TowerRunner runner = new TowerRunner(floors, party);
        TowerRunResult result = runner.runTower();

        System.out.println("\n-----------------------------------------");
        System.out.println("----- TOWER RUN COMPLETE -----");
        System.out.println("Floors cleared: " + result.getFloorsCleared() + " / " + floors.size());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());

        System.out.println("\nFinal hero status:");
        for (Hero hero : party) {
            System.out.println("  " + hero.getName() + ": HP " + hero.getHp() + "/" + hero.getMaxHp() + " | State: " + hero.getState().getName());
        }
    }
}