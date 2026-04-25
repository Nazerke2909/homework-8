package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.PoisonedState;
import java.util.List;
import java.util.Random;

public class CombatFloor extends TowerFloor {
    private final String floorName;
    private Monster monster;
    private final Random rand = new Random();

    public CombatFloor(String name, Monster monster) {
        this.floorName = name;
        this.monster = monster;
    }

    @Override
    protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  A wild " + monster.getName() + " (HP: " + monster.getHp() + ") appears!");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        int totalDamageTaken = 0;
        boolean heroesAlive = true;
        System.out.println("  Combat begins!");

        while (monster.isAlive() && heroesAlive) {
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (hero.getState().canAct()) {
                    hero.attack(monster);
                    if (!monster.isAlive()) break;
                }
                hero.onTurnEnd();
            }
            if (!monster.isAlive()) break;

            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                int damage = monster.getAttackPower();
                hero.applyDamage(damage);
                totalDamageTaken += Math.min(damage, hero.getHp());
                if (rand.nextInt(100) < 30 && hero.isAlive()) {
                    System.out.println("  " + monster.getName() + " poisons " + hero.getName() + "!");
                    hero.setState(new PoisonedState(2));
                }
                if (!hero.isAlive()) {
                    System.out.println("  " + hero.getName() + " has fallen!");
                }
            }
            heroesAlive = party.stream().anyMatch(Hero::isAlive);
        }

        boolean cleared = monster.isAlive() ? false : heroesAlive;
        String summary = cleared ? "Defeated " + monster.getName() : "Party wiped or monster still alive";
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (result.isCleared()) {
            System.out.println("  The party finds treasure! Each hero heals 8 HP.");
            for (Hero hero : party) {
                if (hero.isAlive()) hero.heal(8);
            }
        } else {
            System.out.println("  No loot because the floor was not cleared.");
        }
    }
}