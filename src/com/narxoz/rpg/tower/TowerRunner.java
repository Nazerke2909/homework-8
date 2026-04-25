package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {
    private final List<TowerFloor> floors;
    private final List<Hero> party;

    public TowerRunner(List<TowerFloor> floors, List<Hero> party) {
        this.floors = floors;
        this.party = party;
    }

    public TowerRunResult runTower() {
        int floorsCleared = 0;
        for (TowerFloor floor : floors) {
            System.out.println("\n-----------------------------------------");
            System.out.println("Proceeding to next floor...");

            boolean anyAlive = party.stream().anyMatch(Hero::isAlive);
            if (!anyAlive) {
                System.out.println("All heroes are dead! Tower climb ends.");
                break;
            }

            FloorResult result = floor.explore(party);
            if (result.isCleared()) {
                floorsCleared++;
                System.out.println("Floor cleared! " + result.getSummary());
            } else {
                System.out.println("Floor failed! " + result.getSummary());
                break;
            }
        }

        int survivors = (int) party.stream().filter(Hero::isAlive).count();
        boolean reachedTop = floorsCleared == floors.size() && survivors > 0;
        return new TowerRunResult(floorsCleared, survivors, reachedTop);
    }
}