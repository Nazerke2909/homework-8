package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class RestFloor extends TowerFloor {
    private final String floorName;

    public RestFloor(String name) {
        this.floorName = name;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void announce() {
        System.out.println("\n--- Entering " + getFloorName() + " (a place of rest) ---");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  The party rests by a warm fire.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  Healing energies restore the party.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                int healAmount = 12;
                hero.heal(healAmount);
                System.out.println("  " + hero.getName() + " heals " + healAmount + " HP.");
                if (!(hero.getState() instanceof NormalState)) {
                    hero.setState(new NormalState());
                }
            }
        }
        return new FloorResult(true, 0, "Restored health");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false; 
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) { }
}