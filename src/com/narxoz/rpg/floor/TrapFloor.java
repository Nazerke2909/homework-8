package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class TrapFloor extends TowerFloor {
    private final String floorName;
    private final int trapDamage;

    public TrapFloor(String name, int trapDamage) {
        this.floorName = name;
        this.trapDamage = trapDamage;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  The floor looks suspicious...");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamage = 0;
        System.out.println("  A trap is triggered!");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.applyDamage(trapDamage);
                totalDamage += Math.min(trapDamage, hero.getHp());
                if (hero.isAlive() && party.indexOf(hero) == 0) {
                    System.out.println("  " + hero.getName() + " is stunned by the trap!");
                    hero.setState(new StunnedState(1));
                }
            }
        }
        boolean cleared = party.stream().anyMatch(Hero::isAlive);
        return new FloorResult(cleared, totalDamage, "Survived the trap");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false; 
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
    }
}